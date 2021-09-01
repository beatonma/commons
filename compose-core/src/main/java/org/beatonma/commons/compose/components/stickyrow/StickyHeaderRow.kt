package org.beatonma.commons.compose.components.stickyrow

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import org.beatonma.commons.compose.padding.EndOfContent

@Composable
fun <T, H> StickyHeaderRow(
    items: List<T>,
    headerForItem: (T?) -> H?,
    headerContent: @Composable (H?) -> Unit,
    groupBackground: @Composable (H?) -> Unit,
    itemContent: @Composable LazyItemScope.(item: T) -> Unit,
    modifier: Modifier = Modifier,
    appendEndOfContentSpacing: Boolean = true,
    groupStyle: GroupStyle = rememberGroupStyle(),
    lazyListState: LazyListState = rememberLazyListState(),
) {
    if (items.isEmpty()) return
    val visibleItems = rememberVisibleState()

    val headers = getHeaders(items, headerForItem)
    val headerPositions = getHeaderPositions(headers)
    val annotatedItems = annotateItems(items, headerPositions)
    val visibleHeaders = updateHeaders(visibleItems.visibleItems.value, headers)

    val coroutineScope = rememberCoroutineScope()

    val scrollState = rememberScrollableState { delta ->
        coroutineScope.launch { lazyListState.scrollBy(delta) }
        delta
    }

    StickyHeaderRow(
        annotatedItems,
        visibleHeaders,
        visibleItems,
        scrollState,
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
    items: List<AnnotatedItem<T>>,
    visibleHeaders: Headers<H>,
    visibleItems: VisibleItems,
    scrollState: ScrollableState,
    lazyListState: LazyListState,
    headerContent: @Composable (H?) -> Unit,
    groupBackground: @Composable (H?) -> Unit,
    itemContent: @Composable LazyItemScope.(item: T) -> Unit,
    appendEndOfContentSpacing: Boolean,
    groupStyle: GroupStyle,
    modifier: Modifier,
) {
    StickyLayout(
        modifier
    ) {
        StickyBackgrounds(
            visibleHeaders,
            items,
            visibleItems.visibleItems.value,
            groupStyle,
            Modifier,
        ) {
            visibleHeaders.positions.forEach { index ->
                groupBackground(visibleHeaders.headers[index])
            }
        }

        StickyHeaders(
            visibleHeaders,
            items,
            visibleItems.visibleItems.value,
            groupStyle,
            Modifier.scrollable(
                state = scrollState,
                orientation = Orientation.Horizontal,
                reverseDirection = true,
            ),
        ) {
            visibleHeaders.positions.forEach { index ->
                headerContent(visibleHeaders.headers[index])
            }
        }

        LazyRow(state = lazyListState) {
            items(items) { annotated ->
                visibleItems.update(lazyListState)
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
