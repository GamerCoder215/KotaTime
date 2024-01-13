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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.kanro.compose.jetbrains.expui.control.ProgressBar
import org.jetbrains.skia.Image
import java.io.File

// Utility Functions

@Composable
fun smallSpacer() = Spacer(Modifier.size(14.dp))

@Composable
fun mediumSpacer() = Spacer(Modifier.size(28.dp))

@Composable
fun largeSpacer() = Spacer(Modifier.size(54.dp))

@Composable
fun themedPainterResource(light: String, dark: String) = painterResource(if (darkMode) dark else light)

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

val Number.withAlpha: Long
    get() {
        return toLong() or 0xFF000000
    }

val String.asImage: ImageBitmap
    get() {
        val stream = File(this).inputStream()
        return Image.makeFromEncoded(stream.readBytes()).toComposeImageBitmap()
    }

fun Modifier.background(value: Number, shape: Shape = RectangleShape) = background(Color(value.withAlpha), shape)