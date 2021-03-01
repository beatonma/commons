package org.beatonma.commons.compose.components.stickyrow

import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

data class VisibleItem(
    override val index: Int,
    override val offset: Int,
    override val size: Int,
): LazyListItemInfo

@Composable
internal fun rememberVisibleState(state: LazyListState): State<List<VisibleItem>> {
    val visibleItems: MutableState<List<VisibleItem>> = remember { mutableStateOf(listOf()) }

    val newItems = state.layoutInfo.visibleItemsInfo.map { item: LazyListItemInfo ->
        VisibleItem(item.index, item.offset, item.size)
    }

    if (newItems != visibleItems.value) {
        visibleItems.value = newItems
    }

    return visibleItems
}
