package org.beatonma.commons.compose.animation

import androidx.compose.runtime.MutableState

interface TwoState<T> {
    fun toggle(): T
}

fun <T : TwoState<T>> MutableState<T>.toggle() {
    value = value.toggle()
}
