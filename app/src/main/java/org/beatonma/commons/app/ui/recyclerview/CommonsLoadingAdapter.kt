package org.beatonma.commons.app.ui.recyclerview

import androidx.recyclerview.widget.DiffUtil
import org.beatonma.commons.R
import org.beatonma.lib.ui.recyclerview.LoadingRecyclerViewAdapter

abstract class CommonsLoadingAdapter<T>(
    items: List<T>? = null,
    diffCallback: DiffUtil.Callback? = null,
    emptyLayoutID: Int = R.layout.vh_invisible,
    nullLayoutID: Int = R.layout.vh_loading_custom,
) : LoadingRecyclerViewAdapter<T>(
    items,
    diffCallback,
    emptyLayoutID = emptyLayoutID,
    nullLayoutID = nullLayoutID,
)
