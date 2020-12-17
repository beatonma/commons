package org.beatonma.commons.app.social

import androidx.compose.animation.core.FloatPropKey
import androidx.compose.animation.core.TransitionState
import androidx.compose.animation.core.transitionDefinition
import androidx.compose.foundation.layout.ConstrainedLayoutReference
import androidx.compose.foundation.layout.ConstraintLayout
import androidx.compose.foundation.layout.ConstraintLayoutScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import org.beatonma.commons.ActionBlock
import org.beatonma.commons.compose.util.update
import org.beatonma.commons.snommoc.models.social.SocialComment
import org.beatonma.commons.theme.compose.theme.CommonsSpring

internal val CollapseExpandProgress = FloatPropKey()
internal val ExpandedComposeCommentProgress = FloatPropKey()

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
