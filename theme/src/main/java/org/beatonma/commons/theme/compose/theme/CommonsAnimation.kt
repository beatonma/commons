package org.beatonma.commons.theme.compose.theme

import androidx.annotation.IntRange
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColor
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
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocal
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val DURATION_DEFAULT = 300

val LocalAnimationSpec: CompositionLocal<DefaultAnimation> = staticCompositionLocalOf { CommonsAnimation }

private object CommonsAnimation: DefaultAnimation {
    override val itemDelay: Long = 30L

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

    private fun <T> CommonsSpring(
        dampingRatio: Float = Spring.DampingRatioNoBouncy,
        stiffness: Float = 60F,
//    stiffness: Float = 200F
    ) = spring<T>(dampingRatio = dampingRatio, stiffness = stiffness)

    private fun <T> CommonsTween(
        duration: Int = DURATION_DEFAULT,
        easing: Easing = FastOutSlowInEasing,
    ) = tween<T>(duration, easing = easing)
}

/**
 * Wrapper for compose.animation functions that allows a custom animationSpec to be provided as the default.
 */
@OptIn(ExperimentalAnimationApi::class)
interface DefaultAnimation {
    val itemDelay: Long

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
    ): State<Float> = androidx.compose.animation.core.animateFloatAsState(
        targetValue = targetValue,
        animationSpec = spec(),
        visibilityThreshold = visibilityThreshold,
        finishedListener = finishedListener
    )

    /**
     * Animate with an initial [delay].
     */
    @Composable
    fun animateFloatAsState(
        targetValue: Float,
        initialValue: Float,
        delay: Long,
        visibilityThreshold: Float = 0.01F,
        finishedListener: ((Float) -> Unit)? = null,
    ): State<Float> {
        var value by remember { mutableStateOf(initialValue) }

        val state = androidx.compose.animation.core.animateFloatAsState(
            targetValue = value,
            animationSpec = spec(),
            visibilityThreshold = visibilityThreshold,
            finishedListener = finishedListener
        )

        LaunchedEffect(targetValue) {
            if (targetValue != value) {
                launch {
                    delay(delay)
                    value = targetValue
                }
            }
        }

        return state
    }

    /**
     * Animate with an initial delay based on [position]. Useful for per-item animations in a list.
     */
    @Composable
    fun animateFloatAsState(
        targetValue: Float,
        initialValue: Float,
        position: Int,
        visibilityThreshold: Float = 0.01F,
        finishedListener: ((Float) -> Unit)? = null,
    ): State<Float> = animateFloatAsState(
        targetValue = targetValue,
        initialValue = initialValue,
        delay = itemDelay * position,
    )

    @Composable
    fun animateColorAsState(
        targetValue: Color,
        finishedListener: ((Color) -> Unit)? = null,
    ): State<Color> = androidx.compose.animation.animateColorAsState(
        targetValue = targetValue,
        finishedListener = finishedListener
    )

    @Composable
    fun animateDpAsState(
        targetValue: Dp,
        finishedListener: ((Dp) -> Unit)? = null,
    ): State<Dp> = androidx.compose.animation.core.animateDpAsState(
        targetValue = targetValue,
        finishedListener = finishedListener
    )

    @Composable
    fun <S> animateFloat(
        transition: Transition<S>,
        transitionSpec: @Composable Transition.Segment<S>.() -> FiniteAnimationSpec<Float> = { spec() },
        label: String = "FloatAnimation",
        targetValueByState: @Composable (state: S) -> Float
    ): State<Float> = transition.animateFloat(
        transitionSpec = transitionSpec,
        label = label,
        targetValueByState = targetValueByState
    )

    @Composable
    fun <S> animateColor(
        transition: Transition<S>,
        transitionSpec: @Composable Transition.Segment<S>.() -> FiniteAnimationSpec<Color> = { spec() },
        label: String = "FloatAnimation",
        targetValueByState: @Composable (state: S) -> Color
    ): State<Color> = transition.animateColor(
        transitionSpec = transitionSpec,
        label = label,
        targetValueByState = targetValueByState
    )

    @Composable
    fun <S> animateDp(
        transition: Transition<S>,
        transitionSpec: @Composable Transition.Segment<S>.() -> FiniteAnimationSpec<Dp> = { spec() },
        label: String = "FloatAnimation",
        targetValueByState: @Composable (state: S) -> Dp
    ): State<Dp> = transition.animateDp(
        transitionSpec = transitionSpec,
        label = label,
        targetValueByState = targetValueByState
    )
}

private fun cubicBezier(a: Number, b: Number, c: Number, d: Number) =
    CubicBezierEasing(a.toFloat(), b.toFloat(), c.toFloat(), d.toFloat())
