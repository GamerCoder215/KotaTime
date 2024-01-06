package me.gamercoder215.kotatime.ui

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import io.kanro.compose.jetbrains.expui.control.ProgressBar

@Composable
fun InfiniteProgressBar() {
    val transition = rememberInfiniteTransition()
    val currentOffset by transition.animateFloat(0f, 1f, infiniteRepeatable(animation = keyframes {
        durationMillis = 1000
    }))

    ProgressBar(currentOffset)
}