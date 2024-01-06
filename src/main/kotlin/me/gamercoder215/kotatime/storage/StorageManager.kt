package me.gamercoder215.kotatime.storage

import java.io.File

object StorageManager {

    val FOLDER = File("${System.getProperty("user.home")}${File.separator}.kotatime")

    val languages: MutableSet<Language> = mutableSetOf()

    val machines: MutableSet<Machine> = mutableSetOf()
    val projects: MutableSet<Project> = mutableSetOf()
    val loadedStats: MutableMap<String, Stats> = mutableMapOf()
    val ranks: MutableSet<Rank> = mutableSetOf()

    val isEmpty: Boolean
        get() =
            WUser.email == null && languages.isEmpty() && machines.isEmpty() && projects.isEmpty() && loadedStats.isEmpty() && ranks.isEmpty()

}