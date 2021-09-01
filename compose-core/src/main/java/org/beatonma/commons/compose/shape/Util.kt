package org.beatonma.commons.compose.shape

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.ZeroCornerSize

fun CornerBasedShape.withSquareTop() = copy(topStart = ZeroCornerSize, topEnd = ZeroCornerSize)

fun CornerBasedShape.withSquareStart() =
    copy(bottomStart = ZeroCornerSize, topStart = ZeroCornerSize)

fun CornerBasedShape.withSquareEnd() =
    copy(topEnd = ZeroCornerSize, bottomEnd = ZeroCornerSize)

fun CornerBasedShape.withSquareBottom() =
    copy(bottomStart = ZeroCornerSize, bottomEnd = ZeroCornerSize)
