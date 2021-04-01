package org.beatonma.commons.compose.components.collapsibleheader

import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.ScrollScope
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.beatonma.commons.core.extensions.reversed
import kotlin.math.roundToInt


@Composable
fun rememberCollapsibleHeaderState(
    lazyListState: LazyListState,
    snapToStateAt: Float? = null,
) =
    remember {
        CollapsibleHeaderState(
            lazyListState,
            snapToStateAt,
        )
    }

/**
 * Based on implementation of [ScrollState].
 */
class CollapsibleHeaderState internal constructor(
    private val lazyListState: LazyListState,
    private val snapToStateAt: Float? = null,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main,
) : ScrollableState {
    var value: Int by mutableStateOf(0)
        private set

    private var _maxValue by mutableStateOf(Int.MIN_VALUE)
    var maxValue: Int
        get() = _maxValue
        set(newMax) {
            if (
                newMax > _maxValue
            ) {
                _maxValue = newMax
            }
        }

    val expansion: Float
        get() = 1F - (value / maxValue.toFloat()).coerceIn(0F, 1F)

    val isCollapsed: Boolean
        get() = value == 0

    val isExpanded: Boolean
        get() = value == maxValue

    val isSnappable: Boolean
        get() = snapToStateAt != null

    override val isScrollInProgress: Boolean
        get() = state.isScrollInProgress

    private var accumulator: Float = 0f
    private var sourceIsNestedScroll = false

    private val state = ScrollableState { raw ->
        if (raw == 0F) return@ScrollableState 0F

        val listFirst = !sourceIsNestedScroll && isExpanded

        val d = raw.reversed()
        val delta = when {
            listFirst -> {
                val consumedByList = lazyListState.dispatchRawDelta(d)
                d - consumedByList
            }
            else -> d
        }

        val absolute = value + delta + accumulator
        val newValue = absolute.coerceIn(0f, maxValue.toFloat())
        val valueOverflow = absolute != newValue

        val consumed = newValue - value
        val consumedInt = consumed.roundToInt()

        value += consumedInt
        accumulator = consumed - consumedInt

        when {
            valueOverflow -> consumed.reversed()
            else -> raw
        }
    }

    fun dispatchRawPreScroll(delta: Float): Float {
        sourceIsNestedScroll = true
        return dispatchRawDelta(delta)
    }

    fun finishNestedScroll() {
        sourceIsNestedScroll = false
    }

    override fun dispatchRawDelta(delta: Float): Float = state.dispatchRawDelta(delta)

    override suspend fun scroll(
        scrollPriority: MutatePriority,
        block: suspend ScrollScope.() -> Unit,
    ) {
        state.scroll(scrollPriority, block)
    }

    suspend fun expand() {
        withContext(dispatcher) {
            animateScrollBy((value).toFloat())
        }
    }

    suspend fun collapse() {
        withContext(dispatcher) {
            animateScrollBy((-(maxValue - value).toFloat()))
        }
    }

    suspend fun snap() {
        withContext(dispatcher) {
            check(snapToStateAt != null) { "Cannot snap(): CollapsibleHeaderState.snapToStateAt has not been set"}

            if (expansion > snapToStateAt) {
                expand()
            }
            else {
                collapse()
            }
        }
    }
}
