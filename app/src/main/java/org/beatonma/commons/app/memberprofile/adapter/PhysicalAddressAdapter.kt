package org.beatonma.commons.app.memberprofile.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.recyclerview.*
import org.beatonma.commons.app.ui.views.ChipData
import org.beatonma.commons.app.ui.views.CollapsibleChipHolder
import org.beatonma.commons.data.core.room.entities.member.PhysicalAddress
import org.beatonma.commons.databinding.ItemMemberPhysicalAddressBinding
import org.beatonma.commons.kotlin.extensions.bindText
import org.beatonma.commons.kotlin.extensions.context
import org.beatonma.commons.kotlin.extensions.inflate

class PhysicalAddressAdapter: ThemedCollapsibleAdapter<PhysicalAddress>() {
    override fun onCreateDefaultViewHolder(parent: ViewGroup): TypedRecyclerViewAdapter<PhysicalAddress>.TypedViewHolder =
        object: TypedViewHolder(parent.inflate(R.layout.item_member_physical_address)) {
            private val vh = ItemMemberPhysicalAddressBinding.bind(itemView)
            private val adapter = ActionAdapter()
            init {
                vh.actions.setup(
                    adapter,
                    horizontalLinearLayoutManager(parent.context),
                    space = defaultItemSpace(context, horizontal = true)
                )
            }

            override fun bind(item: PhysicalAddress) {
                vh.apply {
                    bindText(
                        description to item.description,
                        address to item.address,
                        postcode to item.postcode,
                    )
                }

                adapter.items = listOfNotNull(
                    if (item.phone != null) ChipData.forPhoneNumber(context, item.phone) else null,
                    if (item.email != null) ChipData.forEmail(context, item.email) else null
                )
            }
        }

    private class ActionAdapter: ThemedAdapter<ChipData>() {
        override fun onCreateDefaultViewHolder(parent: ViewGroup): RecyclerView.ViewHolder = CollapsibleChipHolder(parent)

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val item = items?.get(position) ?: return
            (holder as CollapsibleChipHolder).bind(item)
        }
    }
}
