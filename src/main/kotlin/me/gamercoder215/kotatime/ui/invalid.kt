package me.gamercoder215.kotatime.ui

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
fun noAPIKey() {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.size(24.dp))

        Icon("assets/svg/warning.svg")
        Label("No API key found!", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = TextUnit(20f, TextUnitType.Sp)))

        Spacer(Modifier.size(14.dp))
        Label("Please add your API key to \"${WAKATIME_FILE.absolutePath}\" and restart the app.")

        Spacer(Modifier.size(14.dp))
        Label("Example File:")

        Spacer(Modifier.size(28.dp))

        TextArea(EXAMPLE_CONFIG, onValueChange = {}, readOnly = true, modifier = Modifier.fillMaxWidth(fraction = 0.8F).height(100.dp))

        Spacer(Modifier.size(14.dp))
        Row {
            Label("View your API Key at ")
            ExternalLink(API_KEY_URL, onClick = { Desktop.getDesktop().browse(URI.create(API_KEY_URL)) })
        }
    }
}

@Composable
fun invalidAPIKey() {
    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.size(24.dp))

        Icon("assets/svg/warning.svg")
        Label("Invalid API Key", fontWeight = FontWeight.Bold, fontSize = TextUnit(20f, TextUnitType.Sp))

        Spacer(Modifier.size(14.dp))
        Label("Please add a valid API key to \"${WAKATIME_FILE.absolutePath}\" and restart the app.")

        Spacer(Modifier.size(14.dp))
        Label("Example Valid File:")

        Spacer(Modifier.size(28.dp))
        TextArea(EXAMPLE_CONFIG, onValueChange = {}, readOnly = true, modifier = Modifier.fillMaxWidth(fraction = 0.8F).height(100.dp))

        Spacer(Modifier.size(14.dp))
        Row {
            Label("View your API Key at ")
            ExternalLink(API_KEY_URL, onClick = { Desktop.getDesktop().browse(URI.create(API_KEY_URL)) })
        }
    }
}

