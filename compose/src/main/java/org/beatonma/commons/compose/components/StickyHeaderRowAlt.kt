package org.beatonma.commons.compose.components

import androidx.compose.foundation.gestures.rememberScrollableController
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.gesture.scrollorientationlocking.Orientation
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.unit.Constraints
import kotlinx.coroutines.launch
import org.beatonma.commons.core.extensions.fastForEachIndexed
import org.beatonma.commons.theme.compose.EndOfContent
import org.beatonma.commons.theme.compose.Padding

private data class AnnotatedItem<T>(
    val item: T,
    val position: GroupPosition
)
private class Headers<H>(
    val visible: List<H?>,
    val positions: List<Int>
)

private enum class GroupPosition {
    Start,
    Middle,
    End,
    Only, // Only item in the group -> single item is both Start and End
    ;
}

private data class Metrics<T, H>(
    val items: List<AnnotatedItem<T>>,
    val visibleHeaders: Headers<H>,
)

@Composable
private fun <T, H> metricsOf(
    items: List<T>,
    visibleItemInfo: List<LazyListItemInfo>,
    headerForItem: (T?) -> H?,
): Metrics<T, H> {
    val headers: List<H?> = items.map(headerForItem)
    val headerPositions = getHeaderPositions(headers)

    val visibleHeaders = visibleItemInfo.map { headers.getOrNull(it.index) }
    val visibleHeaderPositions = getHeaderPositions(visibleHeaders)

    val lastIndex = items.size - 1
    val annotatedItems = items.mapIndexed { index, item ->
        val nextIndex = index + 1

        val isFirstInGroup = index in headerPositions
        val isLastInGroup = nextIndex in headerPositions || nextIndex > lastIndex

        val position: GroupPosition = when {
            isFirstInGroup && isLastInGroup -> GroupPosition.Only
            isFirstInGroup -> GroupPosition.Start
            isLastInGroup -> GroupPosition.End
            else -> GroupPosition.Middle
        }

        AnnotatedItem(item, position)
    }

    println(annotatedItems.map { it.position.name })

    return Metrics(
        annotatedItems,
        visibleHeaders = Headers(visibleHeaders, visibleHeaderPositions)
    )
}

@Composable
fun <T, H> StickyHeaderRowAlt(
    items: List<T>,
    headerForItem: (T?) -> H?,
    headerContent: @Composable (H?) -> Unit,
    groupBackground: @Composable (H?) -> Unit,
    itemContent: @Composable LazyItemScope.(T, Modifier) -> Unit,
    appendEndOfContentSpacing: Boolean = true,
    groupPadding: PaddingValues = Padding.Zero,
    modifier: Modifier = Modifier,
) {
    if (items.isEmpty()) return

    val state = rememberLazyListState()
    val visibleItemsInfo = state.layoutInfo.visibleItemsInfo
    val coroutineScope = rememberCoroutineScope()

    val metrics = metricsOf(items, visibleItemsInfo, headerForItem)

    val scrollController = rememberScrollableController { delta ->
        coroutineScope.launch { state.scroll { scrollBy(delta) } }
        delta
    }

    val startModifier = Modifier.padding(start = groupPadding.start)
    val endModifier = Modifier.padding(end = groupPadding.end)
    val onlyModifier = Modifier.padding(start = groupPadding.start, end = groupPadding.end)

    StickyLayout(modifier) {
        StickyBackground(
            metrics.visibleHeaders,
            visibleItemsInfo,
            Modifier,
            groupBackground,
        )

        StickyHeader(
            metrics.visibleHeaders,
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
            this.items(metrics.items) { annotated ->
                val positionModifier = when (annotated.position) {
                    GroupPosition.Start -> startModifier
                    GroupPosition.End -> endModifier
                    GroupPosition.Only -> onlyModifier
                    GroupPosition.Middle -> Modifier
                }

                itemContent(annotated.item, positionModifier)
            }

            if (appendEndOfContentSpacing) {
                item {
                    EndOfContent(Orientation.Horizontal)
                }
            }
        }
    }
}

