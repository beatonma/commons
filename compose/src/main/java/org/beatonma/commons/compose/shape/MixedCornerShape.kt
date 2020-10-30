package org.beatonma.commons.compose.shape

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.Dp

fun Corner(size: Dp, type: MixedCornerShape.CornerType = MixedCornerShape.CornerType.Convex) =
    MixedCornerShape.Corner(CornerSize(size), type)

class MixedCornerShape(
    val topLeftCorner: Corner,
    val topRightCorner: Corner,
    val bottomRightCorner: Corner,
    val bottomLeftCorner: Corner,
) : CornerBasedShape(topLeftCorner.size,
    topRightCorner.size,
    bottomRightCorner.size,
    bottomLeftCorner.size) {
    override fun copy(
        topLeft: CornerSize,
        topRight: CornerSize,
        bottomRight: CornerSize,
        bottomLeft: CornerSize,
    ): CornerBasedShape = ConcaveCornerShape(topLeft, topRight, bottomRight, bottomLeft)

    override fun createOutline(
        size: Size,
        topLeft: Float,
        topRight: Float,
        bottomRight: Float,
        bottomLeft: Float,
    ): Outline {

        return if (topLeft + topRight + bottomLeft + bottomRight == 0.0f) {
            Outline.Rectangle(size.toRect())
        }
        else Outline.Generic(

            Path().apply {
                val (width, height) = size
                var cornerSize = topLeft
                fun rect(centerX: Float, centerY: Float) =
                    Rect(center = Offset(centerX, centerY), radius = cornerSize)

                moveTo(0F, cornerSize)

                when (topLeftCorner.type) {
                    CornerType.Concave -> concave(rect(0F, 0F), 90F)
                    CornerType.Convex -> convex(rect(cornerSize, cornerSize), 180F)
                    CornerType.Straight -> lineTo(cornerSize, 0F)
                }

                cornerSize = topRight
                lineTo(width - cornerSize, 0F)
                when (topRightCorner.type) {
                    CornerType.Concave -> concave(rect(width, 0F), 180F)
                    CornerType.Convex -> convex(rect(width - cornerSize, cornerSize), 270F)
                    CornerType.Straight -> lineTo(width, cornerSize)
                }

                cornerSize = bottomRight
                lineTo(width, height - cornerSize)

                when (bottomRightCorner.type) {
                    CornerType.Concave -> concave(rect(width, height), 270F)
                    CornerType.Convex -> convex(rect(width - cornerSize, height - cornerSize), 0F)
                    CornerType.Straight -> lineTo(width - cornerSize, height)
                }

                cornerSize = bottomLeft
                lineTo(cornerSize, height)
                when (bottomLeftCorner.type) {
                    CornerType.Concave -> concave(rect(0F, height), 0F)
                    CornerType.Convex -> convex(rect(cornerSize, height - cornerSize), 90F)
                    CornerType.Straight -> lineTo(0F, height - cornerSize)
                }

                close()
            }
        )
    }

    private fun Path.convex(rect: Rect, startAngleDegrees: Float) =
        arcTo(rect, startAngleDegrees, 90F, false)

    private fun Path.concave(rect: Rect, startAngleDegrees: Float) =
        arcTo(rect, startAngleDegrees, -90F, false)

    enum class CornerType {
        Concave,
        Convex,
        Straight,
        ;
    }

    data class Corner(val size: CornerSize, val type: CornerType = CornerType.Convex)
}
