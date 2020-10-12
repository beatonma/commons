package org.beatonma.commons.app.social.compose

import androidx.compose.animation.asDisposableClock
import androidx.compose.animation.core.*
import androidx.compose.animation.transition
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.AnimationClockAmbient
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import org.beatonma.commons.app.social.State
import org.beatonma.commons.compose.ambient.typography
import org.beatonma.commons.compose.modifiers.animateContentSize
import org.beatonma.commons.compose.modifiers.colorize
import org.beatonma.commons.compose.modifiers.onlyWhen
import org.beatonma.commons.compose.modifiers.switch
import org.beatonma.commons.compose.util.update
import org.beatonma.commons.core.extensions.clipToLength
import org.beatonma.commons.theme.compose.theme.CommonsTween

private const val ANIMATION_DEFAULT_DURATION = 1200
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
fun SocialScaffold2(
    modifier: Modifier = Modifier,
    state: MutableState<State> = remember { mutableStateOf(State.COLLAPSED) },
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
            modifier = socialModifier,
            onVoteUpClick = {},
            onVoteDownClick = {},
            onCommentIconClick = { state.update(State.COLLAPSED) },
            onCommentClick = {},
            animationSpec = animationSpec,
            state = state,
            transitionState = transitionState,
            clock = clock,
        )

        Text(
            "${transitionState[progressKey]}".clipToLength(3),
            Modifier.constrainAs(createRef()) {
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end)
            }
                .colorize()
                .padding(8.dp), style = typography.h4)
    }
}

@Composable
fun SocialScaffoldColumn(
    modifier: Modifier = Modifier,
    state: MutableState<State> = remember { mutableStateOf(State.COLLAPSED) },
    animDuration: Int = ANIMATION_DEFAULT_DURATION,
    anim: AnimationSpec<IntSize> = remember { CommonsTween(animDuration) },
    clock: AnimationClockObservable = AnimationClockAmbient.current.asDisposableClock(),
    contentAbove: @Composable () -> Unit,
    contentBelow: @Composable () -> Unit,
) {
    SocialScaffold2(
        modifier.fillMaxSize(),
        state,
        clock,
        animationSpec = anim,
    ) { transitionState: TransitionState, animationSpec: AnimationSpec<IntSize>, social: ConstrainedLayoutReference ->

        val (before, after) = createRefs()
        val progress = transitionState[progressKey]

        Surface(
            Modifier
                .animateContentSize(animationSpec, clock = clock)
                .switch(
                    Pair({ state.value == State.EXPANDED }, { height(0.dp) }),
                    Pair({ state.value == State.COLLAPSED }, { wrapContentHeight() })
                )
                .constrainAs(before) {
                    if (state.value == State.EXPANDED && progress > 0.5F) {
                        bottom.linkTo(social.top)
                    }
                    else {
                        top.linkTo(parent.top)
                        bottom.linkTo(social.top)
                    }
                },
            content = contentAbove,
        )

        Surface(
            Modifier
                .constrainAs(after) {
                    top.linkTo(social.bottom)
                },
            content = contentBelow,
        )

        // Constraints for the social content
        Modifier
            .constrainAs(social) {
                centerHorizontallyTo(parent)
                top.linkTo(before.bottom)
            }
            .switch(state.value,
                Pair({ it == State.EXPANDED }, { animateContentSize() })
            )
            .onlyWhen(state.value == State.EXPANDED) {
                animateContentSize(animationSpec, clock = clock)
            }
//            .animateContentSize(animationSpec, clock = clock)
            .switch(
                Pair({ state.value == State.EXPANDED }, { fillMaxSize() }),
                Pair({ state.value == State.COLLAPSED }, { wrapContentHeight() })
            )
//            .either(progress > 0F,
//                { fillMaxSize() },
//                { wrapContentHeight() }
//            )
            .colorize()
    }
}
