package org.beatonma.commons.compose.components

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.beatonma.commons.compose.R
import org.beatonma.commons.compose.animation.ExpandCollapseState
import org.beatonma.commons.compose.animation.animateExpansion
import org.beatonma.commons.compose.animation.rememberExpandCollapseState
import org.beatonma.commons.compose.animation.toggle
import org.beatonma.commons.compose.modifiers.design.colorize
import org.beatonma.commons.compose.modifiers.withNotNull
import org.beatonma.commons.compose.modifiers.wrapContentHeight
import org.beatonma.commons.compose.util.rememberListOf
import org.beatonma.commons.core.extensions.progressIn
import org.beatonma.commons.core.extensions.reversed
import org.beatonma.commons.core.extensions.withNotNull
import org.beatonma.commons.theme.compose.padding.Padding
import org.beatonma.commons.theme.compose.padding.padding
import org.beatonma.commons.theme.compose.Size
import org.beatonma.commons.theme.compose.components.ComponentTitle
import org.beatonma.commons.theme.compose.theme.CommonsTheme
import kotlin.math.roundToInt

private typealias HeaderBlock = @Composable (
    isCollapsible: Boolean,
    transition: Transition<ExpandCollapseState>,
    clickAction: (() -> Unit)?
) -> Unit

private typealias ItemBlock<T> = @Composable (T) -> Unit


object CollapsedColumn {
    fun simpleHeader(
        title: String,
        description: String? = null,
        modifier: Modifier = Modifier,
    ): HeaderBlock =
        { isCollapsible, transition, clickAction ->
            val contentDescription = stringResource(
                when (transition.currentState) {
                    ExpandCollapseState.Expanded -> R.string.content_description_show_less
                    ExpandCollapseState.Collapsed -> R.string.content_description_show_more
                }
            )

            Row(
                modifier
                    .fillMaxWidth()
                    .padding(Padding.VerticalListItemLarge)
                    .withNotNull(clickAction) {
                        clickable(onClick = it, onClickLabel = contentDescription)
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(
                    Modifier
                        .padding(Padding.ScreenHorizontal)
                ) {
                    ComponentTitle(title, autoPadding = false)
                    withNotNull(description) {
                        Text(it)
                    }
                }

                if (isCollapsible) {
                    val iconRotation by transition.animateFloat {
                        when (it) {
                            ExpandCollapseState.Collapsed -> 0F
                            ExpandCollapseState.Expanded -> 180F
                        }
                    }
                    Box(
                        Modifier.size(Size.IconButton),
                        contentAlignment = Alignment.Center
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
    modifier: Modifier = Modifier,
    collapsedItemCount: Int = 3,
    state: MutableState<ExpandCollapseState> = rememberExpandCollapseState(ExpandCollapseState.Collapsed),
    itemBlock: ItemBlock<T>,
) {
    val isCollapsible = items.size > collapsedItemCount
    val transition = updateTransition(state.value)

    val toggleAction = { state.toggle() }

    var displayItems by rememberListOf<T>()
    val progress by transition.animateFloat {
        when (it) {
            ExpandCollapseState.Expanded -> 1F
            ExpandCollapseState.Collapsed -> 0F
        }
    }
    val displayItemCount = collapsedItemCount + (
            progress * (items.size - collapsedItemCount)
            ).roundToInt()
    displayItems = items.take(displayItemCount)

    CollapsibleColumnLayout(
        displayItems,
        modifier,
        headerBlock = {
            headerBlock(
                isCollapsible,
                transition,
                toggleAction
            )
        },
        moreContentIndication = {
            if (isCollapsible) {
                MoreContentIndication(transition, toggleAction)
            }
        }
    ) { item, index ->
        AnimatedVisibility(
            visible = true,
            initiallyVisible = index < collapsedItemCount
        ) {
            itemBlock(item)
        }
    }
}

@Composable
private fun <T> CollapsibleColumnLayout(
    displayItems: List<T>,
    modifier: Modifier,
    headerBlock: @Composable () -> Unit,
    moreContentIndication: @Composable () -> Unit,
    itemBlock: @Composable (T, Int) -> Unit
) {
    LazyColumn(
        modifier,
    ) {
        item {
            headerBlock()
        }

        itemsIndexed(displayItems) { index, item ->
            itemBlock(item, index)
        }

        item {
            moreContentIndication()
        }
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

@Preview
@Composable
fun CollapsedColumnPreview() {
    CommonsTheme {
        CollapsedColumn(
            items = remember { (1..10).toList() },
            headerBlock = CollapsedColumn.simpleHeader("A wonderful group of integers"),
            collapsedItemCount = 3
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
