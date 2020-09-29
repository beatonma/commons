package org.beatonma.commons.compose.util

import androidx.compose.ui.AlignmentLine
import androidx.compose.ui.MeasureScope
import androidx.compose.ui.Placeable

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
): MeasureScope.MeasureResult = layout(width.value, height.value, alignmentLines, placementBlock)
