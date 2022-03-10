package org.beatonma.commons.compose.layout

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import org.beatonma.commons.core.extensions.fastForEach

@Composable
fun <T> ColumnScope.ListOrEmpty(
    list: List<T>,
    empty: @Composable ColumnScope.() -> Unit,
    item: @Composable ColumnScope.(T) -> Unit,
) {
    if (list.isNotEmpty()) {
        list.fastForEach {
            item(it)
        }
    } else {
        empty()
    }
}
