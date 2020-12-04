package org.beatonma.commons.compose.util

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import org.beatonma.commons.compose.animation.lerpBetween
import org.beatonma.commons.core.extensions.lerpTo
import kotlin.math.roundToInt

/**
 * Wrappers for horizontal/vertical values for type safety when calculating layouts.
 * Reduce potential for errors caused by accidental use of horizontal values in vertical calculations
 * and vice-versa.
 */
interface DirectionalValue {

    val value: Int

    operator fun compareTo(other: Int) = value.compareTo(other)
}

inline class VerticalValue(override val value: Int = 0) : DirectionalValue,
    Comparable<VerticalValue> {

    operator fun plus(other: VerticalValue) = VerticalValue(value + other.value)
    operator fun times(b: Int) = VerticalValue(value * b)
    operator fun div(b: Int) = VerticalValue(value / b)
    operator fun minus(other: VerticalValue) = VerticalValue(value - other.value)

    override fun compareTo(other: VerticalValue) = value.compareTo(other.value)
}

inline class HorizontalValue(override val value: Int = 0) : DirectionalValue,
    Comparable<HorizontalValue> {

    operator fun plus(other: HorizontalValue) = HorizontalValue(value + other.value)
    operator fun times(b: Int) = HorizontalValue(value * b)
    operator fun div(b: Int) = HorizontalValue(value / b)
    operator fun minus(other: HorizontalValue) = HorizontalValue(value - other.value)

    override fun compareTo(other: HorizontalValue) = value.compareTo(other.value)
}

fun Placeable.PlacementScope.placeRelative(
    placeable: Placeable,
    x: HorizontalValue,
    y: VerticalValue,
) {
    placeable.placeRelative(x.value, y.value)
}

fun Placeable.dimensions() = Pair(HorizontalValue(width), VerticalValue(height))

fun Int.asHorizontal() = HorizontalValue(this)
fun Int.asVertical() = VerticalValue(this)

fun MeasureScope.layout(
    width: HorizontalValue,
    height: VerticalValue,
    alignmentLines: Map<AlignmentLine, Int> = emptyMap(),
    placementBlock: Placeable.PlacementScope.() -> Unit,
): MeasureResult = layout(width.value, height.value, alignmentLines, placementBlock)

@Composable
val Dp.pxF: Float
    get() = when (this) {
        Dp.Hairline -> 1F
        else -> (value * AmbientContext.current.resources.displayMetrics.density)
    }

@Composable
val Dp.px: Int
    get() = when (this) {
        Dp.Hairline -> 1
        else -> (value * AmbientContext.current.resources.displayMetrics.density)
            .roundToInt()
    }

fun Dp.lerp(other: Dp, progress: Float) = Dp(value.lerpTo(other.value, progress))

fun PaddingValues.lerp(other: PaddingValues, progress: Float) = PaddingValues(
    start = progress.lerpBetween(this.start, other.start).coerceAtLeast(0.dp),
    top = progress.lerpBetween(this.top, other.top).coerceAtLeast(0.dp),
    end = progress.lerpBetween(this.end, other.end).coerceAtLeast(0.dp),
    bottom = progress.lerpBetween(this.bottom, other.bottom).coerceAtLeast(0.dp),
)
