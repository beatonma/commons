package org.beatonma.commons.compose.animation

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FloatPropKey
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
    key: FloatPropKey = progressKey,
) = remember {
    twoStateProgressTransition(
        defaultState = ExpandCollapseState.Collapsed,
        altState = ExpandCollapseState.Expanded,
        animSpec = animSpec,
        key = key,
    )
}

fun MutableState<ExpandCollapseState>.collapse() {
    value = ExpandCollapseState.Collapsed
}

fun MutableState<ExpandCollapseState>.expand() {
    value = ExpandCollapseState.Expanded
}

val MutableState<ExpandCollapseState>.isCollapsed get() = value == ExpandCollapseState.Collapsed
val MutableState<ExpandCollapseState>.isExpanded get() = value == ExpandCollapseState.Expanded

enum class ExpandCollapseState : TwoState<ExpandCollapseState> {
    Collapsed,
    Expanded,
    ;

    override fun toggle() = when (this) {
        Collapsed -> Expanded
        Expanded -> Collapsed
    }
}

