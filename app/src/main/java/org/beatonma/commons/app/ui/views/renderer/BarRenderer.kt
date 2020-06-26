package org.beatonma.commons.app.ui.views.renderer

import android.graphics.Canvas
import android.graphics.Paint

private const val TAG = "BarRenderer"
private const val DEFAULT_WITH_OUTLINE = true

private const val DEFAULT_STRIPE_GRADIENT = -1F


abstract class BarRenderer(paintInit: (Paint.() -> Unit)? = null) {
    val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        paintInit?.invoke(this)
    }

    abstract fun draw(canvas: Canvas, startX: Float, endX: Float, centerY: Float, thickness: Float, color: Int)

    companion object {
        fun simple(lineThickness: Float) = arrayOf(
            SolidBarRenderer(),
            OutlineBarRenderer(lineThickness),
        )

        fun mixed(lineThickness: Float, spacing: Float, withOutline: Boolean = DEFAULT_WITH_OUTLINE) = arrayOf(
            SolidBarRenderer(),
            OutlineBarRenderer(lineThickness),
            StripeBarRenderer(lineThickness, spacing, withOutline),
            ForwardStripeBarRenderer(lineThickness, spacing, withOutline),
            BackwardStripeBarRenderer(lineThickness, spacing, withOutline),
        )

        fun striped(lineThickness: Float, spacing: Float, withOutline: Boolean = DEFAULT_WITH_OUTLINE) = arrayOf(
            StripeBarRenderer(lineThickness, spacing, withOutline),
            ForwardStripeBarRenderer(lineThickness, spacing, withOutline),
            BackwardStripeBarRenderer(lineThickness, spacing, withOutline),
        )
    }
}

class SolidBarRenderer(paintInit: (Paint.() -> Unit)? = null): BarRenderer(paintInit) {
    override fun draw(
        canvas: Canvas,
        startX: Float,
        endX: Float,
        centerY: Float,
        thickness: Float,
        color: Int,
    ) {
        canvas.drawLine(startX, centerY, endX, centerY, paint.apply {
            this.color = color
            strokeWidth = thickness
        })
    }
}


class OutlineBarRenderer(
    private val lineThickness: Float,
    paintInit: (Paint.() -> Unit)? = null,
): BarRenderer(paintInit) {
    override fun draw(
        canvas: Canvas,
        startX: Float,
        endX: Float,
        centerY: Float,
        thickness: Float,
        color: Int,
    ) {
        canvas.drawRect(startX, centerY - thickness / 2, endX, centerY + thickness / 2, paint.apply {
            this.color = color
            this.strokeWidth = lineThickness
        })
    }
}


abstract class BaseStripeBarRenderer(
    protected val lineThickness: Float,
    protected val spacing: Float,
    private val withOutline: Boolean = DEFAULT_WITH_OUTLINE,
    protected val stripeGradient: Float = DEFAULT_STRIPE_GRADIENT,
    paintInit: (Paint.() -> Unit)? = null,
): BarRenderer(paintInit) {

    override fun draw(
        canvas: Canvas,
        startX: Float,
        endX: Float,
        centerY: Float,
        thickness: Float,
        color: Int,
    ) {
        val topY = centerY - thickness / 2
        val bottomY = centerY + thickness / 2

        drawStripes(canvas, startX, endX, topY, bottomY, color)

        if (withOutline) {
            canvas.drawRect(startX, centerY - thickness / 2, endX, centerY + thickness / 2, paint.apply {
                this.color = color
                this.strokeWidth = lineThickness
            })
        }
    }

    abstract fun drawStripes(
        canvas: Canvas,
        startX: Float,
        endX: Float,
        topY: Float,
        bottomY: Float,
        color: Int,
    )
}


class StripeBarRenderer(
    lineThickness: Float,
    spacing: Float,
    withOutline: Boolean = DEFAULT_WITH_OUTLINE,
    paintInit: (Paint.() -> Unit)? = null,
): BaseStripeBarRenderer(lineThickness, spacing, withOutline, 0F, paintInit) {
    override fun drawStripes(
        canvas: Canvas,
        startX: Float,
        endX: Float,
        topY: Float,
        bottomY: Float,
        color: Int
    ) {
        var n = 0
        var done: Boolean
        do {
            val x = startX + n++ * spacing
            canvas.drawLine(
                x,
                topY,
                x,
                bottomY,
                paint.apply {
                    this.color = color
                    this.strokeWidth = lineThickness
                }
            )
            done = x + spacing > endX
        } while (!done)
    }
}

