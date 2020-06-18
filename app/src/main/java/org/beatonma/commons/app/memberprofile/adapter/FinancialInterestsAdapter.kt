package org.beatonma.commons.app.memberprofile.adapter

import android.view.ViewGroup
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.recyclerview.ThemedCollapsibleAdapter
import org.beatonma.commons.data.core.room.entities.member.FinancialInterest
import org.beatonma.commons.databinding.MemberItemFinancialInterestBinding
import org.beatonma.commons.kotlin.extensions.bindText
import org.beatonma.commons.kotlin.extensions.dateRange
import org.beatonma.commons.kotlin.extensions.inflate


class FinancialInterestsAdapter: ThemedCollapsibleAdapter<FinancialInterest>() {


    override fun onCreateDefaultViewHolder(parent: ViewGroup) =
        object: TypedViewHolder(parent.inflate(R.layout.member_item_financial_interest)) {
            private val vh = MemberItemFinancialInterestBinding.bind(itemView)

            override fun bind(item: FinancialInterest) {
                vh.apply {
                    bindText(
                        description to item.description,
                        category to item.category,
                        dates to dateRange(item.dateCreated, item.dateAmended)
                    )
                }
            }
        }
}
