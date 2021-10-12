package org.beatonma.commons.compose.layout

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import org.beatonma.commons.compose.systemui.statusBarsPadding
import org.beatonma.commons.compose.util.rememberFloat
import org.beatonma.commons.compose.util.rememberInt
import org.beatonma.commons.compose.util.statusBarHeight
import org.beatonma.commons.core.extensions.withNotNull

/**
 * Helper for a LazyRow/LazyColumn item that relies on nullable data and should only be displayed
 * if that data is not null.
 */
fun <T> LazyListScope.optionalItem(
    value: T?,
    block: @Composable (T) -> Unit,
) {
    withNotNull(value) {
        item {
            block(it)
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
fun LazyListScope.stickyHeaderWithInsets(
    state: LazyListState,
    key: Any,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    stickyHeader(key) {
        val statusBarHeight = statusBarHeight
        val stretchHeight = statusBarHeight * 2f

        var position: Int? by remember { mutableStateOf(null) }
        var previousItemHeight by rememberInt(0)
        var statusBarScale by rememberFloat(0f)

        LaunchedEffect(state.firstVisibleItemIndex) {
            if (position == null) {
                position = state.layoutInfo.visibleItemsInfo.find { it.key == key }?.index
                position?.let { p ->
                    previousItemHeight =
                        state.layoutInfo.visibleItemsInfo.find { it.index == p - 1 }?.size ?: 0
                }
            }
        }

        LaunchedEffect(state.firstVisibleItemScrollOffset) {
            val p = position ?: return@LaunchedEffect
            statusBarScale = when {
                state.firstVisibleItemIndex >= p -> 1f

                state.firstVisibleItemIndex == p - 1 -> {
                    val heightAbove = previousItemHeight - state.firstVisibleItemScrollOffset
                    if (heightAbove > stretchHeight || stretchHeight == 0f) 0f
                    else 1f - (heightAbove.toFloat() / stretchHeight).coerceIn(0f, 1f)
                }

                else -> 0f
            }
        }

        Box(modifier.statusBarsPadding(statusBarScale)) {
            content()
        }
    }
}
