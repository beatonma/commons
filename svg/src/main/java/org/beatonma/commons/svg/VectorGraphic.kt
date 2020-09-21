package org.beatonma.commons.svg

import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.withTransform

abstract class VectorGraphic(
    pathCount: Int,
    val width: Int,
    val height: Int,
    val primaryColor: Color,
) {

    private val _paths: Array<VectorPath?> = Array(pathCount) { null }
    val paths: Array<VectorPath> get() = _paths as Array<VectorPath>

    abstract fun buildPaths()

    fun render(drawScope: DrawScope) {
        paths.forEach { path ->
            path.render(drawScope)
        }
    }
}

fun VectorGraphic.getBounds(): Rect {
    paths[0].path.getBounds()
    var rect = Rect.Zero
    paths.forEach {
        rect = rect.expandToInclude(it.path.getBounds())
    }
    return rect
}

val VectorGraphic.size get() = Size(width.toFloat(), height.toFloat())
val VectorGraphic.maxDimension get() = width.coerceAtLeast(height)
val VectorGraphic.minDimension get() = width.coerceAtMost(height)

fun VectorGraphic.render(
    drawScope: DrawScope,
    scaleType: ScaleType,
    alignment: Alignment,
    scaleMultiplier: Float,
    matrix: Matrix,
) {
    fitTo(drawScope.size, scaleType, alignment, matrix, scaleMultiplier)

    drawScope.withTransform(
        transformBlock = {
            transform(matrix)
        }
    ) {
        render(this)
    }

    matrix.reset()
}
