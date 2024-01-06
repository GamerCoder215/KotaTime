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
import me.gamercoder215.kotatime.util.wrapperType
import java.net.NetworkInterface
import java.net.URI
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.util.*

// URLs

const val PING_URL = "https://wakatime.com/api/v1/meta"

val USER_URL = "https://wakatime.com/api/v1/users/current?api_key=$API_KEY"

// Other VARs

val IS_ONLINE: Boolean
    get() {
        try {
            val interfaces = NetworkInterface.networkInterfaces()
            if (interfaces.allMatch { !it.isUp }) return false
            if (interfaces.allMatch { it.isLoopback }) return false

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

suspend fun loadFromNetwork() = withContext(Dispatchers.IO) {
    launch {
        loadObj(baseUrl = USER_URL, clazz = WUser::class.java)
    }.join()

    launch {
        StorageManager.languages.addAll(loadArray(baseUrl = "https://wakatime.com/api/v1/program_languages", clazz = Language::class.java))
    }

    launch {
        StorageManager.machines.addAll(loadArray(baseUrl = "https://wakatime.com/api/v1/users/current/machine_names", clazz = Machine::class.java))
    }

    launch {
        StorageManager.projects.addAll(loadArray(baseUrl = "https://wakatime.com/api/v1/users/current/projects", clazz = Project::class.java))
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
                val info = StatsInfo().apply {
                    for (field in StatsInfo::class.java.declaredFields) {
                        val value = infoJson[field.name] as? JsonPrimitive ?: continue

                        when (field.wrapperType) {
                            Int::class.java -> field[this] = value.intOrNull ?: continue
                            Double::class.java -> field[this] = value.doubleOrNull ?: continue
                            String::class.java -> field[this] = value.content
                            Boolean::class.java -> field[this] = value.booleanOrNull ?: continue
                        }
                    }
                }

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
            .build(),
        HttpResponse.BodyHandlers.ofString()
    )

    return json.parseToJsonElement(res.body()).jsonObject
}

private fun <T> loadObj(
    obj: JsonObject? = null,
    baseUrl: String,
    clazz: Class<T>,
    entryPoint: String = "data",
    extra: String = ""
) {
    val json = obj ?: json("$baseUrl?api_key=$API_KEY$extra")
    lateinit var entry: JsonObject

    for (i in entryPoint.split("."))
        entry = json[i] as? JsonObject ?: throw SerializationException("Invalid entry point (parent is null): $entryPoint")

    for (field in clazz.declaredFields) {
        val value = entry[field.name] as? JsonPrimitive ?: continue

        when (field.type) {
            Int::class.java -> field[null] = value.intOrNull ?: continue
            Double::class.java -> field[null] = value.doubleOrNull ?: continue
            String::class.java -> field[null] = value.content
            Boolean::class.java -> field[null] = value.booleanOrNull ?: continue
        }
    }
}

private fun <T> loadArray(
    obj: JsonObject? = null,
    baseUrl: String,
    clazz: Class<T>,
    entryPoint: String = "data",
    extra: String = ""
): Set<T> {
    val json = json("$baseUrl?api_key=$API_KEY$extra")
    lateinit var entry: JsonArray

    for (i in entryPoint.split(".")) {
        val value = json[i] ?: throw SerializationException("Invalid entry point (parent is null): $entryPoint")

        entry = value as? JsonArray ?: continue
    }

    try {
        entry.size
    } catch (ignored: UninitializedPropertyAccessException) {
        throw SerializationException("Invalid entry point (invalid array): $entryPoint")
    }

    val set = mutableSetOf<T>()
    for (element in entry) {
        val obj = clazz.getDeclaredConstructor().newInstance()
        for (field in clazz.declaredFields) {
            val value = element.jsonObject[field.name] as? JsonPrimitive ?: continue

            when (field.wrapperType) {
                Int::class.java -> field[obj] = value.intOrNull ?: continue
                Double::class.java -> field[obj] = value.doubleOrNull ?: continue
                String::class.java -> field[obj] = value.content
                Boolean::class.java -> field[obj] = value.booleanOrNull ?: continue
            }
        }

        set.add(obj)
    }

    return set
}