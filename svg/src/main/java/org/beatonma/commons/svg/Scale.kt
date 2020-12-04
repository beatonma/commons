package org.beatonma.commons.svg

import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
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
    relativeOffset: Offset, // Values between 0F..1F, as proportion of final size
    matrix: Matrix,
    scaleMultiplier: Float,
) {
    val widthF = width.toFloat()
    val heightF = height.toFloat()
    val widthScale = availableSize.width / widthF
    val heightScale = availableSize.height / heightF
    val scale = when (scaleType) {
        ScaleType.Min -> min(widthScale, heightScale)
        ScaleType.Max -> max(widthScale, heightScale)
    } * scaleMultiplier

    val alignmentOffset = alignment.align(
        intSize(widthF * scale, heightF * scale),
        availableSize.toIntSize(),
        LayoutDirection.Ltr
    )

    val scaledWidth = scale * widthF
    val scaledHeight = scale * heightF

    // alignmentOffset combined with relativeOffset
    val composedOffset = Offset(
        alignmentOffset.x + (scaledWidth * relativeOffset.x),
        alignmentOffset.y + (scaledHeight * relativeOffset.y)
    )

    matrix.apply {
        translate(composedOffset.x, composedOffset.y)
        scale(x = scale, y = scale)
    }
}

private fun intSize(width: Float, height: Float) = IntSize(width.toInt(), height.toInt())
private fun Size.toIntSize() = intSize(width, height)
