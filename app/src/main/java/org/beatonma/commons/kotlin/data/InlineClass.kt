@file:JvmName("InlineClass")

package org.beatonma.commons.kotlin.data

import android.graphics.Color as PlatformColor

inline class Color(val color: Int) {
    val red get() = PlatformColor.red(color)
    val green get() = PlatformColor.green(color)
    val blue get() = PlatformColor.blue(color)
    val hex get() = Integer.toHexString(color)
}
