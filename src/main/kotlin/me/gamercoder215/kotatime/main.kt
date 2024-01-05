package me.gamercoder215.kotatime


import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import me.gamercoder215.kotatime.ui.invalidAPIKey
import me.gamercoder215.kotatime.ui.noAPIKey

const val EXAMPLE_CONFIG = "[settings]\napi_key = YOUR_WAKATIME_API_KEY"

@Composable
fun App() {
    if (API_KEY == null)
        return noAPIKey()

    if (!VALID_API_KEY)
        return invalidAPIKey()

    println(API_KEY)
}

fun main() = application {
    Window(
        title = NAME,
        icon = painterResource(ICON_URL),
        onCloseRequest = ::exitApplication
    ) {
        App()
    }
}