package org.beatonma.commons.app.ui.views

import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.util.AttributeSet
import androidx.core.graphics.withTranslation
import androidx.core.view.NestedScrollingChildHelper
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.views.renderer.BarRenderer
import org.beatonma.commons.data.core.interfaces.Named
import org.beatonma.commons.data.core.interfaces.Periodic
import org.beatonma.commons.data.core.interfaces.Temporal
import org.beatonma.commons.data.resolution.description
import org.beatonma.commons.kotlin.extensions.*
import java.time.LocalDate
import java.time.Period

private const val TAG = "HistoryView"

private const val LABEL_MAX_LENGTH = 31
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

    private var bitmap: Bitmap? = null
    private var alphaCanvas: Canvas? = null

    private val primaryColors = context.resources.getIntArray(R.array.colors_graph_primary)
    private val secondaryColors = context.resources.getIntArray(R.array.colors_graph_secondary)

    private var historyRenderData: HistoryRenderData? = null

    private val barRenderers = BarRenderer.mixed(
        lineThickness = dp * 8F,
        spacing = dp * 16F,
    )

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
        val renderData = HistoryRenderData(items, primaryColors, secondaryColors)
        scrollableHeight = renderData.data.size * barHeightTotal + (verticalMargin * 2)
        scrollableWidth = xForMonth(renderData.durationMonths + paddingMonths)
        historyRenderData = renderData
        requestLayout()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas ?: return
        val c = alphaCanvas ?: return
        val bm = bitmap ?: return

        c.drawColor(0, PorterDuff.Mode.CLEAR)

        val data = historyRenderData ?: return

        c.withTranslation(-contentRect.left.toFloat(), -contentRect.top.toFloat()) {
            drawGrid(c, data, gridPaint)
            drawHistory(c, data)
        }

        canvas.drawBitmap(bm, 0F, 0F, bitmapPaint)
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
        val bottomTextY = scrollableHeight

        val textWidth = paint.measureText(text)
        val textX = x - (textWidth / 2F)

        canvas.drawSolidText(text, paint,
            textX to topTextY,
            textX to bottomTextY,
        )
        canvas.drawLine(x, verticalMargin, x, scrollableHeight - verticalMargin, paint)
    }

    private fun xForMonth(month: Long): Float = ((paddingMonths + month) * dp * scale)
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
            val first = data.minBy { it.start }!!
            val last = data.maxBy { it.end }!!

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







