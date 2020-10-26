package org.beatonma.commons.compose.animation

import androidx.compose.animation.core.FloatPropKey
import androidx.compose.animation.core.TransitionState

val progressKey = FloatPropKey()
val TransitionState.progress get() = this[progressKey]
