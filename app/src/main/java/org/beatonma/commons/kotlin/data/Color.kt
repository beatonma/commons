package org.beatonma.commons.kotlin.data

import android.content.res.ColorStateList
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import android.graphics.Color as PlatformColor

@Suppress("MemberVisibilityCanBePrivate")
inline class Color(val color: Int) {
    val red get() = PlatformColor.red(color)
    val green get() = PlatformColor.green(color)
    val blue get() = PlatformColor.blue(color)
    val alpha get() = PlatformColor.alpha(color)

    val hex get() = Integer.toHexString(color)

    fun hsv(out: FloatArray = FloatArray(3)): FloatArray {
        PlatformColor.RGBToHSV(red, green, blue, out)
        return out
    }

    fun alpha(value: Int) = Color(PlatformColor.argb(value, red, green, blue))
    fun alpha(value: Float) = alpha((value * 255F).toInt())

    fun coerce(
        minSaturation: Float = 0F,
        maxSaturation: Float = 1F,
        minLuminance: Float = 0F,
        maxLuminance: Float = 1F,
    ): Color {
        val hsv = hsv()
        hsv[1] = hsv[1].coerceIn(minSaturation, maxSaturation)
        hsv[2] = hsv[2].coerceIn(minLuminance, maxLuminance)
        return Color(PlatformColor.HSVToColor(hsv))
    }

    @ColorInt
    fun lighten(@FloatRange(from = 0.0, to = 1.0) amount: Float): Int {
        val hsv = hsv()
        hsv[1] = 0f.coerceAtLeast(1f.coerceAtMost(hsv[1] - amount))    // saturation
        hsv[2] = 0f.coerceAtLeast(1f.coerceAtMost(hsv[2] + amount))    // brightness
        return PlatformColor.HSVToColor(hsv)
    }

    @ColorInt
    fun darken(@FloatRange(from = 0.0, to = 1.0) amount: Float): Int {
        val hsv = hsv()
        hsv[1] = 0f.coerceAtLeast(1f.coerceAtMost(hsv[1] + amount))    // saturation
        hsv[2] = 0f.coerceAtLeast(1f.coerceAtMost(hsv[2] - amount))   // brightness
        return PlatformColor.HSVToColor(hsv)
    }
}

fun Int.asStateList() = ColorStateList.valueOf(this)
