package org.beatonma.commons.compose.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.testTag
import org.beatonma.commons.compose.TestTag
import org.beatonma.commons.compose.util.math.Matrix2x2
import kotlin.math.roundToInt

private const val DefaultMin = Int.MAX_VALUE
private const val DefaultMax = Int.MIN_VALUE

/**
 * Deprecated. Use [org.beatonma.commons.compose.layout.stickyHeaderWithInsets] and/or
 * [org.beatonma.commons.compose.layout.stickyHeaderWithState] and/or
 * [org.beatonma.commons.compose.layout.itemWithState].
 */
@Deprecated(
    "Same effect can be achieved reliably with multiple items instead of putting" +
            "everything inside one 'smart' item. See LazyListScope.stickyHeaderWithState.",
)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CollapsibleHeaderLayout(
    header: @Composable (Float) -> Unit,
    state: LazyListState = rememberLazyListState(),
    content: LazyListScope.() -> Unit,
) {
    var expansion by remember { mutableStateOf(1f) }

    var minHeight by remember { mutableStateOf(DefaultMin) }
    var maxHeight by remember { mutableStateOf(DefaultMax) }

    var flexibleHeight by remember { mutableStateOf(0) }

    val matrix = remember { Matrix2x2() }
    val matrixArray = remember { arrayOf(0f, 0f) }

    LaunchedEffect(state.firstVisibleItemScrollOffset) {
        if (minHeight < 0 || maxHeight < 0) {
            return@LaunchedEffect
        }

        val firstItemIndex = state.firstVisibleItemIndex
        val firstItemOffset = state.firstVisibleItemScrollOffset

        if (firstItemIndex == 0) {
            if (maxHeight != DefaultMax) {
                expansion = if (flexibleHeight != 0) {
                    1f - (firstItemOffset / flexibleHeight.toFloat())
                } else {
                    1f - (firstItemOffset / maxHeight.toFloat())
                }.coerceIn(0f, 1f)
            }
        } else {
            // Collapse header completely
            expansion = 0f
        }
    }

    LazyColumn(
        state = state,
        modifier = Modifier.testTag(TestTag.LazyList),
    ) {
        stickyHeader {
            Layout(
                content = {
                    header(expansion)
                },
                Modifier.testTag(TestTag.CollapsingHeader)
            ) { measurables, constraints ->
                check(measurables.size == 1) {
                    "CollapsibleHeaderLayout only supports one child layout (got ${measurables.size})"
                }

                val placeable = measurables.first().measure(constraints)

                val width: Int = placeable.width
                val height: Int = placeable.height


                if (expansion == 1f && maxHeight == DefaultMax) {
                    // First layout
                    maxHeight = height
                } else if (minHeight == DefaultMin) {
                    if (expansion == 0f) {
                        minHeight = height
                        flexibleHeight = maxHeight - minHeight
                    } else {
                        flexibleHeight =
                            predictFlexibleHeight(maxHeight, height, expansion, matrix, matrixArray)
                    }
                } else if (height < minHeight) {
                    minHeight = height
                    flexibleHeight = maxHeight - minHeight
                }

                layout(width, maxHeight) {
                    placeable.placeRelative(0, 0)
                }
            }
        }

        content()
    }
}

/**
 * Current header height = (expansion * flexibleHeight) + minHeight
 *
 * [flexibleHeight] and minHeight are not known until we actually reach expansion == 0f, but we can
 * predict their values by extrapolating from measured heights at 2 different points of expansion.
 *
 * On first layout (with expansion == 1f) we get: maxHeight = (1f * flexibleHeight) + (1f * minHeight)
 * On next layout (with expansion < 1f) we get : currentHeight = (expansion * flexibleHeight) + (1f * minHeight)
 *
 * Or:
 *   y1 = x + c
 *   y2 = ax + c
 *
 * We can then solve this as a system of equations by putting the coefficients of
 * flexibleHeight and minHeight into a matrix.
 *
 * We can then solve Ax = b for x, where A is our matrix of coefficients and y is a vector of known heights.
 *
 * x = inverse(A) * b
 *
 * This gives us the vector x with projected values for flexibleHeight and minHeight.
 */
private fun predictFlexibleHeight(
    maxHeight: Int,
    currentHeight: Int,
    expansion: Float,
    matrix: Matrix2x2,
    knownValues: Array<Float>
): Int {
    matrix.set(1f, 1f, expansion, 1f)

    try {
        matrix.invert()
    } catch (e: ArithmeticException) {
        return 0
    }
    knownValues[0] = maxHeight.toFloat()
    knownValues[1] = currentHeight.toFloat()

    matrix *= knownValues
    return knownValues[0].roundToInt()
}
