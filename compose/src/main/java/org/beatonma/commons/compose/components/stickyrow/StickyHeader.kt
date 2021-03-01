package org.beatonma.commons.compose.components.stickyrow

import androidx.compose.foundation.lazy.LazyListItemInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import org.beatonma.commons.compose.layout.EmptyLayout

@Composable
internal fun <T, H> StickyHeader(
    metrics: Metrics<T, H>,
    itemsInfo: List<LazyListItemInfo>,
    groupStyle: GroupStyle,
    modifier: Modifier,
    content: @Composable () -> Unit,
) {
    println("StickyHeader")
    if (itemsInfo.isEmpty()) {
        EmptyLayout()
        return
    }

    Layout(
        content = content,
        modifier = modifier,
    ) { measurables, constraints ->
        val headers = metrics.visibleHeaders
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

                val positionInGroup =
                    metrics.items.getOrNull(info.index)?.position ?: GroupPosition.Middle

                // Header 'wants' to sit at beginning of group, or the left/start of display area.
                val preferredX = when {
                    positionInGroup.isFirst -> info.offset + groupStartPadding
                    else -> info.offset
                }.coerceAtLeast(0)

                val x = if (nextInfo == null) {
                    preferredX
                }
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
