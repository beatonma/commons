package org.beatonma.commons.compose.util

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
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

@JvmInline
value class VerticalValue(override val value: Int = 0) : DirectionalValue,
    Comparable<VerticalValue> {

    operator fun plus(other: VerticalValue) = VerticalValue(value + other.value)
    operator fun times(b: Int) = VerticalValue(value * b)
    operator fun div(b: Int) = VerticalValue(value / b)
    operator fun minus(other: VerticalValue) = VerticalValue(value - other.value)

    override fun compareTo(other: VerticalValue) = value.compareTo(other.value)
}

@JvmInline
value class HorizontalValue(override val value: Int = 0) : DirectionalValue,
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
val Placeable.size get() = IntSize(width, height)

fun Int.asHorizontal() = HorizontalValue(this)
fun Int.asVertical() = VerticalValue(this)

fun MeasureScope.layout(
    width: HorizontalValue,
    height: VerticalValue,
    alignmentLines: Map<AlignmentLine, Int> = emptyMap(),
    placementBlock: Placeable.PlacementScope.() -> Unit,
): MeasureResult = layout(width.value, height.value, alignmentLines, placementBlock)

fun Dp.pxF(context: Context): Float =
    when (this) {
        Dp.Hairline -> 1F
        else -> (value * context.resources.displayMetrics.density)
    }

val Dp.pxF: Float
    @Composable get() = pxF(LocalContext.current)

fun Dp.px(context: Context): Int =
    when (this) {
        Dp.Hairline -> 1
        else -> (value * context.resources.displayMetrics.density)
            .roundToInt()
    }

val Dp.px: Int
    @Composable get() = px(LocalContext.current)
