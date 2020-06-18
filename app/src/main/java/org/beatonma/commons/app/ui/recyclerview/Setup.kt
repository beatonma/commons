package org.beatonma.commons.app.ui.recyclerview

import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import org.beatonma.commons.app.ui.recyclerview.itemanimator.FadeItemAnimator
import java.lang.ref.WeakReference
import kotlin.math.floor
import kotlin.math.max


fun RecyclerView.setup(
    adapter: BaseRecyclerViewAdapter,
    layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context),
    itemAnimator: RecyclerView.ItemAnimator = FadeItemAnimator(),
    space: RvSpacing? = null,
) {
    setAdapter(adapter)
    setLayoutManager(layoutManager)
    setItemAnimator(itemAnimator)

    if (space != null) addItemDecoration(space.linearDecorator())
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
        layoutManager = StaggeredGridLayoutManager(columnCount.coerceAtLeast(1), StaggeredGridLayoutManager.HORIZONTAL)
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


fun horizontalLinearLayoutManager(context: Context?) =
    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
