package org.beatonma.commons.compose.components.newcollapsibleheader

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import kotlin.math.max
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NewCollapsibleHeaderLayout(
    header: @Composable (Float) -> Unit,
    content: LazyListScope.() -> Unit,
    state: LazyListState = rememberLazyListState(),
) {
    var expansion by remember { mutableStateOf(1f) }

    var minHeight by remember { mutableStateOf(-1) }
    var maxHeight by remember { mutableStateOf(0) }

    LaunchedEffect(state.firstVisibleItemScrollOffset) {
        val firstItemIndex = state.firstVisibleItemIndex
        val firstItemOffset = state.firstVisibleItemScrollOffset

        if (firstItemIndex == 0) {
            val layoutInfo = state.layoutInfo

            if (layoutInfo.totalItemsCount > 1) {
                layoutInfo.visibleItemsInfo
            }

            if (maxHeight != 0) {
                expansion = 1f - (firstItemOffset / maxHeight.toFloat())
                check(expansion in 0f..1f) { "Invalid expansion $expansion should be in 0..1f" }
            }
        } else {
            // Collapse header completely
            expansion = 0f
        }
    }

    LazyColumn(state = state) {
        stickyHeader {
            Layout(
                content = {
                    header(expansion)
                },
            ) { measurables, constraints ->
                val placeables = if (maxHeight == 0 || minHeight < 0) {
                    measurables.map { it.measure(constraints) }
                } else {
                    val c = constraints.copy(
                        maxHeight = max(
                            minHeight,
                            (expansion * maxHeight.toFloat()).roundToInt()
                        )
                    )
                    measurables.map { it.measure(c) }
                }

                val width: Int = placeables.maxOf(Placeable::width)
                val height: Int = placeables.maxOf(Placeable::height)

                when {
                    maxHeight == 0 -> maxHeight = height
                    expansion == 0f -> minHeight = height
                }

                layout(width, maxHeight) {
                    placeables.forEach { it.placeRelative(0, 0) }
                }
            }
        }

        content()
    }
}
