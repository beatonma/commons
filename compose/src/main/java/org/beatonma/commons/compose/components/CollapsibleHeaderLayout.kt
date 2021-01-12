package org.beatonma.commons.compose.components

import androidx.compose.animation.core.FloatPropKey
import androidx.compose.animation.core.TransitionDefinition
import androidx.compose.animation.transition
import androidx.compose.foundation.gestures.ScrollScope
import androidx.compose.foundation.gestures.ScrollableController
import androidx.compose.foundation.gestures.rememberScrollableController
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.gesture.scrollorientationlocking.Orientation
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.beatonma.commons.compose.animation.ExpandCollapseState
import org.beatonma.commons.compose.animation.collapse
import org.beatonma.commons.compose.animation.expand
import org.beatonma.commons.compose.animation.isCollapsed
import org.beatonma.commons.compose.animation.isExpanded
import org.beatonma.commons.compose.animation.rememberExpandCollapseState
import org.beatonma.commons.compose.animation.rememberExpandCollapseTransition
import org.beatonma.commons.compose.modifiers.colorize
import org.beatonma.commons.core.extensions.reversed
import org.beatonma.commons.theme.compose.theme.CommonsTheme

private val HeaderExpandProgress =
    FloatPropKey(label = "Track the progress of [CollapsibleHeaderLayout] header expansion.")

/**
 * Stateful header reacts to scrolling but semi-independent.
 */
@Composable
fun CollapsibleHeaderLayout(
    collapsingHeader: @Composable ColumnScope.(collapseProgress: Float) -> Unit,
    lazyListContent: LazyListScope.() -> Unit,
    modifier: Modifier = Modifier,
    headerState: MutableState<ExpandCollapseState> = rememberExpandCollapseState(ExpandCollapseState.Expanded),
    transitionDef: TransitionDefinition<ExpandCollapseState> =
        rememberExpandCollapseTransition(key = HeaderExpandProgress),
    lazyListState : LazyListState = rememberLazyListState(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) {
    val transition = transition(transitionDef, headerState.value)
    val expandProgress = transition[HeaderExpandProgress]

    val controller = rememberScrollableController { scrollDelta ->
        val isScrollTowardsTopOfList = scrollDelta > 0F
        val isScrollTowardsBottomOfList = scrollDelta < 0F

        when {
            headerState.isExpanded && isScrollTowardsBottomOfList -> {
                headerState.collapse()
            }

            headerState.isCollapsed -> {
                if (isScrollTowardsTopOfList && lazyListState.isAtTop) {
                    headerState.expand()
                }

                else {
                    coroutineScope.launch {
                        lazyListState.scroll { scrollBy(-scrollDelta) }
                    }
                }
            }
        }
        0F
    }

    Box(modifier) {
        Column {
            collapsingHeader(expandProgress)

            LazyColumn(
                state = lazyListState,
                content = lazyListContent,
                modifier = Modifier.fillMaxWidth()
            )
        }

        TouchInterceptor(controller)
    }
}

/**
 * Directly tracks input
 */
@Composable
fun LinearCollapsibleHeaderLayout(
    collapsingHeader: @Composable (collapseProgress: Float) -> Unit,
    lazyListContent: LazyListScope.() -> Unit,
    modifier: Modifier = Modifier,
    touchInterceptModifier: Modifier = Modifier,
    lazyListState : LazyListState = rememberLazyListState(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
) {
    val headerState = remember { HeaderState() }

    /**
     * Run the block in the [ScrollScope] of [lazyListState], launched on [coroutineScope].
     */
    fun withScrollScope(block: ScrollScope.() -> Unit) {
        coroutineScope.launch {
            lazyListState.scroll {
                block()
            }
        }
    }

    fun dispatchScrollBy(delta: Float): Float {
        if (delta == 0F) return 0F

        val isScrollTowardsTopOfList = delta < 0F

        if (isScrollTowardsTopOfList) {
            // List has first chance to consume scroll
            withScrollScope {
                val consumed = scrollBy(delta)
                val unconsumed = delta - consumed

                headerState.scrollBy(unconsumed)
            }
        }

        else {
            // Header has first chance to consume scroll
            val unconsumed = headerState.scrollBy(delta)

            if (unconsumed != 0F) {
                withScrollScope {
                    scrollBy(unconsumed)
                }
            }
        }
        return delta
    }

    val controller = rememberScrollableController(consumeScrollDelta = ::dispatchScrollBy)

    Box(modifier) {
        Column {
            Layout(
                content = { collapsingHeader(headerState.expandProgress) }
            ) { measurables, constraints ->
                require(measurables.size == 1) { "CollapsibleHeaderLayout header should contain exactly one child" }

                val placeable = measurables[0].measure(constraints)

                headerState.updateMaxValue(placeable.height.toFloat())

                layout(placeable.width, placeable.height) {
                    placeable.placeRelative(0, 0)
                }
            }

            LazyColumn(
                state = lazyListState,
                content = lazyListContent,
                modifier = Modifier.fillMaxWidth()
            )
        }

        TouchInterceptor(controller, touchInterceptModifier)
    }
}

@Composable
private fun TouchInterceptor(
    controller: ScrollableController,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier
            .fillMaxSize()
            .scrollable(
                controller = controller,
                orientation = Orientation.Vertical,
                reverseDirection = true,
                onScrollStopped = {

                }
            )
            .zIndex(Float.MAX_VALUE)
    )
}

@Preview
@Composable
fun CollapsibleHeaderLayoutPreview() {
    val listItems = (1..100).toList()

    CommonsTheme {
        Box(Modifier.fillMaxSize()) {
            LinearCollapsibleHeaderLayout(
                collapsingHeader = { expandedProgress ->
                    Column {
                        Text("This should stay here", Modifier.colorize())
                        Box(
                            Modifier.colorize()
                                .height(300.dp * expandedProgress)
                        ) {
                            Text("This should collapse")
                        }
                    }
                },
                lazyListContent = {
                    items(
                        items = listItems,
                    ) { item ->
                        Text("$item", Modifier.padding(4.dp).colorize())
                    }
                }
            )
        }
    }
}

private val LazyListState.isAtTop get() =
    this.firstVisibleItemIndex == 0 && this.firstVisibleItemScrollOffset == 0

internal class HeaderState(
    initial: Float = 0F,
) {
    var value by mutableStateOf(initial)
    var maxValue: Float = Float.POSITIVE_INFINITY
        private set(newMax) {
            field = newMax
            if (value > newMax) {
                value = newMax
            }
        }

    val expandProgress: Float
        get() = when {
            maxValue == 0F || maxValue.isNaN() -> 0F
            else -> this.value / this.maxValue
        }.reversed()

    val isCollapsed: Boolean get() = expandProgress == 0F
    val isExpanded: Boolean get() = expandProgress == 1F

    /**
     * Only update maxValue if the new value is larger, or no value has been set yet (i.e. still has default INFINITY value)
     */
    fun updateMaxValue(newMaxValue: Float) {
        if (
            maxValue == Float.POSITIVE_INFINITY
            || newMaxValue > maxValue
        ) {
            maxValue = newMaxValue
        }
    }

    /**
     * Scroll by up to [delta] and return any unconsumed remainder.
     */
    fun scrollBy(delta: Float): Float {
        val rawResult = value + delta

        value = rawResult.coerceIn(0F, maxValue)

        return when {
            rawResult < 0F -> rawResult
            rawResult > maxValue -> rawResult - maxValue
            else -> 0F
        }
    }
}
