package org.beatonma.commons.compose.components.stickyrow

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import org.beatonma.commons.theme.compose.HorizontalPadding

@Composable
fun rememberGroupStyle(
    padding: HorizontalPadding = HorizontalPadding(),
    spaceBetween: HorizontalPadding = HorizontalPadding()
): GroupStyle = remember { GroupStyle(padding, spaceBetween) }

class GroupStyle internal constructor(
    val padding: HorizontalPadding = HorizontalPadding(),
    val spaceBetween: HorizontalPadding = HorizontalPadding()
) {
    val totalStartPadding get() = padding.start + spaceBetween.start
    val totalEndPadding get() = padding.end + spaceBetween.end
}
