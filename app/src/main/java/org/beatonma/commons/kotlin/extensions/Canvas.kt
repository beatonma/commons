package org.beatonma.commons.kotlin.extensions

import android.graphics.Canvas
import android.graphics.Paint
import android.text.TextPaint
import android.view.Gravity

/**
 * Draw a full-length horizontal line at the given y position.
 */
fun Canvas.hLine(y: Float, paint: Paint) = drawLine(0F, y, width.toFloat(), y, paint)

/**
 * Draw a full-length vertical line at the given x position.
 */
fun Canvas.vLine(x: Float, paint: Paint) = drawLine(x, 0F, x, height.toFloat(), paint)

/**
 * Draw horizontal lines at each of the key heights from [Paint.FontMetrics] to help with alignment issues.
 * Any particular guideline may be disabled by passing false to the relevant argument.
 */
fun Canvas.drawTextWithGuidelines(
    text: String,
    x: Float,
    y: Float,
    textPaint: TextPaint,
    guidelinePaint: Paint = Paint(textPaint).apply {
        color = 0x55_00_FF_00.toInt()
        strokeWidth = 4F
    },
    ascent: Boolean = true,
    descent: Boolean = true,
    top: Boolean = true,
    bottom: Boolean = true,
    baseline: Boolean = true,
) {
    val metrics = textPaint.fontMetrics
    drawText(text, x, y, textPaint)

    if (ascent) {
        hLine(y + metrics.ascent, guidelinePaint)
    }
    if (descent) {
        hLine(y + metrics.descent, guidelinePaint)
    }
    if (top) {
        hLine(y + metrics.top, guidelinePaint)
    }
    if (bottom) {
        hLine(y + metrics.bottom, guidelinePaint)
    }
    if (baseline) {
        hLine(y, guidelinePaint)
    }
}

/**
 * Align the text based on its boundaries - NOT its baseline!
 * e.g. Gravity.BOTTOM means that descents will be above [y], not hanging below.
 */
fun Canvas.drawTextAligned(
    text: String,
    x: Float,
    y: Float,
    paint: TextPaint,
    alignment: Int = combineFlags(Gravity.START, Gravity.BOTTOM)
) {
    val textWidth = paint.measureText(text)
    val metrics = paint.fontMetrics

    val alignedX = when {
        alignment.hasFlag(Gravity.END) -> x - textWidth
        alignment.hasFlag(Gravity.START) -> x
        alignment.hasFlag(Gravity.CENTER_HORIZONTAL) -> x - (textWidth / 2)
        else -> x // start
    }
    val alignedY = when {
        alignment.hasFlag(Gravity.TOP) -> y - metrics.ascent - metrics.descent
        alignment.hasFlag(Gravity.BOTTOM) -> y - metrics.descent
        alignment.hasFlag(Gravity.CENTER_VERTICAL) -> y - ((metrics.ascent + metrics.descent) / 2)
        else -> y // baseline
    }

    drawText(text, alignedX, alignedY, paint)
}