//package org.beatonma.commons.app.ui.views
//
//import android.content.Context
//import android.graphics.*
//import android.text.TextPaint
//import android.util.AttributeSet
//import android.util.Log
//import android.view.GestureDetector
//import android.view.MotionEvent
//import android.view.View
//import android.widget.OverScroller
//import androidx.core.view.*
//import org.beatonma.commons.R
//import org.beatonma.commons.app.ui.views.renderer.BarRenderer
//import org.beatonma.commons.data.core.interfaces.Named
//import org.beatonma.commons.data.core.interfaces.Periodic
//import org.beatonma.commons.data.core.interfaces.Temporal
//import org.beatonma.commons.data.resolution.description
//import org.beatonma.commons.kotlin.extensions.*
//import java.time.LocalDate
//import java.time.Period
//
//private const val TAG = "HistoryView"
//
//private const val LABEL_MAX_LENGTH = 31
//private const val PADDING_YEARS = 10L
//
//// Only show gridlines before/after timeline start/end if they fall within this number of years of the timeline
//private const val GRIDLINE_MONTHS_PADDING = 12L * 5L
//
//
///**
// * The timeline chooses a zero-point from which all events are measured.
// * We refer to the period represented by the complete timeline as an 'epoch' and all
// * measurements within that epoch are denoted as the number of months between 'zero' and the event.
// * This allows us to easily calculate the x-position of events on the timeline.
// */
//class TimelineView @JvmOverloads constructor(
//    context: Context,
//    attrs: AttributeSet? = null,
//    defStyleAttr: Int = 0,
//    defStyleRes: Int = 0,
//): View(context, attrs, defStyleAttr, defStyleRes), NestedScrollingChild {
//
//    private val nestedScrollHelper: NestedScrollingChildHelper = NestedScrollingChildHelper(this).apply {
//        isNestedScrollingEnabled = true
//    }
//
//    private val now: LocalDate = LocalDate.now()
//    private val dp: Float = context.dp(1F)
//    private val maxSize: Point = context.displaySize().apply { y = (200 * dp).toInt() }
//
//    private val scale: Float = 1.5F  // Density-independent pixels per month
//    private val barThickness = 24 * dp
//    private val barSpacing = 16 * dp // Between bars
//    private val barHeightTotal = barThickness + barSpacing
//    private val instantRadius = barThickness / 3
//
//    private val labelTextSize = context.dimenCompat(R.dimen.text_size_label).toFloat()
//    private val halfLabelTextSize = labelTextSize / 2F
//    private val labelMargin = halfLabelTextSize
//
//    // Horizontal padding before/after graph bars, expressed in number of months
//    private val paddingMonths = 12L * PADDING_YEARS
//    private val verticalMargin: Float = labelTextSize * 1.5F
//
//    // Total height of all bars with [verticalMargin] above and below for labels/chrome
//    private var scrollableHeight: Float = 0F
//
//    // Total width required to show the entire timeline with [paddingMonths] before and after
//    private var scrollableWidth: Float = 0F
//
//    //    private val bitmapSize = intArrayOf(1, 1)
//    private var bitmap: Bitmap? = null
//    private var alphaCanvas: Canvas? = null
//
//    private val primaryColors = context.resources.getIntArray(R.array.colors_graph_primary)
//    private val secondaryColors = context.resources.getIntArray(R.array.colors_graph_secondary)
//
//    private var historyRenderData: HistoryRenderData? = null
//
//    private val consumedScroll = intArrayOf(0, 0)
//    private val unconsumedScroll = intArrayOf(0, 0)
//
//    /**
//     * Touch/pan/scroll handling
//     */
//    // Available render space with scroll offsets
//    private val contentRect = Rect()
//    private val gestureDetector = GestureDetectorCompat(context, GestureListener())
//    private val scroller = OverScroller(context)
//
//    private val barRenderers = BarRenderer.mixed(
//        lineThickness = dp * 8F,
//        spacing = dp * 16F,
//    )
//
//    private val bitmapPaint = Paint(Paint.ANTI_ALIAS_FLAG)
//    private val instantPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.FILL }
//
//    private val gridPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
//        color = colorCompat(R.color.graph_grid)
//        strokeWidth = dp * 1
//        style = Paint.Style.STROKE
//        textSize = labelTextSize
//    }
//
//    private val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
//        color = colorCompat(R.color.graph_text)
//        style = Paint.Style.FILL
//        textSize = labelTextSize
//        typeface = Typeface.create("sans-serif-condensed", Typeface.NORMAL)
//    }
//
//    private val clearPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
//        color = Color.TRANSPARENT
//        xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OUT)
//    }
//
//    private val debugPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
//        color = context.colorCompat(R.color.Debug)
//        style = Paint.Style.FILL
//    }
//
//    fun setHistory(items: List<Temporal>) {
//        val renderData = HistoryRenderData(items, primaryColors, secondaryColors)
//        scrollableHeight = renderData.data.size * barHeightTotal + (verticalMargin * 2)
//        scrollableWidth = xForMonth(renderData.durationMonths + paddingMonths)
//        historyRenderData = renderData
//        requestLayout()
//    }
//
//    override fun onDraw(canvas: Canvas?) {
////        org.beatonma.commons.debug.meanBenchmark(60, "onDraw") {
//        canvas ?: return
//        val c = alphaCanvas ?: return
//        val bm = bitmap ?: return
//
//        c.drawColor(0, PorterDuff.Mode.CLEAR)
//
//        val data = historyRenderData ?: return
//
//        drawGrid(c, data, gridPaint)
//        drawHistory(c, data)
//
//        canvas.drawBitmap(bm, 0F, 0F, bitmapPaint)
////        }
//        canvas.drawRect(contentRect, debugPaint)
//    }
//
//    private fun drawHistory(canvas: Canvas, history: HistoryRenderData) {
//        val startY = yFor(verticalMargin + (barHeightTotal / 2))
//
//        var n = 0
//        history.data.forEach { data ->
//            val index = n++
//
//
//            val y = startY + (index * barHeightTotal)
//
//            data.drawLabel(canvas, y)
//
//            data.data.forEach { item ->
//                item.draw(canvas, y, index)
//            }
//        }
//    }
//
//    private fun drawGrid(canvas: Canvas, history: HistoryRenderData, paint: Paint) {
//        history.decades.forEach { decade ->
//            drawGridline(canvas, "${decade.year}", xForMonth(decade.inEpoch - GRIDLINE_MONTHS_PADDING), paint)
//        }
//    }
//
//    private fun drawGridline(canvas: Canvas, text: String, x: Float, paint: Paint) {
//        val topTextY = paint.textSize
//        val bottomTextY = scrollableHeight
//
//        val textWidth = paint.measureText(text)
//        val textX = x - (textWidth / 2F)
//
//        canvas.drawSolidText(text, paint,
//            textX to topTextY,
//            textX to bottomTextY,
//        )
//        canvas.drawLine(x, verticalMargin, x, scrollableHeight - verticalMargin, paint)
//    }
//
//    private fun xForMonth(month: Long): Float = ((paddingMonths + month) * dp * scale) - contentRect.left
//    private fun yFor(targetY: Float): Float = targetY - contentRect.top
//    private fun yFor(targetY: Int): Float = (targetY - contentRect.top).toFloat()
//
//
//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        withMeasureSpec(widthMeasureSpec, heightMeasureSpec) { widthSize, widthMode, heightSize, heightMode ->
//            val hSize: Int = when (heightMode) {
//                MeasureSpec.EXACTLY -> heightSize.dump("EXACTLY")
//                MeasureSpec.AT_MOST -> scrollableHeight.toInt().coerceAtMost(heightSize).dump("AT_MOST")
//                else -> scrollableHeight.toInt().coerceAtMost(maxSize.y).dump("ELSE")
//            }
////            val wSize: Int = when (widthMode) {
////                MeasureSpec.EXACTLY -> widthSize
////                MeasureSpec.AT_MOST -> scrollableWidth.toInt().coerceAtMost(widthSize)
////                else -> scrollableWidth.toInt()
////            }
//
//            super.onMeasure(
//                widthMeasureSpec,
//                MeasureSpec.makeMeasureSpec(hSize, MeasureSpec.AT_MOST)
//            )
//        }
//    }
//
//    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
//        super.onSizeChanged(w, h, oldw, oldh)
//
//        contentRect.set(paddingLeft, paddingTop, width - paddingRight, height - paddingBottom)
//
//        recycle()
//        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
//        this.bitmap = bitmap
//        alphaCanvas = Canvas(bitmap)
//    }
//
//    override fun onTouchEvent(event: MotionEvent?): Boolean {
//        when (event?.actionMasked) {
//            MotionEvent.ACTION_CANCEL -> stopNestedScroll()
//        }
//
//        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event)
//    }
//
////    override fun onDetachedFromWindow() {
////        super.onDetachedFromWindow()
//////        recycle()
////    }
//
//    /**
//     * Release bitmap for garbage collection.
//     */
//    private fun recycle() {
//        if (this.bitmap?.isRecycled == false) {
//            this.bitmap?.recycle()
//        }
//    }
//
//    private inner class HistoryRenderData(
//        items: List<Temporal>,
//        primaryColors: IntArray,
//        secondaryColors: IntArray
//    ): Periodic {
//        override val start: LocalDate = items.sortedBy { it.startOf() }.first().startOf()
//        override val end: LocalDate = items.sortedBy { it.endOf(now) }.last().endOf(now)
//        val period: Period = Period.between(start, end)
//
//        val data: List<ItemRenderDataGroup>
//
//        val durationMonths = period.toTotalMonths()
//        val decades = decadesBetween(start.minusMonths(GRIDLINE_MONTHS_PADDING), end.plusMonths(GRIDLINE_MONTHS_PADDING))
//
//        init {
//            val grouped = items.groupBy {
//                when (it) {
//                    is Named -> {
//                        it.description(context)
//                    }
//                    else -> "noname"
//                }
//            }
//
//            var index = 0
//            data = grouped.map { (label, items) ->
//                index++
//                ItemRenderDataGroup(
//                    label.clipToLength(LABEL_MAX_LENGTH),
//                    items.map { item ->
//                        ItemRenderData(item,
//                            start,
//                            primaryColors.modGet(index),
//                            secondaryColors.modGet(index))
//                    }
//                )
//            }.sortedWith(
//                compareBy(ItemRenderDataGroup::startInEpoch)
//                    .thenByDescending(ItemRenderDataGroup::totalDuration)
//                    .thenBy(ItemRenderDataGroup::label)
//            )
//        }
//    }
//
//    private inner class ItemRenderData(item: Temporal, epochStart: LocalDate, val primaryColor: Int, val secondaryColor: Int): Periodic {
//        override val start = item.startOf()
//        override val end = item.endOf(now)
//
//        // Number of months from the start of this History to the date
//        val startInEpoch = Period.between(epochStart, start).toTotalMonths()
//        val endInEpoch = Period.between(epochStart, end).toTotalMonths()
//
//        /**
//         * [position] is the position of this item in a data list - used for choosing colors/etc.
//         */
//        fun draw(canvas: Canvas, y: Float, position: Int) {
//            if (start == end) {
//                drawInstant(canvas, y, position)
//            }
//            else {
//                drawBar(canvas, y, position)
//            }
//        }
//
//        private fun drawInstant(canvas: Canvas, y: Float, position: Int) {
//            canvas.drawCircle(xForMonth(startInEpoch), y, instantRadius, instantPaint.apply { color = primaryColor })
//        }
//
//        private fun drawBar(canvas: Canvas, y: Float, position: Int) {
//            val startX = xForMonth(startInEpoch)
//            val endX = xForMonth(endInEpoch)
//
//            barRenderers.modGet(position).draw(
//                canvas,
//                startX,
//                endX,
//                y,
//                barThickness,
//                primaryColor,
//                secondaryColor
//            )
//        }
//    }
//
//    private inner class ItemRenderDataGroup(val label: String, val data: List<ItemRenderData>): Periodic {
//        override val start: LocalDate
//        override val end: LocalDate
//
//        val startInEpoch: Long
//        val endInEpoch: Long
//
//        val totalDuration: Long
//
//        init {
//            val first = data.minBy { it.start }!!
//            val last = data.maxBy { it.end }!!
//
//            start = first.start
//            end = last.end
//
//            startInEpoch = first.startInEpoch
//            endInEpoch = last.endInEpoch
//
//            totalDuration = data.map { Period.between(it.start, it.end).toTotalMonths() }.sum()
//        }
//
//
//        fun drawLabel(canvas: Canvas, y: Float) {
//            val textWidth = textPaint.measureText(label)
//            val textX = when (start == end) {
//                true -> xForMonth(startInEpoch) - textWidth - labelMargin - instantRadius
//                false -> xForMonth(startInEpoch) - textWidth - labelMargin
//            }
//            val descent = textPaint.descent()
//            val textY = y - ((descent + textPaint.ascent()) / 2)
//
//            // Clear background (e.g. remove gridlines behind text)
//            canvas.drawRect(
//                textX -  + labelMargin,
//                textY - textPaint.textSize + descent - labelMargin,
//                textX + textWidth + labelMargin,
//                textY + descent + labelMargin,
//                clearPaint
//            )
//
//            canvas.drawText(
//                label,
//                textX,
//                textY,
//                textPaint
//            )
//        }
//    }
//
//    private inner class GestureListener: GestureDetector.SimpleOnGestureListener() {
//        override fun onDown(event: MotionEvent?): Boolean {
//            scroller.forceFinished(true)
//            ViewCompat.postInvalidateOnAnimation(this@TimelineView)
//
//            startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL and ViewCompat.SCROLL_AXIS_HORIZONTAL).dump("startNestedScroll")
//            parent.requestDisallowInterceptTouchEvent(true)
//            return true
//        }
//
//        override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
//            fling(-velocityX.toInt(), -velocityY.toInt())
//            return true
//        }
//
//        override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
//            val dx = distanceX.toInt()
//            val dy = distanceY.toInt()
//            consumedScroll.reset()
//            unconsumedScroll.reset()
//
//            fun updateUnconsumed() {
//                unconsumedScroll[0] = dx - consumedScroll[0]
//                unconsumedScroll[1] = dy - consumedScroll[1]
////                val ok = dx == (unconsumedScroll[0] + consumedScroll[0]) && dy == (unconsumedScroll[1] + consumedScroll[1])
////
////                Log.d(TAG, "ok=$ok scroll[$dx,$dy] consumed:${consumedScroll.str()}, unconsumed: ${unconsumedScroll.str()}, closerToZero:${unconsumedScroll.closerToZeroThan(consumedScroll)}")
//
////                Log.d(TAG, "ok=$ok scroll [$dx,$dy] consumed:[${consumedScroll[0]},${consumedScroll[1]}, unconsumed:[${unconsumedScroll[0]},${unconsumedScroll[1]}]")
//            }
//
//            val consumedByParent = dispatchNestedPreScroll(dx, dy, consumedScroll, null)
//            updateUnconsumed()
//            logConsumed(dx, dy, consumedScroll, unconsumedScroll, "afterDispatchPre")
//
//            var invalidateRequired = false
//
//            if (canScroll()) {
//                invalidateRequired = true
////                if (consumedByParent) {
////                    applyOffsets(unconsumedScroll[0], unconsumedScroll[1], consumedScroll)
////                }
////                else {
//                applyOffsets(dx, dy)
////                }
//                updateUnconsumed()
//                logConsumed(dx, dy, consumedScroll, unconsumedScroll, "afterApplyOffsets")
//
//                dispatchNestedScroll(
//                    consumedScroll[0], consumedScroll[1],
//                    unconsumedScroll[0], unconsumedScroll[1],
//                    null
//                )
//                logConsumed(dx, dy, consumedScroll, unconsumedScroll, "afterDispatchScroll")
//            }
//
//            if (invalidateRequired) ViewCompat.postInvalidateOnAnimation(this@TimelineView)
//
//            return true
//        }
//
//
//    }
//
//    private fun canScrollX(): Boolean = (contentRect.left > 0F || contentRect.right < scrollableWidth)
//    private fun canScrollY(): Boolean = (contentRect.top > 0F || contentRect.bottom < scrollableHeight)
//    private fun canScroll(): Boolean = canScrollX() || canScrollY()
//
//    private fun fling(velocityX: Int, velocityY: Int) {
//        Log.d(TAG, "onFling")
////        Log.d(TAG, "fling $velocityX $velocityY")
////        scroller.forceFinished(true)
////
////        scroller.fling(
////            contentRect.left, contentRect.top, velocityX, velocityY,
////            0, scrollableWidth.toInt(),
////            0, scrollableHeight.toInt(),
////            0, 0
////        )
////        ViewCompat.postInvalidateOnAnimation(this)
//    }
//
//    private fun applyOffsets(x: Int, y: Int) {
//        val startX = contentRect.left
//        val startY = contentRect.top
//
//        val constrainedX = (contentRect.left + x).coerceAtLeast(0).coerceAtMost((scrollableWidth - contentRect.width()).toInt())
//        val constrainedY = (contentRect.top + y).coerceAtLeast(0).coerceAtMost((scrollableHeight - contentRect.height()).toInt())
//
//        contentRect.offsetTo(constrainedX, constrainedY)
//
//        consumedScroll[0] += constrainedX - startX
//        consumedScroll[1] += constrainedY - startY
//    }
//
//    /**
//     * NESTED SCROLLING
//     */
//    override fun setNestedScrollingEnabled(enabled: Boolean) {
//        nestedScrollHelper.isNestedScrollingEnabled = enabled.dump("setNestedScrollingEnabled")
//    }
//
//    override fun isNestedScrollingEnabled(): Boolean {
//        return nestedScrollHelper.isNestedScrollingEnabled.dump("isNestedScrollingEnabled")
//    }
//
//    override fun hasNestedScrollingParent(): Boolean {
//        return nestedScrollHelper.hasNestedScrollingParent().dump("hasNestedScrollingParent")
//    }
//
//    override fun startNestedScroll(axes: Int): Boolean {
//        return nestedScrollHelper.startNestedScroll(axes).dump("startNestedScroll")
//    }
//
//    override fun stopNestedScroll() {
//        return nestedScrollHelper.stopNestedScroll().dump("stopNestedScroll")
//    }
//
//    override fun dispatchNestedPreScroll(
//        dx: Int,
//        dy: Int,
//        consumed: IntArray?,
//        offsetInWindow: IntArray?,
//    ): Boolean {
//        return nestedScrollHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow).dump("dispatchNestedPreScroll")
//    }
//
//    override fun dispatchNestedScroll(
//        dxConsumed: Int,
//        dyConsumed: Int,
//        dxUnconsumed: Int,
//        dyUnconsumed: Int,
//        offsetInWindow: IntArray?,
//    ): Boolean {
//        return nestedScrollHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow).dump("dispatchNestedScroll")
//    }
//
//    override fun dispatchNestedPreFling(velocityX: Float, velocityY: Float): Boolean {
//        return nestedScrollHelper.dispatchNestedPreFling(velocityX, velocityY).dump("dispatchNestedPreFling")
//    }
//
//    override fun dispatchNestedFling(
//        velocityX: Float,
//        velocityY: Float,
//        consumed: Boolean,
//    ): Boolean {
//        return nestedScrollHelper.dispatchNestedFling(velocityX, velocityY, consumed).dump("dispatchNestedFling")
//    }
//
//
//    /**
//     * End of NESTED SCROLLING
//     */
//}
//
//
//internal data class Decade(val year: Int, val inEpoch: Long)
///**
// * Return decades (years where it % 10 == 0) between start and end, for showing helper lines.
// */
//private fun decadesBetween(start: LocalDate, end: LocalDate): List<Decade> {
//    val firstDecade = start.withYear(start.year.roundUp(10)).withDayOfYear(1)
//    val lastDecade = end.withYear(end.year.roundDown(10)).withDayOfYear(1)
//
//    val decadeYears = firstDecade.year .. lastDecade.year step 10
//    return decadeYears.map { year ->
//        val date = LocalDate.of(year, 1, 1)
//        Decade(year, Period.between(start, date).toTotalMonths())
//    }
//}
//
//
//private fun Canvas.drawSolidText(text: String, paint: Paint, vararg xy: Pair<Float, Float>) {
//    val originalStyle = paint.style
//    xy.forEach { (x, y) ->
//        drawText(text, x, y, paint.apply { style = Paint.Style.FILL })
//    }
//    paint.style = originalStyle
//}
//
//
//private fun <T> Array<T>.modGet(index: Int) = this[index % this.size]
//private fun IntArray.modGet(index: Int) = this[index % this.size]
//
//// Debug helpers
//private fun IntArray.str() = "[${this[0]}, ${this[1]}]"
//private fun logConsumed(dx: Int, dy: Int, consumedScroll: IntArray, unconsumedScroll: IntArray, message: String) {
//    val ok = dx == (unconsumedScroll[0] + consumedScroll[0]) && dy == (unconsumedScroll[1] + consumedScroll[1])
//    if (!ok) throw Exception("Scroll inconsistency detected [$dx, $dy] != ${consumedScroll.str()} + ${unconsumedScroll.str()}")
//
//    Log.d(TAG, "($message) scroll[$dx,$dy] consumed:${consumedScroll.str()}, unconsumed: ${unconsumedScroll.str()}")
//}
//private fun IntArray.reset() {
//    this[0] = 0
//    this[1] = 0
//}
