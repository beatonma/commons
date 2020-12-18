package org.beatonma.commons.app.social

import androidx.compose.animation.core.TransitionState
import androidx.compose.animation.transition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

enum class SocialUiState {
    Collapsed,
    Expanded,
    ComposeComment,
}

@Composable
fun rememberSocialUiState(default: SocialUiState = SocialUiState.Collapsed) =
    remember { mutableStateOf(default) }

@Composable
fun socialTransitionState(toState: SocialUiState): TransitionState {
    val transitionDef = rememberSocialScaffoldTransition()
    return transition(definition = transitionDef, toState = toState)
}

fun MutableState<SocialUiState>.toPreviousState(): Boolean =
    when (this.value) {
        SocialUiState.Expanded -> {
            value = SocialUiState.Collapsed
            true
        }

        SocialUiState.ComposeComment -> {
            value = SocialUiState.Expanded
            true
        }

        else -> false
    }
