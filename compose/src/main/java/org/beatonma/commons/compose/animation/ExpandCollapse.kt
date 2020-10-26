package org.beatonma.commons.compose.animation

import androidx.compose.animation.core.transitionDefinition
import org.beatonma.commons.theme.compose.theme.CommonsTween

enum class ExpandCollapseState {
    Collapsed,
    Expanded,
    ;

    fun toggle() = when (this) {
        Collapsed -> Expanded
        Expanded -> Collapsed
    }

    companion object {
        fun defaultTransition() = transitionDefinition<ExpandCollapseState> {
            state(Collapsed) {
                this[progressKey] = 0F
            }
            state(Expanded) {
                this[progressKey] = 1F
            }
            transition(Collapsed to Expanded) {
                progressKey using CommonsTween()
            }
            transition(Expanded to Collapsed) {
                progressKey using CommonsTween()
            }
        }
    }
}

