package me.gamercoder215.kotatime

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.jetbrains.skia.Image

@Composable
@Preview
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