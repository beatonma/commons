package org.beatonma.commons.app.memberprofile.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import org.beatonma.commons.app.ui.data.WeblinkData
import org.beatonma.commons.app.ui.recyclerview.adapter.ThemedAdapter
import org.beatonma.commons.app.ui.views.chip.ChipData
import org.beatonma.commons.app.ui.views.chip.CollapsibleChipHolder

private const val TAG = "WeblinkAdapter"

class WeblinkAdapter: ThemedAdapter<WeblinkData>() {
    override fun onCreateDefaultViewHolder(parent: ViewGroup) = CollapsibleChipHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (items?.size == 0 || position > items?.size ?: -1) {
            super.onBindViewHolder(holder, position)
            return
        }
        val item = items?.get(position) ?: return
        (holder as CollapsibleChipHolder).bind(ChipData.forUrl(item))
    }
}
