package me.gamercoder215.kotatime.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import io.kanro.compose.jetbrains.expui.control.ExternalLink
import io.kanro.compose.jetbrains.expui.control.Icon
import io.kanro.compose.jetbrains.expui.control.Label
import io.kanro.compose.jetbrains.expui.control.TextArea
import me.gamercoder215.kotatime.EXAMPLE_CONFIG
import me.gamercoder215.kotatime.WAKATIME_FILE
import java.awt.Desktop
import java.net.URI

const val API_KEY_URL = "https://wakatime.com/api-key"

@Composable
private fun template(
    title: String,
    message: String,
    extra: @Composable ColumnScope.() -> Unit = {}
) {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        mediumSpacer()

        Icon(themedPainterResource("assets/svg/warning.svg", "assets/svg/warning_dark.svg"))
        Label(title, style = TextStyle(fontWeight = FontWeight.Bold, fontSize = TextUnit(20f, TextUnitType.Sp)))

        smallSpacer()
        Label(message)

        extra()
    }
}

@Composable
private fun apiFileExample() {
    smallSpacer()
    Label("Example File:")

    mediumSpacer()
    TextArea(EXAMPLE_CONFIG, onValueChange = {}, readOnly = true, modifier = Modifier.fillMaxWidth(fraction = 0.8F).height(100.dp))

    smallSpacer()
    Row {
        Label("View your API Key at ")
        ExternalLink(API_KEY_URL, onClick = { Desktop.getDesktop().browse(URI.create(API_KEY_URL)) })
    }
}

@Composable
@Preview
fun noAPIKey() {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        template(
            "No API key found!",
            "Please add your API key to \"${WAKATIME_FILE.absolutePath}\" and restart the app.",
        ) { apiFileExample() }
    }
}

@Composable
@Preview
fun invalidAPIKey() {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        template(
            "Invalid API key!",
            "Please add a valid API key to \"${WAKATIME_FILE.absolutePath}\" and restart the app.",
        ) { apiFileExample() }
    }
}

@Composable
@Preview
fun offline() {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        template(
            "Offline!",
            "KotaTime does not currently have any saved data to display. Please connect to the internet and restart the app. Additionally, check that WakaTime isn't down."
        )
    }
}