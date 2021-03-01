package org.beatonma.commons.app.social

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.FloatPropKey
import androidx.compose.animation.core.TransitionState
import androidx.compose.animation.core.transitionDefinition
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ConstrainedLayoutReference
import androidx.compose.foundation.layout.ConstraintLayout
import androidx.compose.foundation.layout.ConstraintLayoutScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Providers
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import org.beatonma.commons.ActionBlock
import org.beatonma.commons.app.preview.ProvidePreviewAmbients
import org.beatonma.commons.compose.components.CollapsibleHeaderLayout
import org.beatonma.commons.compose.modifiers.design.colorize
import org.beatonma.commons.compose.modifiers.wrapContentHeight
import org.beatonma.commons.compose.modifiers.wrapContentOrFillHeight
import org.beatonma.commons.compose.util.rememberBoolean
import org.beatonma.commons.compose.util.update
import org.beatonma.commons.core.extensions.lerpTo
import org.beatonma.commons.core.extensions.progressIn
import org.beatonma.commons.core.extensions.reversed
import org.beatonma.commons.core.extensions.withEasing
import org.beatonma.commons.snommoc.models.social.SocialComment
import org.beatonma.commons.theme.compose.Layer
import org.beatonma.commons.theme.compose.pdp
import org.beatonma.commons.theme.compose.theme.CommonsSpring

internal val CollapseExpandProgress = FloatPropKey("Track progress between Collapsed and Expanded states")
internal val ExpandedComposeCommentProgress = FloatPropKey("Track progress between Expanded and ComposeComment states")

private fun socialScaffoldTransition() = transitionDefinition<SocialUiState> {
    state(SocialUiState.Collapsed) {
        this[CollapseExpandProgress] = 0F
        this[ExpandedComposeCommentProgress] = 0F
    }
    state(SocialUiState.Expanded) {
        this[CollapseExpandProgress] = 1F
        this[ExpandedComposeCommentProgress] = 0F
    }
    state(SocialUiState.ComposeComment) {
        this[CollapseExpandProgress] = 1F
        this[ExpandedComposeCommentProgress] = 1F
    }

    transition(
        SocialUiState.Collapsed to SocialUiState.Expanded,
        SocialUiState.Expanded to SocialUiState.Collapsed,
    ) {
        CollapseExpandProgress using CommonsSpring()
    }

    transition(
        SocialUiState.Expanded to SocialUiState.ComposeComment,
        SocialUiState.ComposeComment to SocialUiState.Expanded,
    ) {
        ExpandedComposeCommentProgress using CommonsSpring()
    }
}

@Composable
fun rememberSocialScaffoldTransition() = remember(::socialScaffoldTransition)

/**
 * [constraintBlock] must return a Modifier instance with constraints for `social` view.
 */
@Composable
fun SocialScaffold(
    modifier: Modifier = Modifier,
    socialContentModifier: Modifier = Modifier,
    uiState: MutableState<SocialUiState> = AmbientSocialUiState.current,
    onVoteUpClick: ActionBlock = AmbientSocialActions.current.onVoteUpClick,
    onVoteDownClick: ActionBlock = AmbientSocialActions.current.onVoteDownClick,
    onCommentClick: (SocialComment) -> Unit = AmbientSocialActions.current.onCommentClick,
    onCommentIconClick: ActionBlock = { uiState.update(SocialUiState.Collapsed) },
    transitionState: TransitionState = socialTransitionState(toState = uiState.value),
    expandAction: ActionBlock = { uiState.update(SocialUiState.Expanded) },
    constraintBlock: @Composable ConstraintLayoutScope.(
        transitionState: TransitionState,
        socialContainer: ConstrainedLayoutReference,
    ) -> Modifier,
) {
    ConstraintLayout(modifier) {
        val social = createRef()

        val socialModifier = constraintBlock(transitionState, social)

        SocialContentUi(
            modifier = socialContentModifier.then(socialModifier),
            onVoteUpClick = onVoteUpClick,
            onVoteDownClick = onVoteDownClick,
            onCommentIconClick = onCommentIconClick,
            onCommentClick = onCommentClick,
            state = uiState,
            transitionState = transitionState,
            expandAction = expandAction,
        )
    }
}

/**
 * A scaffold for using the social UI in a [CollapsibleHeaderLayout].
 */
