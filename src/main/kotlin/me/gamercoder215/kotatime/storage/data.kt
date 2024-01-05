package me.gamercoder215.kotatime.storage

interface Reloadable {

    val fileName: String

    fun reload(): Reloadable

}

// WakaTime User Data

data class WUser(
    val bio: String,
    val created_at: String,
    val email: String,
    val github_username: String,
    val weekday_start: Int,
    val profile_url: String,
    val photo: String,
)

data class TimeSinceToday(
    val decimal: String,
    val startDate: String,
    val upToDate: Boolean
)

data class Duration(
    val projectName: String,
    val timestamp: Long,
    val seconds: Double
)

// GitHub

// TODO GitHub Data