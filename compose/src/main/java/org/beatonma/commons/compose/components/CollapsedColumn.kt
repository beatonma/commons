package org.beatonma.commons.compose.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FloatPropKey
import androidx.compose.animation.core.TransitionDefinition
import androidx.compose.animation.core.TransitionState
import androidx.compose.animation.transition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.onActive
import androidx.compose.runtime.onDispose
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import org.beatonma.commons.compose.animation.ExpandCollapseState
import org.beatonma.commons.compose.animation.VisibilityState
import org.beatonma.commons.compose.animation.hide
import org.beatonma.commons.compose.animation.progressKey
import org.beatonma.commons.compose.animation.rememberExpandCollapseState
import org.beatonma.commons.compose.animation.rememberVisibilityState
import org.beatonma.commons.compose.animation.show
import org.beatonma.commons.compose.animation.toggle
import org.beatonma.commons.compose.animation.twoStateProgressTransition
import org.beatonma.commons.compose.modifiers.withNotNull
import org.beatonma.commons.compose.modifiers.wrapContentHeight
import org.beatonma.commons.compose.util.rememberListOf
import org.beatonma.commons.core.extensions.fastForEach
import org.beatonma.commons.core.extensions.reversed
import org.beatonma.commons.core.extensions.withNotNull
import org.beatonma.commons.theme.compose.Padding
import org.beatonma.commons.theme.compose.Size
import org.beatonma.commons.theme.compose.components.ComponentTitle
import org.beatonma.commons.theme.compose.theme.CommonsFastSpring
import org.beatonma.commons.theme.compose.theme.CommonsFastTween
import org.beatonma.commons.theme.compose.theme.CommonsSpring
import kotlin.math.roundToInt

private val itemVisibilityKey = FloatPropKey("Track item alpha")
typealias CollapsedColumnHeaderBlock = @Composable (isCollapsible: Boolean, transitionState: TransitionState, clickAction: (() -> Unit)?) -> Unit

object CollapsedColumn {
    fun simpleHeader(
        title: String,
        description: String? = null,
        modifier: Modifier = Modifier,
    ): CollapsedColumnHeaderBlock =
        { isCollapsible, transitionState, clickAction ->
            Row(
                modifier
                    .fillMaxWidth()
                    .padding(Padding.VerticalListItemLarge)
                    .withNotNull(clickAction) {
                        clickable(onClick = it)
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(Modifier.padding(Padding.ScreenHorizontal)) {
                    ComponentTitle(title, autoPadding = false)
                    withNotNull(description) {
                        Text(it)
                    }
                }

                if (isCollapsible) {
                    Box(
                        Modifier.preferredSize(Size.IconButton),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.ArrowDropDown,
                            Modifier.rotate(transitionState[progressKey] * 180F)
                        )
                    }
                }
            }
        }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun <T> CollapsedColumn(
    items: List<T>,
    headerBlock: @Composable (isCollapsible: Boolean, transitionState: TransitionState, clickAction: (() -> Unit)?) -> Unit,
    modifier: Modifier = Modifier,
    collapsedItemCount: Int = 3,
    state: MutableState<ExpandCollapseState> = rememberExpandCollapseState(ExpandCollapseState.Collapsed),
    itemBlock: @Composable (T, Modifier) -> Unit,
) {
    val isCollapsible = items.size > collapsedItemCount

    val transitionDef = rememberCollapsedColumnTransition()
    val transition = transition(transitionDef, state.value)

    val itemTransitionDef = rememberItemTransition()

    val toggleAction = { state.toggle() }

    BaseCollapsibleColumn(items, transition, collapsedItemCount) { displayItems ->
        Column(modifier) {
            headerBlock(
                isCollapsible,
                transition,
                toggleAction
            )

            displayItems.fastForEach { item ->
                AnimatedItem(item = item,
                    itemTransitionDef = itemTransitionDef,
                    itemBlock = itemBlock)
            }

            if (isCollapsible) {
                MoreContentIndication(transition, toggleAction)
            }
        }
    }
}

@Composable
fun <T> LazyCollapsedColumn(
    items: List<T>,
    headerBlock: @Composable (isCollapsible: Boolean, transitionState: TransitionState, clickAction: (() -> Unit)?) -> Unit,
    modifier: Modifier = Modifier,
    collapsedItemCount: Int = 3,
    state: MutableState<ExpandCollapseState> = rememberExpandCollapseState(ExpandCollapseState.Collapsed),
    itemBlock: @Composable (T, Modifier) -> Unit,
) {
    val isCollapsible = items.size > collapsedItemCount

    val transitionDef = rememberCollapsedColumnTransition()
    val transition = transition(transitionDef, state.value)

    val itemTransitionDef = rememberItemTransition()

    BaseCollapsibleColumn(items, transition, collapsedItemCount) { displayItems ->
        LazyColumn(
            modifier,
        ) {
            item {
                headerBlock(
                    isCollapsible,
                    transition
                ) { state.toggle() }
            }

            this.items(displayItems) { item ->
                AnimatedItem(
                    item = item,
                    itemTransitionDef = itemTransitionDef,
                    itemBlock = itemBlock,
                )
            }
        }
    }
}

@Composable
private fun <T> BaseCollapsibleColumn(
    items: List<T>,
    transitionState: TransitionState,
    collapsedItemCount: Int,
    content: @Composable (List<T>) -> Unit
) {
    var displayItems by rememberListOf<T>()

    val visibleItems = collapsedItemCount + (
            transitionState[progressKey] * (items.size - collapsedItemCount)
            ).roundToInt()

    displayItems = items.take(visibleItems)

    content(displayItems)
}

@Composable
private fun <T> AnimatedItem(
    item: T,
    itemTransitionDef: TransitionDefinition<VisibilityState>,
    itemBlock: @Composable (T, Modifier) -> Unit,
) {
    Box {
        val visibility = rememberVisibilityState(VisibilityState.Gone)

        onActive {
            visibility.show()
        }

        onDispose {
            visibility.hide()
        }

        val itemTransition = transition(itemTransitionDef, visibility.value)

        itemBlock(
            item,
            Modifier
                .alpha(itemTransition[itemVisibilityKey])
        )
    }
}

/**
 * Collapse animation is much faster than expand animation.
 */
@Composable
private fun rememberCollapsedColumnTransition() = remember {
    twoStateProgressTransition(
        defaultState = ExpandCollapseState.Collapsed,
        altState = ExpandCollapseState.Expanded,
        toAnimSpec = CommonsSpring(),
        returnAnimSpec = CommonsFastSpring(),
    )
}

@Composable
private fun rememberItemTransition() = remember {
    twoStateProgressTransition(
        defaultState = VisibilityState.Gone,
        altState = VisibilityState.Visible,
        animSpec = CommonsFastTween(),
        key = itemVisibilityKey,
    )
}

/**
 * A visual indication that there is more content available.
 */
@Composable
private fun MoreContentIndication(transitionState: TransitionState, onClick: () -> Unit) {
    val visibility = transitionState[progressKey].reversed()

    Box(
        Modifier
            .fillMaxWidth()
            .alpha(visibility)
            .wrapContentHeight(visibility)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            Icons.Default.MoreVert, // MoreHoriz not available..?
            Modifier
                .padding(Padding.IconSmall)
                .rotate(90F)
        )
    }
}
