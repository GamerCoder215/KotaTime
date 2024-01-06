package me.gamercoder215.kotatime.storage

import kotlin.properties.Delegates

// WakaTime Data

class Language {
    var name by Delegates.notNull<String>()
    var color by Delegates.notNull<String>()
}

// WakaTime User Data

object WUser {
    var bio by Delegates.notNull<String>()
    var created_at by Delegates.notNull<String>()
    var modified_at by Delegates.notNull<String>()
    var email by Delegates.notNull<String>()
    var github_username by Delegates.notNull<String>()
    var profile_url by Delegates.notNull<String>()
    var photo by Delegates.notNull<String>()
    var has_premium_features by Delegates.notNull<Boolean>()
    var weekday_start by Delegates.notNull<Int>()
}

object TimeSinceToday {
    var decimal by Delegates.notNull<String>()
    var is_up_to_date by Delegates.notNull<Boolean>()
}

object Rank {
    var rank by Delegates.notNull<Int>()
    var page by Delegates.notNull<Int>()
    var language by Delegates.notNull<String>()
}

// WakaTime User Data (Classes)

class Machine {
    var id by Delegates.notNull<String>()
    var name by Delegates.notNull<String>()
}

class Project {
    var id by Delegates.notNull<String>()
    var name by Delegates.notNull<String>()
    var created_at by Delegates.notNull<String>()
    var color by Delegates.notNull<String>()
    var has_public_url by Delegates.notNull<Boolean>()
    var repository: ProjectRepository? = null
}

// WakaTime Stats Data

class Stats(
    val info: StatsInfo,
    val categories: List<Stat>,
    val languages: List<Stat>,
    val operating_systems: List<Stat>
)

class StatsInfo {
    var is_up_to_date by Delegates.notNull<Boolean>()
    var range by Delegates.notNull<String>()
    var daily_average by Delegates.notNull<Int>()
    var total_seconds by Delegates.notNull<Double>()
    var total_seconds_including_other_language by Delegates.notNull<Double>()
}

class Stat {
    var total_seconds by Delegates.notNull<Double>()
    var percent by Delegates.notNull<Double>()
    var name by Delegates.notNull<String>()
}

// GitHub

class ProjectRepository {
    var default_branch by Delegates.notNull<String>()
    var description by Delegates.notNull<String>()
    var homepage by Delegates.notNull<String>()
    var html_url by Delegates.notNull<String>()
    var image_icon_url by Delegates.notNull<String>()
    var provider by Delegates.notNull<String>()
    var star_count by Delegates.notNull<Int>()
    var fork_count by Delegates.notNull<Int>()
    var watch_count by Delegates.notNull<Int>()
}

class CommitData {
    var total by Delegates.notNull<Int>()
    var branch by Delegates.notNull<String>()

    var commits: Set<Commit> = mutableSetOf()
}

class Commit {
    var author_avatar_url by Delegates.notNull<String>()
    var author_date by Delegates.notNull<String>()
    var author_name by Delegates.notNull<String>()
    var author_username by Delegates.notNull<String>()
    var author_email by Delegates.notNull<String>()
    var branch by Delegates.notNull<String>()
    var truncated_hash by Delegates.notNull<String>()
    var hash by Delegates.notNull<String>()
    var html_url by Delegates.notNull<String>()
    var human_readable_date by Delegates.notNull<String>()
    var human_readable_natural_date by Delegates.notNull<String>()
    var message by Delegates.notNull<String>()
    var ref by Delegates.notNull<String>()
    var total_seconds by Delegates.notNull<Int>()

    override fun equals(other: Any?): Boolean {
        if (other !is Commit) return false
        return hash == other.hash
    }

    override fun hashCode(): Int = hash.hashCode()
}