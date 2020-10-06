package org.beatonma.commons.compose.util

import androidx.compose.runtime.MutableState

fun <T> MutableState<T>.update(newValue: T) = component2()(newValue)
