package org.beatonma.commons.app.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Point
import android.graphics.Rect
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.OverScroller
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.NestedScrollingChild
import androidx.core.view.NestedScrollingChildHelper
import androidx.core.view.ViewCompat
import org.beatonma.commons.kotlin.extensions.combineFlags
import org.beatonma.commons.kotlin.extensions.displaySize
import org.beatonma.commons.kotlin.extensions.withMeasureSpec

private const val TAG = "ScrollableView"

inline class Coords(val xy: IntArray = intArrayOf(0, 0)) {
    var x: Int
        set(value) { xy[0] = value }
        get() = xy[0]

    var y: Int
        set(value) { xy[1] = value }
        get() = xy[1]

    fun reset() {
        set(0, 0)
    }

    fun set(x: Int, y: Int) {
        this.x = x
        this.y = y
    }

    fun add(x: Int, y: Int) {
        this.x += x
        this.y += y
    }

    fun isEmpty(): Boolean = x == 0 && y == 0

    override fun toString() = "[$x,$y]"
}

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

    protected val maxSize: Point = context.displaySize()

    protected val contentRect = Rect()
    protected open val gestureDetector = GestureDetectorCompat(context, GestureListener())
    protected val scroller = OverScroller(context)

    protected val consumedScroll = Coords()
    protected val unconsumedScroll = Coords()

    // The total size of the view as if it were being rendered all at once on a large display
    protected var scrollableWidth: Int = 0
    protected var scrollableHeight: Int = 0

    protected fun canScrollX(): Boolean = (contentRect.left > 0F || contentRect.right < scrollableWidth)
    protected fun canScrollY(): Boolean = (contentRect.top > 0F || contentRect.bottom < scrollableHeight)
    protected fun canScroll(): Boolean = canScrollX() || canScrollY()

    override fun computeHorizontalScrollRange(): Int = scrollableWidth - width
    override fun computeVerticalScrollRange(): Int = scrollableHeight - height

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event) || super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (scroller.computeScrollOffset()) {
            contentRect.offsetTo(scroller.currX, scroller.currY)
        }
    }

    protected open fun fling(velocityX: Int, velocityY: Int) {
        scroller.forceFinished(true)

        scroller.fling(
            contentRect.left, contentRect.top, velocityX, velocityY,
            0, scrollableWidth - contentRect.width(),
            0, scrollableHeight - contentRect.height(),
            0, 0
        )
    }

    protected fun applyOffsets(x: Int, y: Int) {
        val startX = contentRect.left
        val startY = contentRect.top

        val constrainedX = (contentRect.left + x).coerceAtLeast(0).coerceAtMost((scrollableWidth - contentRect.width()))
        val constrainedY = (contentRect.top + y).coerceAtLeast(0).coerceAtMost((scrollableHeight - contentRect.height()))

        contentRect.offsetTo(constrainedX, constrainedY)

        scrollTo(
            (scrollX + x).coerceAtMost(computeHorizontalScrollRange()).coerceAtLeast(0),
            (scrollY + y).coerceAtMost(computeHorizontalScrollRange()).coerceAtLeast(0)
        )

        consumedScroll.add(constrainedX - startX, constrainedY - startY)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        withMeasureSpec(widthMeasureSpec, heightMeasureSpec) { widthSize, widthMode, heightSize, heightMode ->
            val wSize: Int = when (widthMode) {
                MeasureSpec.EXACTLY -> widthSize
                MeasureSpec.AT_MOST -> scrollableWidth.coerceAtMost(widthSize)
                else -> scrollableWidth.coerceAtMost(maxSize.x)
            }

            val hSize: Int = when (heightMode) {
                MeasureSpec.EXACTLY -> heightSize
                MeasureSpec.AT_MOST -> scrollableHeight.coerceAtMost(heightSize)
                else -> scrollableHeight.coerceAtMost(maxSize.y)
            }

            super.onMeasure(
                MeasureSpec.makeMeasureSpec(wSize, MeasureSpec.AT_MOST),
                MeasureSpec.makeMeasureSpec(hSize, MeasureSpec.AT_MOST)
            )
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        contentRect.set(paddingLeft, paddingTop, width - paddingRight, height - paddingBottom)
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
            consumedScroll.reset()
            unconsumedScroll.reset()

            fun updateUnconsumed() {
                unconsumedScroll.set(dx - consumedScroll.x, dy - consumedScroll.y)
            }

            dispatchNestedPreScroll(dx, dy, consumedScroll.xy, null)
            updateUnconsumed()

            if (canScroll()) {
                applyOffsets(dx, dy)
                updateUnconsumed()

                dispatchNestedScroll(
                    consumedScroll.x, consumedScroll.y,
                    unconsumedScroll.x, unconsumedScroll.y,
                    null
                )
            }

            return true
        }
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
