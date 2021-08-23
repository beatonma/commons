package org.beatonma.commons.theme.compose.color

import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange

@JvmInline
value class EditableColor(val color: Int) {
    val red get() = Color.red(color)
    val green get() = Color.green(color)
    val blue get() = Color.blue(color)
    val alpha get() = Color.alpha(color)

    val hexString get() = Integer.toHexString(color)

    fun hsv(out: FloatArray = FloatArray(3)): FloatArray {
        Color.RGBToHSV(red, green, blue, out)
        return out
    }

    fun alpha(value: Int) = EditableColor(Color.argb(value, red, green, blue))
    fun alpha(value: Float) = alpha((value * 255F).toInt())

    fun coerce(
        minSaturation: Float = 0F,
        maxSaturation: Float = 1F,
        minLuminance: Float = 0F,
        maxLuminance: Float = 1F,
    ): EditableColor {
        val hsv = hsv()
        hsv[1] = hsv[1].coerceIn(minSaturation, maxSaturation)
        hsv[2] = hsv[2].coerceIn(minLuminance, maxLuminance)
        return EditableColor(Color.HSVToColor(hsv))
    }

    @ColorInt
    fun lighten(@FloatRange(from = 0.0, to = 1.0) amount: Float): Int {
        val hsv = hsv()
        hsv[1] = 0f.coerceAtLeast(1f.coerceAtMost(hsv[1] - amount))  // saturation
        hsv[2] = 0f.coerceAtLeast(1f.coerceAtMost(hsv[2] + amount))  // brightness
        return Color.HSVToColor(hsv)
    }

    @ColorInt
    fun darken(@FloatRange(from = 0.0, to = 1.0) amount: Float): Int {
        val hsv = hsv()
        hsv[1] = 0f.coerceAtLeast(1f.coerceAtMost(hsv[1] + amount))  // saturation
        hsv[2] = 0f.coerceAtLeast(1f.coerceAtMost(hsv[2] - amount))  // brightness
        return Color.HSVToColor(hsv)
    }

    fun add(hue: Float = 0F, saturation: Float = 0F, value: Float = 0F): Int {
        val hsv = hsv()
        hsv[0] = (hsv[0] + hue) % 360F
        hsv[1] = (hsv[1] + saturation).coerceAtMost(1F)
        hsv[2] = (hsv[2] + value).coerceAtMost(1F)
        return Color.HSVToColor(hsv)
    }
}
