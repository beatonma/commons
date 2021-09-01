package org.beatonma.commons.compose.padding

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


class HorizontalPadding(
    value: Dp = 0.dp,
    val start: Dp = value,
    val end: Dp = value,
)

fun Modifier.padding(horizontalPadding: HorizontalPadding) = this.then(
    padding(start = horizontalPadding.start, end = horizontalPadding.end)
)
