package org.beatonma.commons.compose.components

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import org.beatonma.commons.compose.util.size
import kotlin.math.min

@Composable
fun <T, H> StickyHeaderRow(
    items: List<T>,
    headerForItem: (T) -> H?,
    itemContent: @Composable (T) -> Unit,
    headerContent: @Composable (H?) -> Unit,
    modifier: Modifier = Modifier,
    groupModifier: @Composable (H?) -> Modifier = { Modifier },
    state: LazyListState = rememberLazyListState()
) {
    val headerMap = items.groupBy(headerForItem)

    LazyRow(modifier, state) {
        var n = 0
        headerMap.forEach { (header, groupItems) ->
            val offset = when (state.firstVisibleItemIndex) {
                n++ -> state.firstVisibleItemScrollOffset
                else -> 0
            }

            item {
                Group(header, groupItems, headerContent, itemContent, offset, groupModifier(header))
            }
        }
    }
}


@Composable
private fun <T, H> Group(
    header: H?,
    items: List<T>,
    headerContent: @Composable (H?) -> Unit,
    itemContent: @Composable (T) -> Unit,
    offset: Int,
    modifier: Modifier
) {
    Layout(
        content = {
            headerContent(header)
            items.forEach { itemContent(it) }
        },
        modifier = modifier,
    ) { measurables, constraints ->
        val headerPlaceable = measurables.first().measure(constraints)
        val itemPlaceables = measurables.drop(1).map { it.measure(constraints) }

        val (headerWidth, headerHeight) = headerPlaceable.size

        val width = maxOf(headerWidth, itemPlaceables.sumOf { it.width })
        val height = headerHeight + itemPlaceables.maxOf { it.height }

        layout(width, height) {
            headerPlaceable.placeRelative(
                min(offset, width - headerWidth),
                0
            )

            var x = 0
            itemPlaceables.forEach {
                it.placeRelative(x, headerHeight)
                x += it.width
            }
        }
    }
}
