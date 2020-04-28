package org.beatonma.commons.kotlin.data

import android.graphics.Color as PlatformColor

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
}
