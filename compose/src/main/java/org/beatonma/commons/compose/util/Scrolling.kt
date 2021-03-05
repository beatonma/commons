package org.beatonma.commons.compose.util

import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.nestedScroll
import kotlin.math.absoluteValue

/**
 * Sanity checks for Modifier.[nestedScroll] calculations.
 */

inline class ConsumedScroll(val value: Float)
inline class UnconsumedScroll(val value: Float)

fun checkConsumedValues(originalValue: Float, consumedScroll: ConsumedScroll, unconsumedScroll: UnconsumedScroll)
    = checkConsumedValues(originalValue, consumedValue = consumedScroll.value, unconsumedValue = unconsumedScroll.value)

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
