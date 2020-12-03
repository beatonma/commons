package org.beatonma.commons.compose.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

fun <T> MutableState<T>.update(newValue: T) = component2()(newValue)

@Composable
fun rememberText(default: String = ""): MutableState<String> = remember { mutableStateOf(default) }
