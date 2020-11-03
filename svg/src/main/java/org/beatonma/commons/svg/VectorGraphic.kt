package org.beatonma.commons.svg

import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.withTransform

data class ImageConfig(
    val scaleType: ScaleType = ScaleType.Min,
    val alignment: Alignment = Alignment.Center,
    val scaleMultiplier: Float = 1.0F,
    val offset: Offset = Offset.Zero,
    val matrix: Matrix = Matrix(),
    val pathConfig: PathConfig? = null,
)

abstract class VectorGraphic(
    pathCount: Int,
    val width: Int,
    val height: Int,
    val primaryColor: Color,
) {

    private val _paths: Array<VectorPath?> = Array(pathCount) { null }
    val paths: Array<VectorPath> get() = _paths as Array<VectorPath>

    abstract fun buildPaths()

    fun render(drawScope: DrawScope, pathConfig: PathConfig? = null) {
        paths.forEach { path ->
            path.render(drawScope, pathConfig)
        }
    }
}

val VectorGraphic.size get() = Size(width.toFloat(), height.toFloat())
val VectorGraphic.maxDimension get() = width.coerceAtLeast(height)
val VectorGraphic.minDimension get() = width.coerceAtMost(height)

fun VectorGraphic.render(
    scope: DrawScope,
    scaleType: ScaleType,
    alignment: Alignment,
    offset: Offset,
    scaleMultiplier: Float,
    matrix: Matrix,
    pathConfig: PathConfig? = null,
) {
    fitTo(scope.size, scaleType, alignment, offset, matrix, scaleMultiplier)

    scope.withTransform(
        transformBlock = {
            transform(matrix)
        }
    ) {
        render(this, pathConfig)
    }

    matrix.reset()
}

fun VectorGraphic.render(
    scope: DrawScope,
    imageConfig: ImageConfig,
) {
    render(
        scope,
        imageConfig.scaleType,
        imageConfig.alignment,
        imageConfig.offset,
        imageConfig.scaleMultiplier,
        imageConfig.matrix,
        imageConfig.pathConfig
    )
}
