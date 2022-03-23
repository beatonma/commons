package org.beatonma.commons.compose.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import org.beatonma.commons.core.extensions.fastForEach
import org.beatonma.commons.core.extensions.fastForEachIndexed


/**
 * A row the wraps to new lines when full.
 */
@Composable
fun FlowRow(
    modifier: Modifier = Modifier,
    alignment: Alignment.Horizontal = Alignment.Start,
    layoutDirection: LayoutDirection = LocalLayoutDirection.current,
    content: @Composable () -> Unit,
) {
    Layout(
        content = content,
        modifier = modifier,
    ) { measurables, constraints ->
        val maxWidth = constraints.maxWidth

        val rows = mutableListOf<List<Placeable>>()
        val currentRow = mutableListOf<Placeable>()
        val rowSizes = mutableListOf<IntSize>()
        val rowStartY = mutableListOf(0)

        val itemConstraints = constraints.copy(minWidth = 0)
        val placeables = measurables.map { it.measure(itemConstraints) }

        var rowHeight = 0
        var rowWidth = 0

        fun storeRowAndReset() {
            // Remember this row and start a new row
            rows += currentRow.toList()
            rowSizes += IntSize(rowWidth, rowHeight)
            rowStartY += rowHeight
            rowHeight = 0
            rowWidth = 0
            currentRow.clear()
        }

        placeables.fastForEach {
            if (rowWidth + it.width > maxWidth) {
                storeRowAndReset()
            }

            currentRow += it
            rowHeight = maxOf(rowHeight, it.height)
            rowWidth += it.width
        }

        if (currentRow.isNotEmpty()) {
            storeRowAndReset()
        }

        val totalHeight = rowSizes.sumOf(IntSize::height)

        check(rows.size < rowStartY.size)
        check(currentRow.isEmpty())

        layout(maxWidth, totalHeight) {
            rows.fastForEachIndexed { rowIndex, rowItems ->
                var left = alignment.align(rowSizes[rowIndex].width, maxWidth, layoutDirection)
                val top = rowStartY[rowIndex]

                rowItems.fastForEach { item ->
                    item.placeRelative(left, top)
                    left += item.width
                }
            }
        }
    }
}
