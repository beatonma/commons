package org.beatonma.commons.compose.components.stickyrow

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Constraints

@Composable
internal fun StickyLayout(
    modifier: Modifier,
    content: @Composable () -> Unit,
) {
    Layout(
        modifier = modifier,
        content = content,
    ) { measurables, constraints ->
        check(measurables.size == 3) {
            "Expected 3 content items: StickyBackground, StickyHeader, LazyRow " +
                    "(found ${measurables.size}: ${measurables[0]})"
        }

        // Determine size of foreground content (i.e. headers and list items)
        val headerPlaceable = measurables[1].measure(constraints)
        val listPlaceable = measurables[2].measure(constraints)

        val height = headerPlaceable.height + listPlaceable.height
        val width = listPlaceable.width

        // Background wraps foreground content
        val backgroundPlaceable = measurables[0].measure(
            Constraints(
                minWidth = width,
                maxWidth = width,
                minHeight = height,
                maxHeight = height
            )
        )

        layout(width, height) {
            backgroundPlaceable.placeRelative(0, 0)
            headerPlaceable.placeRelative(0, 0)
            listPlaceable.placeRelative(0, headerPlaceable.height)
        }
    }
}
