package org.beatonma.commons.compose.shape

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection

fun ConcaveCornerShape(topStart: Dp, topEnd: Dp, bottomEnd: Dp, bottomStart: Dp) =
    ConcaveCornerShape(
        CornerSize(topStart),
        CornerSize(topEnd),
        CornerSize(bottomEnd),
        CornerSize(bottomStart)
    )

fun ConcaveCornerShape(topStart: Float, topEnd: Float, bottomEnd: Float, bottomStart: Float) =
    ConcaveCornerShape(
        CornerSize(topStart),
        CornerSize(topEnd),
        CornerSize(bottomEnd),
        CornerSize(bottomStart)
    )

class ConcaveCornerShape(
    topStart: CornerSize,
    topEnd: CornerSize,
    bottomEnd: CornerSize,
    bottomStart: CornerSize,
) : CornerBasedShape(topStart, topEnd, bottomEnd, bottomStart) {
    override fun copy(
        topStart: CornerSize,
        topEnd: CornerSize,
        bottomEnd: CornerSize,
        bottomStart: CornerSize,
    ): CornerBasedShape = ConcaveCornerShape(topStart, topEnd, bottomEnd, bottomStart)

    override fun createOutline(
        size: Size,
        topStart: Float,
        topEnd: Float,
        bottomEnd: Float,
        bottomStart: Float,
        layoutDirection: LayoutDirection,
    ) = if (topStart + topEnd + bottomStart + bottomEnd == 0.0f) {
        Outline.Rectangle(size.toRect())
    }
    else Outline.Generic(
        Path().apply {
            var cornerSize = topStart
            moveTo(0F, cornerSize)
            arcTo(Rect(-cornerSize, -cornerSize, cornerSize, cornerSize), 90F, -90F, false)

            cornerSize = topEnd
            lineTo(size.width - cornerSize, 0F)
            arcTo(Rect(size.width - cornerSize, -cornerSize, size.width + cornerSize, cornerSize),
                180F, -90F, false)

            cornerSize = bottomEnd
            lineTo(size.width, size.height - cornerSize)
            arcTo(Rect(size.width - cornerSize,
                size.height - cornerSize,
                size.width + cornerSize,
                size.height + cornerSize),
                270F, -90F, false)

            cornerSize = bottomStart
            lineTo(cornerSize, size.height)
            arcTo(Rect(-cornerSize, size.height - cornerSize, cornerSize, size.height + cornerSize),
                0F, -90F, false
            )

            close()
        }
    )
}
