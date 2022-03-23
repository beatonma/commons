package org.beatonma.commons.compose.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import org.beatonma.commons.compose.R
import org.beatonma.commons.compose.TestTag
import org.beatonma.commons.compose.UiIcon
import org.beatonma.commons.compose.animation.AnimatedItemVisibility
import org.beatonma.commons.compose.animation.ExpandCollapseState
import org.beatonma.commons.compose.animation.animateExpansion
import org.beatonma.commons.compose.animation.rememberExpandCollapseState
import org.beatonma.commons.compose.components.text.ComponentTitle
import org.beatonma.commons.compose.components.text.OptionalText
import org.beatonma.commons.compose.modifiers.onlyWhen
import org.beatonma.commons.compose.modifiers.wrapContentHeight
import org.beatonma.commons.compose.padding.padding
import org.beatonma.commons.compose.util.rememberListOf
import org.beatonma.commons.core.extensions.progressIn
import org.beatonma.commons.core.extensions.reversed
import org.beatonma.commons.themed.animation
import org.beatonma.commons.themed.padding
import org.beatonma.commons.themed.size
import kotlin.math.roundToInt

private typealias HeaderBlock = @Composable (
    isCollapsible: Boolean,
    transition: Transition<ExpandCollapseState>,
    onClick: () -> Unit,
) -> Unit

private typealias ItemBlock<T> = @Composable (T) -> Unit

object CollapsedColumn {
    fun simpleHeader(
        title: String,
        description: String? = null,
        autoPadding: Boolean = true,
        modifier: Modifier = Modifier,
    ): HeaderBlock = { isCollapsible, transition, clickAction ->
        val contentDescription: String? =
            when {
                !isCollapsible -> null
                transition.currentState.isExpanded -> stringResource(R.string.content_description_show_less)
                transition.currentState.isCollapsed -> stringResource(R.string.content_description_show_more)
                else -> null
            }

        Row(
            modifier
                .fillMaxWidth()
                .padding(padding.VerticalListItem)
                .semantics(mergeDescendants = true) {
                    heading()
                    this.contentDescription = if (contentDescription == null) {
                        title
                    } else {
                        "$title: $contentDescription"
                    }
                }
                .onlyWhen(isCollapsible) {
                    clickable(onClick = clickAction, onClickLabel = contentDescription)
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            HeaderText(title, description, autoPadding, Modifier)

            if (isCollapsible) {
                CollapseIcon(transition, contentDescription)
            }
        }
    }

    @Composable
    private fun HeaderText(
        title: String,
        description: String?,
        autoPadding: Boolean,
        modifier: Modifier,
    ) {
        Column(
            modifier.onlyWhen(autoPadding) { padding(padding.ScreenHorizontal) }
        ) {
            ComponentTitle(title, autoPadding = false)
            OptionalText(description)
        }
    }

    @Composable
    private fun CollapseIcon(
        transition: Transition<ExpandCollapseState>,
        contentDescription: String?,
    ) {
        val iconRotation by transition.animateFloat(
            transitionSpec = { animation.spec() },
            label = "AnimatedShowMoreLess",
        ) { state ->
            when (state) {
                ExpandCollapseState.Collapsed -> 0F
                ExpandCollapseState.Expanded -> 180F
            }
        }

        Box(
            Modifier.size(size.IconButton),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                UiIcon.DropDown,
                contentDescription = contentDescription,
                Modifier.rotate(iconRotation)
            )
        }
    }
}

class CollapsedColumnState<T> internal constructor(
    items: List<T>,
    val collapsedItemCount: Int = 3,
) {
    private val _items = items
    var items: List<T> by mutableStateOf(listOf())
    var state by mutableStateOf(ExpandCollapseState.Collapsed)
    val isCollapsible = items.size > collapsedItemCount
    val toggleState = { state = state.toggle() }
    private var displayItemCount by mutableStateOf(0)
    lateinit var transition: Transition<ExpandCollapseState>

    @Composable
    fun update() {
        transition = updateTransition(state, "CollapsedColumnState")

        val progress by transition.animateExpansion()

        LaunchedEffect(progress) {
            displayItemCount = collapsedItemCount + (
                    progress * (_items.size - collapsedItemCount)
                    ).roundToInt()
        }

        LaunchedEffect(displayItemCount) {
            items = _items.take(displayItemCount)
        }
    }
}

@Composable
fun <T> rememberCollapsedColumnState(
    items: List<T>,
    collapsedItemCount: Int = 3,
) = remember { CollapsedColumnState(items, collapsedItemCount) }

