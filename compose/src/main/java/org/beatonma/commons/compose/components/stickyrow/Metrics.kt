package org.beatonma.commons.compose.components.stickyrow

import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import org.beatonma.commons.compose.util.updateIfNotEqual
import org.beatonma.commons.core.extensions.fastForEachIndexed

@Composable
internal fun <T, H> metricsOf(
    items: List<T>,
    visibleItemInfo: List<LazyListItemInfo>,
    headerForItem: (T?) -> H?,
): Metrics<T, H> {
    val metrics = remember { Metrics<T, H>() }
    println("metricsOf")
    metrics.update(items, visibleItemInfo, headerForItem)

    return metrics
}

internal class Metrics<T, H> {
    private val _items: MutableState<List<AnnotatedItem<T>>> = mutableStateOf(listOf())
    private val _visibleHeaders: MutableState<Headers<H>> = mutableStateOf(emptyHeadersOf())

    val items: List<AnnotatedItem<T>> get() = _items.value
    val visibleHeaders: Headers<H> get() = _visibleHeaders.value

    @Composable
    fun update(
        items: List<T>,
        visibleItemInfo: List<LazyListItemInfo>,
        headerForItem: (T?) -> H?,
    ) {
        val headers: List<H?> = items.map(headerForItem)

        annotateItems(items, headers)
        updateHeaders(visibleItemInfo, headers)
    }

    @Composable
    private fun annotateItems(
        items: List<T>,
        headers: List<H?>
    ) {
        val lastIndex = items.size - 1

        val annotated = items.mapIndexed { index, item ->
            val nextIndex = index + 1

            val headerPositions = getHeaderPositions(headers)

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

        _items.updateIfNotEqual(annotated)
    }

    @Composable
    private fun updateHeaders(
        visibleItemInfo: List<LazyListItemInfo>,
        headers: List<H?>
    ) {
        val visibleHeaders = visibleItemInfo.map { headers.getOrNull(it.index) }
        val visibleHeaderPositions = getHeaderPositions(visibleHeaders)

        _visibleHeaders.updateIfNotEqual(
            Headers(visibleHeaders, visibleHeaderPositions)
        )
    }
}

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

/**
 * Build list of positions where the header identifier changes.
 */
@Composable
private fun <H> getHeaderPositions(headers: List<H?>): List<Int> {
//    println("getHeaderPositions")
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
