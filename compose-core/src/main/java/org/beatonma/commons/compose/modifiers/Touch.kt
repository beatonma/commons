package org.beatonma.commons.compose.modifiers

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput

/**
 * The receiver will consume all touch events without triggering any other events.
 * Useful for making a surface touch-opaque without making it clickable or otherwise interfering
 * with accessibility.
 *
 * [androidx.compose.foundation.clickable] modifier forces mergeDescendants semantics which is not
 * always desirable.
 */
fun Modifier.consumePointerInput() =
    pointerInput(this) {
        detectTapGestures(
            onTap = {},
            onPress = {},
            onDoubleTap = {},
            onLongPress = {},
        )
    }
