package me.gamercoder215.kotatime.ui

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.FrameWindowScope
import io.kanro.compose.jetbrains.expui.control.ProgressBar
import io.kanro.compose.jetbrains.expui.control.ToolBarActionButton
import io.kanro.compose.jetbrains.expui.theme.DarkTheme
import org.jetbrains.skia.Image
import java.io.File

@Composable
fun smallSpacer() = Spacer(Modifier.size(14.dp))

@Composable
fun mediumSpacer() = Spacer(Modifier.size(28.dp))

@Composable
fun largeSpacer() = Spacer(Modifier.size(54.dp))

// Functions

@Composable
fun InfiniteProgressBar() {
    val transition = rememberInfiniteTransition()
    val currentOffset by transition.animateFloat(0f, 1f, infiniteRepeatable(animation = keyframes {
        durationMillis = 1000
    }))

    ProgressBar(currentOffset)
}

// Extensions

val String.asImage: ImageBitmap
    get() {
        val stream = File(this).inputStream()
        return Image.makeFromEncoded(stream.readBytes()).toComposeImageBitmap()
    }

fun Modifier.background(value: Long, shape: Shape = RectangleShape) = background(Color(value), shape)