package org.beatonma.commons.app.ui.recyclerview

import org.beatonma.commons.R

abstract class CollapsibleAdapter<T>(
    collapsed: Boolean = false,
    items: List<T>? = null,
    emptyLayoutID: Int = R.layout.vh_empty_results,
): CommonsShowWhenEmptyAdapter<T>(items, emptyLayoutID = emptyLayoutID) {

    private var _collapsed = false
    var collapsed: Boolean = collapsed
        private set
    var collapsedItemsVisible: Int = 3

    fun setCollapsed(collapsed: Boolean) {
        when(collapsed) {
            true -> collapse()
            false -> expand()
        }
    }

    fun isCollapsible(): Boolean = items?.size ?: 0 > collapsedItemsVisible

    fun collapse() {
        collapsed = true
        val size = items?.size ?: 0
        if (size > collapsedItemsVisible) {
            notifyItemRangeRemoved(collapsedItemsVisible, size)
        }
    }

    fun expand() {
        collapsed = false
        val size = items?.size ?: 0
        if (size > collapsedItemsVisible) {
            notifyItemRangeInserted(collapsedItemsVisible, size)
        }
    }

    fun toggle(): Boolean {
        if (collapsed) {
            expand()
        }
        else {
            collapse()
        }
        return collapsed
    }

    override fun getItemCount() = when(collapsed) {
        true -> items?.size?.coerceAtMost(collapsedItemsVisible)?.coerceAtLeast(1) ?: super.getItemCount()
        else -> super.getItemCount()
    }
}
