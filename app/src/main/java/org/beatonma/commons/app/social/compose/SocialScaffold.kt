package org.beatonma.commons.app.social.compose

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.TransitionDefinition
import androidx.compose.animation.core.TransitionState
import androidx.compose.animation.core.transitionDefinition
import androidx.compose.animation.transition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ConstrainedLayoutReference
import androidx.compose.foundation.layout.ConstraintLayout
import androidx.compose.foundation.layout.ConstraintLayoutScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawOpacity
import org.beatonma.commons.ActionBlock
import org.beatonma.commons.app.social.State
import org.beatonma.commons.compose.animation.progressKey
import org.beatonma.commons.compose.modifiers.switchEqual
import org.beatonma.commons.compose.modifiers.wrapContentHeight
import org.beatonma.commons.compose.util.update
import org.beatonma.commons.core.extensions.progressIn
import org.beatonma.commons.core.extensions.reverse
import org.beatonma.commons.snommoc.models.social.SocialComment
import org.beatonma.commons.theme.compose.theme.CommonsSpring
import org.beatonma.commons.theme.compose.theme.CommonsTween
import org.beatonma.commons.theme.compose.theme.cubicBezier

private const val ANIMATION_DEFAULT_DURATION = 900
private const val ANIMATION_EXPAND_DURATION = ANIMATION_DEFAULT_DURATION
private const val ANIMATION_COLLAPSE_DURATION = ANIMATION_DEFAULT_DURATION


private fun socialScaffoldTransition() = transitionDefinition<State> {
    state(State.COLLAPSED) {
        this[progressKey] = 0F
    }
    state(State.EXPANDED) {
        this[progressKey] = 1F
    }
    transition(State.COLLAPSED to State.EXPANDED) {
        progressKey using CommonsSpring()
//        progressKey using CommonsTween(ANIMATION_EXPAND_DURATION)
    }
    transition(State.EXPANDED to State.COLLAPSED) {
        progressKey using CommonsSpring()
//        progressKey using CommonsTween(ANIMATION_COLLAPSE_DURATION)
    }
}

/**
 * [constraintBlock] must return a Modifier instance with constraints for `social` view.
 */
@Composable
fun SocialScaffold(
    modifier: Modifier = Modifier,
    socialContentModifier: Modifier = Modifier,
    state: MutableState<State> = remember { mutableStateOf(State.COLLAPSED) },
    onVoteUpClick: ActionBlock = AmbientSocialActions.current.onVoteUpClick,
    onVoteDownClick: ActionBlock = AmbientSocialActions.current.onVoteDownClick,
    onCommentIconClick: ActionBlock = { state.update(State.COLLAPSED) },
    onCommentClick: (SocialComment) -> Unit = AmbientSocialActions.current.onCommentClick,
    transitionDef: TransitionDefinition<State> = remember(::socialScaffoldTransition),
    transitionState: TransitionState = transition(definition = transitionDef,
        toState = state.value),
    expandAction: ActionBlock = { state.update(State.EXPANDED) },
    constraintBlock: @Composable ConstraintLayoutScope.(
        transitionState: TransitionState,
        socialContainer: ConstrainedLayoutReference,
    ) -> Modifier,
) {
    ConstraintLayout(modifier) {
        val social = createRef()

        val socialModifier = constraintBlock(transitionState, social)

        SocialContentView(
            modifier = socialContentModifier.then(socialModifier),
            onVoteUpClick = onVoteUpClick,
            onVoteDownClick = onVoteDownClick,
            onCommentIconClick = onCommentIconClick,
            onCommentClick = onCommentClick,
            state = state,
            transitionState = transitionState,
            expandAction = expandAction,
        )
    }
}

@Composable
fun SocialScaffoldColumn(
    modifier: Modifier = Modifier,
    state: MutableState<State> = remember { mutableStateOf(State.COLLAPSED) },
    contentBefore: @Composable (Modifier) -> Unit,
    contentAfter: @Composable (Modifier) -> Unit,
) {
    SocialScaffold(
        modifier.fillMaxSize(),
        state = state,
    ) { transitionState: TransitionState, social: ConstrainedLayoutReference ->

        val (before, after) = createRefs()
        val progress = transitionState[progressKey]
        val reverseProgress = progress.reverse()

        Box(
            Modifier
                .wrapContentHeight(reverseProgress)
                .constrainAs(before) {
                    top.linkTo(parent.top)
                }
                .drawOpacity(reverseProgress.progressIn(0F, 0.6F))
        ) { contentBefore(Modifier) }

        Box(
            Modifier
                .constrainAs(after) {
                    top.linkTo(social.bottom)
                }
                .drawOpacity(reverseProgress.progressIn(0F, 0.6F))
        ) {
            contentAfter(Modifier)
        }

        // Constraints for the social content
        Modifier
            .constrainAs(social) {
                centerHorizontallyTo(parent)
                top.linkTo(before.bottom)
            }
            .switchEqual(state.value,
                State.EXPANDED to {
                    animateContentSize(CommonsTween(easing = cubicBezier(.66, .01, .47, .74)),
                        clip = false)
                        .fillMaxSize()
                },
                State.COLLAPSED to {
                    animateContentSize(CommonsTween(easing = cubicBezier(.09, .97, 1, .76)),
                        clip = false)
                        .wrapContentHeight()
                }
            )
    }
}
