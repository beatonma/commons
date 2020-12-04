package org.beatonma.commons.compose.modifiers

import androidx.compose.foundation.background
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import org.beatonma.commons.compose.util.toPrettyHexString
import org.beatonma.commons.theme.compose.color.anyMaterialColor

/**
 * Set a random background color - useful for figuring out animations.
 */
fun Modifier.colorize(tag: String? = null, alpha: Float = 0.6F): Modifier = composed {
    val color = remember { anyMaterialColor.copy(alpha = alpha) }
    if (tag != null) {
        println("$tag: ${color.toPrettyHexString()}")
    }
    this.then(background(color))
}
