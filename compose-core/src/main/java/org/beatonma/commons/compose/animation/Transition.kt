package org.beatonma.commons.compose.animation

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import org.beatonma.commons.themed.animation

@Composable
fun <T> animateColor(
    transition: Transition<T>,
    targetValueByState: @Composable (T) -> Color
): State<Color> =
    transition.animateColor(
        transitionSpec = { animation.spec() },
        label = "AnimatedColor",
        targetValueByState = targetValueByState,
    )

@Composable
fun <T> animateDp(
    transition: Transition<T>,
    targetValueByState: @Composable (T) -> Dp
): State<Dp> =
    transition.animateDp(
        transitionSpec = { animation.spec() },
        label = "AnimatedDp",
        targetValueByState = targetValueByState,
    )

@Composable
fun <T> animateFloat(
    transition: Transition<T>,
    targetValueByState: @Composable (T) -> Float
): State<Float> =
    transition.animateFloat(
        transitionSpec = { animation.spec() },
        label = "AnimatedFloat",
        targetValueByState = targetValueByState,
    )


val Boolean.asFloatAnimationTarget get() = if (this) 1f else 0f
