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
    override val key: Any
): LazyListItemInfo


/**
 * This feels right but it causes infinite recomposition.
 * state.layoutInfo.visibleItemsInfo is a new list for every layout pass,
 * even if its content does not change. This (is supposed to) decouple that from the output
 * list of [VisibleItem]s.
 *
 * Basically calling this from anywhere outside of [androidx.compose.foundation.lazy.LazyItemScope]
 * causes recomposition so unable to hoist?
 *
 *
 */
@Deprecated(
    "Use rememberVisibleState() - this causes infinite recomposition but" +
            "feels like the better approach... Revisit later."
)
@Composable
fun rememberVisibleState(state: LazyListState): State<List<VisibleItem>> {
    val visibleItems: MutableState<List<VisibleItem>> = remember { mutableStateOf(listOf()) }

    val newItems = state.layoutInfo.visibleItemsInfo.map { item: LazyListItemInfo ->
        VisibleItem(item.index, item.offset, item.size, item.key)
    }

    visibleItems.value = newItems

    return visibleItems
}


/**
 * This feels wrong but it works.
 */
@Composable
fun rememberVisibleState(): VisibleItems = remember { VisibleItems() }

class VisibleItems internal constructor(
    val visibleItems: MutableState<List<VisibleItem>> = mutableStateOf(listOf()),
) {
    /**
     * Call from LazyRow.items block. Again, feels wrong but it works.
     */
    @Composable
    fun update(state: LazyListState) {
        val newItems = state.layoutInfo.visibleItemsInfo.map { item: LazyListItemInfo ->
            VisibleItem(item.index, item.offset, item.size, item.key)
        }
        visibleItems.value = newItems
    }
}
