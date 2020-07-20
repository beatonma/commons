package org.beatonma.commons.app.ui.recyclerview.adapter

import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.beatonma.commons.R

/**
 * Created by Michael on 29/07/2017.
 * A RecyclerView Adapter that provides an interface to handle null/empty datasets in a simple way.
 */
const val VIEW_TYPE_LOADING = 123
const val VIEW_TYPE_EMPTY = 321

abstract class LoadingAdapter<T> internal constructor(
    items: List<T>? = null,
    diffCallback: DiffUtil.Callback? = null,
    private val nullLayoutID: Int = R.layout.item_loading,
    private val emptyLayoutID: Int = R.layout.invisible,
): TypedAdapter<T>(items) {

    override val diffCallback: DiffUtil.Callback = diffCallback ?: defaultDiffCallback()

    private fun defaultDiffCallback() = object: DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItems?.getOrNull(oldItemPosition) == items?.getOrNull(newItemPosition)
        }

        override fun getOldListSize(): Int = when {
            oldItems == null -> if (nullLayoutID == 0) 0 else 1
            oldItems?.isEmpty() == true -> if (emptyLayoutID == 0) 0 else 1
            else -> oldItems?.size ?: 0
        }

        override fun getNewListSize(): Int = when {
            items == null -> if (nullLayoutID == 0) 0 else 1
            items?.isEmpty() == true -> if (emptyLayoutID == 0) 0 else 1
            else -> items?.size ?: 0
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItems?.getOrNull(oldItemPosition) === items?.getOrNull(newItemPosition)
        }
    }

    /**
     * Return a ViewHolder to use when items == null
     * This usually means data is still being loaded
     */
    open fun getNullViewHolder(view: View): RecyclerView.ViewHolder {
        return LoadingViewHolder(view)
    }

    /**
     * Return a ViewHolder to use when items,isEmpty()
     * This usually means data loading has finished but has no contents
     */
    open fun getEmptyViewHolder(view: View): RecyclerView.ViewHolder {
        return InvisibleViewHolder(view)
    }

    @CallSuper
    override fun getItemCount(): Int {
        return when {
            items == null -> if (nullLayoutID == 0) 0 else 1
            items?.isEmpty() == true -> if (emptyLayoutID == 0) 0 else 1
            else -> items?.size ?: 0
        }
    }

    @CallSuper
    override fun getItemViewType(position: Int): Int {
        return when {
            items == null -> VIEW_TYPE_LOADING
            items?.isEmpty() == true -> VIEW_TYPE_EMPTY
            else -> VIEW_TYPE_DEFAULT
        }
    }

    @CallSuper
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_LOADING -> getNullViewHolder(inflate(parent, nullLayoutID))
            VIEW_TYPE_EMPTY -> getEmptyViewHolder(inflate(parent, emptyLayoutID))
            else -> super.onCreateViewHolder(parent, viewType)
        }
    }

    private class LoadingViewHolder constructor(v: View) : RecyclerView.ViewHolder(v)

    private class InvisibleViewHolder constructor(v: View) : RecyclerView.ViewHolder(v)
}
