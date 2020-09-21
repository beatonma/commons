package org.beatonma.commons.svg

import androidx.annotation.FloatRange
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill

abstract class VectorPath(
    val path: Path,
    val color: Color,
    val style: DrawStyle = Fill,
) {

    fun render(
        scope: DrawScope,
        color: Color = this.color,
        @FloatRange(from = 0.0, to = 1.0) alpha: Float = 1.0f,
        style: DrawStyle = this.style,
        colorFilter: ColorFilter? = null,
        blendMode: BlendMode = DrawScope.DefaultBlendMode,
    ) {
        scope.drawPath(path, color, alpha, style, colorFilter, blendMode)
    }
}

fun vectorPath(path: Path, color: Color, style: DrawStyle = Fill) =
    object : VectorPath(path, color, style) {}

fun plotPath(path: Path = Path(), block: Path.() -> Unit) = path.apply { block() }
