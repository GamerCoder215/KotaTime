package me.gamercoder215.kotatime.storage

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.*
import me.gamercoder215.kotatime.API_KEY
import me.gamercoder215.kotatime.USER_AGENT
import me.gamercoder215.kotatime.client
import me.gamercoder215.kotatime.json
import me.gamercoder215.kotatime.util.asDate
import me.gamercoder215.kotatime.util.load
import java.lang.reflect.Modifier
import java.net.NetworkInterface
import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.charset.StandardCharsets
import java.util.*

// URLs

const val PING_URL = "https://wakatime.com/api/v1/meta"

val USER_URL = "https://wakatime.com/api/v1/users/current?api_key=$API_KEY"

// Other VARs

val IS_ONLINE: Boolean
    get() {
        try {
            val res = client.send(
                HttpRequest.newBuilder()
                    .uri(URI.create(PING_URL))
                    .GET()
                    .header("User-Agent", USER_AGENT)
                    .header("Accept", "application/json")
                    .build(),
                HttpResponse.BodyHandlers.discarding()
            )

            return res.statusCode() == 200
        } catch (ignored: Exception) {
            return false
        }

    }

// Network Loading

fun getStatFilters(start: Date, now: Date = Date()): List<String> {
    val filters = mutableListOf<String>()

    val startYear = (start.time / 31536000000) + 1970
    val startMonth = ((start.time / 2628000000) % 12) + 1
    val currentYear = (now.time / 31536000000) + 1970
    val currentMonth = ((now.time / 2628000000) % 12) + 1

    var year = startYear
    var month = startMonth

    while (year < currentYear || (year == currentYear && month <= currentMonth)) {
        filters.add("$year-${String.format("%02d", month)}")

        if (month == 12L) {
            year++
            month = 1
        } else {
            month++
        }
    }

    return filters
}

suspend fun loadWakatimeDataFromNetwork() = withContext(Dispatchers.IO) {
    launch {
        StorageManager.languages.addAll(loadArray(baseUrl = "https://wakatime.com/api/v1/program_languages", clazz = Language::class.java))
    }
}

suspend fun loadFromNetwork() = withContext(Dispatchers.IO) {
    launch {
        loadObj(baseUrl = "https://wakatime.com/api/v1/users/current", clazz = WUser::class.java)
        loadObj(baseUrl = "https://wakatime.com/api/v1/users/current/all_time_since_today", clazz = TimeSinceToday::class.java)
    }.join()

    launch {
        StorageManager.machines.addAll(loadArray(baseUrl = "https://wakatime.com/api/v1/users/current/machine_names", clazz = Machine::class.java))
    }

    launch {
        val globalJson = json("https://wakatime.com/api/v1/leaders?&api_key=$API_KEY")["current_user"] as? JsonObject ?: throw SerializationException("Invalid entry point (parent is null): current_user")
        val global = Rank().load(globalJson)
        StorageManager.ranks.add(global)

        for (lang in StorageManager.languages)
            async {
                val rank = json("https://wakatime.com/api/v1/leaders?&api_key=$API_KEY&language=${lang.name.replace(" ", "%20")}")
                if (rank["error"] != null) return@async

                val data = rank["current_user"] as? JsonObject ?: throw SerializationException("Invalid entry point (parent is null): data")
                if (data.isEmpty()) return@async
                if (data["page"] is JsonNull) return@async

                val obj = Rank().load(data)
                obj.language = lang.name

                StorageManager.ranks.add(obj)
            }.await()
    }

    launch {
        val json = json("https://wakatime.com/api/v1/users/current/projects?api_key=$API_KEY")
        val data = json["data"] as? JsonArray ?: throw SerializationException("Invalid entry point (parent is null): data")
        if (data.isEmpty()) return@launch

        val projects = loadArray(loaded = json, clazz = Project::class.java)
            .onEach { p ->
                async {
                    val commitsJson = json("https://wakatime.com/api/v1/users/current/projects/${p.id}/commits?api_key=$API_KEY")
                    if (commitsJson["error"] != null) return@async

                    val commits = CommitData().load(commitsJson).apply {
                        val commits = mutableSetOf<Commit>()
                        val commitData = commitsJson["commits"] as? JsonArray ?: throw SerializationException("Invalid entry point (parent is null): commits")
                        if (commitData.isEmpty()) return@apply

                        for (commit in commitData) {
                            val obj = Commit().load(commit.jsonObject)
                            commits.add(obj)
                        }

                        this.commits = commits
                    }

                    StorageManager.commits[p.id] = commits
                }.await()

                async {
                    val repo = data.firstOrNull { it.jsonObject["id"]?.jsonPrimitive?.contentOrNull == p.id }?.jsonObject?.get("repository") as? JsonObject ?: return@async
                    p.repository = ProjectRepository().load(repo)
                }.await()
            }

        StorageManager.projects.addAll(projects)
    }

    launch {
        val stats = mutableMapOf<String, Stats>()

        val times = listOf(
            "last_7_days",
            "last_30_days",
            "last_6_months",
            "last_year",
            "all_time"
        ) + getStatFilters(WUser.created_at.asDate)

        for (time in times) {
            async {
                val infoJson = json("https://wakatime.com/api/v1/users/current/stats/$time?api_key=$API_KEY")
                val info = StatsInfo().load(infoJson)

                val categories = loadArray(baseUrl = "https://wakatime.com/api/v1/users/current/stats/$time", clazz = Stat::class.java, entryPoint = "data.categories")
                val editors = loadArray(baseUrl = "https://wakatime.com/api/v1/users/current/stats/$time", clazz = Stat::class.java, entryPoint = "data.editors")
                val languages = loadArray(baseUrl = "https://wakatime.com/api/v1/users/current/stats/$time", clazz = Stat::class.java, entryPoint = "data.languages")
                val os = loadArray(baseUrl = "https://wakatime.com/api/v1/users/current/stats/$time", clazz = Stat::class.java, entryPoint = "data.operating_systems")

                stats[time] = Stats(info, editors, categories, languages, os)
            }.await()
        }

        StorageManager.loadedStats.putAll(stats)
    }
}

