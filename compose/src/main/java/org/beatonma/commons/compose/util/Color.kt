package org.beatonma.commons.compose.util

import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import org.beatonma.commons.compose.ambient.colors
import org.beatonma.commons.core.extensions.lerpTo
import org.beatonma.commons.core.extensions.mapToByte
import org.beatonma.commons.core.extensions.normalize
import org.beatonma.commons.theme.compose.theme.textPrimaryDark
import org.beatonma.commons.theme.compose.theme.textPrimaryLight

inline class HslColor(private val hsl: FloatArray = FloatArray(4)) {
    constructor(
        hue: Float,
        saturation: Float = 1F,
        lightness: Float = 0.5F,
        alpha: Float = 1F,
    ) : this(
        floatArrayOf(hue, saturation, lightness, alpha)
    )

    /**
     * 0..360
     */
    var hue
        set(value) {
            hsl[0] = value
        }
        get() = hsl[0]

    /**
     * 0..1
     */
    var saturation
        set(value) {
            hsl[1] = value
        }
        get() = hsl[1]

    /**
     * 0..1
     */
    var lightness
        set(value) {
            hsl[2] = value
        }
        get() = hsl[2]

    /**
     * 0..1
     */
    var alpha
        set(value) {
            hsl[3] = value
        }
        get() = hsl[3]

    operator fun component1() = hue
    operator fun component2() = saturation
    operator fun component3() = lightness
    operator fun component4() = alpha

    fun set(
        hue: Float = this.hue,
        saturation: Float = this.saturation,
        lightness: Float = this.lightness,
        alpha: Float = this.alpha,
    ) {
        this.hue = hue
        this.saturation = saturation
        this.lightness = lightness
        this.alpha = alpha
    }
}

/**
 * Get an HSL/HSV representation.
 */
fun Color.toHslColor(out: HslColor = HslColor()): HslColor {
    val r = red
    val g = green
    val b = blue

    val min = minOf(r, g, b)
    val max = maxOf(r, g, b)
    val diff = max - min

    val lightness = (min + max) / 2F

    val saturation = when {
        min == max -> 0F
        lightness <= 0.5F -> diff / (max + min)
        else -> diff / (2F - max - min)
    }

    val hue = (
            when (max) {
                min -> 0F // min == max => greyscale
                r -> (g - b) / diff
                g -> 2F + (b - r) / diff
                else -> 4F + (r - g) / diff
            } * 60F)
        .positiveMod(360F)

    out.set(hue, saturation, lightness, alpha)

    return out
}

/**
 * Get a standard RGB representation.
 */
fun HslColor.toColor(): Color {
    val (h, s, l) = this

    if (s == 0F) {
        // Greyscale
        val rgb = l.mapToByte()
        return Color(rgb, rgb, rgb, alpha.mapToByte())
    }

    val t1 = when {
        l < 0.5F -> l * (1F + s)
        else -> l + s - l * s
    }

    val t2 = ((2F * l) - t1)

    val third = 1F / 3F
    val twoThirds = 2F / 3F

    fun resolve(value: Float) = when {
        value < (1F / 6F) -> {
            t2 + (t1 - t2) * 6F * value
        }

        value < (1F / 2F) -> {
            t1
        }

        value < (2F / 3F) -> {
            t2 + (t1 - t2) * (twoThirds - value) * 6F
        }

        else -> t2
    }.mapToByte()

    val normalisedHue = h.normalize(360F)

    val r = (normalisedHue + third).positiveMod(1F)
    val g = normalisedHue
    val b = (normalisedHue - third).positiveMod(1F)

    val red = resolve(r)
    val green = resolve(g)
    val blue = resolve(b)

    return Color(red, green, blue, alphaByte)
}

@Composable
fun Color.contentColor(): Color {
    // Alternative to contentColorFor(Color) when the receiving color is not defined in the theme.
    return if (luminance() > 0.5F) colors.textPrimaryDark
    else colors.textPrimaryLight
}

fun Color.brighten(factor: Float = 0.1F, hsl: HslColor = HslColor()): Color {
    return toHslColor(hsl).apply {
        lightness = (lightness + factor).coerceIn(0F, 1F)
    }.toColor()
}

fun Color.withBrightness(brightness: Float = 0.1F, hsl: HslColor = HslColor()): Color {
    return toHslColor(hsl).apply {
        lightness = brightness
    }.toColor()
}

fun Color.withHsl(block: HslColor.() -> Unit): Color = toHslColor().apply { block() }.toColor()

fun Color.withLuminance(brightness: Float = 0.05F): Color {
    val xyz = colorSpace.toXyz(red, green, blue)
    xyz[1] = brightness.coerceIn(0F, 1F) // Alter luminance
    val rgb = colorSpace.fromXyz(xyz)
    return Color(rgb[0], rgb[1], rgb[2])
}

val Color.redByte get() = red.mapToByte()
val Color.greenByte get() = green.mapToByte()
val Color.blueByte get() = blue.mapToByte()
val Color.alphaByte get() = alpha.mapToByte()
val HslColor.alphaByte get() = alpha.mapToByte()

@OptIn(ExperimentalUnsignedTypes::class)
fun Color.toHexString() = value.toString(16).substring(0, 8)
fun Color.toPrettyHexString() = "#${toHexString()}"

/**
 * Coerce to positive value between 0..mod
 */
@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
fun Float.positiveMod(mod: Float): Float {
    val x = this % mod
    return when {
        x < 0 -> x + mod
        else -> x
    }.coerceAtMost(mod)
}
