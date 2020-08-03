package org.beatonma.commons.app.ui.recyclerview.adapter

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface AsyncDiffHost: AsyncDiff, LifecycleOwner {
    fun <T> diffAdapterItems(adapter: TypedAdapter<T>, items: List<T>?, diff: DiffUtil.Callback? = null): Job? =
        diffAdapterItems(this, adapter, items, diff)

    fun <T, A: TypedAdapter<T>> A.diffItems(items: List<T>?, diff: DiffUtil.Callback? = null): Job? =
        diffAdapterItems(
            lifecycleOwner = this@AsyncDiffHost,
            adapter = this@diffItems,
            items = items,
            diff = diff
        )
}

interface AsyncDiff {
    var diffJob: Job?

    fun <T> diffAdapterItems(lifecycleOwner: LifecycleOwner, adapter: TypedAdapter<T>, items: List<T>?, diff: DiffUtil.Callback? = null): Job? {
        diffJob?.cancel()

        diffJob = lifecycleOwner.lifecycleScope.launch {
            val oldItems = adapter.items

            val diffCallback = diff ?: when(adapter) {
                is LoadingAdapter -> placeholderDiffCallback(oldItems, items, nullViewCount = 1, emptyViewCount = 1)
                is ShowWhenEmptyAdapter -> placeholderDiffCallback(oldItems, items, nullViewCount = 0, emptyViewCount = 1)
                else -> simpleDiffCallback(oldItems, items)
            }

            val diffResult = DiffUtil.calculateDiff(diffCallback)

            adapter.setItemsSync(items)

            withContext(Dispatchers.Main) {
                diffResult.dispatchUpdatesTo(adapter)
            }
        }
        return diffJob
    }
}


fun <T> simpleDiffCallback(oldItems: List<T>?, newItems: List<T>?) =
    placeholderDiffCallback(oldItems, newItems, nullViewCount = 0, emptyViewCount = 0)

fun <T> placeholderDiffCallback(
    oldItems: List<T>?,
    newItems: List<T>?,
    nullViewCount: Int = 1,
    emptyViewCount: Int = 1,
) = object: DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems?.getOrNull(oldItemPosition) == newItems?.getOrNull(newItemPosition)
    }
    override fun getOldListSize(): Int = sizeOf(oldItems)

    override fun getNewListSize(): Int = sizeOf(newItems)

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems?.getOrNull(oldItemPosition) === newItems?.getOrNull(newItemPosition)
    }

    private fun sizeOf(list: List<T>?): Int = when {
        list == null -> nullViewCount
        list.isEmpty() -> emptyViewCount
        else -> list.size
    }
}
