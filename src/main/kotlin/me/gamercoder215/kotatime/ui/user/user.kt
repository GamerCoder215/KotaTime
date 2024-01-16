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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.kanro.compose.jetbrains.expui.control.ToolBarActionButton
import me.gamercoder215.kotatime.GITHUB_URL
import me.gamercoder215.kotatime.WAKATIME_URL
import me.gamercoder215.kotatime.storage.WUser
import me.gamercoder215.kotatime.ui.*
import java.awt.Desktop
import java.net.URI

@Composable
fun BoxScope.user() {

}

@Composable
fun BoxScope.toolbar() {
    val box = Modifier
        .sizeIn(
            minHeight = 24.dp, minWidth = 24.dp,
            maxHeight = 48.dp, maxWidth = 48.dp
        )
        .size(8.vh)

    val innerBox = Modifier
        .sizeIn(
            minHeight = 16.dp, minWidth = 16.dp,
            maxHeight = 32.dp, maxWidth = 32.dp
        )
        .size(5.vh)
        .padding(8.dp)
        .clip(CircleShape)

    Column(
        Modifier.fillMaxHeight()
            .sizeIn(minWidth = 48.dp, maxWidth = 64.dp)
            .size(10.vh)
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(1.5.vh),
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

        Spacer(Modifier.weight(1F))

        ToolBarActionButton(
            modifier = box, colors = TOOLBAR_BUTTON_COLORS,
            onClick = {
                darkMode = !darkMode
            }
        ) {
            if (darkMode)
                Image(painterResource("assets/svg/dark.svg"), contentDescription = "Dark Mode", modifier = innerBox, contentScale = ContentScale.FillBounds)
            else
                Image(painterResource("assets/svg/light.svg"), contentDescription = "Light Mode", modifier = innerBox, contentScale = ContentScale.FillBounds)
        }

        ToolBarActionButton(
            modifier = box, colors = TOOLBAR_BUTTON_COLORS,
            onClick = {
                Desktop.getDesktop().browse(URI.create(WAKATIME_URL))
            }
        ) {
            Image(
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
            Image(themedPainterResource("assets/svg/github.svg", "assets/svg/github_dark.svg"), contentDescription = "KotaTime GitHub", modifier = innerBox)
        }

    }
}