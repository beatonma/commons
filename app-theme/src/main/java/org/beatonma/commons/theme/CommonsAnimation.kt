package org.beatonma.commons.theme

import androidx.compose.animation.core.DurationBasedAnimationSpec
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.RepeatableSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import org.beatonma.commons.themed.ThemedAnimation

private const val DURATION_DEFAULT = 300

internal object CommonsAnimation : ThemedAnimation {
    override val itemDelay: Long = 30L

    override fun <T> spec(): FiniteAnimationSpec<T> = CommonsSpring()
    override fun <T> fast(): FiniteAnimationSpec<T> = CommonsSpring(stiffness = 2500F)

    override fun <T> spring(): SpringSpec<T> = CommonsSpring()
    override fun <T> tween(duration: Int): TweenSpec<T> = CommonsTween(duration)

    override fun <T> repeatable(
        iterations: Int,
        animation: DurationBasedAnimationSpec<T>,
        repeatMode: RepeatMode
    ): RepeatableSpec<T> = androidx.compose.animation.core.repeatable(
        iterations = iterations,
        animation = animation,
        repeatMode = repeatMode,
    )

    override fun <T> infiniteRepeatable(
        duration: Int,
        repeatMode: RepeatMode
    ): InfiniteRepeatableSpec<T> = infiniteRepeatable(
        animation = tween(duration),
        repeatMode = repeatMode,
    )

    private fun <T> CommonsSpring(
        dampingRatio: Float = Spring.DampingRatioNoBouncy,
        stiffness: Float = 200F,
    ) = spring<T>(dampingRatio = dampingRatio, stiffness = stiffness)

    private fun <T> CommonsTween(
        duration: Int = DURATION_DEFAULT,
        easing: Easing = FastOutSlowInEasing,
    ) = tween<T>(duration, easing = easing)
}
