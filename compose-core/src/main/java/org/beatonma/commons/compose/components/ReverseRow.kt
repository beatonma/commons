package org.beatonma.commons.compose.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import org.beatonma.commons.core.extensions.fastForEach

/**
 * A row with last-item priority. Useful for layouts with a trailing icon or whatever.
 */
@Composable
fun ReverseRow(
    modifier: Modifier = Modifier,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    content: @Composable () -> Unit,
) {
    Layout(content = content, modifier = modifier) { measurables, constraints ->
        if (measurables.isEmpty()) {
            return@Layout layout(0, 0) {}
        }

        var consumedWidth = 0
        val placeables = measurables.reversed().map {
            val p = it.measure(constraints.copy(
                minWidth = 0,
                maxWidth = (constraints.maxWidth - consumedWidth).coerceAtLeast(0)
            ))
            consumedWidth += p.width
            p
        }


        val width = constraints.maxWidth
        val height = placeables.maxOf { it.height }

        layout(width, height) {
            var x = width
            placeables.fastForEach {
                x -= it.width
                it.placeRelative(x, verticalAlignment.align(it.height, height))
            }
        }
    }
}
