package org.beatonma.commons.theme.compose.padding

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun EndOfContent(orientation: Orientation = Orientation.Vertical) =
    Spacer(
        Modifier.endOfContent(orientation)
    )

fun Modifier.endOfContent(orientation: Orientation = Orientation.Vertical) = this.then(
    padding(
        when (orientation) {
            Orientation.Vertical -> Padding.EndOfContent
            Orientation.Horizontal -> Padding.EndOfContentHorizontal
        }
    )
)
