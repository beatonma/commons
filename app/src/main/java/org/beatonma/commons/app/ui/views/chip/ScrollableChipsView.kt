package org.beatonma.commons.app.ui.views.chip

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.core.graphics.withTranslation
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.NestedScrollingChildHelper
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.data.WeblinkData
import org.beatonma.commons.app.ui.views.ScrollableView
import org.beatonma.commons.data.ClickAction
import org.beatonma.commons.kotlin.extensions.*

private const val TAG = "ScrollChips"

/**
 * View for rendering a collection of Chips together.
 *
 * Replaces use of [CollapsibleChipHolder]s in a [RecyclerView] which involved a lot of view
 * re-measuring and hacky use of MotionLayout.
 *
 * Instead, we have a single view that handles rendering and animation of its chips by
 * raw canvas operations.
 */
class ScrollableChipsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
): ScrollableView(context, attrs, defStyleAttr, defStyleRes) {
    override val nestedScrollHelper: NestedScrollingChildHelper = NestedScrollingChildHelper(this).apply {
        isNestedScrollingEnabled = false
    }

    override val supportedScrollDirections: Int = ViewCompat.SCROLL_AXIS_HORIZONTAL

    private var chipData: List<ChipRenderData> = listOf()
    private val renderer = CollapsibleChipRenderer(context)

    private val touchHandler = GestureDetectorCompat(context, TouchHandler())

    private val spaceBetween: Int = context.dimenCompat(R.dimen.flow_gap_horizontal)
    private val iconSize: Int = context.dimenCompat(R.dimen.chip_icon_size)

    private var coroutineScope: CoroutineScope? = null

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
        chipData.forEach {
            // Reinstate auto-close timers for any chips that were left in an expanded state before detaching.
            scheduleAutoClose(it)
        }
    }

    override fun onDetachedFromWindow() {
        coroutineScope?.cancel()
        coroutineScope = null
        super.onDetachedFromWindow()
    }

    /**
     * Index in chipData which represents the chip that was touched most recently. Used to ensure
     * that the chip of interest stays on the screen after size change animation.
     */
    private var latestTouchAtIndex: Int = -1

    init {
        scrollableHeight = context.dimenCompat(R.dimen.chip_min_height)
    }

    fun setData(data: List<WeblinkData>) {
        chipData = data.mapIndexed { index, weblinkData ->
            ChipRenderData(
                text = weblinkData.displayText,
                icon = context.drawableCompat(weblinkData.icon)?.apply { setSize(iconSize) },
                maxWidth = renderer.measureMaxWidth(weblinkData.displayText),
                action = { v -> v.context.openUrl(weblinkData.url) },
                index = index,
            )
        }

        onDataChanged()
    }

    fun setChipData(data: List<ChipData>) {
        chipData = data.mapIndexed { index, chipData ->
            ChipRenderData(
                text = chipData.text ?: "",
                icon = context.drawableCompat(chipData.icon)?.apply { setSize(iconSize) },
                maxWidth = renderer.measureMaxWidth(chipData.text ?: ""),
                action = chipData.action,
                index = index,
            )
        }

        onDataChanged()
    }

    private fun onDataChanged() {
        scrollableWidth = chipData.sumBy { it.maxWidth }
        postInvalidateOnAnimation()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas ?: return

        var requestInvalidation = false
        // Render each chip and accumulate the total occupied width.
        scrollableWidth = chipData.fold(paddingStart) { offset, data ->
            data.currentStartX = offset
            canvas.withTranslation(x = offset.toFloat()) {
                data.currentWidth = renderer.draw(canvas, data)
            }
            if (data.isAnimating()) {
                requestInvalidation = true
            }
            return@fold offset + data.currentWidth + spaceBetween
        } + paddingEnd

        scrollOnScreen()

        if (requestInvalidation) {
            postInvalidateOnAnimation()
        }
    }

    /**
     * Make sure that the scroll position is sensible after animating size changes.
     * Also make sure that the last item to be touched is visible.
     */
    private fun scrollOnScreen() {
        calculateScrollRangeX()
        var targetScrollX: Int = -1
        if (scrollX > horizontalScrollRange) {
            // Something has shrunk - make sure we aren't falling off the side.
            targetScrollX = scrollX
            latestTouchAtIndex = -1
        }
        else if (latestTouchAtIndex >= 0) {
            val target = chipData.safeGet(latestTouchAtIndex) ?: return
            if (target.currentStartX - scrollX < 0) {
                // Expanding a view that was partially off start of screen - nudge it to start of screen.
                targetScrollX = target.currentStartX
                latestTouchAtIndex = -1
            }
            else if (target.currentStartX + target.maxWidth - scrollX > width) {
                // Expanding a view that is now partially off end of screen - nudge it to end of screen.
                targetScrollX = target.currentEndX
                if (!target.isAnimating()) {
                    latestTouchAtIndex = -1
                }
            }
        }

        if (targetScrollX >= 0) {
            scrollX = targetScrollX.coerceIn(0, horizontalScrollRange)
        }
    }

    private fun scheduleAutoClose(data: ChipRenderData) {
        if (data.targetState == ChipState.EXPANDED) {
            data.autoCollapseJob = coroutineScope?.launch {
                delay(CollapsibleChip.AUTO_COLLAPSE_TIME_MILLIS)
                withContext(Dispatchers.Main) {
                    chipData.safeGet(data.index)?.targetState = ChipState.COLLAPSED
                    postInvalidateOnAnimation()
                }
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return touchHandler.onTouchEvent(event) || super.onTouchEvent(event)
    }

    inner class TouchHandler: GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapUp(e: MotionEvent): Boolean {
            val target = findTouchTarget(e.x, e.y)
            withNotNull(target) { data ->
                val action = renderer.getTouchAction(
                    data,
                    x = e.x + scrollX - data.currentStartX,
                    y = e.y + scrollY
                )
                action?.invoke(this@ScrollableChipsView)

                if (data.targetState == ChipState.EXPANDED) {
                    scheduleAutoClose(data)
                }

                postInvalidateOnAnimation()
                return@onSingleTapUp true
            }

            return super.onSingleTapUp(e)
        }
    }

    /**
     * Find which [ChipRenderData] instance is rendered at the given coordinates (if any).
     */
    private fun findTouchTarget(x: Float, y: Float): ChipRenderData? {
        val scrolledX = x + scrollX
        chipData.forEachIndexed { index, data ->
            if (scrolledX > data.currentStartX
                && scrolledX < data.currentStartX + data.currentWidth) {
                latestTouchAtIndex = index
                return data
            }
            latestTouchAtIndex = -1
        }
        return null
    }
}


internal class ChipRenderData(
    val text: String,
    val icon: Drawable?,
    val maxWidth: Int,
    val action: ClickAction?,
    val index: Int,
    var currentWidth: Int = 0,
    var currentStartX: Int = 0,
) {
    val currentEndX: Int get() = currentStartX + currentWidth

    var expandProgress: Float = 0F
    var animationStarted: Long = -1L
    var targetState: ChipState = ChipState.COLLAPSED
        set(value) {
            if (value != field) {
                animationStarted = System.currentTimeMillis()
                autoCollapseJob?.cancel()
            }
            field = value
        }

    var autoCollapseJob: Job? = null
        set(value) {
            field?.cancel()
            field = value
        }

    fun isAnimating() = animationStarted > 0
}


internal fun Drawable.setSize(size: Int) = apply { setBounds(0, 0, size, size) }
