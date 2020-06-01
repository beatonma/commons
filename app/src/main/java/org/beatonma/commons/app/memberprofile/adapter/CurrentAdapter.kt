package org.beatonma.commons.app.memberprofile.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.recyclerview.ThemedCollapsibleAdapter
import org.beatonma.commons.data.core.room.entities.constituency.Constituency
import org.beatonma.commons.databinding.ItemWideTitleActionBinding
import org.beatonma.commons.kotlin.extensions.*

class CurrentAdapter: ThemedCollapsibleAdapter<Any>() {
    override fun onCreateDefaultViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        object: TypedViewHolder(parent.inflate(R.layout.item_wide_title_action)) {
            private val vh = ItemWideTitleActionBinding.bind(
                itemView).apply {
                accent.hide()
            }

            override fun bind(item: Any) {
                when (item) {
                    is String -> {
                        bindText(vh.title to item,
                            linkColor = theme?.accent)
                        vh.action.hide()
                    }
                    is Constituency -> {
                        bindText(
                            vh.title to htmlCompat(R.string.member_member_for_constituency,
                                item.name),
                            linkColor = theme?.accent
                        )
                        vh.action.show()
                        itemView.setOnClickListener { view ->
                            view.navigateTo(
                                R.id.action_memberProfileFragment_to_constituencyDetailFragment,
                                item.bundle(),
                            )
                        }
                    }
                }
            }
        }
}
