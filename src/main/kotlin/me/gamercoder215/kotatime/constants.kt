package me.gamercoder215.kotatime

import java.io.File
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.file.FileSystems

val SEPARATOR: String
    get() = FileSystems.getDefault().separator
val WAKATIME_FILE = File("${System.getProperty("user.home")}$SEPARATOR.wakatime.cfg")

const val NAME = "KotaTime"
const val VERSION = "1.0.0"
const val ICON_URL = "assets/icon/icon128.png"

val client: HttpClient = HttpClient.newBuilder()
    .version(HttpClient.Version.HTTP_2)
    .build()

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

        val res = client.send(HttpRequest.newBuilder()
            .uri(URI.create(USER_URL))
            .GET()
            .build(), HttpResponse.BodyHandlers.ofString())

        return res.statusCode() != 401
    }

// URLs

val USER_URL = "https://wakatime.com/api/user/current?api_key=$API_KEY"