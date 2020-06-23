package org.beatonma.commons.app.ui.views.renderer

import android.graphics.Canvas
import android.graphics.Paint

private const val TAG = "BarRenderer"
private const val DEFAULT_LEAN_STEP = 7
private const val DEFAULT_WITH_OUTLINE = false


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
    protected val leanStep: Int = DEFAULT_LEAN_STEP,
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
        val increments = ((endX - startX) / spacing).toInt()
        for (n in -leanStep..increments) {
            drawIncrement(canvas, n, startX, endX, topY, bottomY, color)
        }

        if (withOutline) {
            canvas.drawRect(startX, centerY - thickness / 2, endX, centerY + thickness / 2, paint.apply {
                this.color = color
                this.strokeWidth = lineThickness
            })
        }
    }

    abstract fun drawIncrement(canvas: Canvas, increment: Int, startX: Float, endX: Float, topY: Float, bottomY: Float, color: Int)
}


class StripeBarRenderer(
    lineThickness: Float,
    spacing: Float,
    withOutline: Boolean = DEFAULT_WITH_OUTLINE,
    paintInit: (Paint.() -> Unit)? = null,
): BaseStripeBarRenderer(lineThickness, spacing, withOutline, leanStep = 0, paintInit) {
    override fun drawIncrement(
        canvas: Canvas,
        increment: Int,
        startX: Float,
        endX: Float,
        topY: Float,
        bottomY: Float,
        color: Int,
    ) {
        if (increment < 0) return
        val x = startX + increment * spacing
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
    }
}


class ForwardStripeBarRenderer(
    lineThickness: Float,
    spacing: Float,
    withOutline: Boolean = DEFAULT_WITH_OUTLINE,
    leanStep: Int = DEFAULT_LEAN_STEP,
    paintInit: (Paint.() -> Unit)? = null,
): BaseStripeBarRenderer(lineThickness, spacing, withOutline, leanStep, paintInit) {
    override fun drawIncrement(
        canvas: Canvas,
        increment: Int,
        startX: Float,
        endX: Float,
        topY: Float,
        bottomY: Float,
        color: Int,
    ) {
        var x1 = startX + (increment * spacing)
        var x2 = startX + (increment + leanStep) * spacing

        var y1 = bottomY
        var y2 = topY

        val m = (y2 - y1) / (x2 - x1)

        if (x1 < startX) {
            val c1 = y1 - m * x1
            x1 = startX
            y1 = x1 * m + c1
        }
        if (x2 > endX) {
            val c2 = y2 - m * x2
            x2 = endX
            y2 = x2 * m + c2
        }

        canvas.drawLine(
            x1,
            y1,
            x2,
            y2,
            paint.apply {
                this.color = color
                this.strokeWidth = lineThickness
            }
        )
    }
}


class BackwardStripeBarRenderer(
    lineThickness: Float,
    spacing: Float,
    withOutline: Boolean = DEFAULT_WITH_OUTLINE,
    leanStep: Int = DEFAULT_LEAN_STEP,
    paintInit: (Paint.() -> Unit)? = null,
): BaseStripeBarRenderer(lineThickness, spacing, withOutline, leanStep, paintInit) {
    override fun drawIncrement(
        canvas: Canvas,
        increment: Int,
        startX: Float,
        endX: Float,
        topY: Float,
        bottomY: Float,
        color: Int,
    ) {
        var x1 = startX + (increment * spacing)
        var x2 = startX + (increment + leanStep) * spacing

        var y1 = topY
        var y2 = bottomY

        val m = (y2 - y1) / (x2 - x1)

        if (x1 < startX) {
            val c1 = y1 - m * x1
            x1 = startX
            y1 = x1 * m + c1
        }
        if (x2 > endX) {
            val c2 = y2 - m * x2
            x2 = endX
            y2 = x2 * m + c2
        }

        canvas.drawLine(
            x1,
            y1,
            x2,
            y2,
            paint.apply {
                this.color = color
                this.strokeWidth = lineThickness
            }
        )
    }
}
