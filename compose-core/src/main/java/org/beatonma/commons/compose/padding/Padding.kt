package org.beatonma.commons.compose.padding

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.beatonma.commons.themed.Padding

fun Modifier.padding(padding: Padding): Modifier = this.then(
    padding(padding.asPaddingValues())
)

fun Modifier.padding(
    all: Dp = 0.dp,
    horizontal: Dp = all,
    vertical: Dp = all,
    start: Dp = horizontal,
    top: Dp = vertical,
    end: Dp = horizontal,
    bottom: Dp = vertical,
) = padding(start, top, end, bottom)