@Composable
fun StickySocialScaffold(
    headerContentAboveSocial: @Composable (headerExpansion: Float, Modifier) -> Unit,
    headerContentBelowSocial: @Composable (headerExpansion: Float, Modifier) -> Unit,
    lazyListContent: LazyListScope.() -> Unit,
    modifier: Modifier = Modifier,
    socialContentModifier: Modifier = Modifier,
    uiState: MutableState<SocialUiState> = AmbientSocialUiState.current,
    onVoteUpClick: ActionBlock = AmbientSocialActions.current.onVoteUpClick,
    onVoteDownClick: ActionBlock = AmbientSocialActions.current.onVoteDownClick,
    onCommentClick: (SocialComment) -> Unit = AmbientSocialActions.current.onCommentClick,
    onCommentIconClick: ActionBlock = { uiState.update(SocialUiState.Collapsed) },
    transitionState : TransitionState = socialTransitionState (toState = uiState.value),
    expandAction: ActionBlock = { uiState.update(SocialUiState.Expanded) },
    onScrollStarted: (Offset) -> Unit = {},
    onScrollStopped: (velocity: Float) -> Unit = {},
    socialTheme: SocialTheme = AmbientSocialTheme.current,
) {
    val scaffoldScrollEnabled = rememberBoolean(true)
    Providers(
        AmbientSocialTheme provides socialTheme,
    ) {
        CollapsibleHeaderLayout(
            collapsingHeader = { headerExpansion: Float ->
                CollapsibleSocialHeader(
                    headerContentAboveSocial = headerContentAboveSocial,
                    headerContentBelowSocial = headerContentBelowSocial,
                    headerExpansion = headerExpansion,
                    modifier = modifier,
                    socialContentModifier = socialContentModifier,
                    uiState = uiState,
                    onVoteUpClick = onVoteUpClick,
                    onVoteDownClick = onVoteDownClick,
                    onCommentClick = onCommentClick,
                    onCommentIconClick = onCommentIconClick,
                    transitionState = transitionState,
                    expandAction = expandAction)
            },
            lazyListContent = {
                lazyListContent()
            },
            scrollEnabled = scaffoldScrollEnabled.value,
            onScrollStarted = onScrollStarted,
            onScrollStopped = onScrollStopped,
            snapToStateAt = 0.3F
        )
    }
}

@Composable
private fun CollapsibleSocialHeader(
    headerContentAboveSocial: @Composable (headerExpansion: Float, Modifier) -> Unit,
    headerContentBelowSocial: @Composable (headerExpansion: Float, Modifier) -> Unit,
    headerExpansion: Float,
    modifier: Modifier = Modifier,
    socialContentModifier: Modifier = Modifier,
    uiState: MutableState<SocialUiState> = AmbientSocialUiState.current,
    onVoteUpClick: ActionBlock = AmbientSocialActions.current.onVoteUpClick,
    onVoteDownClick: ActionBlock = AmbientSocialActions.current.onVoteDownClick,
    onCommentClick: (SocialComment) -> Unit = AmbientSocialActions.current.onCommentClick,
    onCommentIconClick: ActionBlock = { uiState.update(SocialUiState.Collapsed) },
    transitionState: TransitionState = socialTransitionState(toState = uiState.value),
    expandAction: ActionBlock = { uiState.update(SocialUiState.Expanded) },
) {
    val progress = transitionState[CollapseExpandProgress]
    val reverseProgress = progress.reversed()

    Column(
        modifier
            .fillMaxWidth()
    ) {
        if (progress != 1F) {
            headerContentAboveSocial(
                headerExpansion,
                Modifier
                    .wrapContentHeight(reverseProgress)
                    .alpha(reverseProgress.progressIn(0F, 0.6F))
                    .zIndex(Layer.Bottom)
            )
        }

        SocialContentUi(
            modifier = socialContentModifier
                .zIndex(Layer.High)
                .wrapContentOrFillHeight(progress.withEasing(FastOutSlowInEasing))
                .padding(start = 12.lerpTo(0, progress).pdp, bottom = 8.lerpTo(0, progress).pdp)
                .semantics { testTag = "social_content" }
            ,
            onVoteUpClick = onVoteUpClick,
            onVoteDownClick = onVoteDownClick,
            onCommentIconClick = onCommentIconClick,
            onCommentClick = onCommentClick,
            state = uiState,
            transitionState = transitionState,
            expandAction = expandAction,
        )

        if (progress != 1F) {
            headerContentBelowSocial(
                headerExpansion,
                Modifier
                    .wrapContentHeight(reverseProgress)
                    .alpha(reverseProgress.progressIn(0F, 0.6F))
                    .zIndex(Layer.Bottom)
            )
        }
    }
}

@Preview @Composable
private fun CollapsibleSocialPreview() {
    val listItems = (1..100).toList()
    ProvidePreviewAmbients {
        StickySocialScaffold(
            headerContentAboveSocial = { headerExpansion, modifier ->
                Text("Above social content",
                    modifier
                        .height(28.dp + (60.dp * headerExpansion)))
            },
            headerContentBelowSocial = { headerExpansion, modifier ->
                Text("Below social content",
                    modifier
                        .height(60.dp * headerExpansion))
            },
            lazyListContent = {
                items(
                    items = listItems,
                ) { item ->
                    Text(
                        "$item",
                        Modifier
                            .padding(4.dp)
                            .colorize()
                    )
                }
            }
        )
    }
}
