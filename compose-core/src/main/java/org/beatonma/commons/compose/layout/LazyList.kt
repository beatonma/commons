package org.beatonma.commons.compose.layout

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import org.beatonma.commons.compose.systemui.statusBarsPadding
import org.beatonma.commons.compose.util.statusBarHeight
import org.beatonma.commons.core.extensions.reversed
import org.beatonma.commons.core.extensions.withNotNull

/**
 * Helper for a LazyRow/LazyColumn item that relies on nullable data and should only be displayed
 * if that data is not null.
 */
fun <T> LazyListScope.optionalItem(
    value: T?,
    block: @Composable (T) -> Unit,
) {
    withNotNull(value) {
        item {
            block(it)
        }
    }
}

/**
 * Create a stickyHeader with status bar padding applied as it reaches the top of the screen.
 */
@OptIn(ExperimentalFoundationApi::class)
fun LazyListScope.stickyHeaderWithInsets(
    state: LazyListState,
    key: Any,
    content: @Composable (itemState: ItemState, modifier: Modifier) -> Unit
) {
    stickyHeader(key) {
        val stretchHeight = statusBarHeight * 2

        ItemStateLayout(state, key) { itemState ->
            content(
                itemState,
                Modifier
                    .statusBarsPadding(
                        (itemState.offset.toFloat() / stretchHeight.toFloat())
                            .coerceIn(0f, 1f)
                            .reversed()
                    )
            )
        }
    }
}

/**
 * Create a stickyHeader that is aware of its position and visibility via [ItemState].
 */
@OptIn(ExperimentalFoundationApi::class)
fun LazyListScope.stickyHeaderWithState(
    state: LazyListState,
    key: Any,
    content: @Composable (itemState: ItemState) -> Unit,
) {
    stickyHeader(key) {
        ItemStateLayout(state, key, content = content)
    }
}

/**
 * Create an item that is aware of its position and visibility via [ItemState]
 */
fun LazyListScope.itemWithState(
    state: LazyListState,
    key: Any,
    content: @Composable (itemState: ItemState) -> Unit,
) {
    item(key) {
        ItemStateLayout(state, key, content = content)
    }
}

@Composable
private fun ItemStateLayout(
    state: LazyListState,
    key: Any,
    content: @Composable (itemState: ItemState) -> Unit
) {
    LaunchedEffect(Unit) {
        check(state.layoutInfo.totalItemsCount > 0) {
            "LazyListState does not appear to be attached to a LazyList!"
        }
    }

    var itemState by remember {
        mutableStateOf(
            ItemState(
                isFirst = false,
                offset = 0,
                visibility = 1f,
                overlapsPrevious = false,
                overlapsNext = false
            )
        )
    }

    LaunchedEffect(state.firstVisibleItemScrollOffset) {
        state.layoutInfo.visibleItemsInfo.find { it.key == key }
            ?.let { item ->
                val itemTop = item.offset
                val itemBottom = itemTop + item.size

                val previousItemBottom =
                    state.layoutInfo.visibleItemsInfo.find { it.index == item.index - 1 }
                        ?.let { previous ->
                            previous.offset + previous.size
                        }

                val nextItemTop =
                    state.layoutInfo.visibleItemsInfo.find { it.index > item.index }?.offset

                val overlapsPrevious = previousItemBottom != null && previousItemBottom > itemTop
                val overlapsNext = nextItemTop != null && nextItemTop < itemBottom

                itemState = if (overlapsPrevious) {
                    check(previousItemBottom != null) { "Null check already performed in overlapsAbove assignment" }

                    // Previous item is a stickyHeader so we need to adjust offset.
                    val offset = itemTop - previousItemBottom
                    ItemState(
                        isFirst = item.index == state.firstVisibleItemIndex,
                        offset = itemTop - previousItemBottom,
                        visibility = ((item.size + offset).toFloat() / item.size.toFloat())
                            .coerceIn(0f, 1f),
                        overlapsPrevious = overlapsPrevious,
                        overlapsNext = overlapsNext,
                    )
                } else {
                    ItemState(
                        isFirst = item.index == state.firstVisibleItemIndex,
                        offset = itemTop,
                        visibility = ((item.size - itemTop).toFloat() / item.size.toFloat())
                            .coerceIn(0f, 1f)
                            .reversed(),
                        overlapsPrevious = overlapsPrevious,
                        overlapsNext = overlapsNext,
                    )
                }
            }
    }

    content(itemState)
}

@Immutable
class ItemState internal constructor(
    val isFirst: Boolean,
    val offset: Int,
    val visibility: Float,
    val overlapsPrevious: Boolean,
    val overlapsNext: Boolean,
) {
    val isAtTop: Boolean get() = offset == 0

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ItemState

        if (isFirst != other.isFirst) return false
        if (offset != other.offset) return false
        if (visibility != other.visibility) return false
        if (overlapsNext != other.overlapsNext) return false
        if (overlapsPrevious != other.overlapsPrevious) return false

        return true
    }

    override fun hashCode(): Int {
        var result = isFirst.hashCode()
        result = 31 * result + offset
        result = 31 * result + visibility.hashCode()
        result = 31 * result + overlapsPrevious.hashCode()
        result = 31 * result + overlapsNext.hashCode()
        return result
    }
}
