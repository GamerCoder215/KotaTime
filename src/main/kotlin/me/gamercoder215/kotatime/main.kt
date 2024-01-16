package me.gamercoder215.kotatime


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.FrameWindowScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.kanro.compose.jetbrains.expui.control.Label
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.gamercoder215.kotatime.storage.IS_ONLINE
import me.gamercoder215.kotatime.storage.StorageManager
import me.gamercoder215.kotatime.storage.StorageManager.saveGlobalData
import me.gamercoder215.kotatime.storage.loadFromNetwork
import me.gamercoder215.kotatime.storage.loadWakatimeDataFromNetwork
import me.gamercoder215.kotatime.ui.*
import me.gamercoder215.kotatime.ui.user.toolbar
import me.gamercoder215.kotatime.ui.user.user
import me.gamercoder215.kotatime.util.info
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent

const val EXAMPLE_CONFIG = "[settings]\napi_key = YOUR_WAKATIME_API_KEY"

suspend fun loadData(whenDone: () -> Unit = {}) = withContext(Dispatchers.IO) {
    if (API_KEY == null) return@withContext

    val job1 = launch {
        if (StorageManager.isGlobalEmpty) {
            loadWakatimeDataFromNetwork()
            saveGlobalData()
        } else
            StorageManager.loadGlobalData()
    }

    val job2 = launch {
        if (StorageManager.isEmpty) {
            loadFromNetwork()
            StorageManager.save()
        }

        StorageManager.load()
    }

    job1.join()
    job2.join()

    withContext(Dispatchers.Default) {
        whenDone()
    }
}

@Composable
fun FrameWindowScope.App() {
    if (API_KEY == null)
        return noAPIKey()

    if (!IS_ONLINE && StorageManager.isEmpty)
        return offline()

    if (!VALID_API_KEY)
        return invalidAPIKey()

    var loaded by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        loadData {
            loaded = true
        }
    }

    if (!loaded) {
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            largeSpacer()
            InfiniteProgressBar()
            Label("Loading Data...")
        }

        return
    }

    // UI Load

    Box(
        Modifier.fillMaxSize().background(if (darkMode) DARK_BACKGROUND else LIGHT_BACKGROUND),
        Alignment.TopStart
    ) {
        toolbar()
        user()
    }
}

var recompose: () -> Unit = {}
lateinit var window: ComposeWindow

fun main() {
    info("Starting $NAME v$VERSION")

    application {
        val (trigger, triggerRecompose) = remember { mutableStateOf(true) }
        recompose = { triggerRecompose(!trigger) }

        Window(
            title = NAME,
            icon = painterResource(ICON_URL),
            onCloseRequest = ::exitApplication
        ) {
            me.gamercoder215.kotatime.window = window

            window.addComponentListener(object : ComponentAdapter() {
                override fun componentResized(e: ComponentEvent) = recompose()
            })

            App()
        }
    }
}