package org.beatonma.commons.app.social.compose

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.TransitionState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AmbientContentColor
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.ProvidableAmbient
import androidx.compose.runtime.Providers
import androidx.compose.runtime.ambientOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import org.beatonma.commons.ActionBlock
import org.beatonma.commons.app.social.SocialUiState
import org.beatonma.commons.compose.ambient.typography
import org.beatonma.commons.compose.animation.lerpBetween
import org.beatonma.commons.compose.modifiers.wrapContentHeight
import org.beatonma.commons.compose.modifiers.wrapContentOrFillWidth
import org.beatonma.commons.compose.util.update
import org.beatonma.commons.core.extensions.lerpTo
import org.beatonma.commons.core.extensions.progressIn
import org.beatonma.commons.core.extensions.withEasing
import org.beatonma.commons.snommoc.models.social.SocialComment
import org.beatonma.commons.snommoc.models.social.SocialContent
import org.beatonma.commons.theme.compose.pdp
import org.beatonma.commons.theme.compose.theme.systemui.statusBarsPadding

internal val AmbientCollapseExpandProgress = ambientOf { 0F }
internal val AmbientExpandComposeCommentProgress = ambientOf { 0F }
internal val AmbientIconStyle: ProvidableAmbient<IconStyle> = ambientOf { SmallIconStyle }

/**
 * Fully animated transition from collapsed to expanded states
 */
@Composable
fun SocialContentUi(
    modifier: Modifier = Modifier,
    onVoteUpClick: ActionBlock = AmbientSocialActions.current.onVoteUpClick,
    onVoteDownClick: ActionBlock = AmbientSocialActions.current.onVoteDownClick,
    onCommentIconClick: ActionBlock = AmbientSocialActions.current.onExpandedCommentIconClick,
    onCommentClick: (SocialComment) -> Unit = AmbientSocialActions.current.onCommentClick,
    state: MutableState<SocialUiState> = AmbientSocialUiState.current,
    transitionState: TransitionState,
    expandAction: ActionBlock = { state.update(SocialUiState.Expanded) },
) {
    val collapseExpandProgress = transitionState[CollapseExpandProgress]
    val expandComposeCommentProgress = transitionState[ExpandedComposeCommentProgress]

    val theme = AmbientSocialTheme.current
    val tintProgress = collapseExpandProgress.progressIn(0.5F, 0.8F)

    val inactiveTint = tintProgress.lerpBetween(
        theme.collapsedOnBackground.copy(alpha = 0.64F),
        theme.expandedOnBackground.copy(alpha = 0.64F)
    )

    val activeTint = tintProgress.lerpBetween(
        theme.collapsedOnBackground,
        theme.expandedOnBackground,
    )

    Providers(
        AmbientContentColor provides inactiveTint,
        AmbientIconStyle provides SmallIconStyle.lerp(LargeIconStyle, collapseExpandProgress),
        AmbientCollapseExpandProgress provides collapseExpandProgress,
        AmbientExpandComposeCommentProgress provides expandComposeCommentProgress
    ) {
        SocialContentUi(
            AmbientSocialContent.current,
            modifier,
            onVoteUpClick, onVoteDownClick, onCommentIconClick, onCommentClick,
            state, inactiveTint, activeTint, expandAction = expandAction
        )
    }
}

/**
 * Fully animated transition from collapsed to expanded states
 */
@Composable
private fun SocialContentUi(
    socialContent: SocialContent,
    modifier: Modifier = Modifier,
    onVoteUpClick: ActionBlock,
    onVoteDownClick: ActionBlock,
    onCommentIconClick: ActionBlock,
    onCommentClick: (SocialComment) -> Unit,
    state: MutableState<SocialUiState> = AmbientSocialUiState.current,
    inactiveTint: Color = AmbientContentColor.current.copy(alpha = ContentAlpha.medium),
    activeTint: Color = AmbientContentColor.current.copy(alpha = ContentAlpha.high),
    progress: Float = AmbientCollapseExpandProgress.current,
    expandAction: ActionBlock = { state.update(SocialUiState.Expanded) },
) {
    val expandedContentVisibility = progress.progressIn(0.8F, 1F).withEasing(
        if (state.value == SocialUiState.Expanded) LinearOutSlowInEasing else FastOutLinearInEasing
    )
    val isFullyCollapsed = progress == 0F
    val expandedContentIsVisible = expandedContentVisibility != 0F

    Box(
        modifier.statusBarsPadding(progress)
    ) {
        Column {
            if (expandedContentIsVisible) {
                Text(
                    socialContent.title,
                    style = typography.h4,
                    modifier = Modifier
                        .wrapContentHeight(expandedContentVisibility)
                        .alpha(expandedContentVisibility)
                        .align(Alignment.CenterHorizontally)
                )
            }

            SocialIcons(
                modifier = Modifier
                    .padding(
                        horizontal = 0F.lerpTo(32F, progress).pdp,
                        vertical = 0F.lerpTo(16F, progress).pdp
                    )
                    .wrapContentOrFillWidth(progress),
                arrangement = Arrangement.SpaceEvenly,
                inactiveTint = inactiveTint,
                activeTint = activeTint,
                onCommentIconClick = if (isFullyCollapsed) expandAction else onCommentIconClick,
                onVoteUpIconClick = if (isFullyCollapsed) expandAction else onVoteUpClick,
                onVoteDownIconClick = if (isFullyCollapsed) expandAction else onVoteDownClick,
            )

            if (expandedContentIsVisible) {
                CommentList(
                    socialContent.comments.sortedByDescending { it.modified },
                    Modifier
                        .wrapContentHeight(progress.progressIn(0.6F, 0.8F))
                        .alpha(expandedContentVisibility),
                    onClick = onCommentClick,
                )
            }
        }

        if (expandedContentIsVisible) {
            CreateCommentUi()
        }
    }
}
