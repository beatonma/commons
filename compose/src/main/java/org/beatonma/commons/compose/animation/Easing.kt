package org.beatonma.commons.compose.animation

import androidx.compose.animation.core.Easing

fun Float.withEasing(easing: Easing) = easing.transform(this)