open class ForwardStripeBarRenderer(
    lineThickness: Float,
    spacing: Float,
    withOutline: Boolean = DEFAULT_WITH_OUTLINE,
    stripeGradient: Float = DEFAULT_STRIPE_GRADIENT,
    paintInit: (Paint.() -> Unit)? = null,
): BaseStripeBarRenderer(lineThickness, spacing, withOutline, stripeGradient, paintInit) {
    internal open fun plotHorizontal(
        gap: Float,
        startX: Float,
        endX: Float,
        topY: Float,
        bottomY: Float,
    ): Line {
        val x1 = startX + gap
        val c = topY - (stripeGradient * x1)
        val x2 = (bottomY - c) / stripeGradient

        return Line(x1, topY, x2, bottomY).confineTo(startX, topY, endX, bottomY)
    }

    internal open fun plotVertical(
        gap: Float,
        startX: Float,
        endX: Float,
        topY: Float,
        bottomY: Float,
    ): Line {
        val y1 = topY + gap
        val c = y1 - (stripeGradient * startX)
        val x2 = (bottomY - c) / stripeGradient

        return Line(startX, y1, x2, bottomY).confineTo(startX, topY, endX, bottomY)
    }

    override fun drawStripes(
        canvas: Canvas,
        startX: Float,
        endX: Float,
        topY: Float,
        bottomY: Float,
        color: Int,
    ) {
        var n = 0
        var done: Boolean

        // Across
        do {
            val gap = n++ * spacing
            val (x1, y1, x2, y2) = plotHorizontal(gap, startX, endX, topY, bottomY)

            canvas.drawLine(
                x1, y1, x2, y2,
                paint.apply {
                    this.color = color
                    this.strokeWidth = lineThickness
                }
            )

            done = x1 + spacing > endX && x2 + spacing > endX
        } while (!done)

        // Down/backfilling the corner
        n = 0
        do {
            val gap = n++ * spacing
            val (x1, y1, x2, y2) = plotVertical(gap, startX, endX, topY, bottomY)

            canvas.drawLine(
                x1, y1, x2, y2,
                paint.apply {
                    this.color = color
                    this.strokeWidth = lineThickness
                }
            )

            done = y1 + spacing > bottomY && y2 + spacing > bottomY
        } while (!done)
    }
}


class BackwardStripeBarRenderer(
    lineThickness: Float,
    spacing: Float,
    withOutline: Boolean = DEFAULT_WITH_OUTLINE,
    stripeGradient: Float = DEFAULT_STRIPE_GRADIENT,
    paintInit: (Paint.() -> Unit)? = null,
): ForwardStripeBarRenderer(lineThickness, spacing, withOutline, -stripeGradient, paintInit)



internal data class Line(val startX: Float, val startY: Float, val endX: Float, val endY: Float) {
    private val m: Float = (endY - startY) / (endX - startX)
    private val c: Float = startY - (m * startX)

    private fun x(y: Float): Float = (y - c) / m
    private fun y(x: Float): Float = (m * x) + c

    fun confineTo(startX: Float, startY: Float, endX: Float, endY: Float): Line {
        var x1 = this.startX
        var x2 = this.endX
        var y1 = this.startY
        var y2 = this.endY

        if (x1 < startX || x1 > endX) {
            x1 = x1.coerceIn(startX, endX)
            y1 = y(x1)
        }
        if (x2 < startX || x2 > endX) {
            x2 = x2.coerceIn(startX, endX)
            y2 = y(x2)
        }
        if (y1 < startY) {
            y1 = startY
            x1 = x(y1)
        }
        if (y2 > endY) {
            y2 = endY
            x2 = x(y2)
        }

        return Line(x1, y1, x2, y2)
    }
}
