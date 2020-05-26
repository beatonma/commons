package org.beatonma.commons.app.memberprofile.adapter

import android.util.Log
import android.view.View
import android.view.ViewGroup
import org.beatonma.commons.R
import org.beatonma.commons.app.memberprofile.HistoryItem
import org.beatonma.commons.app.ui.recyclerview.ThemedCollapsibleAdapter
import org.beatonma.commons.data.core.Dated
import org.beatonma.commons.data.core.Named
import org.beatonma.commons.data.core.Periodic
import org.beatonma.commons.databinding.ItemWideTitleDescriptionBinding
import org.beatonma.commons.kotlin.extensions.CommonsDateFormat
import org.beatonma.commons.kotlin.extensions.dateRange
import org.beatonma.commons.kotlin.extensions.formatted
import org.beatonma.commons.kotlin.extensions.inflate

private const val TAG = "HistoryAdapter"

class HistoryAdapter: ThemedCollapsibleAdapter<HistoryItem<*>>() {
    private val dateFormatter = CommonsDateFormat.Year

    override fun onCreateDefaultViewHolder(parent: ViewGroup) =
        object : TypedViewHolder(parent.inflate(R.layout.item_wide_title_description)) {
            private val vh = ItemWideTitleDescriptionBinding.bind(itemView).apply {
                accent.visibility = View.GONE
            }

            override fun bind(item: HistoryItem<*>) {
                val obj = item.item

                vh.apply {
                    title.text = when (obj) {
                        is Named -> obj.name

                        else -> {
                            Log.w(TAG, "NO NAME from class ${obj::class.java.canonicalName}")
                            obj::class.java.canonicalName
                        }
                    }
                    description.text = when (obj) {
                        is Periodic -> dateRange(item.start, item.end, formatter = dateFormatter)
                        is Dated -> obj.date.formatted(formatter = dateFormatter)

                        else -> {
                            Log.w(TAG, "NO DATES from class ${obj::class.java.canonicalName}")
                            obj::class.java.canonicalName
                        }
                    }
                }
            }
        }
}

