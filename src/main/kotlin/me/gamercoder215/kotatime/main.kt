package me.gamercoder215.kotatime

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.kanro.compose.jetbrains.expui.control.Icon
import io.kanro.compose.jetbrains.expui.control.ToolBarActionButton
import io.kanro.compose.jetbrains.expui.style.LocalErrorAreaColors

@Composable
fun App() {

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