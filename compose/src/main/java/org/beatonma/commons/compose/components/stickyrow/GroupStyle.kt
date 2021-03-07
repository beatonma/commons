package org.beatonma.commons.compose.components.stickyrow

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.unit.Dp
import org.beatonma.commons.theme.compose.HorizontalPadding

@Composable
fun rememberGroupStyle(
    padding: HorizontalPadding = HorizontalPadding(),
    spaceBetween: HorizontalPadding = HorizontalPadding()
): GroupStyle = remember { GroupStyle(padding, spaceBetween) }

@Composable
fun rememberGroupStyle(
    padding: Dp,
    spaceBetween: Dp
): GroupStyle = rememberGroupStyle(HorizontalPadding(padding), HorizontalPadding(spaceBetween))

class GroupStyle internal constructor(
    /**
     * Padding before first item in a group and after the last item in a group.
     * Effectively pushes item and header away from the edge of area rendered by [StickyBackgrounds].
     */
    val padding: HorizontalPadding = HorizontalPadding(),

    /**
     * Empty space between areas rendered by [StickyBackgrounds].
     */
    val spaceBetween: HorizontalPadding = HorizontalPadding()
) {
    val totalStartPadding get() = padding.start + spaceBetween.start
    val totalEndPadding get() = padding.end + spaceBetween.end
}
