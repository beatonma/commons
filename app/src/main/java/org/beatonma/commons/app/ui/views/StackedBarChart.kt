package org.beatonma.commons.app.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.core.content.withStyledAttributes
import org.beatonma.commons.R
import org.beatonma.commons.kotlin.extensions.boolean
import org.beatonma.commons.kotlin.extensions.color
import org.beatonma.commons.kotlin.extensions.enum
import org.beatonma.commons.kotlin.extensions.long
import org.beatonma.lib.graphic.core.utility.AnimationUtils
import java.lang.Float.min

private const val TAG = "StackedBarChart"

data class BarChartCategory(
    val value: Int,
    val color: Int,
    val label: String?
) {
    @FloatRange(from = 0.0, to = 1.0) var normalisedValue: Float = 0F

    /**
     * Set normalisedValue to the fraction of the totalWidth that this category represents.
     */
    fun calculateNormalisedValue(totalWidth: Float) {
        if (totalWidth <= 0F) {
            Log.w(TAG, "Invalid total width: $totalWidth")
            return
        }
        normalisedValue = value.toFloat() / totalWidth
    }

    fun scaledWidth(availableWidth: Float) = normalisedValue * availableWidth
}


class StackedBarChart @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private val paint: Paint = Paint(ANTI_ALIAS_FLAG)
    private val interpolator = AnimationUtils.getMotionInterpolator()
    private var actualWidth: Float = 0F
    private var actualHeight: Float = 0F

    private var animationDuration: Long = 800L
    private var animationTracker: AnimationTracker? = null

    var showCenter: Boolean = false
    @ColorInt var centerColor: Int = -1
    var animationStyle: AnimationStyle = AnimationStyle.Series

    var categories: List<BarChartCategory> = listOf()
        set(value) {
            if (value == field) return
            field = value
            val totalWidth: Float = value.sumBy { it.value }.toFloat()
            field.forEach { it.calculateNormalisedValue(totalWidth) }
            animationTracker = AnimationTracker(animationDuration)
            postInvalidateOnAnimation()
        }

    init {
        context.withStyledAttributes(attrs, R.styleable.StackedBarChart, defStyleAttr, defStyleRes) {
            showCenter = boolean(context, R.styleable.StackedBarChart_showCenter, false)
            centerColor = color(context, R.styleable.StackedBarChart_centerColor, -1)
            animationDuration = long(context, R.styleable.StackedBarChart_animDuration, 800)
            animationStyle = enum(context, R.styleable.StackedBarChart_animStyle, AnimationStyle.Series)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        actualWidth = w.toFloat()
        actualHeight = h.toFloat()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas ?: return

        val progress = animationTracker?.getProgress() ?: 1F

        paint.strokeWidth = actualHeight - paddingTop - paddingBottom
        drawCategories(canvas, categories, progress)

        if (showCenter) { drawCenterMark(canvas, progress) }

        if (progress >= 1F) {
            animationTracker = null
        } else {
            postInvalidateOnAnimation()
        }
    }

    private fun drawCategories(canvas: Canvas, categories: List<BarChartCategory>, progress: Float) {
        val func = when (animationStyle) {
            AnimationStyle.Series -> ::drawCategoriesSeries
            AnimationStyle.Parallel -> ::drawCategoriesParallel
            AnimationStyle.MiddleOut -> ::drawCategoriesMiddleOut
        }
        func(canvas, categories, progress)
    }

    private fun drawCategoriesSeries(canvas: Canvas, categories: List<BarChartCategory>, progress: Float) {
        var xPosition = 0F
        val yPosition = actualHeight / 2F
        val interpolatedProgress = interpolator.getInterpolation(progress)
        val progressWidth = interpolatedProgress * actualWidth

        categories.forEach { item ->
            val itemWidth = item.scaledWidth(actualWidth)
            val endOfLine = min(progressWidth, xPosition + itemWidth)
            canvas.drawLine(
                xPosition,
                yPosition,
                endOfLine,
                yPosition,
                paint.apply { color = item.color }
            )
            xPosition += itemWidth
            if (xPosition >= progressWidth) {
                return
            }
        }
    }

    private fun drawCategoriesParallel(canvas: Canvas, categories: List<BarChartCategory>, progress: Float) {
        var xPosition = 0F
        val yPosition = actualHeight / 2F
        val interpolatedProgress = interpolator.getInterpolation(progress)

        categories.forEach { item ->
            val itemWidth = item.scaledWidth(actualWidth) * progress
            val endX = xPosition + (itemWidth * interpolatedProgress)
            canvas.drawLine(
                xPosition,
                yPosition,
                endX,
                yPosition,
                paint.apply { color = item.color }
            )
            xPosition = endX
        }
    }

    private fun drawCategoriesMiddleOut(canvas: Canvas, categories: List<BarChartCategory>, progress: Float) {
        var xPosition = 0F
        val yPosition = actualHeight / 2F
        val interpolatedProgress = interpolator.getInterpolation(progress)
        val middleX = actualWidth / 2

        categories.forEach { item ->
            val itemWidth = item.scaledWidth(actualWidth)
            canvas.drawLine(
                middleX - (interpolatedProgress * middleX) + xPosition,
                yPosition,
                middleX - (interpolatedProgress * middleX) + xPosition + (itemWidth * interpolatedProgress),
                yPosition,
                paint.apply { color = item.color }
            )
            xPosition += (itemWidth * interpolatedProgress)
        }
    }

    private fun drawCenterMark(canvas: Canvas, progress: Float) {
        val middle = actualWidth / 2F
        canvas.drawLine(
            middle,
            0F,
            middle,
            actualHeight,
            paint.apply {
                alpha = (progress * 255F).toInt()
                color = centerColor
                strokeWidth = actualHeight / 3
            }
        )
    }

    enum class AnimationStyle {
        Series,
        Parallel,
        MiddleOut,
    }
}


class AnimationTracker(private val duration: Long, private val started: Long = System.currentTimeMillis()) {
    fun getProgress(): Float {
        val now = System.currentTimeMillis()
        return ((now - started).toFloat() / duration.toFloat()).coerceAtMost(1F)
    }
}
