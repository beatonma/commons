package org.beatonma.commons.compose.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

/**
 * Update the current value using the previous value.
 */
inline fun <T> MutableState<T>.mapUpdate(map: (T) -> T) { value = map(value) }

/**
 * Flip the state and return the new value.
 */
fun MutableState<Boolean>.toggle(): Boolean {
    value = !value
    return value
}

@Composable
private fun <T> rememberMutableState(default: T) = remember { mutableStateOf(default) }

@Composable
fun rememberText(default: String = ""): MutableState<String> = rememberMutableState(default)

@Composable
fun rememberBoolean(default: Boolean = false) = rememberMutableState(default)

@Composable
fun rememberInt(default: Int = 0) = rememberMutableState(default)

@Composable
fun rememberFloat(default: Float = 0F) = rememberMutableState(default)

@Composable
fun <T> rememberListOf(default: List<T> = listOf()) = rememberMutableState(default)

@Composable
fun <T> rememberSetOf(default: Set<T> = setOf()) = rememberMutableState(default)
