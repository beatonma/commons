package org.beatonma.commons.compose.layout

import androidx.compose.foundation.lazy.LazyListScope
import org.beatonma.commons.compose.util.TypedComposableBlock
import org.beatonma.commons.core.extensions.withNotNull

/**
 * Helper for a LazyRow/LazyColumn item that relies on nullable data and should only be displayed
 * if that data is not null.
 */
fun <T> LazyListScope.optionalItem(
    value: T?,
    block: TypedComposableBlock<T>,
) {
    withNotNull(value) {
        item {
            block(it)
        }
    }
}
