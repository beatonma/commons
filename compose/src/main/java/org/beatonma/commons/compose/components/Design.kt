package org.beatonma.commons.compose.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Stack
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.beatonma.commons.compose.ambient.invertedColors
import org.beatonma.commons.compose.util.px

@Composable
fun Gridlines(
    spacing: Dp,
    highlightFrequency: Int,
    vertical: Boolean = true,
    horizontal: Boolean = true,
    color: Color = invertedColors.secondary.copy(alpha = 0.6F),
) {
    val strokeWidth = 1F
    val highlightStrokeWidth = if (highlightFrequency > 0) strokeWidth * 2F else strokeWidth
    val spacingPx = spacing.px
    val highlightPx = highlightFrequency * spacingPx

    Canvas(Modifier.fillMaxSize()) {
        val (w, h) = size

        if (horizontal) {
            for (i in 0..w.toInt() step spacingPx) {
                val x = i.toFloat()
                drawLine(color, Offset(x, 0F), Offset(x, h),
                    strokeWidth = if (i % highlightPx == 0) highlightStrokeWidth else strokeWidth
                )
            }
        }

        if (vertical) {
            for (i in 0..h.toInt() step spacingPx) {
                val y = i.toFloat()
                drawLine(color, Offset(0F, y), Offset(w, y),
                    strokeWidth = if (i % highlightPx == 0) highlightStrokeWidth else strokeWidth
                )
            }
        }
    }
}

/**
 * Draw gridlines over the provided content.
 */
@Composable
fun WithDesignGridlines(
    spacing: Dp = 8.dp,
    highlightFrequency: Int = 8,
    enabled: Boolean = true,
    vertical: Boolean = enabled,
    horizontal: Boolean = enabled,
    color: Color = invertedColors.secondary.copy(alpha = 0.6F),
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Stack(modifier) {
        content()
        Gridlines(spacing, highlightFrequency, vertical, horizontal, color)
    }
}
