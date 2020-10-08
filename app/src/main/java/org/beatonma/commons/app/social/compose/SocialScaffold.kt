package org.beatonma.commons.app.social.compose

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FloatPropKey
import androidx.compose.animation.core.TransitionState
import androidx.compose.animation.core.transitionDefinition
import androidx.compose.animation.transition
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawOpacity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import org.beatonma.commons.app.social.State
import org.beatonma.commons.compose.layout.linkTo
import org.beatonma.commons.compose.modifiers.onlyWhen
import org.beatonma.commons.compose.util.update
import org.beatonma.commons.core.extensions.normalize
import org.beatonma.commons.theme.compose.color.MaterialAmber800
import org.beatonma.commons.theme.compose.color.MaterialLightBlue700
import org.beatonma.commons.theme.compose.theme.CommonsSpring
import org.beatonma.commons.theme.compose.theme.CommonsTween

private const val ANIMATION_DEFAULT_DURATION = 3000
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

@Composable
fun SocialScaffold(
    modifier: Modifier = Modifier,
    state: MutableState<State> = remember { mutableStateOf(State.COLLAPSED) },
    constraintBlock: @Composable ConstraintLayoutScope.(transitionState: TransitionState, socialContainer: ConstrainedLayoutReference) -> Unit,
) {
    val transitionDef = remember(::socialScaffoldTransition)
    val transitionState = transition(definition = transitionDef, toState = state.value)

    ConstraintLayout(modifier) {
        val (socialContainer, social) = createRefs()

        constraintBlock(transitionState, socialContainer)

        SocialContentView(
            modifier = Modifier.constrainAs(social) {
                linkTo(socialContainer)
            },
            onVoteUpClick = {},
            onVoteDownClick = {},
            onCommentIconClick = { state.update(State.COLLAPSED) },
            onCommentClick = {},
            state = state,
            transitionState = transitionState,
        )
    }
}

@Composable
fun SocialScaffold2(
    modifier: Modifier = Modifier,
    state: MutableState<State> = remember { mutableStateOf(State.COLLAPSED) },

    // Return a Modifier instance with constraints for `social` view
    constraintBlock: @Composable ConstraintLayoutScope.(
        transitionState: TransitionState,
        socialContainer: ConstrainedLayoutReference,
    ) -> Modifier,
) {
    val transitionDef = remember(::socialScaffoldTransition)
    val transitionState = transition(definition = transitionDef, toState = state.value)

    ConstraintLayout(modifier) {
        val social = createRef()

        val socialModifier = constraintBlock(transitionState, social)

        SocialContentView(
            modifier = socialModifier,
            onVoteUpClick = {},
            onVoteDownClick = {},
            onCommentIconClick = { state.update(State.COLLAPSED) },
            onCommentClick = {},
            state = state,
            transitionState = transitionState,
        )
    }
}

@Composable
fun SocialScaffoldColumn(
    modifier: Modifier = Modifier,
    state: MutableState<State> = remember { mutableStateOf(State.COLLAPSED) },
    anim: AnimationSpec<IntSize> = CommonsSpring(),
    contentAbove: @Composable () -> Unit,
    contentBelow: @Composable () -> Unit,
) {
    SocialScaffold2(
        modifier.fillMaxWidth(),
        state,
    ) { transitionState: TransitionState, social: ConstrainedLayoutReference ->

        val (before, after) = createRefs()
        val progress = transitionState[progressKey]
        val keyPosition = 0.5F

        Surface(
            Modifier
                .animateContentSize(anim)
                .constrainAs(before) {
                    linkTo(parent, bottom = social.top)
                }
                .drawOpacity((keyPosition - progress).normalize(keyPosition))
                .onlyWhen(progress > keyPosition) {
                    height(0.dp)
                },
            color = MaterialLightBlue700,
            content = contentAbove,
        )

        Surface(
            Modifier
                .animateContentSize(anim)
                .constrainAs(after) {
                    top.linkTo(social.bottom)
                }
                .drawOpacity((keyPosition - progress).normalize(keyPosition))
                .onlyWhen(progress > keyPosition) {
                    height(0.dp)
                },
            color = MaterialAmber800,
            content = contentBelow,
        )

        // Constraints for the social content
        Modifier
            .constrainAs(social) {
                centerHorizontallyTo(parent)
                if (progress > keyPosition) {
                    linkTo(parent)
                }
                else {
                    top.linkTo(before.bottom)
                    bottom.linkTo(after.top)
                }
            }.onlyWhen(progress > keyPosition) {
                fillMaxSize()
            }
//            .animateContentSize(anim)
    }
}
