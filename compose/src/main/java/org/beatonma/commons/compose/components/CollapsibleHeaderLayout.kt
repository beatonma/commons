package org.beatonma.commons.compose.components

import androidx.annotation.FloatRange
import androidx.compose.animation.AnimatedFloatModel
import androidx.compose.animation.asDisposableClock
import androidx.compose.animation.core.AnimationClockObservable
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.TargetAnimation
import androidx.compose.animation.core.fling
import androidx.compose.foundation.Interaction
import androidx.compose.foundation.InteractionState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberScrollableController
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.gesture.nestedscroll.NestedScrollConnection
import androidx.compose.ui.gesture.nestedscroll.NestedScrollDispatcher
import androidx.compose.ui.gesture.nestedscroll.NestedScrollSource
import androidx.compose.ui.gesture.nestedscroll.nestedScroll
import androidx.compose.ui.gesture.scrollorientationlocking.Orientation
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.AmbientAnimationClock
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.beatonma.commons.core.extensions.inverted
import org.beatonma.commons.core.extensions.reversed
import org.beatonma.commons.core.extensions.withEasing
import org.beatonma.commons.theme.compose.EndOfContent
import org.beatonma.commons.theme.compose.theme.CommonsDecay
import org.beatonma.commons.theme.compose.theme.CommonsSpring
import kotlin.math.absoluteValue
import kotlin.random.Random


@Composable
fun CollapsibleHeaderLayout(
    collapsingHeader: @Composable (collapseProgress: Float) -> Unit,
    lazyListContent: LazyListScope.() -> Unit,
    modifier: Modifier = Modifier,
    onScrollStarted: (startedPosition: Offset) -> Unit = {},
    onScrollStopped: (velocity: Float) -> Unit = {},
    headerScrollEasing: Easing = FastOutSlowInEasing,
    interactionState: InteractionState = remember(::InteractionState),
    @FloatRange(from = 0.0, to = 1.0) snapToStateAt: Float? = null,
    headerState: CollapsibleHeaderState =
        collapsibleHeaderState(easing = headerScrollEasing, autoSnap = snapToStateAt),
    lazyListState: LazyListState = rememberLazyListState(interactionState = interactionState),
    scrollEnabled: Boolean = true,
    appendEndOfContentSpacing: Boolean = true,
) {
    val resolvedOnScrollStopped: (Float) -> Unit = when {
        snapToStateAt != null -> {
            { velocity ->
                headerState.snap()
                onScrollStopped(velocity)
            }
        }
        else -> onScrollStopped
    }

    OnInteractionFinished(interactionState) { if (headerState.isSnappable) headerState.snap() }

    Column(
        modifier
            .fillMaxSize()
            .interceptScrolling(
                headerState = headerState,
                interactionState = interactionState,
                onScrollStarted = onScrollStarted,
                onScrollStopped = resolvedOnScrollStopped,
                scrollEnabled = scrollEnabled,
            ),
    ) {
        Layout(
            content = { collapsingHeader(headerState.expandProgress) },
            modifier = Modifier,
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

        val resolvedListContent = when {
            appendEndOfContentSpacing -> {
                {
                    lazyListContent()
                    item { EndOfContent() }
                }
            }
            else -> lazyListContent
        }

        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .fillMaxSize(),
            content = resolvedListContent
        )
    }
}

@Composable
private fun OnInteractionFinished(interactionState: InteractionState, block: () -> Unit) {
    val previousInteractionState: MutableState<Set<Interaction>> = remember { mutableStateOf(setOf()) }
    if (interactionState.value.isEmpty() && previousInteractionState.value.isNotEmpty()) {
        block()
    }
    previousInteractionState.value = interactionState.value
}

@Composable
private fun Modifier.interceptScrolling(
    headerState: CollapsibleHeaderState,
    interactionState: InteractionState,
    onScrollStarted: (startedPosition: Offset) -> Unit,
    onScrollStopped: (velocity: Float) -> Unit,
    scrollEnabled: Boolean = true,
): Modifier {
    val scrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val availableY = available.y
                val isScrollTowardsTopOfList = availableY < 0F

                return when {
                    isScrollTowardsTopOfList -> {
                        val unconsumedY = headerState.scrollBy(available.y)

                        return available.copy(y = available.y - unconsumedY)
                    }
                    else -> super.onPreScroll(available, source)
                }
            }

            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource,
            ): Offset {
                val unconsumed = headerState.scrollBy(available.y)
                val consumedY = available.y - unconsumed

                return available.copy(y = consumedY)
            }
        }
    }



    val dispatcher = remember(::NestedScrollDispatcher)
    val controller = rememberScrollableController(interactionState = interactionState) { 0F }

    return this
        .nestedScroll(
            connection = scrollConnection,
            dispatcher = dispatcher,
        )
        .scrollable(
            Orientation.Vertical,
            controller = controller,
            onScrollStarted = onScrollStarted,
            onScrollStopped = onScrollStopped,
            enabled = scrollEnabled,
        )

}

@Composable
fun collapsibleHeaderState(
    initial: Float = 0F,
    clock: AnimationClockObservable = AmbientAnimationClock.current.asDisposableClock(),
    easing: Easing = FastOutSlowInEasing,
    autoSnap: Float? = null,
) = remember {
    CollapsibleHeaderState(
        initial,
        clock,
        easing = easing,
        autoSnap = autoSnap,
    )
}

class CollapsibleHeaderState internal constructor(
    initial: Float,
    clock: AnimationClockObservable,
    private val easing: (Float) -> Float = FastOutSlowInEasing,
    private val reverseScrollDirection: Boolean = true,
    private val autoSnap: Float? = null,
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
            else -> (this.value.value / this.maxValue).withEasing(easing)
        }.reversed()

    val isCollapsed: Boolean get() = expandProgress == 0F
    val isExpanded: Boolean get() = expandProgress == 1F
    val isSnappable: Boolean get() = autoSnap != null

    fun expand() {
        value.animateTo(0F)
    }

    fun collapse() {
        value.animateTo(maxValue)
    }

    fun snap() {
        check(autoSnap != null) { "Cannot snap(): CollapsibleHeaderState.autoSnap has not been set" }
        if (value.isRunning) return

        if (expandProgress > autoSnap) {
            expand()
        } else {
            collapse()
        }
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

        val resolvedDelta = if (reverseScrollDirection) delta.inverted() else delta
        val rawResult = value.value + resolvedDelta

        value.snapTo(rawResult.coerceIn(0F, maxValue))

        return when {
            rawResult < 0F -> rawResult
            rawResult > maxValue -> {
                val unconsumed = (rawResult - maxValue)
                return if (reverseScrollDirection) unconsumed.inverted() else unconsumed
            }
            else -> 0F
        }
    }
}


@Preview
@Composable
private fun CollapsibleHeaderLayoutPreview() {
    val listItems = (1..100).toList()
    val headerState = collapsibleHeaderState()

    Box(Modifier.fillMaxSize()) {
        CollapsibleHeaderLayout(
            modifier = Modifier
                .clickable {
                    println("Click")
                    if (Random.nextBoolean()) {
                        headerState.expand()
                    } else {
                        headerState.collapse()
                    }
                },
            collapsingHeader = { expandedProgress ->
                Column {
                    Text(
                        "This should stay here",
                        Modifier
                            .height(80.dp)
                    )
                    Box(
                        Modifier
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
                    )
                }
            },
            headerState = headerState,
        )
    }
}
