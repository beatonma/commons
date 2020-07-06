package org.beatonma.commons.app.ui.views

import android.content.Context
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

    private var nestedScrollTarget: View? = null
    private var nestedScrollTargetWasUnableToScroll = false
    private var skipsTouchInterception = false

    private fun directionSupported(axes: Int): Boolean = axes.hasAnyFlag(*scrollDirections)

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val temporarilySkipsInterception = nestedScrollTarget != null
        if (temporarilySkipsInterception) {
            // If a descendant view is scrolling we set a flag to temporarily skip our onInterceptTouchEvent implementation
            skipsTouchInterception = true
        }

        // First dispatch, potentially skipping our onInterceptTouchEvent
        var handled = super.dispatchTouchEvent(ev)

        if (temporarilySkipsInterception) {
            skipsTouchInterception = false

            // If the first dispatch yielded no result or we noticed that the descendant view is unable to scroll in the
            // direction the user is scrolling, we dispatch once more but without skipping our onInterceptTouchEvent.
            // Note that RecyclerView automatically cancels active touches of all its descendants once it starts scrolling
            // so we don't have to do that.
            if (!handled || nestedScrollTargetWasUnableToScroll) {
                handled = super.dispatchTouchEvent(ev)
            }
        }

        return handled
    }


    // Skips RecyclerView's onInterceptTouchEvent if requested
    override fun onInterceptTouchEvent(e: MotionEvent) =
        !skipsTouchInterception && super.onInterceptTouchEvent(e)

    override fun onNestedScroll(target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int) {
        if (target === nestedScrollTarget) {
            if (dxUnconsumed != 0 || dyUnconsumed != 0) {
                nestedScrollTargetWasUnableToScroll = !(dyConsumed != 0 || dxConsumed != 0)
            }
        }
    }


    override fun onNestedScrollAccepted(child: View, target: View, axes: Int) {
        if (directionSupported(axes)) {
            // A descendant started scrolling, so we'll observe it.
            nestedScrollTarget = target
            nestedScrollTargetWasUnableToScroll = false
        }

        super.onNestedScrollAccepted(child, target, axes)
    }

    override fun onStartNestedScroll(child: View, target: View, nestedScrollAxes: Int) : Boolean {
        return child is NestedScrollingChild && directionSupported(nestedScrollAxes)
    }


    override fun onStopNestedScroll(child: View) {
        // The descendant finished scrolling. Clean up!
        nestedScrollTarget = null
        skipsTouchInterception = false
        nestedScrollTargetWasUnableToScroll = false
    }

    override fun toString(): String {
        return "$nestedScrollTarget, targetDragged:nestedScrollTargetIsBeingDragged, targetNotScrolled:$nestedScrollTargetWasUnableToScroll, skipsInterception:$skipsTouchInterception"
    }
}
