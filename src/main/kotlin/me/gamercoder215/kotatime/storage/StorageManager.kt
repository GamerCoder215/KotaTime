package me.gamercoder215.kotatime.storage

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.JsonObject
import me.gamercoder215.kotatime.ICON_URL
import me.gamercoder215.kotatime.json
import me.gamercoder215.kotatime.util.*
import java.io.File

object StorageManager {

    val folder = File("${System.getProperty("user.home")}${File.separator}.kotatime")

    val settings: MutableMap<String, String> = mutableMapOf()

    val languages: MutableSet<Language> = mutableSetOf()

    val machines: MutableSet<Machine> = mutableSetOf()
    val projects: MutableSet<Project> = mutableSetOf()
    val loadedStats: MutableMap<String, Stats> = mutableMapOf()
    val ranks: MutableSet<Rank> = mutableSetOf()
    val commits: MutableMap<String, CommitData> = mutableMapOf()

    val isGlobalEmpty: Boolean
        get() {
            val langs = File(folder, "languages.dat")
            return !langs.exists()
        }

    val isEmpty: Boolean
        get() {
            val user = File(folder, "user")
            return !user.exists() || user.list()?.isEmpty() ?: true
        }

    fun clear() {
        settings.clear()

        languages.clear()

        machines.clear()
        projects.clear()
        loadedStats.clear()
        ranks.clear()
        commits.clear()

        WUser.clearFields()
        TimeSinceToday.clearFields()
    }

    // Saving

    fun saveSettings() {
        // Settings
        val settings = File(folder, "settings.json")
        if (!settings.exists()) settings.createNewFile()
        settings.writeText(json.encodeToString(this.settings))
    }

    fun saveGlobalData() {
        if (languages.isNotEmpty()) {
            // Languages
            val langs = File(folder, "languages.dat")
            if (!langs.exists()) langs.createNewFile()
            langs.serialize(languages)
        }
    }

    fun savePhoto() {
        val user = File(folder, "user")
        if (!user.exists()) user.mkdirs()

        val picture = File(user, "profile.png")
        if (!picture.exists()) {
            picture.createNewFile()
            picture.writeBytes(WUser.photo.asData)
        }

        WUser.photo = picture.absolutePath
    }

    fun save() {
        if (!folder.exists()) folder.mkdirs()
        saveSettings()
        saveGlobalData()

        // User
        val user = File(folder, "user")
        if (!user.exists()) user.mkdirs()

        savePhoto()

        val userData = File(user, "data.json")
        if (!userData.exists()) userData.createNewFile()
        userData.writeText(json.encodeToString(WUser.toJson()))

        val timeSinceToday = File(user, "time_since_today.json")
        if (!timeSinceToday.exists()) timeSinceToday.createNewFile()
        timeSinceToday.writeText(json.encodeToString(TimeSinceToday.toJson()))

        // Machines
        val machs = File(user, "machines.dat")
        if (!machs.exists()) machs.createNewFile()
        machs.serialize(machines)

        // Ranks
        val rank = File(user, "ranks.dat")
        if (!rank.exists()) rank.createNewFile()
        rank.serialize(ranks)

        // Projects
        val projects = File(user, "projects")
        if (!projects.exists()) projects.mkdirs()

        for (proj in this.projects) {
            val projFolder = File(projects, proj.id)
            if (!projFolder.exists()) projFolder.mkdirs()

            val projData = File(projFolder, "data.dat")
            if (!projData.exists()) projData.createNewFile()
            projData.serialize(proj)

            val commits = File(projFolder, "commits.dat")
            if (!commits.exists()) commits.createNewFile()
            commits.serialize(this.commits[proj.id])
        }

        // Stats
        val stats = File(folder, "stats")
        if (!stats.exists()) stats.mkdirs()

        for ((time, stat) in loadedStats) {
            val statFile = File(stats, "$time.dat")
            if (!statFile.exists()) statFile.createNewFile()

            statFile.serialize(stat)
        }
    }

    // Loading

    fun loadSettings() {
        // Settings
        val settings = File(folder, "settings.json")
        if (!settings.exists()) settings.createNewFile()
        this.settings.putAll(json.decodeFromString(settings.readText()))
    }

    fun loadGlobalData() {
        // Languages
        if (languages.isEmpty()) {
            val langs = File(folder, "languages.dat")
            if (!langs.exists()) langs.createNewFile()
            languages.addAll(langs.deserialize<Set<Language>>() ?: emptySet())
        }
    }

    fun load() {
        if (!folder.exists()) folder.mkdirs()
        loadSettings()
        loadGlobalData()

        // User
        val user = File(folder, "user")
        if (!user.exists()) user.mkdirs()

        val picture = File(user, "profile.png")
        WUser.photo = if (picture.exists()) picture.absolutePath else ICON_URL

        val userData = File(user, "data.json")
        if (!userData.exists()) userData.createNewFile()
        WUser.fromJson(json.decodeFromString(userData.readText()))

        val timeSinceToday = File(user, "time_since_today.json")
        if (!timeSinceToday.exists()) timeSinceToday.createNewFile()
        TimeSinceToday.fromJson(json.decodeFromString(timeSinceToday.readText()))

        // Machines
        val machs = File(folder, "machines.dat")
        if (!machs.exists()) machs.createNewFile()
        machines.addAll(machs.deserialize<Set<Machine>>() ?: emptySet())

        // Ranks
        val rank = File(folder, "ranks.dat")
        if (!rank.exists()) rank.createNewFile()
        ranks.addAll(rank.deserialize<Set<Rank>>() ?: emptySet())

        // Projects
        val projects = File(folder, "projects")
        if (!projects.exists()) projects.mkdirs()
        for (projFile in projects.listFiles() ?: emptyArray()) {
            val proj = File(projFile, "data.dat")
            if (!proj.exists()) proj.createNewFile()
            val projObj = proj.deserialize<Project>() ?: continue
            this.projects.add(projObj)

            val commits = File(projFile, "commits.dat")
            if (!commits.exists()) commits.createNewFile()
            this.commits[projObj.id] = commits.deserialize<CommitData>() ?: continue
        }

        // Stats
        val stats = File(folder, "stats")
        if (!stats.exists()) stats.mkdirs()
        for (statFile in stats.listFiles() ?: emptyArray()) {
            val stat = statFile.deserialize<Stats>() ?: continue
            loadedStats[stat.info.range] = stat
        }
    }
}