package org.beatonma.commons.theme.compose.theme

import androidx.annotation.IntRange
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.DurationBasedAnimationSpec
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.RepeatableSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

private const val DURATION_DEFAULT = 300

val LocalAnimationSpec: CompositionLocal<DefaultAnimation> = staticCompositionLocalOf { CommonsAnimation }

private object CommonsAnimation: DefaultAnimation {
    override fun <T> spec(): FiniteAnimationSpec<T> = CommonsSpring()
    override fun <T> fast(): FiniteAnimationSpec<T> = CommonsSpring(stiffness = 2500F)

    override fun <T> spring(): SpringSpec<T> = CommonsSpring()
    override fun <T> tween(duration: Int): TweenSpec<T> = CommonsTween()

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
}

/**
 * Wrapper for compose.animation functions that allows a custom animationSpec to be provided as the default.
 */
@OptIn(ExperimentalAnimationApi::class)
interface DefaultAnimation {
    fun <T> spec(): FiniteAnimationSpec<T>
    fun <T> fast(): FiniteAnimationSpec<T>

    fun <T> spring(): SpringSpec<T>
    fun <T> tween(duration: Int = DURATION_DEFAULT): TweenSpec<T>

    fun <T> repeatable(
        @IntRange(from = 2) iterations: Int = 3,
        animation: DurationBasedAnimationSpec<T>,
        repeatMode: RepeatMode = RepeatMode.Restart,
    ): RepeatableSpec<T>

    fun <T> infiniteRepeatable(
        duration: Int,
        repeatMode: RepeatMode = RepeatMode.Reverse
    ): InfiniteRepeatableSpec<T>

    @Composable
    fun <T> Crossfade(
        targetState: T,
        modifier: Modifier = Modifier,
        animationSpec: FiniteAnimationSpec<Float> = spec(),
        content: @Composable (T) -> Unit
    ) {
        androidx.compose.animation.Crossfade(
            targetState = targetState,
            animationSpec = tween(1000),
            content = content,
        )
    }

    @Composable
    fun animateFloatAsState(
        targetValue: Float,
        visibilityThreshold: Float = 0.01F,
        finishedListener: ((Float) -> Unit)? = null,
    ) = androidx.compose.animation.core.animateFloatAsState(
        targetValue = targetValue,
        animationSpec = spec(),
        visibilityThreshold = visibilityThreshold,
        finishedListener = finishedListener
    )

    @Composable
    fun animateColorAsState(
        targetValue: Color,
        finishedListener: ((Color) -> Unit)? = null,
    ) = androidx.compose.animation.animateColorAsState(
        targetValue = targetValue,
        finishedListener = finishedListener
    )

    @Composable
    fun animateDpAsState(
        targetValue: Dp,
        finishedListener: ((Dp) -> Unit)? = null,
    ) = androidx.compose.animation.core.animateDpAsState(
        targetValue = targetValue,
        finishedListener = finishedListener
    )

    @Composable
    fun <S> animateFloat(
        transition: Transition<S>,
        transitionSpec: @Composable Transition.Segment<S>.() -> FiniteAnimationSpec<Float> = { spec() },
        label: String = "FloatAnimation",
        targetValueByState: @Composable (state: S) -> Float
    ) = transition.animateFloat(
        transitionSpec = transitionSpec,
        label = label,
        targetValueByState = targetValueByState
    )
}

private fun <T> CommonsSpring(
    dampingRatio: Float = Spring.DampingRatioNoBouncy,
    stiffness: Float = 200F
) = spring<T>(dampingRatio = dampingRatio, stiffness = stiffness)

private fun <T> CommonsTween(
    duration: Int = DURATION_DEFAULT,
    easing: Easing = FastOutSlowInEasing,
) = tween<T>(duration, easing = easing)


private fun cubicBezier(a: Number, b: Number, c: Number, d: Number) =
    CubicBezierEasing(a.toFloat(), b.toFloat(), c.toFloat(), d.toFloat())
