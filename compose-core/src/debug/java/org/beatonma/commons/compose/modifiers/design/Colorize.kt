package org.beatonma.commons.compose.modifiers.design

import androidx.compose.foundation.background
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ExperimentalGraphicsApi
import org.beatonma.commons.compose.util.toPrettyHexString
import kotlin.random.Random

/**
 * Set a random background color - useful for figuring out animations.
 */
fun Modifier.colorize(
    tag: String? = null,
    alpha: Float = 0.6F
): Modifier = composed {
    val color = remember { anyColor(alpha) }
    if (tag != null) {
        println("$tag: ${color.toPrettyHexString()}")
    }
    this.then(background(color))
}


@OptIn(ExperimentalGraphicsApi::class)
private fun anyColor(alpha: Float): Color =
    Color.hsl(
        hue = Random.nextFloat() * 360F,
        saturation = (Random.nextFloat() * .9f) + .1f,
        lightness = (Random.nextFloat() * .8f) + .2f,
        alpha = alpha,
    )
