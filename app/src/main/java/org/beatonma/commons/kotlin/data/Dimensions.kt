package org.beatonma.commons.kotlin.data

import android.graphics.Point
import androidx.annotation.Size

/**
 * Functions like [Pair] or [Point] but only using primitives.
 * Represents the width and height of something.
 */
inline class Dimensions(@Size(2) val dimensions: IntArray = intArrayOf(0, 0)) {
    var width: Int
        set(value) {
            dimensions[0] = value
        }
        get() = dimensions[0]

    var height: Int
        set(value) {
            dimensions[1] = value
        }
        get() = dimensions[1]

    fun set(w: Int, h: Int) {
        dimensions[0] = w
        dimensions[1] = h
    }

    fun isEmpty() = width == 0 && height == 0
    fun isSet() = width > 0 && height > 0

    override fun toString(): String = "[$width, $height]"
}
