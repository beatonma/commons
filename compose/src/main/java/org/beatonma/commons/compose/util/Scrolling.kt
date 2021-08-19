package org.beatonma.commons.compose.util

import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import kotlin.math.abs
import kotlin.math.absoluteValue

/**
 * Sanity checks for Modifier.[nestedScroll] calculations.
 */

@JvmInline
value class ConsumedScroll(val value: Float)

@JvmInline
value class UnconsumedScroll(val value: Float)

fun checkConsumedValues(
    originalValue: Float,
    consumedScroll: ConsumedScroll,
    unconsumedScroll: UnconsumedScroll
) = checkConsumedValues(
    originalValue,
    consumedValue = consumedScroll.value,
    unconsumedValue = unconsumedScroll.value
)

/**
 * Check that Modifier.[nestedScroll]/[NestedScrollConnection] scroll consumption calculations make sense.
 */
fun checkConsumedValues(originalValue: Float, consumedValue: Float, unconsumedValue: Float) {
    // Consumed and unconsumed values should both be closer to zero than the original value.
    check(consumedValue.isLowerMagnitudeThan(originalValue)) {
        "consumedValue ($consumedValue) must be closer to zero than originalValue ($originalValue)"
    }
    check(unconsumedValue.isLowerMagnitudeThan(originalValue)) {
        "unconsumedValue ($unconsumedValue) must be closer to zero than originalValue ($originalValue)"
    }

    // Consumed and unconsumed values should both be on the same side of zero as the original value.
    // i.e. both are negative or both are positive.
    check(consumedValue.isSameDirectionAs(originalValue)) {
        if (originalValue > 0) "Scroll value ($originalValue) is positive so consumed value ($consumedValue) must also be >= 0F"
        else "Scroll value ($originalValue) is negative so consumed value ($consumedValue) must also be <= 0F"
    }
    check(unconsumedValue.isSameDirectionAs(originalValue)) {
        if (originalValue > 0) "Scroll value ($originalValue) is positive so unconsumed value ($unconsumedValue) must also be >= 0F"
        else "Scroll value ($originalValue) is negative so unconsumed value ($unconsumedValue) must also be <= 0F"
    }
}

fun checkConsumedValues(originalValue: Float, consumedValue: Float) {
    // Consumed and unconsumed values should both be closer to zero than the original value.
    check(consumedValue.isLowerMagnitudeThan(originalValue)) {
        "consumedValue ($consumedValue) must be closer to zero than originalValue ($originalValue)"
    }

    // Consumed and unconsumed values should both be on the same side of zero as the original value.
    // i.e. both are negative or both are positive.
    check(consumedValue.isSameDirectionAs(originalValue)) {
        if (originalValue > 0) "Scroll value ($originalValue) is positive so consumed value ($consumedValue) must also be >= 0F"
        else "Scroll value ($originalValue) is negative so consumed value ($consumedValue) must also be <= 0F"
    }
}

fun printConsumedValues(originalValue: Float, consumedValue: Float) {
    if (!consumedValue.isLowerMagnitudeThan(originalValue)) {
        println(
            "consumedValue $consumedValue magnitude is larger than originalValue $originalValue (difference=${
                abs(
                    originalValue - consumedValue
                )
            })"
        )
    }
    if (!consumedValue.isSameDirectionAs(originalValue)) {
        println("Consumed value $consumedValue has the wrong sign: $originalValue")
    }
}

private fun Float.isLowerMagnitudeThan(other: Float, orEqual: Boolean = true) =
    when {
        orEqual -> this.absoluteValue <= other.absoluteValue
        else -> this.absoluteValue < other.absoluteValue
    }

private fun Float.isSameDirectionAs(other: Float) = when {
    this == other -> true
    this == 0F || this == -0F -> true
    this <= 0F && other <= 0F -> true
    this >= 0F && other >= 0F -> true
    else -> false
}

enum class ScrollDirection {
    Zero,
    TowardsTop,
    TowardsBottom,
    ;

    fun sameAs(other: ScrollDirection) = this == other || this == Zero || other == Zero
}

fun Float.withDirection(direction: ScrollDirection): Float {
    return when (direction) {
        ScrollDirection.Zero -> 0F
        ScrollDirection.TowardsTop -> if (this > 0F) this else this * -1
        ScrollDirection.TowardsBottom -> if (this < 0F) this else this * -1
    }
}

private fun scrollDirection(delta: Float) = when {
    delta > 0F -> ScrollDirection.TowardsTop
    delta < 0F -> ScrollDirection.TowardsBottom
    else -> ScrollDirection.Zero
}


enum class FlingDirection {
    Zero,
    TowardsTop,
    TowardsBottom,
    ;

    fun sameAs(other: FlingDirection) = this == other || this == Zero || other == Zero
}

private fun flingDirection(fling: Float) = when {
    fling > 0F -> FlingDirection.TowardsTop
    fling < 0F -> FlingDirection.TowardsBottom
    else -> FlingDirection.Zero
}

fun Float.withDirection(direction: FlingDirection): Float {
    return when (direction) {
        FlingDirection.Zero -> 0F
        FlingDirection.TowardsTop -> if (this > 0F) this else this * -1
        FlingDirection.TowardsBottom -> if (this < 0F) this else this * -1
    }
}
