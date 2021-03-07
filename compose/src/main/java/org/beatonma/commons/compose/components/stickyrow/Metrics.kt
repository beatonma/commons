package org.beatonma.commons.compose.components.stickyrow

import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import org.beatonma.commons.core.extensions.fastForEachIndexed

@Composable
internal fun <T, H> getHeaders(
    items: List<T>,
    headerForItem: (T?) -> H?,
): List<H?> {
    val rememberedHeaders: MutableState<List<H?>> = remember { mutableStateOf(listOf()) }
    rememberedHeaders.value = items.map(headerForItem)
    return rememberedHeaders.value
}

@Composable
internal fun <T> annotateItems(
    items: List<T>,
    headerPositions: List<Int>
): List<AnnotatedItem<T>> {
    val annotated: MutableState<AnnotatedItems<T>> = remember { mutableStateOf(listOf()) }

    val lastIndex = items.size - 1
    annotated.value =
        items.mapIndexed { index, item ->
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

    return annotated.value
}

@Composable
internal fun <H> updateHeaders(
    visibleItemsInfo: List<LazyListItemInfo>,
    headers: List<H?>,
): Headers<H> {
    val visibleHeadersState = remember { mutableStateOf(emptyHeadersOf<H>()) }
    val visibleHeaders = visibleItemsInfo.map { headers.getOrNull(it.index) }
    val visibleHeaderPositions = getHeaderPositions(visibleHeaders)

    visibleHeadersState.value = Headers(visibleHeaders, visibleHeaderPositions)

    return visibleHeadersState.value
}

/**
 * Build list of positions where the header identifier changes.
 */
@Composable
internal fun <H> getHeaderPositions(headers: List<H?>): List<Int> {
    val positions: MutableState<List<Int>> = remember { mutableStateOf(listOf()) }

    val headerPositions = mutableListOf<Int>()
    var previousHeader: H? = null

    headers.fastForEachIndexed { index, h ->
        if (h != previousHeader) {
            headerPositions += index
            previousHeader = h
        }
    }
    positions.value = headerPositions
    return positions.value
}

internal typealias AnnotatedItems<T> = List<AnnotatedItem<T>>
internal data class AnnotatedItem<T>(
    val item: T,
    val position: GroupPosition
)
internal data class Headers<H>(
    val headers: List<H?>,
    val positions: List<Int>
)
internal fun <H> emptyHeadersOf() = Headers<H>(listOf(), listOf())

internal enum class GroupPosition {
    Start,
    Middle,
    End,
    Only, // Only item in the group -> single item is both Start and End
    ;
    val isFirst: Boolean get() = this == Start || this == Only
    val isLast: Boolean get() = this == End || this == Only

}

