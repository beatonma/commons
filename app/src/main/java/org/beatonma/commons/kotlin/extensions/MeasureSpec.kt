package org.beatonma.commons.kotlin.extensions

import android.view.View

inline class MeasureMode(val mode: Int)
inline class MeasureSize(val size: Int)

inline fun View.withMeasureSpec(
    widthMeasureSpec: Int,
    heightMeasureSpec: Int,
    crossinline block: (
        widthSize: MeasureSize,
        widthMode: MeasureMode,
        heightSize: MeasureSize,
        heightMode: MeasureMode,
    ) -> Unit,
) {
    val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
    val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
    val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)
    val heightSize = View.MeasureSpec.getSize(heightMeasureSpec)

    block(MeasureSize(widthSize), MeasureMode(widthMode), MeasureSize(heightSize), MeasureMode(heightMode))
}
