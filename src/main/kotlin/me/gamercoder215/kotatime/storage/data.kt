package me.gamercoder215.kotatime.storage

import kotlinx.serialization.json.*
import me.gamercoder215.kotatime.ICON_URL
import me.gamercoder215.kotatime.util.load
import me.gamercoder215.kotatime.util.wrapperType
import java.io.Serial
import java.io.Serializable
import java.lang.reflect.Modifier
import kotlin.properties.Delegates

// WakaTime Data

class Language : Serializable {
    var name = "Unknown"
    var color: Int = 0xffffff

    companion object {
        @Serial
        private const val serialVersionUID: Long = 1L
    }
}

// WakaTime User Data

object WUser {
    var username = "Unknown"
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

class Rank : Serializable {
    var rank = 0
    var page = 0
    var language: String? = null

    companion object {
        @Serial
        private const val serialVersionUID: Long = 1L
    }
}

class Machine : Serializable {
    var id = "00000000-0000-0000-0000-000000000000"
    var name = "Unknown"

    companion object {
        @Serial
        private const val serialVersionUID: Long = 1L
    }
}

class Project : Serializable {
    var id = "00000000-0000-0000-0000-000000000000"
    var name = "Unknown"
    var created_at = "1970-01-01T00:00:00Z"
    var color = "#000000"
    var has_public_url = false
    var repository: ProjectRepository? = null

    companion object {
        @Serial
        private const val serialVersionUID: Long = 1L
    }
}

// WakaTime Stats Data

class Stats(
    val info: StatsInfo,
    val editors: Set<Stat>,
    val categories: Set<Stat>,
    val languages: Set<Stat>,
    val operating_systems: Set<Stat>
) : Serializable {
    companion object {
        @Serial
        private const val serialVersionUID: Long = 1L
    }
}

class StatsInfo : Serializable {
    var is_up_to_date = false
    var range = "all_time"
    var daily_average = 0
    var total_seconds = 0.0
    var total_seconds_including_other_language = 0.0

    companion object {
        @Serial
        private const val serialVersionUID: Long = 1L
    }
}

class Stat : Serializable {
    var total_seconds = 0.0
    var percent = 100.0
    var name = "Unknown"

    companion object {
        @Serial
        private const val serialVersionUID: Long = 1L
    }
}

// GitHub

class ProjectRepository : Serializable {
    var default_branch = "master"
    var description = ""
    var homepage: String? = null
    var html_url = "https://github.com"
    var image_icon_url: String? = null
    var provider = "github"
    var star_count = 0
    var fork_count = 0
    var watch_count = 0

    companion object {
        @Serial
        private const val serialVersionUID: Long = 1L
    }
}

class CommitData : Serializable {
    var total = 0
    var branch = "master"
    var commits: Set<Commit> = mutableSetOf()

    companion object {
        @Serial
        private const val serialVersionUID: Long = 1L
    }
}

class Commit : Serializable {
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

    companion object {
        @Serial
        private const val serialVersionUID: Long = 1L
    }

    override fun equals(other: Any?): Boolean {
        if (other !is Commit) return false
        return hash == other.hash
    }

    override fun hashCode(): Int = hash.hashCode()
}