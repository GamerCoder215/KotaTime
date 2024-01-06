package me.gamercoder215.kotatime


import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import me.gamercoder215.kotatime.storage.IS_ONLINE
import me.gamercoder215.kotatime.storage.StorageManager
import me.gamercoder215.kotatime.ui.invalidAPIKey
import me.gamercoder215.kotatime.ui.noAPIKey
import me.gamercoder215.kotatime.ui.offline

const val EXAMPLE_CONFIG = "[settings]\napi_key = YOUR_WAKATIME_API_KEY"

@Composable
fun App() {
    if (API_KEY == null)
        return noAPIKey()

    if (!IS_ONLINE && StorageManager.isEmpty)
        return offline()

    if (!VALID_API_KEY)
        return invalidAPIKey()
}

fun main() = application {
    var loaded by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        // loadData()
        loaded = true
    }

    Window(
        title = NAME,
        icon = painterResource(ICON_URL),
        onCloseRequest = ::exitApplication
    ) {
        App()
    }


}