package org.beatonma.commons.app.social

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import org.beatonma.commons.compose.ambient.typography
import org.beatonma.commons.compose.animation.withEasing
import org.beatonma.commons.compose.modifiers.wrapContentHeight
import org.beatonma.commons.compose.modifiers.wrapContentOrFillWidth
import org.beatonma.commons.compose.util.positiveDp
import org.beatonma.commons.core.extensions.lerpBetween
import org.beatonma.commons.core.extensions.progressIn
import org.beatonma.commons.snommoc.models.social.SocialContent
import org.beatonma.commons.theme.compose.theme.systemui.statusBarsPadding

/**
 * Fully animated transition from collapsed to expanded states
 */
@Composable
internal fun SocialContentUi(
    modifier: Modifier,
    socialContent: SocialContent,
    state: SocialUiState,
    onStateChange: (SocialUiState) -> Unit,
    actions: SocialActions,
    theme: SocialTheme = LocalSocialTheme.current,
) {
    val expandAction = { onStateChange(SocialUiState.Expanded) }
    val collapseAction = { onStateChange(SocialUiState.Collapsed) }

    val expandProgress by state.animateExpansionAsState()

    CompositionLocalProvider(
        LocalContentColor provides theme.inactiveColor(expandProgress = expandProgress),
    ) {
        SocialContentLayout(
            socialContent = socialContent,
            modifier = modifier,
            actions = actions,
            state = state,
            theme = theme,
            expandProgress = expandProgress,
            expandAction = expandAction,
            collapseAction = collapseAction,
        )
    }
}

/**
 * Fully animated transition from collapsed to expanded states
 */
@Composable
private fun SocialContentLayout(
    socialContent: SocialContent,
    modifier: Modifier = Modifier,
    state: SocialUiState,
    theme: SocialTheme,
    expandProgress: Float,
    actions: SocialActions,
    expandAction: () -> Unit,
    collapseAction: () -> Unit
) {
    val expandedContentVisibility = expandProgress.progressIn(0.8F, 1F)
        .withEasing(
            if (state == SocialUiState.Expanded) LinearOutSlowInEasing else FastOutLinearInEasing
        )

    val expandedContentIsVisible = expandedContentVisibility != 0F

    Box(
        modifier.statusBarsPadding(expandProgress)
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
                socialContent = socialContent,
                state = state,
                theme = theme,
                actions = actions,
                modifier = Modifier
                    .padding(
                        horizontal = expandProgress.lerpBetween(0F, 32F).positiveDp,
                        vertical = expandProgress.lerpBetween(0F, 16F).positiveDp
                    )
                    .wrapContentOrFillWidth(expandProgress),
                expandAction = expandAction,
                collapseAction = collapseAction,
            )

            if (expandedContentIsVisible) {
                CommentList(
                    socialContent.comments.sortedByDescending { it.modified },
                    Modifier
                        .wrapContentHeight(expandProgress.progressIn(0.6F, 0.8F))
                        .alpha(expandedContentVisibility),
                    expandProgress = expandProgress,
                    onClick = actions.onCommentClick,
                )
            }
        }

        if (expandedContentIsVisible) {
            CreateCommentUi()
        }
    }
}
