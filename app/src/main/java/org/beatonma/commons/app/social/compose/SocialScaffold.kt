package org.beatonma.commons.app.social.compose

import androidx.compose.animation.asDisposableClock
import androidx.compose.animation.core.*
import androidx.compose.animation.transition
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawOpacity
import androidx.compose.ui.platform.AnimationClockAmbient
import androidx.compose.ui.unit.IntSize
import org.beatonma.commons.ActionBlock
import org.beatonma.commons.app.social.State
import org.beatonma.commons.compose.modifiers.*
import org.beatonma.commons.compose.util.update
import org.beatonma.commons.core.extensions.progressIn
import org.beatonma.commons.core.extensions.reverse
import org.beatonma.commons.snommoc.models.social.SocialComment
import org.beatonma.commons.theme.compose.theme.CommonsTween
import org.beatonma.commons.theme.compose.theme.cubicBezier

private const val ANIMATION_DEFAULT_DURATION = 1800
private const val ANIMATION_EXPAND_DURATION = ANIMATION_DEFAULT_DURATION
private const val ANIMATION_COLLAPSE_DURATION = ANIMATION_DEFAULT_DURATION
internal val progressKey = FloatPropKey()

private fun socialScaffoldTransition() = transitionDefinition<State> {
    state(State.COLLAPSED) {
        this[progressKey] = 0F
    }
    state(State.EXPANDED) {
        this[progressKey] = 1F
    }
    transition(State.COLLAPSED to State.EXPANDED) {
        progressKey using CommonsTween(ANIMATION_EXPAND_DURATION)
    }
    transition(State.EXPANDED to State.COLLAPSED) {
        progressKey using CommonsTween(ANIMATION_COLLAPSE_DURATION)
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
    onVoteUpClick: ActionBlock = {},
    onVoteDownClick: ActionBlock = {},
    onCommentIconClick: ActionBlock = { state.update(State.COLLAPSED) },
    onCommentClick: (SocialComment) -> Unit = {},
    clock: AnimationClockObservable = AnimationClockAmbient.current.asDisposableClock(),
    animationSpec: AnimationSpec<IntSize> = remember { CommonsTween(ANIMATION_DEFAULT_DURATION) },
    constraintBlock: @Composable ConstraintLayoutScope.(
        transitionState: TransitionState,
        animationSpec: AnimationSpec<IntSize>,
        socialContainer: ConstrainedLayoutReference,
    ) -> Modifier,
) {
    val transitionDef = remember(::socialScaffoldTransition)
    val transitionState =
        transition(definition = transitionDef, toState = state.value, clock = clock)

    ConstraintLayout(modifier) {
        val social = createRef()

        val socialModifier = constraintBlock(transitionState, animationSpec, social)

        SocialContentView(
            modifier = socialContentModifier.then(socialModifier),
            onVoteUpClick = onVoteUpClick,
            onVoteDownClick = onVoteDownClick,
            onCommentIconClick = onCommentIconClick,
            onCommentClick = onCommentClick,
            animationSpec = animationSpec,
            state = state,
            transitionState = transitionState,
            clock = clock,
        )
    }
}

@Composable
fun SocialScaffoldColumn(
    modifier: Modifier = Modifier,
    state: MutableState<State> = remember { mutableStateOf(State.COLLAPSED) },
    animDuration: Int = ANIMATION_DEFAULT_DURATION,
    anim: AnimationSpec<IntSize> = remember { CommonsTween(animDuration) },
    clock: AnimationClockObservable = AnimationClockAmbient.current.asDisposableClock(),
    contentBefore: @Composable (Modifier) -> Unit,
    contentAfter: @Composable (Modifier) -> Unit,
) {
    SocialScaffold(
        modifier.fillMaxSize(),
        state = state,
        clock = clock,
        animationSpec = anim,
    ) { transitionState: TransitionState, animationSpec: AnimationSpec<IntSize>, social: ConstrainedLayoutReference ->

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
                    animateContentSize(CommonsTween(easing = cubicBezier(.66,.01,.47,.74)), clock = clock, clip = false)
                        .fillMaxSize()
                },
                State.COLLAPSED to {
                    animateContentSize(CommonsTween(easing = cubicBezier(.09,.97,1,.76)), clock = clock, clip = false)
                        .wrapContentHeight()
                }
            )
    }
}




//@Composable
//internal fun ScrollableSocialScaffoldColumn(
//    modifier: Modifier = Modifier.wrapContentSize(),
//    state: MutableState<State> = remember { mutableStateOf(State.COLLAPSED) },
//    animDuration: Int = ANIMATION_DEFAULT_DURATION,
//    anim: AnimationSpec<IntSize> = remember { CommonsTween(animDuration) },
//    clock: AnimationClockObservable = AnimationClockAmbient.current.asDisposableClock(),
//    scrollState: ScrollState = rememberScrollState(0f),
//    contentAbove: @Composable () -> Unit,
//    contentBelow: @Composable () -> Unit,
//) {
//    SocialScaffoldColumn(
//        modifier.verticalScroll(scrollState, enabled = true, reverseScrolling = false),
//        state, animDuration, anim, clock, contentAbove, contentBelow)
//}