private fun json(
    uri: String
): JsonObject {
    val res = client.send(
        HttpRequest.newBuilder()
            .uri(URI.create(uri))
            .GET()
            .header("User-Agent", USER_AGENT)
            .header("Accept", "application/json")
            .build(),
        HttpResponse.BodyHandlers.ofString()
    )

    return json.parseToJsonElement(res.body()).jsonObject
}

private fun <T> loadObj(
    loaded: JsonObject? = null,
    baseUrl: String = "",
    clazz: Class<T>,
    entryPoint: String = "data",
    extra: String = ""
) {
    val json = loaded ?: json("$baseUrl?api_key=$API_KEY$extra")
    var entry: JsonObject = json

    if (entryPoint.contains("."))
        for (i in entryPoint.split("."))
            entry = entry[i] as? JsonObject ?: throw SerializationException("Invalid entry point (parent is null): $entryPoint")
    else
        entry = entry[entryPoint] as? JsonObject ?: throw SerializationException("Invalid entry point (parent is null): $entryPoint")

    if (entry.isEmpty()) return

    for (field in clazz.declaredFields.filter { !Modifier.isFinal(it.modifiers) })
        field.load(null, entry[field.name] as? JsonPrimitive ?: continue)
}

private fun <T> loadArray(
    loaded: JsonObject? = null,
    baseUrl: String = "",
    clazz: Class<T>,
    entryPoint: String = "data",
    extra: String = ""
): Set<T> {
    val json = loaded ?: json("$baseUrl?api_key=$API_KEY$extra")
    var current = json
    lateinit var entry: JsonArray

    if (entryPoint.contains("."))
        for (i in entryPoint.split(".")) {
            val value = current[i] ?: throw SerializationException("Invalid entry point (parent is null): $entryPoint")
            if (value is JsonArray) {
                entry = value
                break
            } else if (value is JsonObject)
                current = value
            else
                throw SerializationException("Invalid entry point (invalid value type): $entryPoint")
        }
    else
        entry = current[entryPoint] as? JsonArray ?: throw SerializationException("Invalid entry point (parent is null): $entryPoint")

    try {
        entry.size
    } catch (ignored: UninitializedPropertyAccessException) {
        throw SerializationException("Invalid entry point (invalid array): $entryPoint")
    }

    if (entry.isEmpty()) return emptySet()

    val set = mutableSetOf<T>()
    for (element in entry) {
        val obj = clazz.getDeclaredConstructor().newInstance()
        for (field in clazz.declaredFields.filter { !Modifier.isFinal(it.modifiers) && !Modifier.isStatic(it.modifiers) })
            field.load(obj, element.jsonObject[field.name] as? JsonPrimitive ?: continue)

        set.add(obj)
    }

    return set
}