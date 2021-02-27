package org.beatonma.commons.compose.components

import androidx.compose.foundation.gestures.rememberScrollableController
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
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
import org.beatonma.commons.theme.compose.HorizontalPadding

class GroupStyle(
    val padding: HorizontalPadding = HorizontalPadding(),
    val spaceBetween: HorizontalPadding = HorizontalPadding()
) {
    val totalStartPadding get() = padding.start + spaceBetween.start
    val totalEndPadding get() = padding.end + spaceBetween.end
}

@Composable
fun <T, H> StickyHeaderRowAlt(
    items: List<T>,
    headerForItem: (T?) -> H?,
    headerContent: @Composable (H?) -> Unit,
    groupBackground: @Composable (H?) -> Unit,
    itemContent: @Composable LazyItemScope.(item: T, itemModifier: Modifier) -> Unit,
    appendEndOfContentSpacing: Boolean = true,
    groupStyle: GroupStyle = GroupStyle(),
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

    val metrics = metricsOf(items, visibleItemsInfo, headerForItem)
    val visibleHeaders = metrics.visibleHeaders

    StickyLayout(
        modifier
    ) {
        // TODO StickyBackground and StickyHeader should probably be combined to avoid over-traversal.
        // Background content
        StickyBackground(
            metrics,
            visibleItemsInfo,
            groupStyle,
            Modifier,
        ) {
            visibleHeaders.positions.forEach { index ->
                groupBackground(visibleHeaders.headers[index])
            }
        }

        // Header content
        StickyHeader(
            metrics,
            visibleItemsInfo,
            groupStyle,
            Modifier.scrollable(
                orientation = Orientation.Horizontal,
                controller = scrollController,
                reverseDirection = true,
            ),
        ) {
            visibleHeaders.positions.forEach { index ->
                headerContent(visibleHeaders.headers[index])
            }
        }

        LazyRow(
            state = state,
        ) {
            this.items(metrics.items) { annotated ->
                if (annotated.position.isFirst) {
                    Spacer(Modifier.width(groupStyle.totalStartPadding))
                }

                itemContent(annotated.item, Modifier)

                if (annotated.position.isLast) {
                    Spacer(Modifier.width(groupStyle.totalEndPadding))
                }
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
        content = content,
    ) { measurables, constraints ->
        check(measurables.size == 3) {
            "Expected 3 content items: StickyBackground, StickyHeader, LazyRow " +
                    "(found ${measurables.size}: ${measurables[0]})"
        }

        // Determine size of foreground content (i.e. headers and list items)
        val headerPlaceable = measurables[1].measure(constraints)
        val listPlaceable = measurables[2].measure(constraints)

        val height = headerPlaceable.height + listPlaceable.height
        val width = listPlaceable.width

        // Background wraps foreground content
        val backgroundPlaceable = measurables[0].measure(
            Constraints(
                minWidth = width,
                maxWidth = width,
                minHeight = height,
                maxHeight = height
            )
        )

        layout(width, height) {
            backgroundPlaceable.placeRelative(0, 0)
            headerPlaceable.placeRelative(0, 0)
            listPlaceable.placeRelative(0, headerPlaceable.height)
        }
    }
}

@Composable
private fun <T, H> StickyBackground(
    metrics: Metrics<T, H>,
    itemsInfo: List<LazyListItemInfo>,
    groupStyle: GroupStyle,
    modifier: Modifier,
    content: @Composable () -> Unit,
) {
    if (itemsInfo.isEmpty()) {
        EmptyLayout
        return
    }
    val headers = metrics.visibleHeaders

    Layout(
        content = content,
        modifier = modifier,
    ) { measurables, constraints ->
        // Remember these are the backgrounds, not the items!
        val xPositions = mutableListOf<Int>()
        val placeables = measurables.mapIndexed { index, measurable ->
            val headerIndex = headers.positions[index]
            val nextHeaderIndex = headers.positions.getOrNull(index + 1) ?: -1

            val info = itemsInfo[headerIndex]
            val nextInfo = itemsInfo.getOrNull(nextHeaderIndex)

            val positionInGroup = metrics.items.getOrNull(info.index)?.position ?: GroupPosition.Middle
            val x = when {
                positionInGroup.isFirst -> info.offset + groupStyle.spaceBetween.start.toIntPx()
                else -> info.offset
            }
            xPositions.add(x)

            val nextX = nextInfo?.offset

            val width =
                when (nextX) {
                    null -> {
                        // Use end coordinate of the last visible item.
                        val lastInfo = itemsInfo.last()
                        val lastItemPosition = metrics.items.getOrNull(lastInfo.index)?.position ?: GroupPosition.End

                        val lastEndX = if (lastItemPosition.isLast) {
                            lastInfo.offset + lastInfo.size - groupStyle.spaceBetween.end.toIntPx()
                        }
                        else {
                            lastInfo.offset + lastInfo.size
                        }
                        lastEndX - x
                    }
                    else -> {
                        // Use start coordinate of next header and offset with specified padding.
                        nextX - x - groupStyle.spaceBetween.end.toIntPx()
                    }
                }

            measurable.measure(constraints.copy(minWidth = width, maxWidth = width))
        }

        val width: Int = constraints.maxWidth
        val height: Int = placeables.maxOfOrNull { it.height } ?: 0

        layout(width, height) {
            placeables.forEachIndexed { index, blockPlaceable ->
                val x = xPositions[index]
                blockPlaceable.placeRelative(x, 0)
            }
        }
    }
}

@Composable
private fun <T, H> StickyHeader(
    metrics: Metrics<T, H>,
    itemsInfo: List<LazyListItemInfo>,
    groupStyle: GroupStyle,
    modifier: Modifier,
    content: @Composable () -> Unit,
) {
    if (itemsInfo.isEmpty()) {
        EmptyLayout
        return
    }
    val headers = metrics.visibleHeaders

    Layout(
        content = content,
        modifier = modifier,
    ) { measurables, constraints ->
        val groupStartPadding = (groupStyle.spaceBetween.start + groupStyle.padding.start).toIntPx()
        val groupEndPadding = (groupStyle.spaceBetween.end + groupStyle.padding.end).toIntPx()

        val placeables = measurables.map { it.measure(constraints) }

        val width: Int = constraints.maxWidth
        val height: Int = placeables.maxOf(Placeable::height)

        layout(width, height) {
            placeables.forEachIndexed { index, blockPlaceable ->
                val headerIndex = headers.positions[index]
                val nextHeaderIndex = headers.positions.getOrNull(index + 1) ?: -1

                val info = itemsInfo[headerIndex]
                val nextInfo = itemsInfo.getOrNull(nextHeaderIndex)

                val positionInGroup = metrics.items.getOrNull(info.index)?.position ?: GroupPosition.Middle

                // Header 'wants' to sit at beginning of group, or the left/start of display area.
                val preferredX = when {
                    positionInGroup.isFirst -> info.offset + groupStartPadding
                    else -> info.offset
                }.coerceAtLeast(0)

                val x = if (nextInfo == null) { preferredX }
                else {
                    val endX = preferredX + blockPlaceable.width + groupEndPadding
                    val overlapAmount = (endX - nextInfo.offset)

                    when {
                        overlapAmount > 0 -> preferredX - overlapAmount
                        else -> preferredX
                    }
                }

                blockPlaceable.placeRelative(x, 0)
            }
        }
    }
}


private val EmptyLayout
    @Composable get() = Layout(content = {}) { _, _ -> layout(0, 0) {} }


private data class AnnotatedItem<T>(
    val item: T,
    val position: GroupPosition
)
private class Headers<H>(
    val headers: List<H?>,
    val positions: List<Int>
)

private enum class GroupPosition {
    Start,
    Middle,
    End,
    Only, // Only item in the group -> single item is both Start and End
    ;
    val isFirst: Boolean get() = this == Start || this == Only
    val isLast: Boolean get() = this == End || this == Only

}

private data class Metrics<T, H>(
    val items: List<AnnotatedItem<T>>,
    val visibleHeaders: Headers<H>,
)

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

    return Metrics(
        annotatedItems,
        visibleHeaders = Headers(visibleHeaders, visibleHeaderPositions),
    )
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
