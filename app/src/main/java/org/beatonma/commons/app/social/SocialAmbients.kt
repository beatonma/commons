package org.beatonma.commons.app.social

import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import org.beatonma.commons.BuildConfig
import org.beatonma.commons.app.signin.LocalUserToken
import org.beatonma.commons.app.signin.NullUserToken
import org.beatonma.commons.app.signin.UserAccountViewModel
import org.beatonma.commons.app.ui.base.SocialTargetProvider
import org.beatonma.commons.app.ui.colors.ComposePartyColors
import org.beatonma.commons.compose.animation.lerpBetween
import org.beatonma.commons.compose.components.text.TextValidationRules
import org.beatonma.commons.core.extensions.progressIn
import org.beatonma.commons.snommoc.models.social.EmptySocialContent
import org.beatonma.commons.snommoc.models.social.SocialComment
import org.beatonma.commons.snommoc.models.social.SocialContent
import org.beatonma.commons.snommoc.models.social.SocialTarget
import org.beatonma.commons.snommoc.models.social.SocialVoteType

val LocalSocialContent: ProvidableCompositionLocal<SocialContent> = compositionLocalOf { EmptySocialContent }
val LocalSocialTheme: ProvidableCompositionLocal<SocialTheme> = compositionLocalOf { error("SocialTheme has not been provided") }
val LocalSocialActions: ProvidableCompositionLocal<SocialActions> = compositionLocalOf { SocialActions() }
val LocalSocialUiState: ProvidableCompositionLocal<MutableState<SocialUiState>> =
    compositionLocalOf { mutableStateOf(SocialUiState.Collapsed) }
val LocalSocialCommentValidator: ProvidableCompositionLocal<TextValidationRules> = compositionLocalOf {
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
    SocialCompositionLocalProvider(
        socialTarget = targetProvider.socialTarget,
        socialViewModel = socialViewModel,
        userAccountViewModel = userAccountViewModel,
        theme = theme,
        content = content,
    )
}

@Composable
private fun SocialCompositionLocalProvider(
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
            onCommentClick = { comment ->
                println("Clicked comment $comment")
            },
            onCommentSubmitClick = { commentText ->
                socialViewModel.postComment(commentText, activeUserToken)
            }
        )
    }

    CompositionLocalProvider(
        LocalSocialActions provides socialActions,
        LocalSocialUiState provides socialViewModel.uiState,
        LocalSocialContent provides socialContent,
        LocalUserToken provides activeUserToken,
        LocalSocialTheme provides theme,
        content = content,
    )
}

class SocialActions(
    val onVoteUpClick: () -> Unit = {},
    val onVoteDownClick: () -> Unit = {},
    val onCommentClick: (SocialComment) -> Unit = {},
    val onCommentSubmitClick: (String) -> Unit = {},
)


private const val InactiveAlpha = 0.64F
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
) {
    @Composable
    fun inactiveColor(expandProgress: Float): Color =
        expandProgress
            .progressIn(0.5F, 0.8F)
            .lerpBetween(
                collapsedOnBackground.copy(alpha = InactiveAlpha),
                expandedOnBackground.copy(alpha = InactiveAlpha)
            )

    @Composable
    fun activeColor(expandProgress: Float): Color = expandProgress
        .progressIn(0.5F, 0.8F)
        .lerpBetween(
            collapsedOnBackground,
            expandedOnBackground,
        )
}

@Composable
fun ComposePartyColors.asSocialTheme() = SocialTheme(
    primary,
    onPrimary,
    colors.background,
    colors.onBackground
)
