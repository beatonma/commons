package org.beatonma.commons.app.ui.screens.frontpage

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.unit.dp
import org.beatonma.commons.compose.util.size
import org.beatonma.commons.core.extensions.fastForEach
import kotlin.math.ceil
import kotlin.math.roundToInt

/**
 * TODO cannot browse with accessibility
 *
 * Displays member profile of two different sizes: big ones with an avatar and small ones without.
 * Small profiles are grouped into columns among the large profiles.
 *
 * e.g. Two big profiles and three small ones might look like:
 *
 *  --- === ---
 *  | | === | |
 *  --- === ---
 */
@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@Composable
private fun MembersLayout(
    modifier: Modifier,
    wrapHeight: Int,
    content: @Composable () -> Unit,
) {
    Layout(content, modifier) { measurables, constraints ->
        val placeables = measurables.map { it.measure(constraints) }

        buildProfilesLayout(placeables, wrapHeight)
    }
}

@Composable
internal fun MembersLayout(
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(0),
    isScrollEnabled: Boolean = true,
    reverseScrollDirection: Boolean = false,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    content: @Composable () -> Unit,
) {
    MembersLayout(
        modifier = modifier
            .horizontalScroll(
                scrollState,
                isScrollEnabled,
                reverseScrolling = reverseScrollDirection,
            )
            .clipToBounds()
            .padding(contentPadding),
        wrapHeight = 240.dp.value.toInt(),
        content = content
    )
}

/**
 * Layout items as a simple row - all items must have the same dimensions.
 */
private fun MeasureScope.buildProfilesLayoutAllSameSize(
    placeables: List<Placeable>,
): MeasureResult {

    val f = placeables.first()
    val (w, h) = f.size

    val totalWidth = w * placeables.size

    return layout(totalWidth, h) {
        var x = 0
        val y = 0

        placeables.fastForEach { placeable ->
            placeable.placeRelative(x, y)
            x += w
        }
    }
}

/**
 * Expects items of 0-2 different sizes. Small items are grouped into columns.
 * Large items get a column of their own.
 */
private fun MeasureScope.buildProfilesLayout(
    placeables: List<Placeable>,
    wrapHeight: Int,
): MeasureResult {
    if (placeables.isEmpty()) {
        return layout(0, 0) {}
    }

    val groups = placeables.groupBy { it.height }

    if (groups.size == 1) {
        return buildProfilesLayoutAllSameSize(placeables)
    }

    check(groups.size == 2)

    val heights = groups.keys.sorted()
    val minHeight = (heights.firstOrNull() ?: 1)
    val maxHeight = (heights.lastOrNull() ?: wrapHeight)

    val smallRows = (maxHeight.toFloat() / minHeight.toFloat()).roundToInt()

    val smallCount = groups[minHeight]!!.size
    val largeCount = groups[maxHeight]!!.size
    val smallColumnsCount = ceil((smallCount.toFloat() / smallRows.toFloat())).toInt()
    val columnCount = smallColumnsCount + largeCount
    val totalWidth = (columnCount * placeables.first().width)

    return layout(totalWidth, maxHeight) {
        var smallRow = 0
        var smallX = Int.MIN_VALUE
        var smallY = 0

        var largeX = Int.MIN_VALUE
        val largeY = 0

        var nextX = 0

        placeables.fastForEach { placeable ->
            val (w, h) = placeable.size

            // Combine small items into columns
            if (h == minHeight) {
                if (smallX < 0) {
                    smallX = nextX
                    nextX = maxOf(smallX, largeX) + w
                }

                placeable.placeRelative(smallX, smallY)

                if (smallRow >= smallRows) {
                    smallRow = 0
                    smallX = nextX
                    smallY = 0
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
                placeable.placeRelative(largeX, largeY)

                largeX = nextX
            }

            nextX = maxOf(smallX, largeX) + w
        }
    }
}
