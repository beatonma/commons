package org.beatonma.commons.compose.animation

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FloatPropKey
import androidx.compose.animation.core.TransitionDefinition
import androidx.compose.animation.core.transitionDefinition
import androidx.compose.runtime.MutableState
import org.beatonma.commons.theme.compose.theme.CommonsSpring

interface TwoState<T> {
    fun toggle(): T
}

fun <T : TwoState<T>> MutableState<T>.toggle() {
    value = value.toggle()
}

fun <T> twoStateProgressTransition(
    defaultState: T,
    altState: T,
    animSpec: AnimationSpec<Float> = CommonsSpring(),
    key: FloatPropKey = progressKey,
): TransitionDefinition<T> =
    transitionDefinition {
        state(defaultState) {
            this[key] = 0F
        }
        state(altState) {
            this[key] = 1F
        }

        transition(
            defaultState to altState,
            altState to defaultState,
        ) {
            key using animSpec
        }
    }
