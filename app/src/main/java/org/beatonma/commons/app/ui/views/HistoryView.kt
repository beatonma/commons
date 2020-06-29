package org.beatonma.commons.app.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import org.beatonma.commons.R
import org.beatonma.commons.app.memberprofile.HistoryItem
import org.beatonma.commons.app.ui.views.renderer.BarRenderer
import org.beatonma.commons.data.core.interfaces.Named
import org.beatonma.commons.data.core.interfaces.Periodic
import org.beatonma.commons.kotlin.extensions.*
import java.time.LocalDate
import java.time.Period

private const val TAG = "HistoryView"

class HistoryView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
): View(context, attrs, defStyleAttr, defStyleRes) {
    private val now: LocalDate = LocalDate.now()

    private val dp: Float = context.dp(1F)
    private val scale: Float = 1.5F  // Density-independent pixels per month
    private val barThickness = 24 * dp
    private val barSpacing = 16 * dp // Between bars
    private val barHeightTotal = barThickness + barSpacing

    private val labelTextSize = context.dimenCompat(R.dimen.text_size_label).toFloat()
    private val halfLabelTextSize = labelTextSize / 2F

    // Horizontal padding before/after graph bars, expressed in number of months
    private val paddingMonths = 12L * 5L
    private val verticalMargin: Float = labelTextSize * 1.5F

    // Total height of all bars with [verticalMargin] above and below for labels/chrome
    private var requiredHeight: Float = 0F

    // Total width required to show the entire timeline with [paddingMonths] before and after
    private var requiredWidth: Float = 0F


    private val colors = context.resources.getIntArray(R.array.colors_graph)

    private var history: History? = null

    private var historyRenderData: HistoryRenderData? = null

    private val barRenderers = BarRenderer.striped(
        lineThickness = dp * 1F,
        spacing = dp * 8F,
    )

    private val gridPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = colorCompat(R.color.graph_grid)
        strokeWidth = dp * 2
        style = Paint.Style.STROKE
        textSize = labelTextSize
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = colorCompat(R.color.graph_text)
        style = Paint.Style.FILL
        textSize = labelTextSize
    }

    fun setHistory(items: List<HistoryItem<*>>) {
        val history = History(items)
        val renderData = HistoryRenderData(history, colors)
        requiredHeight = history.items.size * barHeightTotal + (verticalMargin * 2)
        requiredWidth = xForMonth(renderData.durationMonths + paddingMonths)
        historyRenderData = renderData
        requestLayout()
    }

    override fun onDraw(canvas: Canvas?) {
        canvas ?: return
        val data = historyRenderData ?: return

        drawGrid(canvas, data, gridPaint)
        drawHistory(canvas, data)
    }

    private fun drawHistory(canvas: Canvas, history: HistoryRenderData) {
        val startY = verticalMargin + (barHeightTotal / 2)

        var n = 0
        history.data.forEach { data ->
            val index = n++

            val y = startY + (index * barHeightTotal)

            data.data.forEach { item ->
                val startX = xForMonth(item.startInEpoch)
                val endX = xForMonth(item.endInEpoch)
                barRenderers.modGet(index).draw(
                    canvas,
                    startX,
                    endX,
                    y,
                    barThickness,
                    item.color
                )
            }
            canvas.drawText(
                data.label,
                xForMonth(data.startInEpoch) - textPaint.measureText(data.label),
                y + halfLabelTextSize,
                textPaint
            )
        }
    }

    private fun drawGrid(canvas: Canvas, history: HistoryRenderData, paint: Paint) {

        drawGridline(canvas, "${history.start.year}", xForMonth(0), paint)

        history.decades.forEach { decade ->
            drawGridline(canvas, "${decade.year}", xForMonth(decade.inEpoch), paint)
        }

        if (history.end != now) {
            drawGridline(canvas, history.end.formatted(default = "-", today = now), xForMonth(history.durationMonths), paint)
        }
    }

    private fun drawGridline(canvas: Canvas, text: String, x: Float, paint: Paint) {
        val topTextY = paint.textSize
        val bottomTextY = requiredHeight

        val textWidth = paint.measureText(text)
        val textX = x - (textWidth / 2F)

        canvas.drawSolidText(text, paint,
            textX to topTextY,
            textX to bottomTextY,
        )
        canvas.drawLine(x, verticalMargin, x, requiredHeight - verticalMargin, paint)
    }

    private fun xForMonth(month: Long): Float = ((paddingMonths + month) * dp * scale)

    private class HistoryRenderData(history: History, colors: IntArray): Periodic {
        val data: List<ItemRenderDataGroup>
        val durationMonths = history.period.toTotalMonths()
        val decades = decades(history.start, history.end)
        override val start = history.start
        override val end = history.end

        init {
            val grouped = history.items.groupBy {
                when (it.item) {
                    is Named -> it.item.name
                    else -> "noname"
                }
            }

            var index = 0
            data = grouped.map { (label, items) ->
                index++
                    ItemRenderDataGroup(
                        label,
                        items.map { item ->
                            ItemRenderData(item, history, colors.modGet(index))
                        }
                    )
            }.sortedByDescending { it.totalDuration }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        withMeasureSpec(widthMeasureSpec, heightMeasureSpec) { widthSize, widthMode, heightSize, heightMode ->
            val hSize: Int = when (heightMode) {
                MeasureSpec.EXACTLY -> heightSize
                MeasureSpec.AT_MOST -> requiredHeight.toInt().coerceAtMost(heightSize)
                else -> requiredHeight.toInt()
            }
            val wSize: Int = when (widthMode) {
                MeasureSpec.EXACTLY -> widthSize
                MeasureSpec.AT_MOST -> requiredWidth.toInt().coerceAtMost(widthSize)
                else -> requiredWidth.toInt()
            }

            super.onMeasure(
                MeasureSpec.makeMeasureSpec(wSize, MeasureSpec.AT_MOST),
                MeasureSpec.makeMeasureSpec(hSize, MeasureSpec.AT_MOST)
            )
        }
    }
}



