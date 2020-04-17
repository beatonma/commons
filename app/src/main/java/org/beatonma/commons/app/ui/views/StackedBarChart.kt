package org.beatonma.commons.app.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.content.withStyledAttributes
import org.beatonma.commons.R
import org.beatonma.lib.util.kotlin.extensions.colorCompat

private const val TAG = "StackedBarChart"

data class BarChartCategory(
    val value: Int,
    val color: Int,
    val label: String?
) {
    var normalisedValue: Float = 0F

    /**
     * Set normalisedValue to a value in the range 0..1F
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
    private var actualWidth: Float = 0F
    private var actualHeight: Float = 0F
    var animationDuration: Long = 800L

    var animationTracker: AnimationTracker? = null

    var showCenter: Boolean = false
    @ColorInt var centerColor: Int = -1

    init {
        context.withStyledAttributes(attrs, R.styleable.StackedBarChart, defStyleAttr, defStyleRes) {
            showCenter = getBoolean(R.styleable.StackedBarChart_showCenter, false)
            val colorResId = getResourceId(R.styleable.StackedBarChart_centerColor, -1)
            centerColor = if (colorResId != -1) {
                context.colorCompat(colorResId)
            } else {
                getColor(R.styleable.StackedBarChart_centerColor, -1)
            }
        }
    }

    var categories: List<BarChartCategory> = listOf()
        set(value) {
            if (value == field) return
            field = value
            val totalWidth: Float = value.sumBy { it.value }.toFloat()
            field.forEach { it.calculateNormalisedValue(totalWidth) }
            animationTracker = AnimationTracker(animationDuration)
            postInvalidateOnAnimation()
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
        var xPosition = 0F
        val yPosition = actualHeight / 2F
        categories.forEach { item ->
            val itemWidth = item.scaledWidth(actualWidth)
            canvas.drawLine(
                xPosition,
                yPosition,
                xPosition + (itemWidth * progress),
                yPosition,
                paint.apply { color = item.color }
            )
            xPosition += itemWidth
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
                color = centerColor
                strokeWidth = actualHeight / 3
            }
        )
    }
}


class AnimationTracker(private val duration: Long, private val started: Long = System.currentTimeMillis()) {
    fun getProgress(): Float {
        val now = System.currentTimeMillis()
        return ((now - started).toFloat() / duration.toFloat()).coerceAtMost(1F)
    }
}
