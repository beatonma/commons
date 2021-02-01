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
    reverseOrder: Boolean = false,
    key: FloatPropKey = progressKey,
): TransitionDefinition<T> =
    transitionDefinition {
        state(defaultState) {
            this[key] = if (reverseOrder) 1F else 0F
        }
        state(altState) {
            this[key] = if (reverseOrder) 0F else 1F
        }

        transition(
            defaultState to altState,
            altState to defaultState,
        ) {
            key using animSpec
        }
    }

fun <T> twoStateProgressTransition(
    defaultState: T,
    altState: T,
    toAnimSpec: AnimationSpec<Float>,
    returnAnimSpec: AnimationSpec<Float>,
    reverseOrder: Boolean = false,
    key: FloatPropKey = progressKey,
): TransitionDefinition<T> =
    transitionDefinition {
        state(defaultState) {
            this[key] = if (reverseOrder) 1F else 0F
        }
        state(altState) {
            this[key] = if (reverseOrder) 0F else 1F
        }

        transition(
            defaultState to altState,
        ) {
            key using toAnimSpec
        }

        transition(
            altState to defaultState,
        ) {
            key using returnAnimSpec
        }
    }
