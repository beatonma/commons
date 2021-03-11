package org.beatonma.commons.compose.animation

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import org.beatonma.commons.core.extensions.lerpBetween
import org.beatonma.commons.theme.compose.padding.Padding

fun Float.lerpBetween(start: Color, end: Color): Color = Color(
    alpha = lerpBetween(start.alpha, end.alpha),
    red = lerpBetween(start.red, end.red),
    green = lerpBetween(start.green, end.green),
    blue = lerpBetween(start.blue, end.blue),
)

fun Float.lerpBetween(start: Padding, end: Padding): Padding = Padding(
    start = lerpBetween(start.start, end.start),
    top = lerpBetween(start.top, end.top),
    end = lerpBetween(start.end, end.end),
    bottom = lerpBetween(start.bottom, end.bottom),
)

fun Float.lerpBetween(start: Dp, end: Dp): Dp = Dp(lerpBetween(start.value, end.value))
