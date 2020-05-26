package org.beatonma.commons.app.memberprofile.adapter

import android.view.ViewGroup
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.recyclerview.ThemedAdapter
import org.beatonma.commons.data.core.room.entities.member.PhysicalAddress
import org.beatonma.commons.databinding.MemberItemPhysicalAddressBinding
import org.beatonma.commons.kotlin.extensions.bindText
import org.beatonma.commons.kotlin.extensions.inflate

class PhysicalAddressAdapter: ThemedAdapter<PhysicalAddress>() {
    override fun onCreateDefaultViewHolder(parent: ViewGroup) =
        object: TypedViewHolder(parent.inflate(R.layout.member_item_physical_address)) {
            private val vh =
                MemberItemPhysicalAddressBinding.bind(
                    itemView)

            override fun bind(item: PhysicalAddress) {
                vh.apply {
                    bindText(
                        description to item.description,
                        address to item.address,
                        postcode to item.postcode,
                        email to item.email,
                        phone to item.phone,
                        fax to item.fax,
                        linkColor = theme?.accent,
                    )
                }
            }
        }
}
