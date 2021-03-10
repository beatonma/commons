package org.beatonma.commons.compose.modifiers.design

import androidx.annotation.FloatRange
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.DrawModifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.gridOverlay(
    spacing: Dp = 8.dp,
    color: Color? = null,
    @FloatRange(from = 0.0, to = 1.0) alpha: Float = .5F,
    strokeWidth: Dp = 0.dp,// Stroke.HairlineWidth,
    colorFilter: ColorFilter? = null,
    blendMode: BlendMode = DrawScope.DefaultBlendMode,
) = composed {
    this.then(
        GridOverlay(
            spacing,
            color ?: LocalContentColor.current,
            alpha = alpha,
            strokeWidth = strokeWidth,
            colorFilter,
            blendMode,
        )
    )
}

private class GridOverlay(
    private val spacing: Dp,
    private val color: Color,
    private val alpha: Float,
    private val strokeWidth: Dp,
    private val colorFilter: ColorFilter?,
    private val blendMode: BlendMode,
): DrawModifier {
    override fun ContentDrawScope.draw() {
        drawContent()
        drawGrid()
    }

    private fun ContentDrawScope.drawGrid() {
        val step = spacing.roundToPx()
        val strokeWidth = strokeWidth.toPx()

        val width = size.width
        val height = size.height

        for (x in 0..width.toInt() step step) {
            val xf = x.toFloat()
            drawLine(
                color,
                start = Offset(xf, 0F),
                end = Offset(xf, height),
                alpha = alpha,
                strokeWidth = strokeWidth,
                colorFilter = colorFilter,
                blendMode = blendMode,
            )
        }

        for (y in 0..height.toInt() step step) {
            val yf = y.toFloat()
            drawLine(
                color,
                start = Offset(0F, yf),
                end = Offset(width, yf),
                alpha = alpha,
                strokeWidth = strokeWidth,
                colorFilter = colorFilter,
                blendMode = blendMode,
            )
        }
    }
}
