package me.gamercoder215.kotatime

import kotlinx.serialization.json.Json
import me.gamercoder215.kotatime.storage.USER_URL
import java.io.File
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.file.FileSystems

val WAKATIME_FILE = File("${System.getProperty("user.home")}${File.separator}.wakatime.cfg")

const val NAME = "KotaTime"
const val VERSION = "1.0.0"
const val ICON_URL = "assets/icon/icon128.png"
val USER_AGENT = "$NAME/$VERSION ${System.getProperty("os.name")} ${System.getProperty("os.version")} ${System.getProperty("os.arch")} (Java ${System.getProperty("java.version")})"

val client: HttpClient = HttpClient.newBuilder()
    .version(HttpClient.Version.HTTP_2)
    .build()

val json = Json {
    isLenient = true
    prettyPrint = true
}

// API

val API_KEY: String?
    get() {
        if (!WAKATIME_FILE.exists()) return null

        return (WAKATIME_FILE.readLines()
            .firstOrNull { it.startsWith("api_key") } ?: return null)
            .split("=")
            .last()
            .trim()
    }

val VALID_API_KEY: Boolean
    get() {
        if (API_KEY == null) return false

        val res = client.send(
            HttpRequest.newBuilder()
                .uri(URI.create(USER_URL))
                .GET()
                .header("User-Agent", USER_AGENT)
                .build(), HttpResponse.BodyHandlers.ofString()
        )

        return res.statusCode() != 401
    }