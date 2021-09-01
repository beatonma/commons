package org.beatonma.commons.compose.util

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

fun <E, T: Enum<E>> Modifier.testTag(enum: T) = this.then(Modifier.testTag(enum.name))
