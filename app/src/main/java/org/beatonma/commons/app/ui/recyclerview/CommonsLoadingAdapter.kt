package org.beatonma.commons.app.ui.recyclerview

import androidx.recyclerview.widget.DiffUtil
import org.beatonma.commons.R

abstract class CommonsLoadingAdapter<T>(
    items: List<T>? = null,
    diffCallback: DiffUtil.Callback? = null,
    emptyLayoutID: Int = R.layout.invisible,
    nullLayoutID: Int = R.layout.item_loading_custom,
) : LoadingRecyclerViewAdapter<T>(
    items,
    diffCallback,
    emptyLayoutID = emptyLayoutID,
    nullLayoutID = nullLayoutID,
)
