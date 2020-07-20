package org.beatonma.commons.app.memberprofile.adapter

import android.util.Log
import android.view.View
import android.view.ViewGroup
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.recyclerview.adapter.ThemedCollapsibleAdapter
import org.beatonma.commons.data.core.interfaces.Dated
import org.beatonma.commons.data.core.interfaces.Named
import org.beatonma.commons.data.core.interfaces.Periodic
import org.beatonma.commons.data.core.interfaces.Temporal
import org.beatonma.commons.databinding.ItemWideTitleDescriptionBinding
import org.beatonma.commons.kotlin.extensions.CommonsDateFormat
import org.beatonma.commons.kotlin.extensions.dateRange
import org.beatonma.commons.kotlin.extensions.formatted
import org.beatonma.commons.kotlin.extensions.inflate

private const val TAG = "HistoryAdapter"

@Deprecated("Replaced by TimelineView")
class HistoryAdapter: ThemedCollapsibleAdapter<Temporal>() {
    private val dateFormatter = CommonsDateFormat.Year

    override fun onCreateDefaultViewHolder(parent: ViewGroup) =
        object : TypedViewHolder(parent.inflate(R.layout.item_wide_title_description)) {
            private val vh = ItemWideTitleDescriptionBinding.bind(itemView).apply {
                accent.visibility = View.GONE
            }

            override fun bind(item: Temporal) {

                vh.apply {
                    title.text = when (item) {
                        is Named -> item.name

                        else -> {
                            Log.w(TAG, "NO NAME from class ${item::class.java.canonicalName}")
                            item::class.java.canonicalName
                        }
                    }
                    description.text = when (item) {
                        is Periodic -> dateRange(item.start, item.end, formatter = dateFormatter)
                        is Dated -> item.date.formatted(formatter = dateFormatter)

                        else -> {
                            Log.w(TAG, "NO DATES from class ${item::class.java.canonicalName}")
                            item::class.java.canonicalName
                        }
                    }
                }
            }
        }
}

