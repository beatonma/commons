package org.beatonma.commons.theme.compose.theme

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material.Shapes
import androidx.compose.ui.unit.dp

val CommonsShapes = Shapes(
    large = RoundedCornerShape(24.dp),
    medium = RoundedCornerShape(16.dp),
    small = RoundedCornerShape(8.dp),
)

fun CornerBasedShape.withSquareTop() = copy(topLeft = ZeroCornerSize, topRight = ZeroCornerSize)
fun CornerBasedShape.withSquareBottom() =
    copy(bottomLeft = ZeroCornerSize, bottomRight = ZeroCornerSize)

fun CornerBasedShape.withSquareLeft() = copy(bottomLeft = ZeroCornerSize, topLeft = ZeroCornerSize)
fun CornerBasedShape.withSquareRight() =
    copy(topRight = ZeroCornerSize, bottomRight = ZeroCornerSize)
