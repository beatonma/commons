package org.beatonma.commons.app.featured

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Layout
import androidx.compose.ui.MeasureScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.Placeable
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.annotation.VisibleForTesting
import androidx.compose.ui.util.fastForEach
import org.beatonma.commons.compose.util.*
import kotlin.math.ceil
import kotlin.math.roundToInt

/**
 * Displays member profile of two different sizes: big ones with an avatar and small ones without.
 * Small profiles are grouped into columns among the large profiles.
 *
 * e.g. Two big profiles and three small ones might look like:
 *
 *  --- === ---
 *  | | === | |
 *  --- === ---
 */
@Composable
internal fun MembersLayout(
    modifier: Modifier = Modifier,
    wrapHeight: Int = 240.dp.value.toInt(),
    children: @Composable () -> Unit,
) {
    Layout(children, modifier) { measurables, constraints ->
        val placeables = measurables.map { it.measure(constraints) }

        buildProfilesLayout(placeables, wrapHeight)
    }
}

@Composable
internal fun ScrollableMembersLayout(
    modifier: Modifier = Modifier.wrapContentSize(),
    scrollState: ScrollState = rememberScrollState(0f),
    isScrollEnabled: Boolean = true,
    reverseScrollDirection: Boolean = false,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    children: @Composable () -> Unit,
) {
    MembersLayout(modifier = modifier
        .horizontalScroll(
            scrollState,
            isScrollEnabled,
            reverseScrolling = reverseScrollDirection,
        )
        .clipToBounds()
        .padding(contentPadding),
        children = children
    )
}

/**
 * Layout items as a simple row - all items must have the same dimensions.
 */
private fun MeasureScope.buildProfilesLayoutAllSameSize(
    placeables: List<Placeable>,
): MeasureScope.MeasureResult {

    val f = placeables.first()
    val (w, h) = f.dimensions()

    val totalWidth = w * placeables.size

    return layout(totalWidth, h) {
        var x = HorizontalValue()
        val y = VerticalValue()

        placeables.fastForEach { placeable ->
            placeRelative(placeable, x, y)
            x += w
        }
    }
}

/**
 * Expects items of 0-2 different sizes. Small items are grouped into columns.
 * Large items get a column of their own.
 */
@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
fun MeasureScope.buildProfilesLayout(
    placeables: List<Placeable>,
    wrapHeight: Int,
): MeasureScope.MeasureResult {

    if (placeables.isEmpty()) {
        return layout(0, 0) {}
    }

    val groups = placeables.groupBy { it.height }

    if (groups.size == 1) {
        return buildProfilesLayoutAllSameSize(placeables)
    }

    check(groups.size == 2)

    val heights = groups.keys.sorted()
    val minHeight = (heights.firstOrNull() ?: 1).asVertical()
    val maxHeight = (heights.lastOrNull() ?: wrapHeight).asVertical()

    val smallRows = (maxHeight.value.toFloat() / minHeight.value.toFloat()).roundToInt()

    val smallCount = groups[minHeight.value]!!.size
    val largeCount = groups[maxHeight.value]!!.size
    val smallColumnsCount = ceil((smallCount.toFloat() / smallRows.toFloat())).toInt()
    val columnCount = smallColumnsCount + largeCount
    val totalWidth = (columnCount * placeables.first().width).asHorizontal()

    return layout(totalWidth, maxHeight) {
        var smallRow = 0
        var smallX = HorizontalValue(Int.MIN_VALUE)
        var smallY = VerticalValue()

        var largeX = HorizontalValue(Int.MIN_VALUE)
        val largeY = VerticalValue()

        var nextX = HorizontalValue()

        placeables.fastForEach { placeable ->
            val (w, h) = placeable.dimensions()

            // Combine small items into columns
            if (h == minHeight) {
                if (smallX < 0) {
                    smallX = nextX
                    nextX = maxOf(smallX, largeX) + w
                }

                placeRelative(placeable, smallX, smallY)

                if (smallRow >= smallRows) {
                    smallRow = 0
                    smallX = nextX
                    smallY = 0.asVertical()
                }
                else {
                    smallY += h
                }
            }

            // Large items get their own column.
            else {
                if (largeX < 0) {
                    largeX = nextX
                    nextX = maxOf(smallX, largeX) + w
                }
                placeRelative(placeable, largeX, largeY)

                largeX = nextX
            }

            nextX = maxOf(smallX, largeX) + w
        }
    }
}
