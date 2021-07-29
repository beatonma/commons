package org.beatonma.commons.testcompose

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assertHeightIsAtLeast
import androidx.compose.ui.test.assertWidthIsAtLeast
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import kotlin.math.absoluteValue


/**
 * Ensure a node that responds to touch events meets minimum expected width and height for accessibility.
 */
fun SemanticsNodeInteraction.assertSizeIsTouchable() =
    assertSizeIsAtLeast(Accessibility.MinTouchSize)

/**
 * Ensure node width and height at each at least the given value.
 */
fun SemanticsNodeInteraction.assertSizeIsAtLeast(expected: Dp) =
    this.assertWidthIsAtLeast(expected)
        .assertHeightIsAtLeast(expected)

/**
 * Ensure node width and height are equal.
 */
fun SemanticsNodeInteraction.assertSizeIsSquare() =
    withUnclippedBoundsInRoot {
        assert(it.width == it.height) {
            "Actual size is ${it.width.toDp()}dp x ${it.height.toDp()}dp, expected width and height to be equal"
        }
    }

fun SemanticsNodeInteraction.assertWidthIsAtMost(expected: Dp) =
    withUnclippedBoundsInRoot {
        isAtMostOrThrow("width", it.width, expected)
    }

fun SemanticsNodeInteraction.assertHeightIsAtMost(expected: Dp) =
    withUnclippedBoundsInRoot {
        isAtMostOrThrow("height", it.height, expected)
    }


private fun SemanticsNodeInteraction.withUnclippedBoundsInRoot(
    assertion: Density.(Rect) -> Unit
): SemanticsNodeInteraction {
    val node = fetchSemanticsNode("Failed to retrieve bounds of the node.")
    val density = node.root!!.density

    assertion.invoke(density, node.unclippedBoundsInRoot)
    return this
}

object Accessibility {
    val MinTouchSize: Dp get() = 48.dp
}

private const val floatTolerance = 0.5f
private fun Dp.assertIsEqualTo(
    expected: Dp,
    subject: String = "",
    tolerance: Dp = Dp(floatTolerance)
) {
    val diff = (this - expected).value.absoluteValue
    if (diff > tolerance.value) {
        // Comparison failed, report the error in DPs
        throw AssertionError(
            "Actual $subject is $this, expected $expected (tolerance: $tolerance)"
        )
    }
}

private val SemanticsNode.unclippedBoundsInRoot: Rect
    get() {
        return Rect(positionInRoot, size.toSize())
    }

private fun Density.isAtMostOrThrow(
    subject: String,
    actualPx: Float,
    expected: Dp
) {
    if (actualPx + floatTolerance > expected.toPx()) {
        // Comparison failed, report the error in DPs
        throw AssertionError(
            "Actual $subject is ${actualPx.toDp()}, expected at most $expected"
        )
    }
}
