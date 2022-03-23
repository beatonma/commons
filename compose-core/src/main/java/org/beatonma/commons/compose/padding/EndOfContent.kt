package org.beatonma.commons.compose.padding

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import org.beatonma.commons.themed.padding

@Composable
fun EndOfContent(orientation: Orientation = Orientation.Vertical) =
    Spacer(
        Modifier.endOfContent(orientation)
    )

fun Modifier.endOfContent(orientation: Orientation = Orientation.Vertical) =
    this.then(
        composed {
            padding(
                when (orientation) {
                    Orientation.Vertical -> padding.EndOfContent
                    Orientation.Horizontal -> padding.EndOfContentHorizontal
                }
            )
        }
    )

fun LazyListScope.endOfContent(orientation: Orientation = Orientation.Vertical) {
    item {
        EndOfContent(orientation)
    }
}
