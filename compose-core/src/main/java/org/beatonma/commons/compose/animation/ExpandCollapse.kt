package org.beatonma.commons.compose.animation

import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateFloat
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import org.beatonma.commons.themed.themedAnimation

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

val MutableState<ExpandCollapseState>.isCollapsed: Boolean get() = value.isCollapsed
val MutableState<ExpandCollapseState>.isExpanded: Boolean get() = value.isExpanded

/**
 * Currently throwing
 * java.lang.ClassCastException: org.beatonma.commons.compose.animation.ExpandCollapseKt$animateExpansion$1 cannot be cast to kotlin.jvm.functions.Function1
 * Possibly a kotlin plugin version issue?
 */
@Composable
fun Transition<ExpandCollapseState>.animateExpansion(): State<Float> =
    animateFloat(
        transitionSpec = { themedAnimation.spec() },
        label = "AnimatedExpandCollapse",
    ) { state ->
        when (state) {
            ExpandCollapseState.Collapsed -> 0F
            ExpandCollapseState.Expanded -> 1F
        }
    }


@Composable
fun ExpandCollapseState.animateExpansionAsState(): State<Float> =
    themedAnimation.animateFloatAsState(
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