package org.beatonma.commons.app.social

import androidx.compose.runtime.Composable
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
