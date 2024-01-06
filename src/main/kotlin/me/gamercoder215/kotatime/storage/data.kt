package me.gamercoder215.kotatime.storage

import me.gamercoder215.kotatime.ICON_URL
import kotlin.properties.Delegates

// WakaTime Data

class Language {
    var name = "Unknown"
    var color = "#000000"
}

// WakaTime User Data

object WUser {
    var bio = ""
    var created_at = "1970-01-01T00:00:00Z"
    var modified_at = "1970-01-01T00:00:00Z"
    var email: String? = null
    var github_username: String? = null
    var profile_url = "https://wakatime.com"
    var photo = ICON_URL
    var has_premium_features = false
    var twitter_username: String? = null
    var weekday_start = 0
}

object TimeSinceToday {
    var decimal = "0.0"
    var is_up_to_date = true
}

// WakaTime User Data (Classes)

class Rank {
    var rank = 0
    var page = 0
    var language = "Unknown"
}

class Machine {
    var id = "00000000-0000-0000-0000-000000000000"
    var name = "Unknown"
}

class Project {
    var id = "00000000-0000-0000-0000-000000000000"
    var name = "Unknown"
    var created_at = "1970-01-01T00:00:00Z"
    var color = "#000000"
    var has_public_url = false
    var repository: ProjectRepository? = null
}

// WakaTime Stats Data

class Stats(
    val info: StatsInfo,
    val editors: Set<Stat>,
    val categories: Set<Stat>,
    val languages: Set<Stat>,
    val operating_systems: Set<Stat>
)

class StatsInfo {
    var is_up_to_date = false
    var range = "all_time"
    var daily_average = 0
    var total_seconds = 0.0
    var total_seconds_including_other_language = 0.0
}

class Stat {
    var total_seconds = 0.0
    var percent = 100.0
    var name = "Unknown"
}

// GitHub

class ProjectRepository {
    var default_branch = "master"
    var description = ""
    var homepage: String? = null
    var html_url = "https://github.com"
    var image_icon_url: String? = null
    var provider = "github"
    var star_count = 0
    var fork_count = 0
    var watch_count = 0
}

class CommitData {
    var total = 0
    var branch = "master"

    var commits: Set<Commit> = mutableSetOf()
}

class Commit {
    var author_avatar_url = ICON_URL
    var author_date = "1970-01-01T00:00:00Z"
    var author_name = "Unknown"
    var author_username = "Unknown"
    var author_email = ""
    var branch = "master"
    var truncated_hash = "0000000"
    var hash = "0000000000000000000000000000000000000000"
    var html_url = "https://github.com"
    var human_readable_date = "Jan 1, 1970"
    var human_readable_natural_date = "0 seconds ago"
    var message = ""
    var ref = "refs/heads/master"
    var total_seconds = 0

    override fun equals(other: Any?): Boolean {
        if (other !is Commit) return false
        return hash == other.hash
    }

    override fun hashCode(): Int = hash.hashCode()
}