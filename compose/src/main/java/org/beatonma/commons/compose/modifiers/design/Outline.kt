package org.beatonma.commons.compose.modifiers.design

import androidx.compose.material.LocalContentColor
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.outline(
    thickness: Dp = 2.dp,
    color: Color? = null,
) = composed {
    val resolvedColor = color ?: LocalContentColor.current.copy(alpha = .75F)
    drawWithCache {
        val shape = RectangleShape
        val strokeWidth = thickness.toPx()
        val stroke = Stroke(strokeWidth)

        val halfStroke = strokeWidth / 2F

        val insetSize = Size(
            size.width - strokeWidth,
            size.height - strokeWidth
        )
        val insetOutline = shape.createOutline(insetSize, layoutDirection, this)

        this.onDrawWithContent {
            drawContent()

            clipRect {
                drawRect(
                    color = resolvedColor,
                    style = stroke,
                    size = insetOutline.bounds.size,
                    topLeft = Offset(halfStroke, halfStroke)
                )
            }

        }
    }
}
