package org.beatonma.commons.compose.components.stickyrow

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import org.beatonma.commons.compose.layout.EmptyLayout

@Composable
internal fun <T, H> StickyBackgrounds(
    headers: Headers<H>,
    annotatedItems: List<AnnotatedItem<T>>,
    itemsInfo: List<VisibleItem>,
    groupStyle: GroupStyle,
    modifier: Modifier,
    content: @Composable () -> Unit,
) {
    if (itemsInfo.isEmpty()) {
        EmptyLayout()
        return
    }

    Layout(
        content = content,
        modifier = modifier,
    ) { measurables, constraints ->
        val xPositions = mutableListOf<Int>()
        val placeables = measurables.mapIndexed { index, measurable ->
            val headerIndex = headers.positions[index]
            val nextHeaderIndex = headers.positions.getOrNull(index + 1) ?: -1

            val info = itemsInfo[headerIndex]
            val nextInfo = itemsInfo.getOrNull(nextHeaderIndex)

            val positionInGroup = annotatedItems.getOrNull(info.index)?.position ?: GroupPosition.Middle
            val x = when {
                positionInGroup.isFirst -> info.offset + groupStyle.spaceBetween.start.roundToPx()
                else -> info.offset
            }
            xPositions.add(x)

            val nextX = nextInfo?.offset

            val width =
                when (nextX) {
                    null -> {
                        // Use end coordinate of the last visible item.
                        val lastInfo = itemsInfo.last()
                        val lastItemPosition = annotatedItems.getOrNull(lastInfo.index)?.position ?: GroupPosition.End

                        val lastEndX = if (lastItemPosition.isLast) {
                            lastInfo.offset + lastInfo.size - groupStyle.spaceBetween.end.roundToPx()
                        }
                        else {
                            lastInfo.offset + lastInfo.size
                        }
                        lastEndX - x
                    }
                    else -> {
                        // Use start coordinate of next header and offset with specified padding.
                        nextX - x - groupStyle.spaceBetween.end.roundToPx()
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
