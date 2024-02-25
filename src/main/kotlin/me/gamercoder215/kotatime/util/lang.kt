package me.gamercoder215.kotatime.util

import me.gamercoder215.kotatime.storage.StorageManager
import me.gamercoder215.kotatime.ui.lang
import java.util.*

fun isSupported(lang: String): Boolean {
    val suffix = if (lang == "en") "" else "_$lang"

    StorageManager::class.java.getResourceAsStream("/lang/kotatime$suffix.properties").use {
        return it != null
    }
}

fun lang(key: String, vararg args: Any = arrayOf()): String {
    val p = Properties()
    val locale = Locale(lang)
    val suffix = if (lang == "en") "" else "_$lang"

    StorageManager::class.java.getResourceAsStream("/lang/kotatime$suffix.properties").use {
        p.load(it)
    }

    return if (args.isEmpty()) p.getProperty(key) else String.format(locale, p.getProperty(key), *args)
}