package org.beatonma.commons.compose.components

import androidx.compose.animation.AnimatedFloatModel
import androidx.compose.animation.asDisposableClock
import androidx.compose.animation.core.AnimationClockObservable
import androidx.compose.animation.core.TargetAnimation
import androidx.compose.animation.core.fling
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.gesture.scrollorientationlocking.Orientation
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.AmbientAnimationClock
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.beatonma.commons.compose.modifiers.colorize
import org.beatonma.commons.core.extensions.reversed
import org.beatonma.commons.theme.compose.theme.CommonsDecay
import org.beatonma.commons.theme.compose.theme.CommonsSpring
import kotlin.math.absoluteValue
import kotlin.random.Random


/**
 * Directly tracks input
 */
@Composable
fun CollapsibleHeaderLayout(
    collapsingHeader: @Composable (collapseProgress: Float) -> Unit,
    lazyListContent: LazyListScope.() -> Unit,
    modifier: Modifier = Modifier,
    onScrollStarted: (startedPosition: Offset) -> Unit = {},
    onScrollStopped: (velocity: Float) -> Unit = {},
    touchInterceptModifier: Modifier = Modifier,
    headerState: CollapsibleHeaderState = collapsibleHeaderState(),
    lazyListState : LazyListState = rememberLazyListState(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
) {
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
                require(measurables.size == 1) {
                    "CollapsibleHeaderLayout header should contain exactly one child"
                }

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

        TouchInterceptor(
            controller, touchInterceptModifier,
            onScrollStarted = onScrollStarted,
            onScrollStopped = onScrollStopped,
        )
    }
}

@Composable
private fun TouchInterceptor(
    controller: ScrollableController,
    modifier: Modifier = Modifier,
    onScrollStarted: (startedPosition: Offset) -> Unit = {},
    onScrollStopped: (velocity: Float) -> Unit = {},
) {
    Box(
        modifier
            .fillMaxSize()
            .scrollable(
                controller = controller,
                orientation = Orientation.Vertical,
                reverseDirection = true,
                onScrollStarted = onScrollStarted,
                onScrollStopped = onScrollStopped,
            )
            .zIndex(Float.MAX_VALUE)
    )
}

@Preview
@Composable
fun CollapsibleHeaderLayoutPreview() {
    val listItems = (1..100).toList()
    val headerState = collapsibleHeaderState()

    Box(Modifier.fillMaxSize()) {
        CollapsibleHeaderLayout(
            modifier = Modifier
                .clickable {
                    println("Click")
                    if (Random.nextBoolean()) {
                        headerState.expand()
                    }
                    else {
                        headerState.collapse()
                    }
                },
            collapsingHeader = { expandedProgress ->
                Column {
                    Text(
                        "This should stay here",
                        Modifier
                            .colorize()
                            .height(80.dp)
                    )
                    Box(
                        Modifier
                            .colorize()
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
                    Text(
                        "$item",
                        Modifier
                            .padding(4.dp)
                            .colorize()
                    )
                }
            },
            headerState = headerState,
        )
    }
}

private val LazyListState.isAtTop get() =
    this.firstVisibleItemIndex == 0 && this.firstVisibleItemScrollOffset == 0

@Composable
fun collapsibleHeaderState(
    initial: Float = 0F,
    clock: AnimationClockObservable = AmbientAnimationClock.current.asDisposableClock(),
) = remember {
    CollapsibleHeaderState(
        initial,
        clock
    )
}

class CollapsibleHeaderState internal constructor(
    initial: Float,
    clock: AnimationClockObservable,
) {
    val value = AnimatedFloatModel(
        initialValue = initial,
        clock = clock,
    )
    var maxValue: Float = Float.POSITIVE_INFINITY
        private set(newMax) {
            field = newMax
            value.setBounds(0F, newMax)
        }

    val expandProgress: Float
        get() = when {
            maxValue == 0F || maxValue.isNaN() -> 0F
            else -> this.value.value / this.maxValue
        }.reversed()

    val isCollapsed: Boolean get() = expandProgress == 0F
    val isExpanded: Boolean get() = expandProgress == 1F

    fun expand() {
        value.animateTo(0F)
    }

    fun collapse() {
        value.animateTo(maxValue)
    }

    fun expand(velocity: Float) {
        value.fling(
            velocity,
            adjustTarget = {
                TargetAnimation(0F, CommonsSpring())
            },
            decay = CommonsDecay(),
        )
    }

    fun collapse(velocity: Float) {
        value.fling(
            velocity,
            adjustTarget = {
                TargetAnimation(maxValue, CommonsSpring())
            },
            decay = CommonsDecay(),
        )
    }

    /**
     * Only update maxValue if the new value is larger, or no value has been set yet
     * (i.e. still has default INFINITY value)
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
        if (value.isRunning && value.velocity.absoluteValue < 20F) {
            return 0F
        }

        val rawResult = value.value + delta

        value.snapTo(rawResult.coerceIn(0F, maxValue))

        return when {
            rawResult < 0F -> rawResult
            rawResult > maxValue -> rawResult - maxValue
            else -> 0F
        }
    }
}
