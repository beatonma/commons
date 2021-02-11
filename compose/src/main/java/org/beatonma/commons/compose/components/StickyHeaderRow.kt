package org.beatonma.commons.compose.components

import androidx.compose.foundation.gestures.rememberScrollableController
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.gesture.scrollorientationlocking.Orientation
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import kotlinx.coroutines.launch
import org.beatonma.commons.core.extensions.fastForEach
import org.beatonma.commons.core.extensions.fastForEachIndexed
import org.beatonma.commons.theme.compose.EndOfContent

@Composable
fun <T, H> StickyHeaderRow(
    items: List<T>,
    headerForItem: (T) -> H,
    headerContent: @Composable (H) -> Unit,
    itemContent: @Composable LazyItemScope.(T) -> Unit,
    appendEndOfContentSpacing: Boolean = true,
    modifier: Modifier = Modifier,
) {
    if (items.isEmpty()) return

    val state = rememberLazyListState()
    val visibleItemsInfo = state.layoutInfo.visibleItemsInfo
    val coroutineScope = rememberCoroutineScope()

    val scrollController = rememberScrollableController { delta ->
        coroutineScope.launch { state.scroll { scrollBy(delta) } }
        delta
    }

    val headers: List<H?> = visibleItemsInfo.map { info ->
        val item = items.getOrNull(info.index)
        if (item != null) headerForItem(item)
        else null
    }

    val headerPositions = getHeaderPositions(headers)

    Column(
        modifier
    ) {
        StickyHeader(
            headers,
            headerPositions,
            visibleItemsInfo,
            Modifier.scrollable(
                orientation = Orientation.Horizontal,
                controller = scrollController,
                reverseDirection = true,
            ),
            content = headerContent
        )

        LazyRow(
            state = state,
        ) {
            this.items(items, itemContent)

            if (appendEndOfContentSpacing) {
                item {
                    EndOfContent(Orientation.Horizontal)
                }
            }
        }
    }
}

@Composable
private fun <H> StickyHeader(
    headers: List<H?>,
    headerPositions: List<Int>,
    itemsInfo: List<LazyListItemInfo>,
    modifier: Modifier = Modifier,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    content: @Composable (H) -> Unit,
) {
    if (headers.isEmpty()) return
    val headerContent = constructHeaders(headers, content)

    Layout(
        content = headerContent,
        modifier = modifier,
    ) { measurables, constraints ->
        val placeables = measurables.mapIndexed { index, measurable ->
            measurable.measure(
                constraints.copy(
                    minHeight = constraints.minHeight,
                    maxHeight = constraints.maxHeight
                )
            )
        }

        val width: Int = constraints.maxWidth
        val height: Int = placeables.maxOf(Placeable::height)

        layout(width, height) {
            placeables.forEachIndexed { index, p ->
                val headerIndex = headerPositions[index]
                val nextHeaderIndex = headerPositions.getOrNull(index + 1) ?: -1

                val info = itemsInfo[headerIndex]
                val nextInfo = itemsInfo.getOrNull(nextHeaderIndex)

                val preferredX = info.offset.coerceAtLeast(0)
                val x = when {
                    nextInfo == null -> preferredX

                    preferredX + p.width > nextInfo.offset -> {
                        // Slide this header out of the way of the next one when they start to overlap
                        nextInfo.offset - p.width
                    }
                    else -> preferredX
                }

                val y = verticalAlignment.align(p.height, height)
                p.placeRelative(x, y)
            }
        }
    }
}

@Composable
private fun <H> constructHeaders(
    headers: List<H?>,
    content: @Composable (H) -> Unit,
): @Composable () -> Unit {
    val headerPositions = getHeaderPositions(headers)

    return {
        headerPositions.fastForEach { index ->
            val h = headers[index] ?: return@fastForEach

            content(h)
        }
    }
}

/**
 * Build list of positions where the header identifier changes.
 */
private fun <H> getHeaderPositions(headers: List<H?>): List<Int> {
    val headerPositions = mutableListOf<Int>()
    var previousHeader: H? = null
    headers.fastForEachIndexed { index, h ->
        if (h != null && h != previousHeader) {
            headerPositions += index
            previousHeader = h
        }
    }
    return headerPositions
}
