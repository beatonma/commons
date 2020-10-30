package org.beatonma.commons.compose.shape

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.Dp

fun ConcaveCornerShape(topLeft: Dp, topRight: Dp, bottomRight: Dp, bottomLeft: Dp) =
    ConcaveCornerShape(
        CornerSize(topLeft),
        CornerSize(topRight),
        CornerSize(bottomRight),
        CornerSize(bottomLeft)
    )

class ConcaveCornerShape(
    topLeft: CornerSize,
    topRight: CornerSize,
    bottomRight: CornerSize,
    bottomLeft: CornerSize,
) : CornerBasedShape(topLeft, topRight, bottomRight, bottomLeft) {
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
    ) = if (topLeft + topRight + bottomLeft + bottomRight == 0.0f) {
        Outline.Rectangle(size.toRect())
    }
    else Outline.Generic(
        Path().apply {
            var cornerSize = topLeft
            moveTo(0F, cornerSize)
            arcTo(Rect(-cornerSize, -cornerSize, cornerSize, cornerSize), 90F, -90F, false)

            cornerSize = topRight
            lineTo(size.width - cornerSize, 0F)
            arcTo(Rect(size.width - cornerSize, -cornerSize, size.width + cornerSize, cornerSize),
                180F, -90F, false)

            cornerSize = bottomRight
            lineTo(size.width, size.height - cornerSize)
            arcTo(Rect(size.width - cornerSize,
                size.height - cornerSize,
                size.width + cornerSize,
                size.height + cornerSize),
                270F, -90F, false)

            cornerSize = bottomLeft
            lineTo(cornerSize, size.height)
            arcTo(Rect(-cornerSize, size.height - cornerSize, cornerSize, size.height + cornerSize),
                0F, -90F, false
            )

            close()
        }
    )
}
