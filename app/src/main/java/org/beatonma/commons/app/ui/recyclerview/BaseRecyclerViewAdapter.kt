package org.beatonma.commons.app.ui.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.recyclerview.viewholder.StaticViewHolder

const val VIEW_TYPE_DEFAULT = 0

abstract class BaseRecyclerViewAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    init {
        stateRestorationPolicy = StateRestorationPolicy.ALLOW
    }

    protected fun inflate(parent: ViewGroup, layoutID: Int, attachToRoot: Boolean = false): View =
        LayoutInflater.from(parent.context).inflate(layoutID, parent, attachToRoot)
}

abstract class TypedRecyclerViewAdapter<T>(
    items: List<T>? = null,
    diffCallback: DiffUtil.Callback? = null
): BaseRecyclerViewAdapter() {
    protected var oldItems: List<T>? = null
    open var items = items
        set(value) {
            oldItems = field
            field = value
            DiffUtil.calculateDiff(diffCallback, false)
                .dispatchUpdatesTo(this)
        }

    protected open val diffCallback: DiffUtil.Callback = diffCallback ?: defaultDiffCallback()

    override fun getItemCount(): Int = items?.size ?: 0


    @CallSuper
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_DEFAULT -> onCreateDefaultViewHolder(parent)
            else -> object: StaticViewHolder(inflate(parent, R.layout.invisible)) {}
        }
    }

    abstract fun onCreateDefaultViewHolder(parent: ViewGroup): RecyclerView.ViewHolder

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TypedRecyclerViewAdapter<*>.TypedViewHolder -> {
                val item = items?.get(position) ?: return
                (holder as? TypedRecyclerViewAdapter<T>.TypedViewHolder)?.bind(item)
            }

            is StaticViewHolder -> holder.bind()
        }
    }


    private fun defaultDiffCallback() = object: DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItems?.getOrNull(oldItemPosition) == items?.getOrNull(newItemPosition)
        }

        override fun getOldListSize(): Int {
            return oldItems?.size ?: 0
        }

        override fun getNewListSize(): Int {
            return items?.size ?: 0
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItems?.getOrNull(oldItemPosition) === items?.getOrNull(newItemPosition)
        }
    }

    abstract inner class TypedViewHolder(view: View): RecyclerView.ViewHolder(view) {
        abstract fun bind(item: T)
    }
}
