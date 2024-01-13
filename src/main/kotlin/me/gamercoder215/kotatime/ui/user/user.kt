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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.FrameWindowScope
import io.kanro.compose.jetbrains.expui.control.Icon
import io.kanro.compose.jetbrains.expui.control.ToolBarActionButton
import io.kanro.compose.jetbrains.expui.theme.DarkTheme
import me.gamercoder215.kotatime.GITHUB_URL
import me.gamercoder215.kotatime.WAKATIME_URL
import me.gamercoder215.kotatime.storage.WUser
import me.gamercoder215.kotatime.ui.TOOLBAR_BUTTON_COLORS
import me.gamercoder215.kotatime.ui.asImage
import me.gamercoder215.kotatime.ui.darkMode
import me.gamercoder215.kotatime.ui.themedPainterResource
import java.awt.Desktop
import java.net.URI

@Composable
fun BoxScope.user() {

}

val box = Modifier.size(48.dp)
val innerBox = Modifier
    .requiredSize(32.dp)
    .clip(CircleShape)

@Composable
fun BoxScope.toolbar() {
    Column(
        Modifier.fillMaxHeight().width(64.dp).padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var toolbarSelected by remember { mutableStateOf(0) }

        ToolBarActionButton(
            selected = toolbarSelected == 0, modifier = box, colors = TOOLBAR_BUTTON_COLORS,
            onClick = { toolbarSelected = 0 }
        ) {
            Image(
                WUser.photo.asImage, "Profile Picture",
                modifier = innerBox.border(2.dp, Color.Gray, CircleShape)
            )
        }

        ToolBarActionButton(
            modifier = box, colors = TOOLBAR_BUTTON_COLORS,
            onClick = {
                darkMode = !darkMode
            }
        ) {
            if (darkMode)
                Icon(painterResource("assets/svg/dark.svg"), contentDescription = "Dark Mode", modifier = innerBox)
            else
                Icon(painterResource("assets/svg/light.svg"), contentDescription = "Light Mode", modifier = innerBox)
        }

        Spacer(box.fillMaxHeight())

        ToolBarActionButton(
            modifier = box, colors = TOOLBAR_BUTTON_COLORS,
            onClick = {
                Desktop.getDesktop().browse(URI.create(WAKATIME_URL))
            }
        ) {
            Icon(
                themedPainterResource("assets/svg/wakatime.svg", "assets/svg/wakatime_dark.svg"),
                contentDescription = "WakaTime Dashboard", modifier = innerBox
            )
        }
        ToolBarActionButton(
            modifier = box, colors = TOOLBAR_BUTTON_COLORS,
            onClick = {
                Desktop.getDesktop().browse(URI.create(GITHUB_URL))
            }
        ) {
            Icon(painterResource("assets/svg/github.svg"), contentDescription = "KotaTime GitHub", modifier = innerBox)
        }

    }
}