@OptIn(ExperimentalFoundationApi::class)
fun <T> LazyListScope.CollapsedColumn(
    state: CollapsedColumnState<T>,
    headerBlock: HeaderBlock,
    itemBlock: ItemBlock<T>,
) {

    item {
        state.update()

        headerBlock(state.isCollapsible, state.transition, state.toggleState)
    }

    itemsIndexed(state.items) { index, item ->
        animation.AnimatedItemVisibility(
            visible = true,
            initiallyVisible = index < state.collapsedItemCount,
            position = index
        ) {
            itemBlock(item)
        }
    }

    if (state.isCollapsible) {
        item {
            MoreContentIndication(state.transition, state.toggleState)
        }
    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun <T> CollapsedColumn(
    items: List<T>,
    headerBlock: HeaderBlock,
    lazy: Boolean,
    modifier: Modifier = Modifier,
    collapsedItemCount: Int = 3,
    itemBlock: ItemBlock<T>,
) {
    var state by rememberExpandCollapseState(ExpandCollapseState.Collapsed)
    WithDisplayItems(
        items,
        collapsedItemCount,
        state,
        { state = it },
    ) { displayItems, transition, isCollapsible, toggleAction ->
        val header: @Composable () -> Unit = {
            headerBlock(isCollapsible, transition, toggleAction)
        }

        val item: @Composable (Int, T) -> Unit = { index, item ->
            animation.AnimatedItemVisibility(
                visible = true,
                initiallyVisible = index < collapsedItemCount,
                position = index
            ) {
                itemBlock(item)
            }
        }

        val moreContent: (@Composable () -> Unit)? = if (isCollapsible) {
            { MoreContentIndication(transition, toggleAction) }
        } else null

        if (lazy) {
            LazyContent(
                headerBlock = header,
                itemBlock = item,
                moreContent = moreContent,
                displayItems = displayItems,
                modifier = modifier,
            )
        } else {
            EagerContent(
                headerBlock = header,
                itemBlock = item,
                moreContent = moreContent,
                displayItems = displayItems,
                modifier = modifier,
            )
        }
    }
}

/**
 * A CollapsedColumn wrapper for a custom content block.
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun <T> CollapsedColumn(
    items: List<T>,
    headerBlock: HeaderBlock,
    state: ExpandCollapseState,
    onStateChange: (ExpandCollapseState) -> Unit,
    modifier: Modifier = Modifier,
    collapsedItemCount: Int = 3,
    contentBlock: @Composable (List<T>) -> Unit,
) {
    WithDisplayItems(
        items,
        collapsedItemCount,
        state,
        onStateChange,
    ) { displayItems, transition, isCollapsible, toggleAction ->
        Column(modifier) {
            headerBlock(isCollapsible, transition, toggleAction)

            contentBlock(displayItems)

            if (isCollapsible) {
                MoreContentIndication(transition, toggleAction)
            }
        }
    }
}

@Composable
private fun <T> LazyContent(
    headerBlock: @Composable () -> Unit,
    itemBlock: @Composable (Int, T) -> Unit,
    moreContent: (@Composable () -> Unit)?,
    displayItems: List<T>,
    modifier: Modifier,
) {
    LazyColumn(
        modifier,
    ) {
        item { headerBlock() }

        itemsIndexed(displayItems) { index, item -> itemBlock(index, item) }

        if (moreContent != null) {
            item { moreContent() }
        }
    }
}

@Composable
private fun <T> EagerContent(
    headerBlock: @Composable () -> Unit,
    itemBlock: @Composable (Int, T) -> Unit,
    moreContent: (@Composable () -> Unit)?,
    displayItems: List<T>,
    modifier: Modifier,
) {
    Column(modifier) {
        headerBlock()

        displayItems.forEachIndexed { index, item -> itemBlock(index, item) }

        moreContent?.invoke()
    }
}

/**
 * A visual indication that there is more content available.
 */
@Composable
private fun MoreContentIndication(
    transition: Transition<ExpandCollapseState>,
    onClick: () -> Unit,
) {
    val visibility = transition.animateExpansion().value.reversed()
    if (visibility == 0f) return

    val contentDescription = stringResource(R.string.content_description_show_more)

    Box(
        Modifier
            .fillMaxWidth()
            .alpha(visibility.progressIn(0.5F, 1F))
            .wrapContentHeight(visibility.progressIn(0F, 0.8F))
            .clickable(
                onClick = onClick,
                role = Role.Button,
                onClickLabel = contentDescription
            )
            .testTag(TestTag.ShowMore),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            UiIcon.EllipsisVertical,
            contentDescription = contentDescription,
            Modifier
                .padding(padding.IconSmall)
                .rotate(90F)
        )
    }
}

/**
 * Calculate how many items need to be visible given the current [state] and call [block] with the result.
 */
@Composable
private inline fun <T> WithDisplayItems(
    items: List<T>,
    collapsedItemCount: Int,
    state: ExpandCollapseState,
    crossinline onStateChange: (ExpandCollapseState) -> Unit,
    block: @Composable (
        displayItems: List<T>,
        transition: Transition<ExpandCollapseState>,
        isCollapsible: Boolean,
        toggleAction: () -> Unit,
    ) -> Unit,
) {
    var displayItems by rememberListOf<T>()
    val isCollapsible = items.size > collapsedItemCount
    val transition = updateTransition(state, label = "Display items transition")

    val toggleAction: () -> Unit = { onStateChange(state.toggle()) }

    val progress by transition.animateExpansion()
    val displayItemCount = collapsedItemCount + (
            progress * (items.size - collapsedItemCount)
            ).roundToInt()

    LaunchedEffect(displayItemCount) {
        displayItems = items.take(displayItemCount)
    }

    block(displayItems, transition, isCollapsible, toggleAction)
}
