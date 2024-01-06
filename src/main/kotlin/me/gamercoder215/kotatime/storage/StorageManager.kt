package me.gamercoder215.kotatime.storage

import kotlinx.serialization.encodeToString
import me.gamercoder215.kotatime.ICON_URL
import me.gamercoder215.kotatime.json
import me.gamercoder215.kotatime.util.*
import java.io.File

object StorageManager {

    val folder = File("${System.getProperty("user.home")}${File.separator}.kotatime")

    val languages: MutableSet<Language> = mutableSetOf()

    val machines: MutableSet<Machine> = mutableSetOf()
    val projects: MutableSet<Project> = mutableSetOf()
    val loadedStats: MutableMap<String, Stats> = mutableMapOf()
    val ranks: MutableSet<Rank> = mutableSetOf()
    val commits: MutableMap<String, CommitData> = mutableMapOf()

    val isEmpty: Boolean
        get() =
            WUser.email == null && machines.isEmpty() && projects.isEmpty() && loadedStats.isEmpty() && ranks.isEmpty() && commits.isEmpty()

    fun clear() {
        languages.clear()
        machines.clear()
        projects.clear()
        loadedStats.clear()
        ranks.clear()
        commits.clear()

        WUser.clearFields()
        TimeSinceToday.clearFields()
    }

    fun save() {
        if (!folder.exists()) folder.mkdirs()

        // Languages
        val langs = File(folder, "languages.dat")
        if (!langs.exists()) langs.createNewFile()
        langs.serialize(languages)

        // User
        val user = File(folder, "user")
        if (!user.exists()) user.mkdirs()

        val picture = File(user, "profile.png")
        if (!picture.exists()) picture.createNewFile()
        picture.writeBytes(WUser.photo.asData)

        val userData = File(user, "data.json")
        if (!userData.exists()) userData.createNewFile()
        userData.writeText(json.encodeToString(WUser.toJson()))

        val timeSinceToday = File(user, "time_since_today.json")
        if (!timeSinceToday.exists()) timeSinceToday.createNewFile()
        timeSinceToday.writeText(json.encodeToString(TimeSinceToday.toJson()))

        // Machines
        val machs = File(folder, "machines.dat")
        if (!machs.exists()) machs.createNewFile()
        machs.serialize(machines)

        // Ranks
        val rank = File(folder, "ranks.dat")
        if (!rank.exists()) rank.createNewFile()
        rank.serialize(ranks)

        // Projects
        val projects = File(folder, "projects")
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

    fun load() {
        if (!folder.exists()) folder.mkdirs()

        // Languages
        val langs = File(folder, "languages.dat")
        if (!langs.exists()) langs.createNewFile()
        languages.addAll(langs.deserialize<Set<Language>>() ?: emptySet())

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