private class History(
    val items: List<HistoryItem<*>> = listOf(),
): Periodic {
    override val start: LocalDate = items.sortedBy { it.start }.first().start
    override val end: LocalDate = items.sortedBy { it.end }.last().end
    val period: Period = Period.between(start, end)

    fun isEmpty() = items.isEmpty()

    override fun toString(): String {
        return "History(items=${items.size}, start=$start, end=$end, period=$period)"
    }
}


private class ItemRenderDataGroup(val label: String, val data: List<ItemRenderData>): Periodic {
    override val start: LocalDate
    override val end: LocalDate

    val startInEpoch: Long
    val endInEpoch: Long

    val totalDuration: Long

    init {
        val first = data.minBy { it.start }!!
        val last = data.maxBy { it.end }!!

        start = first.start
        end = last.end

        startInEpoch = first.startInEpoch
        endInEpoch = last.endInEpoch

        totalDuration = data.map { Period.between(it.start, it.end).toTotalMonths() }.sum()
    }
}

private class ItemRenderData(item: HistoryItem<*>, history: History, val color: Int): Periodic {
    override val start = item.start
    override val end = item.end

    // Number of months from the start of this History to the date
    val startInEpoch = Period.between(history.start, start).toTotalMonths()
    val endInEpoch = Period.between(history.start, end).toTotalMonths()

    init {
        Log.i(TAG, "${if (item.item is Named) item.item.name else "noname"}: ${item.start}=$startInEpoch -> $endInEpoch=${item.end}")
    }
}


/**
 * Return decades (years where it % 10 == 0) between start and end, for showing helper lines.
 */
private fun decades(start: LocalDate, end: LocalDate): List<Decade> {
    val firstDecade = start.withYear(start.year.roundUp(10)).withDayOfYear(1)
    val lastDecade = end.withYear(end.year.roundDown(10)).withDayOfYear(1)

    val decadeYears = firstDecade.year .. lastDecade.year step 10
    return decadeYears.map { year ->
        val date = LocalDate.of(year, 1, 1).dump()
        Decade(year, Period.between(start, date).toTotalMonths())
    }
}


internal data class Decade(val year: Int, val inEpoch: Long)


private fun Canvas.drawSolidText(text: String, paint: Paint, vararg xy: Pair<Float, Float>) {
    val originalStyle = paint.style
    xy.forEach { (x, y) ->
        drawText(text, x, y, paint.apply { style = Paint.Style.FILL })
    }
    paint.style = originalStyle
}


private fun <T> Array<T>.modGet(index: Int) = this[index % this.size]
private fun IntArray.modGet(index: Int) = this[index % this.size]


