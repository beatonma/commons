package org.beatonma.commons.app.ui.screens.social

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import org.beatonma.commons.themed.themedAnimation

enum class SocialUiState {
    Collapsed,
    Expanded,
    ComposeComment,
}

@Composable
fun rememberSocialUiState(default: SocialUiState = SocialUiState.Collapsed) =
    remember { mutableStateOf(default) }

@Composable
fun SocialUiState.animateExpansionAsState() = themedAnimation.animateFloatAsState(
    when (this) {
        SocialUiState.Collapsed -> 0F
        else -> 1F
    }
)

/**
 * Try to update value to previous state, and return true if this was successful.
 */
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
