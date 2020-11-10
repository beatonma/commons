package org.beatonma.commons.compose.animation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import org.beatonma.commons.compose.util.lerp

fun Float.lerpBetween(start: Color, end: Color): Color = start.lerp(end, this)
fun Float.lerpBetween(start: PaddingValues, end: PaddingValues): PaddingValues =
    start.lerp(end, this)

fun Float.lerpBetween(start: Dp, end: Dp): Dp = start.lerp(end, this)
