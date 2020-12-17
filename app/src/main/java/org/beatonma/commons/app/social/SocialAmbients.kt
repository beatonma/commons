package org.beatonma.commons.app.social.compose

import androidx.compose.animation.core.TransitionState
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.ProvidableAmbient
import androidx.compose.runtime.ambientOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.staticAmbientOf
import androidx.compose.ui.graphics.Color
import org.beatonma.commons.ActionBlock
import org.beatonma.commons.BuildConfig
import org.beatonma.commons.app.social.SocialUiState
import org.beatonma.commons.app.ui.colors.ComposePartyColors
import org.beatonma.commons.compose.components.TextValidationRules
import org.beatonma.commons.snommoc.models.social.EmptySocialContent
import org.beatonma.commons.snommoc.models.social.SocialComment
import org.beatonma.commons.snommoc.models.social.SocialContent

val AmbientSocialContent: ProvidableAmbient<SocialContent> = ambientOf { EmptySocialContent }
val AmbientSocialTheme: ProvidableAmbient<SocialTheme> =
    ambientOf { error("Social theme has not been provided") }
val AmbientSocialActions: ProvidableAmbient<SocialActions> = ambientOf { SocialActions() }
val AmbientSocialUiState: ProvidableAmbient<MutableState<SocialUiState>> =
    ambientOf { mutableStateOf(SocialUiState.Collapsed) }
val AmbientSocialUiTransition: ProvidableAmbient<TransitionState> =
    ambientOf { error("TransitionState has not been set") }
val AmbientSocialCommentValidator: ProvidableAmbient<TextValidationRules> = staticAmbientOf {
    TextValidationRules(
        minLength = BuildConfig.SOCIAL_COMMENT_MIN_LENGTH,
        maxLength = BuildConfig.SOCIAL_COMMENT_MAX_LENGTH,
    )
}

class SocialActions(
    val onVoteUpClick: ActionBlock = {},
    val onVoteDownClick: ActionBlock = {},
    val onExpandedCommentIconClick: ActionBlock = {},
    val onCommentClick: (SocialComment) -> Unit = {},
    val onCreateCommentClick: ActionBlock = {},
    val onCommentSubmitClick: (String) -> Unit = {},
)

class SocialTheme(
    val collapsedBackground: Color = Color.Transparent,
    val collapsedOnBackground: Color = Color.Transparent,
    val expandedBackground: Color = Color.Transparent,
    val expandedOnBackground: Color = Color.Transparent,
)

@Composable
fun ComposePartyColors.asSocialTheme() = SocialTheme(
    primary,
    onPrimary,
    colors.background,
    colors.onBackground
)
