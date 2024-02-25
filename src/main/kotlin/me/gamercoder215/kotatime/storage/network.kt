package me.gamercoder215.kotatime.storage

import kotlinx.coroutines.*
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.*
import me.gamercoder215.kotatime.API_KEY
import me.gamercoder215.kotatime.USER_AGENT
import me.gamercoder215.kotatime.client
import me.gamercoder215.kotatime.json
import me.gamercoder215.kotatime.util.*
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
    val startMonth = ((start.time / 2628000000) % 12)
    val currentYear = (now.time / 31536000000) + 1970
    val currentMonth = ((now.time / 2628000000) % 12)

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
        val languages = loadArray(baseUrl = "https://wakatime.com/api/v1/users/current/stats", clazz = Language::class.java, entryPoint = "data.languages")
            .onEach { l ->
                val wl = WLanguage.byName(l.name)
                l.color = wl?.color ?: 0xffffff
            }

        StorageManager.languages.addAll(languages)

        info("Loaded ${StorageManager.languages.size} WakaTime Languages")
    }
}

suspend fun loadFromNetwork() = withContext(Dispatchers.IO) {
    launch {
        loadObj(baseUrl = "https://wakatime.com/api/v1/users/current", clazz = WUser::class.java)
        loadObj(baseUrl = "https://wakatime.com/api/v1/users/current/all_time_since_today", clazz = TimeSinceToday::class.java)
        StorageManager.savePhoto()

        info("Loaded user data for ${WUser.username}")
    }.join()

    coroutineScope {
        StorageManager.machines.addAll(loadArray(baseUrl = "https://wakatime.com/api/v1/users/current/machine_names", clazz = Machine::class.java))

        info("Loaded machine data for ${WUser.username}")
    }

    coroutineScope {
        val globalJson = json("https://wakatime.com/api/v1/leaders?&api_key=$API_KEY")["current_user"] as? JsonObject ?: throw SerializationException("Invalid entry point (parent is null): current_user")
        val global = Rank().load(globalJson)
        StorageManager.ranks.add(global)
        info("Loaded Global Rank for ${WUser.username}")

        for (lang in StorageManager.languages)
            launch {
                delay(100L)
                val rank = json("https://wakatime.com/api/v1/leaders?&api_key=$API_KEY&language=${lang.name.replace(" ", "%20")}")
                if (rank["error"] != null) return@launch

                val data = rank["current_user"] as? JsonObject ?: throw SerializationException("Invalid entry point (parent is null): data")
                if (data.isEmpty()) return@launch
                if (data["page"] is JsonNull) return@launch

                val obj = Rank().load(data)
                obj.language = lang.name

                StorageManager.ranks.add(obj)
                info("Loaded Rank for ${WUser.username} in '${lang.name}'")
            }
    }

    coroutineScope {
        val json = json("https://wakatime.com/api/v1/users/current/projects?api_key=$API_KEY")
        val data = json["data"] as? JsonArray ?: throw SerializationException("Invalid entry point (parent is null): data")
        if (data.isEmpty()) return@coroutineScope warn("No projects found for ${WUser.username}")

        val projects = loadArray(loaded = json, clazz = Project::class.java)
            .onEachIndexed { i, p ->
                launch {
                    delay(100L * i)
                    val commitsJson = json("https://wakatime.com/api/v1/users/current/projects/${p.id}/commits?api_key=$API_KEY")
                    if (commitsJson["error"] != null) return@launch

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
                    info("Loaded ${commits.commits.size} commits for '${p.name}'")
                }

                launch {
                    val repo = data.firstOrNull { it.jsonObject["id"]?.jsonPrimitive?.contentOrNull == p.id }?.jsonObject?.get("repository") as? JsonObject ?: return@launch
                    p.repository = ProjectRepository().load(repo)
                    info("Added Git Repository for '${p.name}'")
                }
            }

        StorageManager.projects.addAll(projects)
    }

    coroutineScope {
        val times = listOf(
            "last_7_days",
            "last_30_days",
            "last_6_months",
            "last_year",
            "all_time"
        ) + getStatFilters(WUser.created_at.asDate)
        debug("${times.size} time filters: $times")

        for ((i, time) in times.withIndex())
            launch {
                delay(100L * i)
                val infoJson = json("https://wakatime.com/api/v1/users/current/stats/$time?api_key=$API_KEY")
                val info = StatsInfo().load(infoJson)

                val categories = loadArray(baseUrl = "https://wakatime.com/api/v1/users/current/stats/$time", clazz = Stat::class.java, entryPoint = "data.categories")
                val editors = loadArray(baseUrl = "https://wakatime.com/api/v1/users/current/stats/$time", clazz = Stat::class.java, entryPoint = "data.editors")
                val languages = loadArray(baseUrl = "https://wakatime.com/api/v1/users/current/stats/$time", clazz = Stat::class.java, entryPoint = "data.languages")
                val os = loadArray(baseUrl = "https://wakatime.com/api/v1/users/current/stats/$time", clazz = Stat::class.java, entryPoint = "data.operating_systems")

                StorageManager.loadedStats[time] = Stats(info, editors, categories, languages, os)

                info("Loaded Stats for ${WUser.username} in '$time'")
            }
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

    val body = res.body()
    debug("Request to '$uri' returned ${res.statusCode()}, size ${body.length}")

    if (res.statusCode() == 302 || res.statusCode() == 429)
        throw IllegalStateException("We are currently making too many API Requests. Wait about 5 minutes then re-open the application.")

    return json.parseToJsonElement(body).jsonObject
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
            val value = current[i] ?: throw SerializationException("Invalid entry point (location doesn't exist): $entryPoint")
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