@Composable
private fun StickyLayout(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Layout(
        modifier = modifier,
        content = {
            content()
        }
    ) { measurables, constraints ->
        check(measurables.size == 3) {
            "Expected 3 content items: StickyBackground, StickyHeader, LazyRow " +
                    "(found ${measurables.size}: ${measurables[0]})"
        }

        val headerPlaceable = measurables[1].measure(constraints)
        val listPlaceable = measurables[2].measure(constraints)

        val height = headerPlaceable.height + listPlaceable.height
        val width = listPlaceable.width

        val backgroundPlaceable = measurables[0].measure(Constraints(
            minWidth = width,
            maxWidth = width,
            minHeight = height,
            maxHeight = height
        ))

        layout(width, height) {
            backgroundPlaceable.placeRelative(0, 0)
            headerPlaceable.placeRelative(0, 0)
            listPlaceable.placeRelative(0, headerPlaceable.height)
        }
    }
}

@Composable
private fun <H> StickyHeader(
    headers: Headers<H>,
    itemsInfo: List<LazyListItemInfo>,
    modifier: Modifier = Modifier,
    content: @Composable (H?) -> Unit,
) {
    val headerContent = constructStickyBlock(headers, content)

    StickyBlock(
        headers,
        itemsInfo,
        modifier,
        headerContent
    )
}

@Composable
private fun <H> StickyBackground(
    headerMap: Headers<H>,
    itemsInfo: List<LazyListItemInfo>,
    modifier: Modifier,
    content: @Composable (H?) -> Unit,
) {
    val backgrounds = constructStickyBlock(headerMap, content)

    StickyBlock(
        headerMap,
        itemsInfo,
        modifier,
        backgrounds
    )
}

@Composable
private fun EmptyLayout() = Layout(content = {}) { _, _ -> layout(0, 0) {} }

@Composable
private fun <H> constructStickyBlock(
    headers: Headers<H>,
    content: @Composable (H?) -> Unit,
): @Composable () -> Unit =
    {
        headers.positions.forEach { index ->
            content(headers.visible[index])
        }
    }

/**
 * Build list of positions where the header identifier changes.
 */
private fun <H> getHeaderPositions(headers: List<H?>): List<Int> {
    val headerPositions = mutableListOf<Int>()
    var previousHeader: H? = null
    headers.fastForEachIndexed { index, h ->
        if (h != previousHeader) {
            headerPositions += index
            previousHeader = h
        }
    }
    return headerPositions
}

@Composable
private fun <H> StickyBlock(
    headers: Headers<H>,
    itemsInfo: List<LazyListItemInfo>,
    modifier: Modifier,
    content: @Composable () -> Unit,
) {
    if (itemsInfo.isEmpty()) {
        EmptyLayout()
        return
    }

    Layout(
        content = content,
        modifier = modifier,
    ) { measurables, constraints ->
        val placeables = measurables.map { it.measure(constraints) }

        val width: Int = constraints.maxWidth
        val height: Int = placeables.maxOf(Placeable::height)

        layout(width, height) {
            placeables.forEachIndexed { index, blockPlaceable ->
                val headerIndex = headers.positions[index]
                val nextHeaderIndex = headers.positions.getOrNull(index + 1) ?: -1

                val info = itemsInfo[headerIndex]
                val nextInfo = itemsInfo.getOrNull(nextHeaderIndex)

                val preferredX = info.offset.coerceAtLeast(0)
                val x = when {
                    nextInfo == null -> preferredX

                    preferredX + blockPlaceable.width > nextInfo.offset -> {
                        // Slide this header out of the way of the next one when they start to overlap
                        nextInfo.offset - blockPlaceable.width
                    }
                    else -> preferredX
                }

                blockPlaceable.placeRelative(x, 0)
            }
        }
    }
}
