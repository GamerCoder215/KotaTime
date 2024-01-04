package me.gamercoder215.kotatime.server

import io.javalin.Javalin
import me.gamercoder215.kotatime.Env
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

val client: HttpClient = HttpClient.newBuilder()
    .version(HttpClient.Version.HTTP_2)
    .build()

var server: Javalin? = null
var accessToken: String? = null

fun start() {
    server = Javalin.create { config ->
        config.staticFiles.add("/assets")
    }.get("/") { ctx ->
        val code = ctx.queryParam("code") ?: return@get

        ctx.html("Code: $code")
    }.start(Env.SERVER_PORT)
}

fun getAccessToken(code: String, token: (String) -> Unit) {
    val request = HttpRequest.newBuilder()
        .uri(URI.create("https://wakatime.com/oauth/token?client_id=${Env["CLIENT_ID"]}&client_secret=${Env["CLIENT_SECRET"]}&grant_type=authorization_code&code=$code"))
        .POST(HttpRequest.BodyPublishers.noBody())
        .build()

    val responseC = client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
    responseC.whenComplete { response, _ ->
        token(response.body())
    }
}