package org.beatonma.commons.themed

import androidx.annotation.IntRange
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.DurationBasedAnimationSpec
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.RepeatableSpec
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.TweenSpec
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


val LocalAnimationSpec: ProvidableCompositionLocal<ThemedAnimation> =
    staticCompositionLocalOf { error("LocalAnimationSpec has not been provided") }

inline val animation
    @ReadOnlyComposable @Composable get() = LocalAnimationSpec.current

/**
 * Wrapper for compose.animation functions that allows a custom animationSpec to be provided as the default.
 */
@OptIn(ExperimentalAnimationApi::class)
interface ThemedAnimation {
    val itemDelay: Long

    fun <T> spec(): FiniteAnimationSpec<T>
    fun <T> fast(): FiniteAnimationSpec<T>

    fun <T> spring(): SpringSpec<T>
    fun <T> stiffSpring(): SpringSpec<T> = spring()
    fun <T> tween(duration: Int): TweenSpec<T>

    fun <T> repeatable(
        @IntRange(from = 2) iterations: Int = 3,
        animation: DurationBasedAnimationSpec<T>,
        repeatMode: RepeatMode = RepeatMode.Restart,
    ): RepeatableSpec<T>

    fun <T> infiniteRepeatable(
        duration: Int,
        repeatMode: RepeatMode = RepeatMode.Reverse,
    ): InfiniteRepeatableSpec<T>

    @Composable
    fun animateFloatAsState(
        targetValue: Float,
        visibilityThreshold: Float = 0.01F,
        animationSpec: FiniteAnimationSpec<Float> = spec(),
        finishedListener: ((Float) -> Unit)? = null,
    ): State<Float> = androidx.compose.animation.core.animateFloatAsState(
        targetValue = targetValue,
        animationSpec = animationSpec,
        visibilityThreshold = visibilityThreshold,
        finishedListener = finishedListener,
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
            finishedListener = finishedListener,
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
    fun animateIntAsState(
        targetValue: Int,
        animationSpec: FiniteAnimationSpec<Int> = spec(),
        finishedListener: ((Int) -> Unit)? = null,
    ): State<Int> = androidx.compose.animation.core.animateIntAsState(
        targetValue = targetValue,
        animationSpec = animationSpec,
        finishedListener = finishedListener,
    )

    @Composable
    fun animateColorAsState(
        targetValue: Color,
        animationSpec: AnimationSpec<Color> = spec(),
        finishedListener: ((Color) -> Unit)? = null,
    ): State<Color> = androidx.compose.animation.animateColorAsState(
        targetValue = targetValue,
        animationSpec = animationSpec,
        finishedListener = finishedListener,
    )

    @Composable
    fun animateDpAsState(
        targetValue: Dp,
        animationSpec: AnimationSpec<Dp> = spec(),
        finishedListener: ((Dp) -> Unit)? = null,
    ): State<Dp> = androidx.compose.animation.core.animateDpAsState(
        targetValue = targetValue,
        animationSpec = animationSpec,
        finishedListener = finishedListener,
    )
}
