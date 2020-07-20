package org.beatonma.commons.app.ui.recyclerview.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.recyclerview.viewholder.StaticViewHolder
import org.beatonma.commons.app.ui.recyclerview.viewholder.staticViewHolderOf

const val VIEW_TYPE_DEFAULT = 0

/**
 * Generally you should extend [TypedAdapter] or preferably one of its child classes:
 * [LoadingAdapter] or [ShowWhenEmptyAdapter]
 */
abstract class BaseRecyclerViewAdapter internal constructor(): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    init {
        stateRestorationPolicy = StateRestorationPolicy.ALLOW
    }

    protected fun inflate(parent: ViewGroup, layoutID: Int, attachToRoot: Boolean = false): View =
        LayoutInflater.from(parent.context).inflate(layoutID, parent, attachToRoot)
}

/**
 * In most cases you should extend one of the child classes [LoadingAdapter] or [ShowWhenEmptyAdapter]
 * rather than extending this directly.
 */
abstract class TypedAdapter<T> internal constructor(
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

//    fun setItems(items: List<T>?, lifecycleOwner: LifecycleOwner) {
//        val newItems = items
//        val oldItems = this.items
//        lifecycleOwner.lifecycleScope.launch {
//            val result = DiffUtil.calculateDiff(
//                createDiffCallback(oldItems, newItems),
//                false
//            )
//
//            withContext(Dispatchers.Main) {
//                result.dispatchUpdatesTo(this@TypedAdapter)
//                this@TypedAdapter.items = newItems
////                oldItems = null
//            }
//        }
//    }

    protected open val diffCallback: DiffUtil.Callback = diffCallback ?: defaultDiffCallback()

    override fun getItemCount(): Int = items?.size ?: 0


    @CallSuper
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_DEFAULT -> onCreateDefaultViewHolder(parent)
            else -> staticViewHolderOf(inflate(parent, R.layout.invisible))
        }
    }

    abstract fun onCreateDefaultViewHolder(parent: ViewGroup): RecyclerView.ViewHolder

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TypedAdapter<*>.TypedViewHolder -> {
                val item = items?.get(position) ?: return
                (holder as? TypedAdapter<T>.TypedViewHolder)?.bind(item)
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

//    private fun createDiffCallback(oldItems: List<T>?, newItems: List<T>?) = object: DiffUtil.Callback() {
//        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
//            return oldItems?.getOrNull(oldItemPosition) == newItems?.getOrNull(newItemPosition)
//        }
//
//        override fun getOldListSize(): Int {
//            return oldItems?.size ?: 0
//        }
//
//        override fun getNewListSize(): Int {
//            return newItems?.size ?: 0
//        }
//
//        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
//            return oldItems?.getOrNull(oldItemPosition) === newItems?.getOrNull(newItemPosition)
//        }
//    }

    abstract inner class TypedViewHolder(view: View): GenericTypedViewHolder<T>(view) {
//        abstract fun bind(item: T)
    }
}


abstract class GenericTypedViewHolder<T>(view: View): RecyclerView.ViewHolder(view) {
    abstract fun bind(item: T)
}
