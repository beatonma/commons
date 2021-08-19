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

fun CornerBasedShape.withSquareTop() = copy(topStart = ZeroCornerSize, topEnd = ZeroCornerSize)
fun CornerBasedShape.withSquareBottom() =
    copy(bottomStart = ZeroCornerSize, bottomEnd = ZeroCornerSize)

fun CornerBasedShape.withSquareStart() = copy(bottomStart = ZeroCornerSize, topStart = ZeroCornerSize)
fun CornerBasedShape.withSquareEnd() =
    copy(topEnd = ZeroCornerSize, bottomEnd = ZeroCornerSize)
