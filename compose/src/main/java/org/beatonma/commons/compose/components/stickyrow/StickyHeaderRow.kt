package org.beatonma.commons.compose.components.stickyrow

import androidx.compose.foundation.gestures.ScrollableController
import androidx.compose.foundation.gestures.rememberScrollableController
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.gesture.scrollorientationlocking.Orientation
import kotlinx.coroutines.launch
import org.beatonma.commons.theme.compose.EndOfContent

@Composable
fun <T, H> StickyHeaderRow(
    items: List<T>,
    headerForItem: (T?) -> H?,
    headerContent: @Composable (H?) -> Unit,
    groupBackground: @Composable (H?) -> Unit,
    itemContent: @Composable LazyItemScope.(item: T) -> Unit,
    appendEndOfContentSpacing: Boolean = true,
    groupStyle: GroupStyle = rememberGroupStyle(),
    lazyListState: LazyListState = rememberLazyListState(),
    modifier: Modifier = Modifier,
) {
    if (items.isEmpty()) return
    val visibleItems by rememberVisibleState(state = lazyListState)

    println("PUBLIC | StickyHeaderRowAlt")

    val coroutineScope = rememberCoroutineScope()

    val scrollController = rememberScrollableController { delta ->
        coroutineScope.launch { lazyListState.scroll { scrollBy(delta) } }
        delta
    }
    val metrics = metricsOf(items, visibleItems, headerForItem)

    StickyHeaderRow(
        metrics,
        visibleItems,
        scrollController,
        lazyListState,
        headerContent,
        groupBackground,
        itemContent,
        appendEndOfContentSpacing,
        groupStyle,
        modifier
    )
}

@Composable
private fun <T, H> StickyHeaderRow(
    metrics: Metrics<T, H>,
    visibleItems: List<LazyListItemInfo>,
    scrollController: ScrollableController,
    lazyListState: LazyListState,
    headerContent: @Composable (H?) -> Unit,
    groupBackground: @Composable (H?) -> Unit,
    itemContent: @Composable LazyItemScope.(item: T) -> Unit,
    appendEndOfContentSpacing: Boolean,
    groupStyle: GroupStyle,
    modifier: Modifier,
) {
    println("PRIVATE StickyHeaderRowAlt")
    StickyLayout(
        modifier
    ) {
        val visibleHeaders = metrics.visibleHeaders
        // TODO StickyBackground and StickyHeader should probably be combined to avoid over-traversal.

        // Background content
        StickyBackground(
            metrics,
            visibleItems,
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
            visibleItems,
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
            state = lazyListState,
        ) {
            this.items(metrics.items) { annotated ->
                if (annotated.position.isFirst) {
                    Spacer(Modifier.width(groupStyle.totalStartPadding))
                }

                itemContent(annotated.item)

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

