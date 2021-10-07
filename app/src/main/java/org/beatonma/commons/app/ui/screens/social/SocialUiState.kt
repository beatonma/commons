package org.beatonma.commons.app.ui.screens.social

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import org.beatonma.commons.themed.themedAnimation

enum class SocialUiState {
    Collapsed,
    Expanded,
    ComposeComment,
    ;
}

val SocialUiState.isCollapsed get() = this == SocialUiState.Collapsed
val SocialUiState.isExpanded get() = this != SocialUiState.Collapsed

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

val SocialUiState.previousState
    get() = when (this) {
        SocialUiState.Collapsed -> throw IllegalArgumentException("previousState failed: SocialUiState is already collapsed")
        SocialUiState.Expanded -> SocialUiState.Collapsed
        SocialUiState.ComposeComment -> SocialUiState.Expanded
    }
