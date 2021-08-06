package org.beatonma.commons.compose.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.beatonma.commons.compose.R
import org.beatonma.commons.compose.ambient.animation
import org.beatonma.commons.compose.animation.AnimatedItemVisibility
import org.beatonma.commons.compose.animation.ExpandCollapseState
import org.beatonma.commons.compose.animation.animateExpansion
import org.beatonma.commons.compose.animation.rememberExpandCollapseState
import org.beatonma.commons.compose.animation.toggle
import org.beatonma.commons.compose.components.text.ComponentTitle
import org.beatonma.commons.compose.components.text.OptionalText
import org.beatonma.commons.compose.modifiers.design.colorize
import org.beatonma.commons.compose.modifiers.onlyWhen
import org.beatonma.commons.compose.modifiers.wrapContentHeight
import org.beatonma.commons.compose.util.rememberListOf
import org.beatonma.commons.core.extensions.progressIn
import org.beatonma.commons.core.extensions.reversed
import org.beatonma.commons.theme.compose.Size
import org.beatonma.commons.theme.compose.padding.Padding
import org.beatonma.commons.theme.compose.padding.padding
import org.beatonma.commons.theme.compose.theme.CommonsTheme
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
    ): HeaderBlock =
        { isCollapsible, transition, clickAction ->
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
                    .padding(Padding.VerticalListItem)
                    .semantics(mergeDescendants = true) {
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
                Column(
                    Modifier.onlyWhen(autoPadding) { padding(Padding.ScreenHorizontal) }
                ) {
                    ComponentTitle(title, autoPadding = false)
                    OptionalText(description)
                }

                if (isCollapsible) {
                    val iconRotation by animation.animateFloat(transition) {
                        when (it) {
                            ExpandCollapseState.Collapsed -> 0F
                            ExpandCollapseState.Expanded -> 180F
                        }
                    }
                    Box(
                        Modifier.size(Size.IconButton),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            Icons.Default.ArrowDropDown,
                            contentDescription = contentDescription,
                            Modifier.rotate(iconRotation)
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
    headerBlock: HeaderBlock,
    scrollable: Boolean,
    modifier: Modifier = Modifier,
    collapsedItemCount: Int = 3,
    state: MutableState<ExpandCollapseState> = rememberExpandCollapseState(ExpandCollapseState.Collapsed),
    itemBlock: ItemBlock<T>,
) {
    WithDisplayItems(items, collapsedItemCount, state) { displayItems, transition, isCollapsible, toggleAction ->
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

        val moreContent: @Composable () -> Unit = {
            if (isCollapsible) {
                MoreContentIndication(transition, toggleAction)
            }
        }

        if (scrollable) {
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
    modifier: Modifier = Modifier,
    collapsedItemCount: Int = 3,
    state: MutableState<ExpandCollapseState> = rememberExpandCollapseState(ExpandCollapseState.Collapsed),
    contentBlock: @Composable (List<T>) -> Unit,
) {
    WithDisplayItems(items, collapsedItemCount, state) { displayItems, transition, isCollapsible, toggleAction ->
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
    moreContent: @Composable () -> Unit,
    displayItems: List<T>,
    modifier: Modifier,
) {
    LazyColumn(
        modifier,
    ) {
        item { headerBlock() }

        itemsIndexed(displayItems) { index, item -> itemBlock(index, item) }

        item { moreContent() }
    }
}

@Composable
private fun <T> EagerContent(
    headerBlock: @Composable () -> Unit,
    itemBlock: @Composable (Int, T) -> Unit,
    moreContent: @Composable () -> Unit,
    displayItems: List<T>,
    modifier: Modifier,
) {
    Column(modifier) {
        headerBlock()

        displayItems.forEachIndexed { index, item -> itemBlock(index, item) }

        moreContent()
    }
}

/**
 * A visual indication that there is more content available.
 */
@Composable
private fun MoreContentIndication(
    transition: Transition<ExpandCollapseState>,
    onClick: () -> Unit
) {
    val visibility = transition.animateExpansion().value.reversed()
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
            .testTag("more_content_indication"),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            Icons.Default.MoreVert, // MoreHoriz not available..?
            contentDescription = contentDescription,
            Modifier
                .padding(Padding.IconSmall)
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
    state: MutableState<ExpandCollapseState>,
    block: @Composable (
        displayItems: List<T>,
        transition: Transition<ExpandCollapseState>,
        isCollapsible: Boolean,
        toggleAction: () -> Unit
    ) -> Unit,
) {
    val isCollapsible = items.size > collapsedItemCount
    val transition = updateTransition(state.value, label = "Display items transition")

    val toggleAction = { state.toggle() }

    val progress by transition.animateFloat(label = "Display items progress") { expansionState ->
        when (expansionState) {
            ExpandCollapseState.Expanded -> 1F
            ExpandCollapseState.Collapsed -> 0F
        }
    }
    val displayItemCount = collapsedItemCount + (
        progress * (items.size - collapsedItemCount)
        ).roundToInt()

    var displayItems by rememberListOf<T>()
    displayItems = items.take(displayItemCount)

    block(displayItems, transition, isCollapsible, toggleAction)
}

@Preview
@Composable
fun CollapsedColumnPreview() {
    CommonsTheme {
        CollapsedColumn(
            items = remember { (1..10).toList() },
            headerBlock = CollapsedColumn.simpleHeader("A wonderful group of integers"),
            scrollable = true,
            collapsedItemCount = 3,
        ) { integer ->
            Text(
                "$integer",
                Modifier
                    .colorize()
                    .padding(8.dp)
                    .fillMaxWidth()
            )
        }
    }
}
