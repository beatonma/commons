package org.beatonma.commons.compose.modifiers.design

import androidx.annotation.FloatRange
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.DrawModifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.centerOverlay(
    size: Dp = 24.dp,
    color: Color = LocalContentColor.current,
    @FloatRange(from = 0.0, to = 1.0) alpha: Float = .5F,
    strokeWidth: Dp = 1.dp,
    colorFilter: ColorFilter? = null,
    blendMode: BlendMode = DrawScope.DefaultBlendMode,
) = this.then(
    CenterOverlay(
        size,
        color,
        alpha = alpha,
        strokeWidth = strokeWidth,
        colorFilter,
        blendMode,
    )
)

private class CenterOverlay(
    private val centerSize: Dp,
    private val color: Color,
    private val alpha: Float,
    private val strokeWidth: Dp,
    private val colorFilter: ColorFilter?,
    private val blendMode: BlendMode,
): DrawModifier {
    override fun ContentDrawScope.draw() {
        drawContent()
        drawCenter()
    }

    private fun ContentDrawScope.drawCenter() {
        val strokeWidth = strokeWidth.toPx()
        val length = centerSize.toPx()

        val (x, y) = this.center

        drawLine(
            color,
            start = Offset(x - length, y),
            end = Offset(x + length, y),
            alpha = alpha,
            strokeWidth = strokeWidth,
            colorFilter = colorFilter,
            blendMode = blendMode,
        )

        drawLine(
            color,
            start = Offset(x, y - length),
            end = Offset(x, y + length),
            alpha = alpha,
            strokeWidth = strokeWidth,
            colorFilter = colorFilter,
            blendMode = blendMode,
            cap = StrokeCap.Round
        )
    }
}
