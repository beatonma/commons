package org.beatonma.commons.app.ui.views

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.OverScroller
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.NestedScrollingChild
import androidx.core.view.NestedScrollingChildHelper
import androidx.core.view.ViewCompat
import org.beatonma.commons.kotlin.data.Dimensions
import org.beatonma.commons.kotlin.extensions.combineFlags
import org.beatonma.commons.kotlin.extensions.displaySize
import org.beatonma.commons.kotlin.extensions.withMeasureSpec

private const val TAG = "ScrollableView"


abstract class ScrollableView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
): View(context, attrs, defStyleAttr, defStyleRes), NestedScrollingChild {

    abstract val nestedScrollHelper: NestedScrollingChildHelper

    protected open val supportedScrollDirections: Int = combineFlags(
        ViewCompat.SCROLL_AXIS_VERTICAL,
        ViewCompat.SCROLL_AXIS_HORIZONTAL,
    )

    protected open val maxSize: Dimensions = context.displaySize()

    protected open val gestureDetector = GestureDetectorCompat(context, GestureListener())
    protected val scroller = OverScroller(context)

    protected val tracker = ScrollTracker()

    // The total size of the view as if it were being rendered all at once on a large display
    protected var scrollableWidth: Int = 0
    protected var scrollableHeight: Int = 0

    protected fun canScrollX(): Boolean = width < scrollableWidth
    protected fun canScrollY(): Boolean = height < scrollableHeight
    protected fun canScroll(): Boolean = canScrollX() || canScrollY()

    protected var horizontalScrollRange: Int = 0
        private set
    protected var verticalScrollRange: Int = 0
        private set

    override fun computeHorizontalScrollRange(): Int = scrollableWidth - width
    override fun computeVerticalScrollRange(): Int = scrollableHeight - height

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.currX, scroller.currY)
        }
    }

    protected open fun fling(velocityX: Int, velocityY: Int) {
        scroller.forceFinished(true)

        scroller.fling(
            scrollX, scrollY,
            velocityX, velocityY,
            0, horizontalScrollRange,
            0, verticalScrollRange,
            0, 0
        )
    }

    protected fun applyScrollOffsets(x: Int, y: Int) {
        tracker.forScroll(x, y)

        val startX = scrollX
        val startY = scrollY

        scrollTo(
            (scrollX + x).coerceAtMost(horizontalScrollRange).coerceAtLeast(0),
            (scrollY + y).coerceAtMost(verticalScrollRange).coerceAtLeast(0)
        )

        tracker.consume(scrollX - startX, scrollY - startY)
    }

    private inner class GestureListener: GestureDetector.SimpleOnGestureListener() {
        override fun onDown(event: MotionEvent?): Boolean {
            scroller.forceFinished(true)

            startNestedScroll(supportedScrollDirections)
            return true
        }

        override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
            fling(-velocityX.toInt(), -velocityY.toInt())
            return true
        }

        override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
            val dx = distanceX.toInt()
            val dy = distanceY.toInt()
            tracker.forScroll(dx, dy)
            dispatchNestedPreScroll(dx, dy, tracker.consumed, null)

            if (canScroll()) {
                val unconsumedByPreScroll = tracker.unconsumed
                applyScrollOffsets(unconsumedByPreScroll[0], unconsumedByPreScroll[1])

                val consumed = tracker.consumed
                val unconsumed = tracker.unconsumed

                dispatchNestedScroll(
                    consumed[0], consumed[1],
                    unconsumed[0], unconsumed[1],
                    null
                )
            }

            return true
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        withMeasureSpec(widthMeasureSpec, heightMeasureSpec) { widthSize, widthMode, heightSize, heightMode ->
            val wSize: Int = when (widthMode) {
                MeasureSpec.EXACTLY -> widthSize
                MeasureSpec.AT_MOST -> scrollableWidth.coerceAtMost(widthSize)
                else -> scrollableWidth.coerceAtMost(maxSize.width)
            }

            val hSize: Int = when (heightMode) {
                MeasureSpec.EXACTLY -> heightSize
                MeasureSpec.AT_MOST -> scrollableHeight.coerceAtMost(heightSize)
                else -> scrollableHeight.coerceAtMost(maxSize.height)
            }

            super.onMeasure(
                MeasureSpec.makeMeasureSpec(wSize, MeasureSpec.AT_MOST),
                MeasureSpec.makeMeasureSpec(hSize, MeasureSpec.AT_MOST)
            )
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        calculateScrollRangeX()
        calculateScrollRangeY()
    }

    protected fun calculateScrollRangeX() {
        horizontalScrollRange = (scrollableWidth - width).coerceAtLeast(0)
    }

    protected fun calculateScrollRangeY() {
        verticalScrollRange = (scrollableHeight - height).coerceAtLeast(0)
    }

    /**
     * NESTED SCROLLING: Delegate methods
     */
    override fun setNestedScrollingEnabled(enabled: Boolean) {
        nestedScrollHelper.isNestedScrollingEnabled = enabled
    }

    override fun isNestedScrollingEnabled(): Boolean {
        return nestedScrollHelper.isNestedScrollingEnabled
    }

    override fun hasNestedScrollingParent(): Boolean {
        return nestedScrollHelper.hasNestedScrollingParent()
    }

    override fun startNestedScroll(axes: Int): Boolean {
        return nestedScrollHelper.startNestedScroll(axes)
    }

    override fun stopNestedScroll() {
        return nestedScrollHelper.stopNestedScroll()
    }

    override fun dispatchNestedPreScroll(
        dx: Int,
        dy: Int,
        consumed: IntArray?,
        offsetInWindow: IntArray?,
    ): Boolean {
        return nestedScrollHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow)
    }

    override fun dispatchNestedScroll(
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        offsetInWindow: IntArray?,
    ): Boolean {
        return nestedScrollHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow)
    }

    override fun dispatchNestedPreFling(velocityX: Float, velocityY: Float): Boolean {
        return nestedScrollHelper.dispatchNestedPreFling(velocityX, velocityY)
    }

    override fun dispatchNestedFling(
        velocityX: Float,
        velocityY: Float,
        consumed: Boolean,
    ): Boolean {
        return nestedScrollHelper.dispatchNestedFling(velocityX, velocityY, consumed)
    }


    /**
     * End of NESTED SCROLLING
     */
}


class ScrollTracker {
    val completeScroll = intArrayOf(0, 0)
    val consumed = intArrayOf(0, 0)
    private val _unconsumed = intArrayOf(0, 0)
    val unconsumed: IntArray get() {
        _unconsumed[0] = completeScroll[0] - consumed[0]
        _unconsumed[1] = completeScroll[1] - consumed[1]
        return _unconsumed
    }

    fun forScroll(dx: Int, dy: Int) {
        completeScroll.setXY(dx, dy)
        consumed.setXY(0, 0)
    }

    fun consume(dx: Int, dy: Int) {
        consumed[0] += dx
        consumed[1] += dy
    }

    private fun IntArray.setXY(x: Int, y: Int) {
        this[0] = x
        this[1] = y
    }
}
