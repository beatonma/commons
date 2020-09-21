package org.beatonma.commons.svg

import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.unit.IntSize
import kotlin.math.max
import kotlin.math.min

enum class ScaleType {
    Min,  // Scale according to minimum available dimension - fits inside
    Max,  // Scale according to largest available dimension - may overflow in other dimensions.
    ;
}

/**
 * @param scaleMultiplier is applied after scaleType so is relative to availableSize,
 *                   not the original VectorGraphic.
 */
fun VectorGraphic.fitTo(
    availableSize: Size,
    scaleType: ScaleType,
    alignment: Alignment,
    matrix: Matrix,
    scaleMultiplier: Float,
) {
    val widthScale = availableSize.width / width.toFloat()
    val heightScale = availableSize.height / height.toFloat()
    val scale = when (scaleType) {
        ScaleType.Min -> min(widthScale, heightScale)
        ScaleType.Max -> max(widthScale, heightScale)
    } * scaleMultiplier

    val offset = alignment.align(
        IntSize(
            (availableSize.width - (width * scale)).toInt(),
            (availableSize.height - (height * scale)).toInt()
        )
    )

    matrix.apply {
        translate(offset.x.toFloat(), offset.y.toFloat())
        scale(x = scale, y = scale)
    }
}
