package org.beatonma.commons.theme.compose.theme

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material.Shapes
import androidx.compose.ui.unit.dp

const val CORNER_SMALL = 8
const val CORNER_MEDIUM = 16
const val CORNER_LARGE = 24

val CommonsShapes = Shapes(
    large = RoundedCornerShape(CORNER_LARGE.dp),
    medium = RoundedCornerShape(CORNER_MEDIUM.dp),
    small = RoundedCornerShape(CORNER_SMALL.dp),
)

fun CornerBasedShape.withSquareTop() = copy(topLeft = ZeroCornerSize, topRight = ZeroCornerSize)
fun CornerBasedShape.withSquareBottom() =
    copy(bottomLeft = ZeroCornerSize, bottomRight = ZeroCornerSize)

fun CornerBasedShape.withSquareLeft() = copy(bottomLeft = ZeroCornerSize, topLeft = ZeroCornerSize)
fun CornerBasedShape.withSquareRight() =
    copy(topRight = ZeroCornerSize, bottomRight = ZeroCornerSize)
