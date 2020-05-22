package org.beatonma.commons.kotlin.extensions

import android.graphics.Rect
import android.graphics.RectF

/* Rect */
fun Rect.update(
    left: Int = this.left, top: Int = this.top,
    right: Int = this.right, bottom: Int = this.bottom,
) {
    set(left, top, right, bottom)
}

fun Rect.updateBy(
    left: Int = 0, top: Int = 0,
    right: Int = 0, bottom: Int = 0,
) {
    set(this.left + left, this.top + top,
        this.right + right, this.bottom + bottom)
}

/* RectF */
fun RectF.update(
    left: Float = this.left, top: Float = this.top,
    right: Float = this.right, bottom: Float = this.bottom,
) {
    set(left, top, right, bottom)
}

fun RectF.updateBy(
    left: Float = 0F, top: Float = 0F,
    right: Float = 0F, bottom: Float = 0F,
) {
    set(this.left + left, this.top + top,
        this.right + right, this.bottom + bottom)
}
