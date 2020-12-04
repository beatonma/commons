package org.beatonma.commons.app.ui.views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Typeface
import android.text.TextPaint
import android.util.AttributeSet
import androidx.core.graphics.withTranslation
import androidx.core.view.NestedScrollingChildHelper
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.views.renderer.SolidBarRenderer
import org.beatonma.commons.core.extensions.clipToLength
import org.beatonma.commons.data.core.interfaces.Named
import org.beatonma.commons.data.core.interfaces.Periodic
import org.beatonma.commons.data.core.interfaces.Temporal
import org.beatonma.commons.data.resolution.description
import org.beatonma.commons.kotlin.data.Dimensions
import org.beatonma.commons.kotlin.extensions.colorCompat
import org.beatonma.commons.kotlin.extensions.dimenCompat
import org.beatonma.commons.kotlin.extensions.displaySize
import org.beatonma.commons.kotlin.extensions.dp
import org.beatonma.commons.kotlin.extensions.roundDown
import org.beatonma.commons.kotlin.extensions.roundUp
import java.time.LocalDate
import java.time.Period
import kotlin.math.absoluteValue

private const val TAG = "HistoryView"

private const val LABEL_MAX_LENGTH = 48
private const val PADDING_YEARS = 10L

// Only show gridlines before/after timeline start/end if they fall within this number of years of the timeline
private const val GRIDLINE_MONTHS_PADDING = 12L * 5L


/**
 * The timeline chooses a zero-point from which all events are measured.
 * We refer to the period represented by the complete timeline as an 'epoch' and all
 * measurements within that epoch are denoted as the number of months between 'zero' and the event.
 * This allows us to easily calculate the x-position of events on the timeline.
 */
