package org.beatonma.commons.app.ui.recyclerview.adapter

import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.recyclerview.viewholder.staticViewHolderOf
import org.beatonma.commons.kotlin.extensions.inflate

/**
 * Shows a view when items is empty, but not when null.
 * Intended for use when many views are loading - instead of each showing its own loading
 * indicator you should just show one for the whole group.
 */
abstract class ShowWhenEmptyAdapter<T>(
    items: List<T>? = null,
    diffCallback: DiffUtil.Callback? = null,
    private val emptyLayoutID: Int = R.layout.vh_empty_results,
) : TypedAdapter<T>(
    items,
    diffCallback,
) {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ShowWhenEmptyAdapter<*> -> Unit
        }

        if (position < items?.size ?: 0) {
            super.onBindViewHolder(holder, position)
        }
    }

    @CallSuper
    override fun getItemCount() = when {
        items?.isEmpty() == true -> if (emptyLayoutID == 0) 0 else 1
        else -> super.getItemCount()
    }

    @CallSuper
    override fun getItemViewType(position: Int) = when {
        items?.isEmpty() == true -> VIEW_TYPE_EMPTY
        else -> super.getItemViewType(position)
    }

    @CallSuper
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_EMPTY -> getEmptyViewHolder(parent.inflate(emptyLayoutID))
            else -> super.onCreateViewHolder(parent, viewType)
        }
    }

    open fun getEmptyViewHolder(view: View): RecyclerView.ViewHolder = staticViewHolderOf(view)
}
