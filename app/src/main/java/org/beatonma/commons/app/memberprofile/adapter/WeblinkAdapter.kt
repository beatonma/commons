package org.beatonma.commons.app.memberprofile.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import org.beatonma.commons.app.ui.data.WeblinkData
import org.beatonma.commons.app.ui.recyclerview.ThemedAdapter
import org.beatonma.commons.app.ui.views.ChipData
import org.beatonma.commons.app.ui.views.CollapsibleChipHolder

private const val TAG = "WeblinkAdapter"

class WeblinkAdapter: ThemedAdapter<WeblinkData>() {
    override fun onCreateDefaultViewHolder(parent: ViewGroup) = CollapsibleChipHolder(parent)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items?.get(position) ?: return
        (holder as CollapsibleChipHolder).bind(ChipData.forUrl(item))
    }
}
