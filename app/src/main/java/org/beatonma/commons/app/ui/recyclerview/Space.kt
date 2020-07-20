package org.beatonma.commons.app.ui.recyclerview

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import org.beatonma.commons.R
import org.beatonma.commons.data.ShouldNotHappen
import org.beatonma.commons.kotlin.extensions.dimenCompat

class RvSpacing(
    spaceAround: Int = -1,
    verticalSpace: Int = spaceAround,
    horizontalSpace: Int = spaceAround,
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
        is LinearLayoutManager -> linearDecorator(layoutManager.orientation)
        is GridLayoutManager -> gridDecorator(layoutManager.spanCount)
        is StaggeredGridLayoutManager -> gridDecorator(layoutManager.spanCount)
        else -> linearDecorator()
    }

    fun linearDecorator(@RecyclerView.Orientation orientation: Int = RecyclerView.VERTICAL) = when(orientation) {
        RecyclerView.VERTICAL -> linearDecoratorVertical()
        RecyclerView.HORIZONTAL -> linearDecoratorHorizontal()
        else -> throw ShouldNotHappen("orientation should only accept VERTICAL or HORIZONTAL values")
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

    private fun linearDecoratorVertical() = object: RecyclerView.ItemDecoration() {
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

            when (itemPosition) {
                // First item
                0 -> {
                    outRect.updateIfSet(
                        l = startSpace,
                        t = topSpace,
                        r = endSpace,
                        b = halfVertical
                    )
                }

                // Last item
                itemCount - 1 -> {
                    outRect.updateIfSet(
                        l = startSpace,
                        t = halfVertical,
                        r = endSpace,
                        b = bottomSpace
                    )
                }

                else -> {
                    outRect.updateIfSet(
                        l = startSpace,
                        t = halfVertical,
                        r = endSpace,
                        b = halfVertical
                    )
                }
            }
        }
    }

    private fun linearDecoratorHorizontal() = object: RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State,
        ) {
            super.getItemOffsets(outRect, view, parent, state)

            val itemCount = state.itemCount

            if (itemCount == 1) {
                outRect.updateIfSet(
                    l = startSpace,
                    t = topSpace,
                    r = endSpace,
                    b = bottomSpace
                )
                return
            }

            val itemPosition = parent.getChildAdapterPosition(view)
            val halfHorizontal = horizontalItemSpace / 2

            when (itemPosition) {
                // First item
                0 -> {
                    outRect.updateIfSet(
                        l = startSpace,
                        t = topSpace,
                        r = halfHorizontal,
                        b = bottomSpace
                    )
                }

                // Last item
                itemCount - 1 -> {
                    outRect.updateIfSet(
                        l = halfHorizontal,
                        t = topSpace,
                        r = endSpace,
                        b = bottomSpace
                    )
                }

                else -> {
                    outRect.updateIfSet(
                        l = halfHorizontal,
                        t = topSpace,
                        r = halfHorizontal,
                        b = bottomSpace
                    )
                }
            }
        }
    }
}

/**
 * Spacing for a RecyclerView that is embedded in a larger UI - item spacing only, no overscroll or
 * external margins.
 */
fun defaultItemSpace(context: Context, horizontal: Boolean = true, vertical: Boolean = true) = RvSpacing(
    spaceAround = context.dimenCompat(R.dimen.list_space_around_default),
    verticalItemSpace = if (vertical) context.dimenCompat(R.dimen.flow_gap_vertical) else -1,
    horizontalItemSpace = if (horizontal) context.dimenCompat(R.dimen.flow_gap_horizontal) else -1,
)


fun defaultSpace(
    context: Context,
    @RecyclerView.Orientation orientation: Int,
    overscroll: Boolean = true
) = when {
    overscroll && orientation == RecyclerView.HORIZONTAL -> defaultHorizontalOverscroll(context)
    overscroll && orientation == RecyclerView.VERTICAL -> defaultPrimaryContentSpacing(context)
    else -> defaultItemSpace(context)
}


/**
 * Spacing for a RecyclerView that is the primary content holder on screen.
 */
fun defaultPrimaryContentSpacing(context: Context) = RvSpacing(
    topSpace = context.dimenCompat(R.dimen.list_overscroll_top),
    bottomSpace = context.dimenCompat(R.dimen.list_overscroll_bottom),
    verticalItemSpace = context.dimenCompat(R.dimen.flow_gap_vertical),
    horizontalItemSpace = context.dimenCompat(R.dimen.flow_gap_horizontal),
)


/**
 * Spacing for a RecyclerView that is the primary content holder on screen.
 */
fun primaryContentWithToolbarSpacing(context: Context) = RvSpacing(
    topSpace = context.dimenCompat(R.dimen.toolbar_height),
    bottomSpace = context.dimenCompat(R.dimen.list_overscroll_bottom),
    verticalItemSpace = context.dimenCompat(R.dimen.flow_gap_vertical),
    horizontalItemSpace = context.dimenCompat(R.dimen.flow_gap_horizontal),
)


fun defaultHorizontalOverscroll(context: Context) = RvSpacing(
    startSpace = context.dimenCompat(R.dimen.list_overscroll_left),
    horizontalItemSpace = context.dimenCompat(R.dimen.flow_gap_horizontal),
    endSpace = context.dimenCompat(R.dimen.list_overscroll_right)
)
