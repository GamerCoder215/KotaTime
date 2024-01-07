package me.gamercoder215.kotatime.ui.user

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.FrameWindowScope
import io.kanro.compose.jetbrains.expui.control.Icon
import io.kanro.compose.jetbrains.expui.control.ToolBarActionButton
import io.kanro.compose.jetbrains.expui.theme.DarkTheme
import me.gamercoder215.kotatime.GITHUB_URL
import me.gamercoder215.kotatime.WAKATIME_URL
import me.gamercoder215.kotatime.storage.WUser
import me.gamercoder215.kotatime.ui.asImage
import java.awt.Desktop
import java.net.URI

@Composable
fun FrameWindowScope.user() {

}

val box = Modifier.size(32.dp)
val innerBox = Modifier
    .requiredSize(24.dp)
    .clip(CircleShape)

@Composable
fun FrameWindowScope.toolbar() {
    Column(
        Modifier.fillMaxHeight().width(48.dp).padding(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var toolbarSelected by remember { mutableStateOf(0) }

        ToolBarActionButton(
            selected = toolbarSelected == 0, modifier = box,
            onClick = { toolbarSelected = 0 }
        ) {
            Image(
                WUser.photo.asImage, "Profile Picture",
                modifier = innerBox.border(2.dp, Color.Gray, CircleShape)
            )
        }

        ToolBarActionButton(
            modifier = box,
            onClick = {
                Desktop.getDesktop().browse(URI.create(WAKATIME_URL))
            }
        ) {
            Icon("assets/svg/wakatime.svg", contentDescription = "WakaTime Dashboard", modifier = innerBox)
        }
        ToolBarActionButton(
            modifier = box,
            onClick = {
                Desktop.getDesktop().browse(URI.create(GITHUB_URL))
            }
        ) {
            Icon("assets/svg/github.svg", contentDescription = "KotaTime GitHub", modifier = innerBox)
        }

    }
}