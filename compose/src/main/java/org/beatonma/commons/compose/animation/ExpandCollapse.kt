package org.beatonma.commons.compose.animation

import androidx.compose.animation.core.Transition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import org.beatonma.commons.compose.ambient.animation

@Composable
fun rememberExpandCollapseState(
    default: ExpandCollapseState = ExpandCollapseState.Collapsed,
) = remember { mutableStateOf(default) }

fun MutableState<ExpandCollapseState>.collapse() {
    value = ExpandCollapseState.Collapsed
}

fun MutableState<ExpandCollapseState>.expand() {
    value = ExpandCollapseState.Expanded
}

val MutableState<ExpandCollapseState>.isCollapsed get() = value.isCollapsed
val MutableState<ExpandCollapseState>.isExpanded get() = value.isExpanded

@Composable
fun Transition<ExpandCollapseState>.animateExpansion(): State<Float> = animation.animateFloat(this) { state ->
    when {
        state.isCollapsed -> 0F
        else -> 1F
    }
}

@Composable
fun ExpandCollapseState.animateExpansionAsState(): State<Float> = animation.animateFloatAsState(
    when (this) {
        ExpandCollapseState.Collapsed -> 0F
        ExpandCollapseState.Expanded -> 1F
    }
)


enum class ExpandCollapseState : TwoState<ExpandCollapseState> {
    Collapsed,
    Expanded,
    ;

    override fun toggle() = when (this) {
        Collapsed -> Expanded
        Expanded -> Collapsed
    }

    val isCollapsed: Boolean get() = this == Collapsed
    val isExpanded: Boolean get() = this == Expanded
}
