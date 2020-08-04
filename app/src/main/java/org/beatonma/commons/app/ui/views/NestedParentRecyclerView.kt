package org.beatonma.commons.app.ui.views

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.view.NestedScrollingChild
import androidx.core.view.NestedScrollingParent
import androidx.recyclerview.widget.RecyclerView
import org.beatonma.commons.kotlin.extensions.hasAnyFlag

private const val TAG = "NestedParentRv"

/**
 * RecyclerView that implements NestedScrollingParent
 * Scrolling child views may scroll in any direction but it is assumed
 * that this recyclerview only scrolls vertically.
 *
 *
 * Modified from https://medium.com/widgetlabs-engineering/scrollable-nestedscrollviews-inside-recyclerview-ca65050d828a
 *
 * dispatchTouchEvent
 * -> onInterceptTouchEvent
 * -> onStartNestedScroll
 * -> onNestedScrollAccepted
 *  -> dispatchTouchEvent
 *  -> onInterceptTouchEvent
 *  -> onNestedScroll
 * -> onStopNestedScroll
 *
 */
open class NestedParentRecyclerView
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
): RecyclerView(context, attrs, defStyleAttr), NestedScrollingParent {
    private val scrollDirections = intArrayOf(
        View.SCROLL_AXIS_VERTICAL,
        View.SCROLL_AXIS_HORIZONTAL,
    )

    private val scrollIsOngoing: Boolean get() = nestedScrollTarget != null

    private var nestedScrollTarget: View? = null

    // Flags for tracking state across event callbacks
    private var preventTouchInterception: Boolean = false
    private var targetUnableToScroll: Boolean = false
    private var targetConsumedScroll: Boolean = false

    private val rect: Rect = Rect()


    private fun directionSupported(axes: Int): Boolean = axes.hasAnyFlag(*scrollDirections)

    /**
     * super calls [onInterceptTouchEvent], then [onTouchEvent] on child views
     */
    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        // nestedScrollTarget is only set while a scroll is ongoing
        // We should let the target have first refusal of events
        preventTouchInterception = scrollIsOngoing

        var handled = super.dispatchTouchEvent(event)

        if (preventTouchInterception) {
            preventTouchInterception = false
            if (!handled || targetUnableToScroll) {
                handled = super.dispatchTouchEvent(event)
            }
        }

        return handled
    }

    /**
     * Called during super.[dispatchTouchEvent]
     * Return true to prevent child views from seeing event
     */
    override fun onInterceptTouchEvent(e: MotionEvent) =
        !preventTouchInterception && super.onInterceptTouchEvent(e)


    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
        target.getDrawingRect(rect)
        offsetDescendantRectToMyCoords(target, rect)

        // Position of child view inside this parent view.
        val childRelativeTop = rect.top
        val childRelativeBottom = rect.bottom
        consumed[0] = 0
        consumed[1] = 0

        if (childRelativeTop >= 0 && childRelativeBottom < height) {
//            Log.d(TAG, "No pre-scroll $childRelativeTop,$childRelativeBottom (height=$height)")
            return
        }
        else {
//            Log.d(TAG, "super.onNestedPreScroll $childRelativeTop,$childRelativeBottom (height=$height)")
            super.onNestedPreScroll(target, dx, dy, consumed)
        }
    }

    override fun onNestedScroll(target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int) {
        if (target === nestedScrollTarget) {// && !targetConsumedScroll) {
            val wasScrolledX = dxConsumed != 0
            val wasScrolledY = dyConsumed != 0

            if (wasScrolledX || wasScrolledY) {
                targetConsumedScroll = true
                targetUnableToScroll = false
                if (wasScrolledY && dyUnconsumed != 0) {
                    scrollBy(0, dyUnconsumed)
                }
            }
            else {
                val wasUnableToScrollY = dyConsumed == 0 && dyUnconsumed != 0

                if (wasUnableToScrollY) {
                    targetConsumedScroll = false
                    targetUnableToScroll = true
                }
            }
        }
    }

    override fun onStartNestedScroll(child: View, target: View, nestedScrollAxes: Int) : Boolean {
        return child is NestedScrollingChild && directionSupported(nestedScrollAxes)
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int) {
        if (directionSupported(axes)) {
            nestedScrollTarget = child
        }

        super.onNestedScrollAccepted(child, target, axes)
    }

    override fun onStopNestedScroll(child: View) {
        nestedScrollTarget = null
        targetUnableToScroll = false
        targetConsumedScroll = false
    }
}