class TimelineView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
): ScrollableView(context, attrs, defStyleAttr, defStyleRes) {

    override val nestedScrollHelper: NestedScrollingChildHelper = NestedScrollingChildHelper(this).apply {
        isNestedScrollingEnabled = true
    }

    override val maxSize: Dimensions
        get() = context.displaySize().apply {
            height /= 2
        }

    private val now: LocalDate = LocalDate.now()
    private val dp: Float = context.dp(1F)

    private val scale: Float = 1.5F  // Density-independent pixels per month
    private val barThickness = 24 * dp
    private val barSpacing = 16 * dp // Between bars
    private val barHeightTotal = barThickness + barSpacing
    private val instantRadius = barThickness / 3

    private val labelTextSize = context.dimenCompat(R.dimen.text_size_label).toFloat()
    private val halfLabelTextSize = labelTextSize / 2F
    private val labelMargin = halfLabelTextSize

    // Horizontal padding before/after graph bars, expressed in number of months
    private val paddingMonths = 12L * PADDING_YEARS
    private val verticalMargin: Float = labelTextSize * 1.5F
    private var paddingExtraForLabels: Int = 0  // Additional space before start of graph to ensure labels are fully visible

    private var bitmap: Bitmap? = null
    private var alphaCanvas: Canvas? = null

    private val primaryColors = context.resources.getIntArray(R.array.colors_graph_primary)
    private val secondaryColors = context.resources.getIntArray(R.array.colors_graph_secondary)

    private var historyRenderData: HistoryRenderData? = null

    private val barRenderers = arrayOf(SolidBarRenderer())
//    private val barRenderers = BarRenderer.mixed(
//        lineThickness = dp * 8F,
//        spacing = dp * 16F,
//    )

    private val bitmapPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val instantPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.FILL }

    private val gridPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = colorCompat(R.color.graph_grid)
        strokeWidth = dp * 1
        style = Paint.Style.STROKE
        textSize = labelTextSize
    }

    private val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
        color = colorCompat(R.color.graph_text)
        style = Paint.Style.FILL
        textSize = labelTextSize
        typeface = Typeface.create("sans-serif-condensed", Typeface.NORMAL)
    }

    private val clearPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.TRANSPARENT
        xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OUT)
    }

    fun setHistory(items: List<Temporal>) {
        if (items.isEmpty()) return

        val renderData = HistoryRenderData(items, primaryColors, secondaryColors)
        scrollableHeight = calculateScrollableHeight(renderData)
        scrollableWidth = calculateScrollableWidth(renderData)
        historyRenderData = renderData
        requestLayout()
    }

    private fun calculateScrollableHeight(renderData: HistoryRenderData): Int {
        return (renderData.data.size * barHeightTotal + (verticalMargin * 2)).toInt()
    }

    private fun calculateScrollableWidth(renderData: HistoryRenderData): Int {
        paddingExtraForLabels = 0
        val minX = renderData.data
            .map { it.getTextStartX(textPaint) }
            .minByOrNull { it } ?: 0
        if (minX < 0) paddingExtraForLabels = (minX.absoluteValue + labelMargin).toInt()

        return xForMonth(renderData.durationMonths + paddingMonths).toInt()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas ?: return
        val c = alphaCanvas ?: return
        val bm = bitmap ?: return

        c.drawColor(0, PorterDuff.Mode.CLEAR)

        val data = historyRenderData ?: return

        val scrollX = this.scrollX.toFloat()
        val scrollY = this.scrollY.toFloat()

        c.withTranslation(-scrollX, 0F) {
            drawGrid(c, data, gridPaint)
        }

        c.withTranslation(-scrollX, -scrollY) {
            drawHistory(c, data)
        }

        canvas.drawBitmap(bm, scrollX, scrollY, bitmapPaint)
    }

    private fun drawHistory(canvas: Canvas, history: HistoryRenderData) {
        val startY = yFor(verticalMargin + (barHeightTotal / 2))

        var n = 0
        history.data.forEach { data ->
            val index = n++


            val y = startY + (index * barHeightTotal)

            data.drawLabel(canvas, y)

            data.data.forEach { item ->
                item.draw(canvas, y, index)
            }
        }
    }

    private fun drawGrid(canvas: Canvas, history: HistoryRenderData, paint: Paint) {
        history.decades.forEach { decade ->
            drawGridline(canvas, "${decade.year}", xForMonth(decade.inEpoch - GRIDLINE_MONTHS_PADDING), paint)
        }
    }

    private fun drawGridline(canvas: Canvas, text: String, x: Float, paint: Paint) {
        val topTextY = paint.textSize
        val bottomTextY = height.toFloat()

        val textWidth = paint.measureText(text)
        val textX = x - (textWidth / 2F)

        canvas.drawSolidText(text, paint,
            textX to topTextY,
            textX to bottomTextY,
        )
        canvas.drawLine(x, verticalMargin, x, height - verticalMargin, paint)
    }

    private fun xForMonth(month: Long): Float = ((paddingMonths + month) * dp * scale) + paddingExtraForLabels
    private fun yFor(targetY: Float): Float = targetY

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        recycle()
        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        this.bitmap = bitmap
        alphaCanvas = Canvas(bitmap)
    }

    /**
     * Release bitmap for garbage collection.
     */
    private fun recycle() {
        if (this.bitmap?.isRecycled == false) {
            this.bitmap?.recycle()
        }
    }

    private inner class HistoryRenderData(
        items: List<Temporal>,
        primaryColors: IntArray,
        secondaryColors: IntArray
    ): Periodic {
        override val start: LocalDate = items.sortedBy { it.startOf() }.first().startOf()
        override val end: LocalDate = items.sortedBy { it.endOf(now) }.last().endOf(now)
        val period: Period = Period.between(start, end)

        val data: List<ItemRenderDataGroup>

        val durationMonths = period.toTotalMonths()
        val decades = decadesBetween(start.minusMonths(GRIDLINE_MONTHS_PADDING), end.plusMonths(GRIDLINE_MONTHS_PADDING))

        init {
            val grouped = items.groupBy {
                when (it) {
                    is Named -> {
                        it.description(context)
                    }
                    else -> "noname"
                }
            }

            var index = 0
            data = grouped.map { (label, items) ->
                index++
                ItemRenderDataGroup(
                    label.clipToLength(LABEL_MAX_LENGTH),
                    items.map { item ->
                        ItemRenderData(item,
                            start,
                            primaryColors.modGet(index),
                            secondaryColors.modGet(index))
                    }
                )
            }.sortedWith(
                compareBy(ItemRenderDataGroup::startInEpoch)
                    .thenByDescending(ItemRenderDataGroup::totalDuration)
                    .thenBy(ItemRenderDataGroup::label)
            )
        }
    }

    private inner class ItemRenderData(item: Temporal, epochStart: LocalDate, val primaryColor: Int, val secondaryColor: Int): Periodic {
        override val start = item.startOf()
        override val end = item.endOf(now)

        // Number of months from the start of this History to the date
        val startInEpoch = Period.between(epochStart, start).toTotalMonths()
        val endInEpoch = Period.between(epochStart, end).toTotalMonths()

        /**
         * [position] is the position of this item in a data list - used for choosing colors/etc.
         */
        fun draw(canvas: Canvas, y: Float, position: Int) {
            if (start == end) {
                drawInstant(canvas, y, position)
            }
            else {
                drawBar(canvas, y, position)
            }
        }

        private fun drawInstant(canvas: Canvas, y: Float, position: Int) {
            canvas.drawCircle(xForMonth(startInEpoch), y, instantRadius, instantPaint.apply { color = primaryColor })
        }

        private fun drawBar(canvas: Canvas, y: Float, position: Int) {
            val startX = xForMonth(startInEpoch)
            val endX = xForMonth(endInEpoch)

            barRenderers.modGet(position).draw(
                canvas,
                startX,
                endX,
                y,
                barThickness,
                primaryColor,
                secondaryColor
            )
        }
    }

    private inner class ItemRenderDataGroup(val label: String, val data: List<ItemRenderData>): Periodic {
        override val start: LocalDate
        override val end: LocalDate

        val startInEpoch: Long
        val endInEpoch: Long

        val totalDuration: Long

        init {
            val first = data.minByOrNull { it.start }!!
            val last = data.maxByOrNull { it.end }!!

            start = first.start
            end = last.end

            startInEpoch = first.startInEpoch
            endInEpoch = last.endInEpoch

            totalDuration = data.map { Period.between(it.start, it.end).toTotalMonths() }.sum()
        }


        fun drawLabel(canvas: Canvas, y: Float) {
            val textWidth = textPaint.measureText(label)
            val textX = when (start == end) {
                true -> xForMonth(startInEpoch) - textWidth - labelMargin - instantRadius
                false -> xForMonth(startInEpoch) - textWidth - labelMargin
            }
            val descent = textPaint.descent()
            val textY = y - ((descent + textPaint.ascent()) / 2)

            // Clear background (e.g. remove gridlines behind text)
            canvas.drawRect(
                textX -  + labelMargin,
                textY - textPaint.textSize + descent - labelMargin,
                textX + textWidth + labelMargin,
                textY + descent + labelMargin,
                clearPaint
            )

            canvas.drawText(
                label,
                textX,
                textY,
                textPaint
            )
        }

        fun getTextStartX(textPaint: Paint): Int {
            val textWidth = textPaint.measureText(label)
            return when (start == end) {
                true -> xForMonth(startInEpoch) - textWidth - labelMargin - instantRadius
                false -> xForMonth(startInEpoch) - textWidth - labelMargin
            }.toInt()
        }
    }
}


internal data class Decade(val year: Int, val inEpoch: Long)

/**
 * Return decades (years where it % 10 == 0) between start and end, for showing helper lines.
 */
private fun decadesBetween(start: LocalDate, end: LocalDate): List<Decade> {
    val firstDecade = start.withYear(start.year.roundUp(10)).withDayOfYear(1)
    val lastDecade = end.withYear(end.year.roundDown(10)).withDayOfYear(1)

    val decadeYears = firstDecade.year .. lastDecade.year step 10
    return decadeYears.map { year ->
        val date = LocalDate.of(year, 1, 1)
        Decade(year, Period.between(start, date).toTotalMonths())
    }
}


private fun Canvas.drawSolidText(text: String, paint: Paint, vararg xy: Pair<Float, Float>) {
    val originalStyle = paint.style
    xy.forEach { (x, y) ->
        drawText(text, x, y, paint.apply { style = Paint.Style.FILL })
    }
    paint.style = originalStyle
}


private fun <T> Array<T>.modGet(index: Int) = this[index % this.size]
private fun IntArray.modGet(index: Int) = this[index % this.size]
