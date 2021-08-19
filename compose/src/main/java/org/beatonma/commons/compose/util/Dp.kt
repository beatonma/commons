package org.beatonma.commons.compose.util

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val Float.positiveDp: Dp get() = this.coerceAtLeast(0F).dp
