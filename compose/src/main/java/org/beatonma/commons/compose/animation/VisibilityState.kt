package org.beatonma.commons.compose.animation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun rememberVisibilityState(
    default: VisibilityState = VisibilityState.Visible,
) = remember { mutableStateOf(default) }



fun MutableState<VisibilityState>.hide() {
    value = VisibilityState.Gone
}

fun MutableState<VisibilityState>.show() {
    value = VisibilityState.Visible
}

val MutableState<VisibilityState>.isGone get() = value == VisibilityState.Gone
val MutableState<VisibilityState>.isVisible get() = value == VisibilityState.Visible

enum class VisibilityState : TwoState<VisibilityState> {
    Gone,
    Visible,
    ;

    override fun toggle() = when (this) {
        Gone -> Visible
        Visible -> Gone
    }
}
