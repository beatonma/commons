package org.beatonma.commons.app.ui.recyclerview

import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import org.beatonma.commons.app.ui.recyclerview.adapter.BaseRecyclerViewAdapter
import org.beatonma.commons.app.ui.recyclerview.itemanimator.FadeItemAnimator
import org.beatonma.commons.app.ui.recyclerview.itemdecorator.CommonsDividerItemDecoration
import java.lang.ref.WeakReference
import kotlin.math.floor
import kotlin.math.max


fun RecyclerView.setup(
    adapter: BaseRecyclerViewAdapter,
    @RecyclerView.Orientation orientation: Int = RecyclerView.VERTICAL,
    reverseLayout: Boolean = false,
    layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context, orientation, reverseLayout),
    itemAnimator: RecyclerView.ItemAnimator = FadeItemAnimator(),
    itemDecoration: RecyclerView.ItemDecoration? = null,  // Applied to all items, if set
    showSeparators: Boolean = false,
    space: RvSpacing? = null,
) {
    setAdapter(adapter)
    setLayoutManager(layoutManager)
    setItemAnimator(itemAnimator)

    if (space != null) {
        addItemDecoration(space.linearDecorator(orientation))
    }

    if (itemDecoration != null) {
        addItemDecoration(itemDecoration)
    }

    if (showSeparators) {
        when (layoutManager) {
            is LinearLayoutManager -> addItemDecoration(
                CommonsDividerItemDecoration(context, layoutManager.orientation)
            )
        }
    }
}


/**
 * Automatically set up the correct span count of the grid based on the required span width
 * and the space available on the device
 *
 * Otherwise, you may provide columnCount to set a static number of columns.
 */
fun RecyclerView.setupGrid(
    adapter: BaseRecyclerViewAdapter,
    columnWidth: Int = 0,
    columnCount: Int = 0,
    space: RvSpacing? = null,
) {
    setup(
        adapter,
        layoutManager = StaggeredGridLayoutManager(columnCount.coerceAtLeast(1), StaggeredGridLayoutManager.VERTICAL)
    )

    if (columnWidth == 0) {
        if (space != null) {
            addItemDecoration(space.gridDecorator(columnCount.coerceAtLeast(1)))
        }
    }
    else {
        val weakRv = WeakReference(this)
        post {
            val recyclerView1 = weakRv.get() ?: return@post

            try {
                val spanCount =
                    max(1, floor((recyclerView1.measuredWidth / columnWidth).toDouble()).toInt())
                val lm = recyclerView1.layoutManager as StaggeredGridLayoutManager
                lm.spanCount = spanCount
                if (space != null) addItemDecoration(space.gridDecorator(spanCount))
            }
            catch (e: Exception) {
                Log.e("RV.setupGrid", "Error updating StaggeredGridLayoutManager column count: $e")
            }
        }
    }
}
