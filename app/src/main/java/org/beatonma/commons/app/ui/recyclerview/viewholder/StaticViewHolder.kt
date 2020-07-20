package org.beatonma.commons.app.ui.recyclerview.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class StaticViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    constructor(parent: ViewGroup, layoutId: Int) : this(
        LayoutInflater.from(parent.context)
            .inflate(layoutId, parent, false)
    )

    open fun bind() {}

    fun setOnClickListener(listener: View.OnClickListener) {
        itemView.setOnClickListener(listener)
    }
}

inline fun staticViewHolderOf(view: View, crossinline initBlock: StaticViewHolder.() -> Unit = {}) =
    object: StaticViewHolder(view) {
        init {
            initBlock()
        }
    }

inline fun staticViewHolderOf(parent: ViewGroup, layoutId: Int, crossinline initBlock: StaticViewHolder.() -> Unit = {}) =
    object: StaticViewHolder(parent, layoutId) {
        init {
            initBlock()
        }
    }
