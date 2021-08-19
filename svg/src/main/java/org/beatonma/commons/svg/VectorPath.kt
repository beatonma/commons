package org.beatonma.commons.svg

import androidx.annotation.FloatRange
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill

/**
 * Container for rendering options.
 */
data class PathConfig(
    val color: Color? = null,
    @FloatRange(from = 0.0, to = 1.0) val alpha: Float = 1.0f,
    val style: DrawStyle = Fill,
    val colorFilter: ColorFilter? = null,
    val blendMode: BlendMode = DrawScope.DefaultBlendMode,
    val colorBlock: ((Color) -> Color)? = null,
)

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

fun VectorPath.render(scope: DrawScope, config: PathConfig? = null) {
    val c = config?.colorBlock?.invoke(config.color ?: this.color) ?: this.color
    render(
        scope,
        color = c,
        style = config?.style ?: this.style,
        colorFilter = config?.colorFilter,
        blendMode = config?.blendMode ?: DrawScope.DefaultBlendMode,
    )
}

fun vectorPath(path: Path, color: Color, style: DrawStyle = Fill) =
    object : VectorPath(path, color, style) {}

inline fun plotPath(path: Path = Path(), block: Path.() -> Unit) = path.apply(block)
