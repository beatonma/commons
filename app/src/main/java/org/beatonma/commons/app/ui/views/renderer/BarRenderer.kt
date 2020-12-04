package org.beatonma.commons.app.ui.views.renderer

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path

private const val TAG = "BarRenderer"
private const val DEFAULT_WITH_BACKGROUND = true

private const val DEFAULT_STRIPE_GRADIENT = -1F


abstract class BarRenderer(paintInit: (Paint.() -> Unit)? = null) {
    val paint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        paintInit?.invoke(this)
    }
    val path = Path()

    abstract fun draw(canvas: Canvas, startX: Float, endX: Float, centerY: Float, thickness: Float, colorPrimary: Int, colorSecondary: Int)

    companion object {
        fun simple(lineThickness: Float) = arrayOf(
            SolidBarRenderer(),
            OutlineBarRenderer(lineThickness),
        )

        fun mixed(lineThickness: Float, spacing: Float, withBackground: Boolean = DEFAULT_WITH_BACKGROUND) = arrayOf(
            SolidBarRenderer(),
//            OutlineBarRenderer(lineThickness),
            StripeBarRenderer(lineThickness, spacing, withBackground),
            ForwardStripeBarRenderer(lineThickness, spacing, withBackground),
            BackwardStripeBarRenderer(lineThickness,
                spacing,
                withBackground),
        )

        fun striped(lineThickness: Float, spacing: Float, withBackground: Boolean = DEFAULT_WITH_BACKGROUND) = arrayOf(
            StripeBarRenderer(lineThickness, spacing, withBackground),
            ForwardStripeBarRenderer(lineThickness, spacing, withBackground),
            BackwardStripeBarRenderer(lineThickness,
                spacing,
                withBackground),
        )
    }
}


class SolidBarRenderer(
    paintInit: (Paint.() -> Unit)? = { style = Paint.Style.STROKE },
): BarRenderer(paintInit) {
    override fun draw(
        canvas: Canvas,
        startX: Float,
        endX: Float,
        centerY: Float,
        thickness: Float,
        colorPrimary: Int,
        colorSecondary: Int
    ) {
        canvas.drawLine(startX, centerY, endX, centerY, paint.apply {
            color = colorPrimary
            strokeWidth = thickness
        })
    }
}


class OutlineBarRenderer(
    private val lineThickness: Float,
    paintInit: (Paint.() -> Unit)? = { style = Paint.Style.FILL },
): BarRenderer(paintInit) {
    override fun draw(
        canvas: Canvas,
        startX: Float,
        endX: Float,
        centerY: Float,
        thickness: Float,
        colorPrimary: Int,
        colorSecondary: Int,
    ) {
        val topY = centerY - thickness / 2F
        val bottomY = centerY + thickness / 2F

        // Background
        canvas.drawRect(startX, topY, endX, bottomY, paint.apply {
            color = colorSecondary
        })

        // Inset outline
        path.reset()
        path.addRect(startX, topY, endX, topY + lineThickness, Path.Direction.CW)
        path.addRect(endX - lineThickness, topY, endX, bottomY, Path.Direction.CW)
        path.addRect(startX, bottomY - lineThickness, endX, bottomY, Path.Direction.CW)
        path.addRect(startX, topY, startX + lineThickness, bottomY, Path.Direction.CW)
        canvas.drawPath(path, paint.apply {
            color = colorPrimary
        })
    }
}


abstract class BaseStripeBarRenderer(
    protected val lineThickness: Float,
    protected val spacing: Float,
    private val withBackground: Boolean = DEFAULT_WITH_BACKGROUND,
    protected val stripeGradient: Float = DEFAULT_STRIPE_GRADIENT,
    paintInit: (Paint.() -> Unit)?,
): BarRenderer(paintInit) {

    override fun draw(
        canvas: Canvas,
        startX: Float,
        endX: Float,
        centerY: Float,
        thickness: Float,
        colorPrimary: Int,
        colorSecondary: Int,
    ) {
        val topY = centerY - thickness / 2
        val bottomY = centerY + thickness / 2

        if (withBackground) {
            canvas.drawRect(startX, centerY - thickness / 2, endX, centerY + thickness / 2, paint.apply {
                color = colorSecondary
                style = Paint.Style.FILL
            })
        }

        drawStripes(canvas, startX, endX, topY, bottomY, colorPrimary)
    }

    abstract fun drawStripes(
        canvas: Canvas,
        startX: Float,
        endX: Float,
        topY: Float,
        bottomY: Float,
        stripeColor: Int,
    )
}

/**
 * Simple vertical stripes
 */
