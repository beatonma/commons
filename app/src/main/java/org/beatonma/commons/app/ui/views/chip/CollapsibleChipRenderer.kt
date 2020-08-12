package org.beatonma.commons.app.ui.views.chip

import android.animation.TimeInterpolator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.TextPaint
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.withTranslation
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.Interpolation
import org.beatonma.commons.data.ClickAction
import org.beatonma.commons.kotlin.extensions.*

/**
 * Default animation position after which 'expanded' data should be displayed.
 */
private const val PROGRESS_TRANSITION_POINT = .6F

/**
 * How far through the animation should the chip reach its full width.
 */
private const val PROGRESS_FULL_WIDTH = PROGRESS_TRANSITION_POINT

/**
 * How far through the animation should the cancel button start to appear.
 */
private const val PROGRESS_START_SHOW_CANCEL = PROGRESS_TRANSITION_POINT

/**
 * How far through the animation should the separator start to appear.
 */
private const val PROGRESS_START_SHOW_SEPARATOR = .65F

/**
 * How far through the animation should the text start to appear.
 */
private const val PROGRESS_START_SHOW_TEXT = .7F


internal enum class ChipState {
    COLLAPSED,
    EXPANDED,
    ;
}

class CollapsibleChipRenderer(
    private val collapsedHeight: Int,
    private val collapsedWidth: Int,
    private val iconSize: Int,
    private val background: Drawable?,
    private val cancelBitmap: Bitmap?,
    separatorColor: Int,
    separatorWidth: Float,
    private val separatorMargin: Float,
    private val animationDuration: Long,
    private val interpolator: TimeInterpolator = Interpolation.motion,
) {
    private val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    private val debugPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val alphaPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val separatorPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = separatorWidth
        color = separatorColor
    }

    private val separatorAlpha: Int = separatorPaint.alpha

    private val iconOffset = (collapsedHeight - iconSize) / 2F
    private val textY = (collapsedHeight / 2) + textPaint.textSize

    constructor(context: Context): this(
        collapsedHeight = context.dimenCompat(R.dimen.chip_min_height),
        collapsedWidth = context.dimenCompat(R.dimen.chip_min_height),
        iconSize = context.dimenCompat(R.dimen.chip_icon_size),
        background = context.drawableCompat(R.drawable.chip_outline),
        cancelBitmap = context.drawableCompat(
            R.drawable.ic_close,
            tint = context.colorCompat(R.color.Negative)
        )
            ?.toBitmap(
                width = context.dimenCompat(R.dimen.chip_icon_size),
                height = context.dimenCompat(R.dimen.chip_icon_size)
            ),
        separatorColor = context.colorCompat(R.color.TextTertiary),
        separatorWidth = context.dimenCompat(R.dimen.separator_thickness).toFloat(),
        separatorMargin = context.dimenCompat(R.dimen.separator_margin).toFloat(),
        animationDuration = context.longCompat(R.integer.animation_chip_duration),
    ) {
        textPaint.apply {
            color = context.colorCompat(R.color.TextPrimary)
            textSize = context.dimenCompat(R.dimen.text_size_secondary).toFloat()
        }
        debugPaint.apply {
            color = context.colorCompat(R.color.Debug)
            strokeWidth = context.dp(2F)
        }
    }

    internal fun draw(canvas: Canvas, chipRenderData: ChipRenderData): Int {
        with (chipRenderData) {
            if (animationStarted > 0L) {
                val now = System.currentTimeMillis()
                val delta = now - animationStarted

                if (delta >= animationDuration) {
                    animationStarted = -1L
                }

                expandProgress = (when (targetState) {
                    ChipState.COLLAPSED -> animationDuration - delta
                    ChipState.EXPANDED -> delta
                }.toFloat() / animationDuration.toFloat()).coerceIn(0F, 1F)
            }
        }
        return draw(canvas, chipRenderData, chipRenderData.expandProgress)
    }

    /**
     * Return with of rendered content.
     */
    private fun draw(canvas: Canvas, chipRenderData: ChipRenderData, progress: Float): Int {
        val widthProgress = interpolator.getInterpolation(progress.normalizeIn(0F, PROGRESS_FULL_WIDTH))
        val width = collapsedWidth + (widthProgress * (chipRenderData.maxWidth - collapsedWidth)).toInt()

        background?.setBounds(0, 0, width, collapsedHeight)
        drawBackground(canvas, progress)

        drawCancelIcon(canvas, progress)
        drawSeparator(canvas, progress)

        drawIcon(canvas, chipRenderData.icon, progress)

        drawText(canvas, chipRenderData.text, progress)

        return width
    }

    private fun drawDebugGuidelines(canvas: Canvas, width: Float) {
        val middleX = (width / 2)
        val middleY = (collapsedHeight / 2).toFloat()
        canvas.drawLine(0F, middleY, width, middleY, debugPaint)
        canvas.drawLine(middleX, 0F, middleX, collapsedHeight.toFloat(), debugPaint)
    }

    private fun drawBackground(canvas: Canvas, progress: Float) {
        background?.draw(canvas)
    }

    /**
     * Separator between cancel icon and action icon
     */
    private fun drawSeparator(canvas: Canvas, progress: Float) {
        val x = calculateSeparatorX(progress)
        canvas.drawLine(x, separatorMargin, x, collapsedHeight - separatorMargin,
            separatorPaint.apply {
                alpha = progress.normalizeIn(PROGRESS_START_SHOW_SEPARATOR, 1F)
                    .mapTo(0, separatorAlpha)
            })
    }

    private fun drawCancelIcon(canvas: Canvas, progress: Float) {
        withNotNull(cancelBitmap) { bitmap ->
            canvas.withTranslation(x = calculateCancelIconX(progress), y = iconOffset) {
                canvas.drawBitmap(bitmap, 0F, 0F,
                    alphaPaint.withAlpha(progress.normalizeIn(PROGRESS_START_SHOW_CANCEL, 1F)))
            }
        }
    }

    private fun drawText(canvas: Canvas, text: String, progress: Float) {
        canvas.drawText(text, calculateTextX(progress), textY,
            textPaint.withAlpha(progress.normalizeIn(PROGRESS_START_SHOW_TEXT, 1F)))
    }

    private fun drawIcon(canvas: Canvas, icon: Drawable?, progress: Float) {
        canvas.withTranslation(x = calculateIconX(progress), y = iconOffset) {
            icon?.draw(canvas)
        }
    }

    internal fun getTouchAction(chipRenderData: ChipRenderData, x: Float, y: Float): ClickAction? {
        with (chipRenderData) {
            return when (targetState) {
                ChipState.COLLAPSED -> { _ ->
                    targetState = ChipState.EXPANDED
                }
                ChipState.EXPANDED -> if (x > calculateSeparatorX(chipRenderData.expandProgress)) {
                    action
                } else {
                    { targetState = ChipState.COLLAPSED }
                }
            }
        }
    }

    private fun calculateCancelIconX(progress: Float): Float = (progress * iconOffset)

    private fun calculateSeparatorX(progress: Float): Float =
        calculateCancelIconX(progress) + (progress * (iconSize + separatorMargin))

    private fun calculateIconX(progress: Float): Float =
        (calculateSeparatorX(progress) + separatorMargin).coerceAtLeast(iconOffset)

    private fun calculateTextX(progress: Float): Float =
        calculateIconX(progress) + iconSize + separatorMargin

    private fun measureText(text: String) = textPaint.measureText(text)

    internal fun measureMaxWidth(text: String): Int =
        (calculateTextX(1F) + measureText(text) + iconOffset).toInt()

    private inline fun Paint.withAlpha(alpha: Float) = apply { this.alpha = alpha.mapToByte() }
}
