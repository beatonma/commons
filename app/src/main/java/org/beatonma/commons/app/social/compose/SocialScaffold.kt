package org.beatonma.commons.app.social.compose

import androidx.compose.animation.core.FloatPropKey
import androidx.compose.animation.core.TransitionState
import androidx.compose.animation.core.transitionDefinition
import androidx.compose.animation.core.tween
import androidx.compose.animation.transition
import androidx.compose.foundation.layout.ConstrainedLayoutReference
import androidx.compose.foundation.layout.ConstraintLayout
import androidx.compose.foundation.layout.ConstraintLayoutScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import org.beatonma.commons.app.social.State
import org.beatonma.commons.compose.layout.linkTo
import org.beatonma.commons.compose.util.update

private enum class Ref {
    Container,
    Social,
    ;
}

internal val progressKey = FloatPropKey()

@Composable
fun SocialScaffold(
    modifier: Modifier = Modifier,
    state: MutableState<State> = remember { mutableStateOf(State.COLLAPSED) },
    constraintBlock: @Composable ConstraintLayoutScope.(transitionState: TransitionState, socialContainer: ConstrainedLayoutReference) -> Unit,
) {

    val transitionDef = remember {
        transitionDefinition<State> {
            state(State.COLLAPSED) {
                this[progressKey] = 0F
            }
            state(State.EXPANDED) {
                this[progressKey] = 1F
            }
            transition(
                State.COLLAPSED to State.EXPANDED,
            ) {
                progressKey using tween(3000)
            }
            transition(State.EXPANDED to State.COLLAPSED) { progressKey using tween(3000) }
        }
    }
    val transitionState = transition(definition = transitionDef, toState = state.value)

    ConstraintLayout(modifier) {
        val (socialContainer, social) = createRefs()

        constraintBlock(transitionState, socialContainer)

        SocialContentView(
//        AdaptiveSocialContent(
            Modifier.constrainAs(social) {
                linkTo(socialContainer)
            },
            onVoteUpClick = {},
            onVoteDownClick = {},
            onExpandedCommentIconClick = { state.update(State.COLLAPSED) },
            onCommentClick = {},
            state = state,
        )
    }
}

@Composable
fun Modifier.socialContainer(state: State) = when (state) {
    State.COLLAPSED -> this.also { println("$state no override") }
    else -> fillMaxSize().also { println("$state fillMaxSize") }
}
