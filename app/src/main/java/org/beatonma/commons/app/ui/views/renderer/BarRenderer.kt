package org.beatonma.commons.app.ui.views.renderer

import android.graphics.Canvas
import android.graphics.Paint

private const val TAG = "BarRenderer"
private const val DEFAULT_WITH_OUTLINE = false

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
            val x = startX + (n++ * spacing)
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

        canvas.drawLine(
            endX,
            topY,
            endX,
            bottomY,
            paint.apply {
                this.color = color
                this.strokeWidth = lineThickness
            }
        )
    }
}

open class ForwardStripeBarRenderer(
    lineThickness: Float,
    spacing: Float,
    withOutline: Boolean = DEFAULT_WITH_OUTLINE,
    stripeGradient: Float = DEFAULT_STRIPE_GRADIENT,
    paintInit: (Paint.() -> Unit)? = null,
): BaseStripeBarRenderer(lineThickness, spacing, withOutline, stripeGradient, paintInit) {
    private val line = Line()

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
        return line.apply {
            set(x1, topY, x2, bottomY)
            confineTo(startX, topY, endX, bottomY)
        }
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

        return line.apply {
            set(startX, y1, x2, bottomY)
            confineTo(startX, topY, endX, bottomY)
        }
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
            plotHorizontal(gap, startX, endX, topY, bottomY)
            line.draw(canvas, paint.apply {
                this.color = color
                this.strokeWidth = lineThickness
            })

            done = line.startX + spacing > endX && line.endX + spacing > endX
        } while (!done)

        // Down/backfilling the corner
        n = 0
        do {
            val gap = n++ * spacing
            plotVertical(gap, startX, endX, topY, bottomY)
            line.draw(canvas, paint.apply {
                this.color = color
                this.strokeWidth = lineThickness
            })

            done = line.startY + spacing > bottomY && line.endY + spacing > bottomY
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



internal class Line {
    var startX: Float = 0F
    var startY: Float = 0F
    var endX: Float = 0F
    var endY: Float = 0F
    private var m: Float = 1F
    private var c: Float = 0F

    private fun x(y: Float): Float = (y - c) / m
    private fun y(x: Float): Float = (m * x) + c

    fun confineTo(startX: Float, startY: Float, endX: Float, endY: Float) {
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

        this.startX = x1
        this.startY = y1
        this.endX = x2
        this.endY = y2
    }

    fun set(startX: Float, startY: Float, endX: Float, endY: Float) {
        this.startX = startX
        this.startY = startY
        this.endX = endX
        this.endY = endY
        m = (endY - startY) / (endX - startX)
        c = startY - (m * startX)
    }

    fun draw(canvas: Canvas, paint: Paint) = canvas.drawLine(startX, startY, endX, endY, paint)
}
