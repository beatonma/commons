package org.beatonma.commons.app.ui.recyclerview.itemdecorator

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.withStyledAttributes
import androidx.core.view.children
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

class CommonsDividerItemDecoration(
    context: Context,
    private val orientation: Int,
): RecyclerView.ItemDecoration() {

    private lateinit var drawable: Drawable
    private val padding = Rect()
    private val bounds = Rect()

    init {
        context.withStyledAttributes(attrs = intArrayOf(android.R.attr.listDivider)) {
            drawable = getDrawable(0) as Drawable
        }
        drawable.getPadding(padding)
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.layoutManager == null || parent.layoutManager !is LinearLayoutManager) {
            return
        }
        if (orientation == LinearLayoutManager.VERTICAL) {
            drawVertical(canvas, parent, state)
        }
        else {
            drawHorizontal(canvas, parent, state)
        }
    }

    private fun drawVertical(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val itemCount = state.itemCount

        canvas.save()
        val width = drawable.intrinsicWidth
        val left: Int = (parent.width - width) / 2
        val right: Int = (parent.width + width) / 2

        parent.children.forEachIndexed { position, child ->
            if (position.isLastIn(itemCount)) return
            parent.getDecoratedBoundsWithMargins(child, bounds)
            val bottom = bounds.bottom + child.translationY.roundToInt() - padding.bottom
            val top = bottom - drawable.intrinsicHeight
            drawable.setBounds(left, top, right, bottom)
            drawable.draw(canvas)
        }
        canvas.restore()
    }

    private fun drawHorizontal(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val itemCount = state.itemCount

        canvas.save()
        val height = drawable.intrinsicHeight
        val top: Int = (parent.height - height) / 2
        val bottom = (parent.height + height) / 2

        parent.children.forEachIndexed { position, child ->
            if (position.isLastIn(itemCount)) return
            parent.getDecoratedBoundsWithMargins(child, bounds)
            val right = bounds.right + child.translationX.roundToInt()
            val left = right - drawable.intrinsicWidth
            drawable.setBounds(left, top, right, bottom)
            drawable.draw(canvas)
        }
        canvas.restore()
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        val position = parent.getChildAdapterPosition(view)
        when {
            position.isLastIn(state.itemCount) -> {
                // Do not show separator after last item
                outRect.setEmpty()
            }
            orientation == LinearLayoutManager.VERTICAL -> {
                outRect.set(0, 0, 0, drawable.intrinsicHeight + padding.top + padding.bottom)
            }
            orientation == LinearLayoutManager.VERTICAL -> {
                outRect.set(0, 0, drawable.intrinsicWidth + padding.left + padding.right, 0)
            }
            else -> {
                outRect.setEmpty()
            }
        }
    }

    private fun Int.isLastIn(itemCount: Int) = this == itemCount - 1
}
