package org.beatonma.commons.theme.compose.theme

import androidx.compose.animation.core.AnimationConstants.Infinite
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween

private const val DEFAULT_DURATION = 300

fun <T> CommonsSpring() = spring<T>(Spring.DampingRatioNoBouncy, 100F)
fun <T> CommonsTween(
    duration: Int = DEFAULT_DURATION,
    easing: CubicBezierEasing = FastOutSlowInEasing,
) =
    tween<T>(duration, easing = easing)

fun <T> CommonsRepeatable(
    iterations: Int = Infinite,
    duration: Int = DEFAULT_DURATION,
    easing: CubicBezierEasing = FastOutSlowInEasing,
    repeatMode: RepeatMode = RepeatMode.Restart,
) =
    repeatable(
        iterations = iterations,
        animation = CommonsTween<T>(duration = duration, easing = easing),
        repeatMode = repeatMode
    )

fun cubicBezier(a: Number, b: Number, c: Number, d: Number) =
    CubicBezierEasing(a.toFloat(), b.toFloat(), c.toFloat(), d.toFloat())
