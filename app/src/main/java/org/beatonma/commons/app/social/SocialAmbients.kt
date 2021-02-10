package org.beatonma.commons.app.social

import androidx.compose.animation.core.TransitionState
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.ProvidableAmbient
import androidx.compose.runtime.Providers
import androidx.compose.runtime.ambientOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticAmbientOf
import androidx.compose.ui.graphics.Color
import org.beatonma.commons.ActionBlock
import org.beatonma.commons.BuildConfig
import org.beatonma.commons.app.signin.AmbientUserToken
import org.beatonma.commons.app.signin.NullUserToken
import org.beatonma.commons.app.signin.UserAccountViewModel
import org.beatonma.commons.app.ui.base.SocialTargetProvider
import org.beatonma.commons.app.ui.colors.ComposePartyColors
import org.beatonma.commons.compose.components.TextValidationRules
import org.beatonma.commons.compose.util.update
import org.beatonma.commons.snommoc.models.social.EmptySocialContent
import org.beatonma.commons.snommoc.models.social.SocialComment
import org.beatonma.commons.snommoc.models.social.SocialContent
import org.beatonma.commons.snommoc.models.social.SocialTarget
import org.beatonma.commons.snommoc.models.social.SocialVoteType

val AmbientSocialContent: ProvidableAmbient<SocialContent> = ambientOf { EmptySocialContent }
val AmbientSocialTheme: ProvidableAmbient<SocialTheme> = ambientOf { error("SocialTheme has not been provided") }
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

@Composable
fun ProvideSocial(
    targetProvider: SocialTargetProvider,
    socialViewModel: SocialViewModel,
    userAccountViewModel: UserAccountViewModel,
    theme: SocialTheme = socialTheme(),
    content: @Composable () -> Unit,
) {
    SocialProviders(
        socialTarget = targetProvider.socialTarget,
        socialViewModel = socialViewModel,
        userAccountViewModel = userAccountViewModel,
        theme = theme,
        content = content,
    )
}

@Composable
private fun SocialProviders(
    socialTarget: SocialTarget,
    socialViewModel: SocialViewModel,
    userAccountViewModel: UserAccountViewModel,
    theme: SocialTheme,
    content: @Composable () -> Unit,
) {
    val activeUserToken by userAccountViewModel.userTokenLiveData.observeAsState(NullUserToken)
    socialViewModel.forTarget(socialTarget, activeUserToken)

    val socialContent by socialViewModel.livedata.observeAsState(EmptySocialContent)

    val socialActions = remember {
        SocialActions(
            onVoteUpClick = { socialViewModel.updateVote(SocialVoteType.aye, activeUserToken) },
            onVoteDownClick = { socialViewModel.updateVote(SocialVoteType.no, activeUserToken) },
            onExpandedCommentIconClick = { },
            onCommentClick = { comment ->
                println("Clicked comment $comment")
            },
            onCreateCommentClick = { socialViewModel.uiState.update(SocialUiState.ComposeComment) },
            onCommentSubmitClick = { commentText ->
                socialViewModel.postComment(commentText, activeUserToken)
            }
        )
    }

    Providers(
        AmbientSocialActions provides socialActions,
        AmbientSocialUiState provides socialViewModel.uiState,
        AmbientSocialContent provides socialContent,
        AmbientUserToken provides activeUserToken,
        AmbientSocialTheme provides theme,
        content = content,
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

/**
 * Simple SocialTheme using app surface/content colors.
 */
@Composable
fun socialTheme(
    collapsedBackground: Color = colors.surface,
    collapsedOnBackground: Color = colors.onSurface,
    expandedBackground: Color = colors.surface,
    expandedOnBackground: Color = colors.onSurface,
) = SocialTheme(
    collapsedBackground = collapsedBackground,
    collapsedOnBackground = collapsedOnBackground,
    expandedBackground = expandedBackground,
    expandedOnBackground = expandedOnBackground,
)

class SocialTheme internal constructor(
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