class StripeBarRenderer(
    lineThickness: Float,
    spacing: Float,
    withBackground: Boolean = DEFAULT_WITH_BACKGROUND,
    paintInit: (Paint.() -> Unit)? = { style = Paint.Style.FILL },
): BaseStripeBarRenderer(lineThickness, spacing, withBackground, Float.MAX_VALUE, paintInit) {
    override fun drawStripes(
        canvas: Canvas,
        startX: Float,
        endX: Float,
        topY: Float,
        bottomY: Float,
        stripeColor: Int
    ) {
        paint.color = stripeColor

        var n = 0
        var done: Boolean
        do {
            val x = startX + (n++ * spacing)
            val widthX = (x + lineThickness).coerceAtMost(endX)

            path.apply {
                reset()
                moveTo(x, topY)
                lineTo(widthX, topY)
                lineTo(widthX, bottomY)
                lineTo(x, bottomY)
                close()
            }

            canvas.drawPath(path, paint)

            done = x + spacing > endX
        } while (!done)
    }
}

/**
 * Stripes that lean forward with a gradient of [stripeGradient]
 */
open class ForwardStripeBarRenderer(
    lineThickness: Float,
    spacing: Float,
    withBackground: Boolean = DEFAULT_WITH_BACKGROUND,
    stripeGradient: Float = DEFAULT_STRIPE_GRADIENT,
    paintInit: (Paint.() -> Unit)? = { style = Paint.Style.FILL },
): BaseStripeBarRenderer(lineThickness, spacing, withBackground, stripeGradient, paintInit) {
    private val line = Line()

    /**
     * Plot a line from (startX + gap, topY) with given gradient to intersect [bottomY].
     */
    private fun plotHorizontal(
        gap: Float,
        startX: Float,
        topY: Float,
        bottomY: Float,
    ) {
        val x1 = startX + gap
        val c = topY - (stripeGradient * x1)
        val x2 = (bottomY - c) / stripeGradient

        line.set(x1, topY, x2, bottomY)
    }

    /**
     * Plot a line from (startX, topY + gap) with given gradient to intersect [bottomY].
     */
    private fun plotVertical(
        gap: Float,
        startX: Float,
        topY: Float,
        bottomY: Float,
    ) {
        val y1 = topY + gap
        val c = y1 - (stripeGradient * startX)
        val x2 = (bottomY - c) / stripeGradient

        line.set(startX, y1, x2, bottomY)
    }

    override fun drawStripes(
        canvas: Canvas,
        startX: Float,
        endX: Float,
        topY: Float,
        bottomY: Float,
        stripeColor: Int,
    ) {
        var n = 0
        var done: Boolean

        paint.color = stripeColor

        // Go along the x-axis creating stripes
        do {
            val gap = n++ * spacing

            // Plot parallelogram
            path.reset()
            plotHorizontal(gap, startX, topY, bottomY)
            line.confineTo(startX, topY, endX, bottomY)
            path.moveTo(line)
            plotHorizontal(gap, startX + lineThickness, topY, bottomY)
            line.confineTo(startX, topY, endX, bottomY)
            path.lineTo(line)
            path.close()

            canvas.drawPath(path, paint.apply { style = Paint.Style.FILL })

            done = line.startX + spacing > endX && line.endX + spacing > endX
        } while (!done)

        if (stripeGradient > 0) {
            // Backfill the missing corner by going down the y-axis
            n = 0
            val offsetY = topY + lineThickness  // Avoid overlap with previous lines
            do {
                val gap = n++ * spacing

                // Plot parallelogram
                path.reset()
                plotVertical(gap, startX, offsetY, bottomY)
                line.confineTo(startX, topY, endX, bottomY)
                path.moveTo(line)
                plotVertical(gap, startX, offsetY + lineThickness, bottomY + lineThickness)
                line.confineTo(startX, topY, endX, bottomY)
                path.lineTo(line)
                path.close()

                canvas.drawPath(path, paint)

                done = line.startY + spacing > bottomY && line.endY + spacing > bottomY
            } while (!done)
        }
    }
}


/**
 * Stripes that lean backward with a gradient of [stripeGradient]
 */
class BackwardStripeBarRenderer(
    lineThickness: Float,
    spacing: Float,
    withBackground: Boolean = DEFAULT_WITH_BACKGROUND,
    stripeGradient: Float = DEFAULT_STRIPE_GRADIENT,
    paintInit: (Paint.() -> Unit)? = { style = Paint.Style.FILL },
): ForwardStripeBarRenderer(lineThickness, spacing, withBackground, -stripeGradient, paintInit)



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

//    fun draw(canvas: Canvas, paint: Paint) = canvas.drawLine(startX, startY, endX, endY, paint)
}

private fun Path.moveTo(line: Line) {
    moveTo(line.startX, line.startY)
    lineTo(line.endX, line.endY)
}

private fun Path.lineTo(line: Line) {
    lineTo(line.endX, line.endY)
    lineTo(line.startX, line.startY)
}

internal fun Line.draw(canvas: Canvas, paint: Paint) =
    canvas.drawLine(startX, startY, endX, endY, paint)
