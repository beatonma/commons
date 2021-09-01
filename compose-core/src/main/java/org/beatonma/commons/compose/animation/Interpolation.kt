package org.beatonma.commons.compose.animation

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import org.beatonma.commons.compose.shape.ConcaveCornerShape
import org.beatonma.commons.core.extensions.lerpBetween
import org.beatonma.commons.themed.Padding

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

/**
 * Bit hacky
 */
@Composable
fun <T : CornerBasedShape> Float.lerpBetween(start: T, end: T): CornerBasedShape {
    // Specific size unimportant - just needs to be large for CornerSize.toPx().
    val size = Size(10000F, 10000F)
    val density = LocalDensity.current

    val topStart =
        lerpBetween(start.topStart.toPx(size, density), end.topStart.toPx(size, density))
    val topEnd =
        lerpBetween(start.topEnd.toPx(size, density), end.topEnd.toPx(size, density))
    val bottomStart = lerpBetween(
        start.bottomStart.toPx(size, density),
        end.bottomStart.toPx(size, density)
    )
    val bottomEnd = lerpBetween(
        start.bottomEnd.toPx(size, density),
        end.bottomEnd.toPx(size, density)
    )

    return when (start) {
        is CutCornerShape -> {
            CutCornerShape(
                topStart = topStart,
                topEnd = topEnd,
                bottomStart = bottomStart,
                bottomEnd = bottomEnd,
            )
        }
        is ConcaveCornerShape -> {
            ConcaveCornerShape(
                topStart = topStart,
                topEnd = topEnd,
                bottomStart = bottomStart,
                bottomEnd = bottomEnd,
            )
        }
        else -> {
            RoundedCornerShape(
                topStart = topStart,
                topEnd = topEnd,
                bottomStart = bottomStart,
                bottomEnd = bottomEnd,
            )
        }
    }
}
