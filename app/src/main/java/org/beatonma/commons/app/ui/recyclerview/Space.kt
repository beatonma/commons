package org.beatonma.commons.app.ui.recyclerview

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import org.beatonma.commons.R
import org.beatonma.commons.kotlin.extensions.dimenCompat


data class RvSpacing(
    val verticalSpace: Int = -1,
    val horizontalSpace: Int = -1,
    val topSpace: Int = verticalSpace,
    val bottomSpace: Int = verticalSpace,
    val startSpace: Int = horizontalSpace,
    val endSpace: Int = horizontalSpace,
    val horizontalItemSpace: Int = -1,
    val verticalItemSpace: Int = -1,
) {
    private fun Rect.updateIfSet(l: Int = -1, t: Int = -1, r: Int = -1, b: Int = -1) {
        if (l >= 0) left = l
        if (t >= 0) top = t
        if (r >= 0) right = r
        if (b >= 0) bottom = b
    }

    fun decorator(layoutManager: RecyclerView.LayoutManager) = when(layoutManager) {
        is LinearLayoutManager -> linearDecorator()
        is GridLayoutManager -> gridDecorator(layoutManager.spanCount)
        is StaggeredGridLayoutManager -> gridDecorator(layoutManager.spanCount)
        else -> linearDecorator()
    }

    fun linearDecorator() = object: RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State,
        ) {
            super.getItemOffsets(outRect, view, parent, state)

            val itemCount = state.itemCount
            val itemPosition = parent.getChildAdapterPosition(view)

            if (itemCount == 1) {
                outRect.updateIfSet(
                    l = startSpace,
                    t = topSpace,
                    r = endSpace,
                    b = bottomSpace
                )
                return
            }

            val halfVertical = verticalItemSpace / 2
            val halfHorizontal = horizontalItemSpace / 2

            when (itemPosition) {
                // First item
                0 -> {
                    outRect.updateIfSet(
                        l = startSpace,
                        t = topSpace,
                        r = halfHorizontal,
                        b = halfVertical
                    )
                }

                // Last item
                itemCount - 1 -> {
                    outRect.updateIfSet(
                        l = halfHorizontal,
                        t = halfVertical,
                        r = endSpace,
                        b = bottomSpace
                    )
                }

                else -> {
                    outRect.updateIfSet(
                        l = halfHorizontal,
                        t = halfVertical,
                        r = halfHorizontal,
                        b = halfVertical
                    )
                }
            }
        }
    }

    fun gridDecorator(spanCount: Int) = object: RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State,
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            val halfVertical = verticalItemSpace / 2
            val halfHorizontal = horizontalItemSpace / 2

            val spanIndex = (view.layoutParams as StaggeredGridLayoutManager.LayoutParams).spanIndex
            when (spanIndex) {
                0 -> {
                    outRect.updateIfSet(
                        l = startSpace + halfHorizontal,
                        t = topSpace + halfVertical,
                        r = halfHorizontal,
                        b = halfVertical
                    )
                }
                spanCount - 1 -> {
                    outRect.updateIfSet(
                        l = halfHorizontal,
                        t = halfVertical,
                        r = horizontalItemSpace,
                        b = halfVertical
                    )
                }
                else -> {
                    outRect.updateIfSet(
                        l = halfHorizontal,
                        t = halfVertical,
                        r = halfHorizontal,
                        b = verticalItemSpace
                    )
                }
            }
        }
    }
}


fun defaultItemSpace(context: Context, horizontal: Boolean = false, vertical: Boolean = false) = RvSpacing(
    verticalItemSpace = if (vertical) context.dimenCompat(R.dimen.flow_gap_vertical) else -1,
    horizontalItemSpace = if (horizontal) context.dimenCompat(R.dimen.flow_gap_horizontal) else -1,
)
