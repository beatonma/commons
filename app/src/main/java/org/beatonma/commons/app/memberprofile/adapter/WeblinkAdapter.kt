package org.beatonma.commons.app.memberprofile.adapter

import android.content.res.ColorStateList
import android.view.ViewGroup
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.data.WeblinkData
import org.beatonma.commons.app.ui.recyclerview.ThemedAdapter
import org.beatonma.commons.databinding.ItemWeblinkBinding
import org.beatonma.commons.kotlin.extensions.inflate

class WeblinkAdapter: ThemedAdapter<WeblinkData>() {
    override fun onCreateDefaultViewHolder(parent: ViewGroup) =
        object : TypedViewHolder(parent.inflate(R.layout.item_weblink)) {
            private val vh =
                ItemWeblinkBinding.bind(itemView)

            override fun bind(item: WeblinkData) {
                item.bindTo(vh.chip)
                val accent = theme?.accent
                if (accent != null) {
                    vh.chip.chipIconTint =
                        ColorStateList.valueOf(accent)
                }
            }
        }
}
