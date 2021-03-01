package org.beatonma.commons.compose.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

/**
 * Set a new value.
 */
fun <T> MutableState<T>.update(newValue: T) { value = newValue }

fun <T> MutableState<T>.updateIfNotEqual(newValue: T) {
    if (value != newValue) {
        println("UPDATE $newValue")
        value = newValue
    }
}

/**
 * Update the current value using the previous value.
 */
inline fun <T> MutableState<T>.mapUpdate(map: (T) -> T) { value = map(value) }

fun MutableState<Boolean>.toggle() = update(!value)

@Composable
fun rememberText(default: String = ""): MutableState<String> = remember { mutableStateOf(default) }

@Composable
fun rememberBoolean(default: Boolean = false) = remember { mutableStateOf(default) }

@Composable
fun <T> rememberListOf(default: List<T> = listOf()) = remember { mutableStateOf(default) }

@Composable
fun <T> rememberSetOf(default: Set<T> = setOf()) = remember { mutableStateOf(default) }

/**
 * Debug helper - print to log if the given value has changed since the previous recomposition.
 */
@Composable
fun <T> detectChanges(value: T) {
    val previous = remember { mutableStateOf(value) }
    if (previous != value) {
        println("UPDATE $value")
        previous.value = value
    }
}
