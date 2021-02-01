package org.beatonma.commons.theme.compose.theme

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.ExponentialDecay
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.FloatDecayAnimationSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween

private const val DURATION_DEFAULT = 300
private const val DURATION_FAST = 120

fun <T> CommonsSpring(
    dampingRatio: Float = Spring.DampingRatioNoBouncy,
    stiffness: Float = 100F
) = spring<T>(dampingRatio = dampingRatio, stiffness = stiffness)

fun <T> CommonsFastSpring() = CommonsSpring<T>(stiffness = 2500F)

fun <T> CommonsTween(
    duration: Int = DURATION_DEFAULT,
    easing: CubicBezierEasing = FastOutSlowInEasing,
) = tween<T>(duration, easing = easing)

fun <T> CommonsFastTween() = CommonsTween<T>(duration = DURATION_FAST)

fun <T> CommonsRepeatable(
    iterations: Int = 0,
    duration: Int = DURATION_DEFAULT,
    easing: CubicBezierEasing = FastOutSlowInEasing,
    repeatMode: RepeatMode = RepeatMode.Restart,
) =
    if (iterations <= 0) {
        infiniteRepeatable(
            animation = CommonsTween<T>(duration = duration, easing = easing),
            repeatMode = repeatMode,
        )
    }
    else {
        repeatable(
            iterations = iterations,
            animation = CommonsTween<T>(duration = duration, easing = easing),
            repeatMode = repeatMode
        )
    }

fun CommonsDecay(): FloatDecayAnimationSpec = ExponentialDecay(
    frictionMultiplier = 3F,
)

fun cubicBezier(a: Number, b: Number, c: Number, d: Number) =
    CubicBezierEasing(a.toFloat(), b.toFloat(), c.toFloat(), d.toFloat())
