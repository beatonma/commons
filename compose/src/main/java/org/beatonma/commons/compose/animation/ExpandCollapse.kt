package org.beatonma.commons.compose.animation

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.TransitionDefinition
import androidx.compose.animation.core.transitionDefinition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import org.beatonma.commons.theme.compose.theme.CommonsSpring

@Composable
fun rememberExpandCollapseState(
    default: ExpandCollapseState = ExpandCollapseState.Collapsed,
) = remember { mutableStateOf(default) }

@Composable
fun rememberExpandCollapseTransition(
    animSpec: AnimationSpec<Float> = CommonsSpring(),
): TransitionDefinition<ExpandCollapseState> = remember {
    transitionDefinition {
        state(ExpandCollapseState.Collapsed) {
            this[progressKey] = 0F
        }
        state(ExpandCollapseState.Expanded) {
            this[progressKey] = 1F
        }

        transition(
            ExpandCollapseState.Collapsed to ExpandCollapseState.Expanded,
            ExpandCollapseState.Expanded to ExpandCollapseState.Collapsed,
        ) {
            progressKey using animSpec
        }
    }
}

fun MutableState<ExpandCollapseState>.toggle() {
    value = value.toggle()
}

val MutableState<ExpandCollapseState>.isCollapsed get() = value == ExpandCollapseState.Collapsed
val MutableState<ExpandCollapseState>.isExpanded get() = value == ExpandCollapseState.Expanded

enum class ExpandCollapseState {
    Collapsed,
    Expanded,
    ;

    fun toggle() = when (this) {
        Collapsed -> Expanded
        Expanded -> Collapsed
    }
}

