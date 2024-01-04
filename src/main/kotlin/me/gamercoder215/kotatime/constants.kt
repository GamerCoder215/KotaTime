package me.gamercoder215.kotatime

import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.dotenv
import org.jetbrains.skiko.OS
import org.jetbrains.skiko.hostOs

const val NAME = "KotaTime"
const val VERSION = "1.0.0"
const val ICON_URL = "assets/icon/icon128.png"

object Env {

    private val dotenv: Dotenv = dotenv()

    operator fun get(key: String): String = dotenv.get(key)

}