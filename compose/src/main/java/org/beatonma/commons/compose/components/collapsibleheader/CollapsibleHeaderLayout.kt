package org.beatonma.commons.compose.components.collapsibleheader

import androidx.annotation.FloatRange
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.beatonma.commons.compose.util.toggle
import org.beatonma.commons.core.extensions.reversed
import org.beatonma.commons.theme.compose.padding.EndOfContent
import org.beatonma.commons.theme.compose.theme.CommonsTheme

@Composable
fun CollapsibleHeaderLayout(
    collapsingHeader: @Composable (collapseProgress: Float) -> Unit,
    lazyListContent: LazyListScope.() -> Unit,
    modifier: Modifier = Modifier,
    onScrollStarted: () -> Unit = {},
    onScrollFinished: () -> Unit = {},
    @FloatRange(from = 0.0, to = 1.0) snapToStateAt: Float? = null,
    lazyListState: LazyListState = rememberLazyListState(),
    interactionSource: MutableInteractionSource = lazyListState.interactionSource as MutableInteractionSource,
    flingBehavior: FlingBehavior = ScrollableDefaults.flingBehavior(),
    headerState: CollapsibleHeaderState = rememberCollapsibleHeaderState(lazyListState, snapToStateAt),
    scrollEnabled: Boolean = true,
    appendEndOfContentSpacing: Boolean = true,
) {
    val coroutineScope = rememberCoroutineScope()
    val resolvedOnScrollStopped: () -> Unit = when {
        headerState.isSnappable -> {
            {
                coroutineScope.launch {
                    headerState.snap()
                }
                onScrollFinished()
            }
        }
        else -> onScrollFinished
    }

    OnScrollEvents(
        interactionSource,
        onScrollStarted,
        resolvedOnScrollStopped
    )

    val resolvedListContent = when {
        appendEndOfContentSpacing -> {
            {
                lazyListContent()
                item { EndOfContent() }
            }
        }
        else -> lazyListContent
    }

    val resolvedHeaderContent: @Composable () -> Unit = {
        collapsingHeader(headerState.expansion)
    }

    CollapsibleHeaderLayout(
        modifier
            .scrollable(
                headerState,
                orientation = Orientation.Vertical,
                enabled = scrollEnabled,
                interactionSource = interactionSource,
                flingBehavior = flingBehavior,
            )
            .fillMaxSize(),
        headerContent = resolvedHeaderContent,
        lazyListContent = resolvedListContent,
        lazyListState,
        lazyListModifier = Modifier
            .nestedScroll(
                rememberNestedScrollConnection(
                    headerState,
                    lazyListState,
                    flingBehavior,
                )
            )
            .fillMaxSize(),
        onLayoutHeightMeasured = { newHeight -> headerState.maxValue = newHeight }
    )
}

@Composable
private fun CollapsibleHeaderLayout(
    modifier: Modifier,
    headerContent: @Composable () -> Unit,
    lazyListContent: LazyListScope.() -> Unit,
    lazyListState: LazyListState,
    lazyListModifier: Modifier,
    onLayoutHeightMeasured: (Int) -> Unit,
) {
    Column(modifier) {
        Layout(
            content = headerContent,
            modifier = Modifier.testTag("collapsing_header"),
        ) { measurables, constraints ->
            require(measurables.size == 1) {
                "CollapsibleHeaderLayout header should contain exactly one child"
            }

            val placeable = measurables[0].measure(constraints)

            onLayoutHeightMeasured(placeable.height)

            layout(placeable.width, placeable.height) {
                placeable.placeRelative(0, 0)
            }
        }

        LazyColumn(
            state = lazyListState,
            modifier = lazyListModifier
                .testTag("lazy_list"),
            content = lazyListContent
        )
    }
}

@Composable
private fun OnScrollEvents(
    interactionSource: MutableInteractionSource,
    onScrollStarted: () -> Unit,
    onScrollFinished: () -> Unit,
) {
    val dragged by interactionSource.collectIsDraggedAsState()

    if (dragged) {
        onScrollStarted()
    }
    else {
        onScrollFinished()
    }
}

private fun LazyListState.isAtTop() = this.firstVisibleItemIndex == 0
        && this.firstVisibleItemScrollOffset == 0

@Composable
private fun rememberNestedScrollConnection(
    headerState: CollapsibleHeaderState,
    lazyListState: LazyListState,
    flingBehavior: FlingBehavior,
) = remember {
    object : NestedScrollConnection {
        override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
            if (available == Offset.Zero) return Offset.Zero

            val consumed = when {
                lazyListState.isAtTop() -> headerState.dispatchRawPreScroll(available.y)
                else -> 0F
            }

            return Offset(0F, y = consumed)
        }

        override fun onPostScroll(
            consumed: Offset,
            available: Offset,
            source: NestedScrollSource,
        ): Offset {
            headerState.finishNestedScroll()
            return Offset.Zero
        }

        override suspend fun onPreFling(available: Velocity): Velocity {
            if (available.y == 0F) return Velocity.Zero

            var result = Velocity.Zero

            if (lazyListState.isAtTop()) {
                headerState.scroll(MutatePriority.Default) {
                    with (flingBehavior) {
                        result = Velocity(0F, performFling(available.y.reversed()))
                    }
                }
            }

            return result
        }

        override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
            return available
        }
    }
}

@Preview
@Composable
fun CollapsibleHeaderLayoutPreview() {
    val flingBehavior = ScrollableDefaults.flingBehavior()
    val listItems = (1..100).toList()
    val lazyListState = rememberLazyListState()
    val headerState = rememberCollapsibleHeaderState(lazyListState, snapToStateAt = 0.3F)
    val coroutineScope = rememberCoroutineScope()
    val expandOnClick = mutableStateOf(false)

    CommonsTheme {
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            CollapsibleHeaderLayout(
                headerState = headerState,
                lazyListState = lazyListState,
                modifier = Modifier
                    .clickable {
                        coroutineScope.launch {
                            if (expandOnClick.toggle()) {
                                headerState.expand()
                            }
                            else {
                                headerState.collapse()
                            }
                        }
                    },
                collapsingHeader = { expandedProgress ->
                    Column {
                        Text(
                            "This should stay here",
                            Modifier
                                .height(80.dp)
                                .background(Color.Blue)
                        )
                        Box(
                            Modifier
                                .height(300.dp * expandedProgress)
                                .background(Color.Red)
                        ) {
                            Text("This should collapse")
                        }
                    }
                },
                lazyListContent = {
                    itemsIndexed(listItems) { index, item ->
                        Text(
                            "$item",
                            Modifier
                                .fillMaxWidth()
                                .background(
                                    Color.Yellow.copy(
                                        alpha = (index / 100F).coerceIn(
                                            0F,
                                            1F
                                        )
                                    )
                                )
                                .padding(4.dp)
                        )
                    }
                },
                flingBehavior = flingBehavior,
            )
        }
    }
}
