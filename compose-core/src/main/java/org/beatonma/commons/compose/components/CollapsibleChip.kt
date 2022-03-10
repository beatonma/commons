package org.beatonma.commons.compose.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.beatonma.commons.compose.R
import org.beatonma.commons.compose.TestTag
import org.beatonma.commons.compose.UiIcon
import org.beatonma.commons.compose.ambient.LocalAccessibility
import org.beatonma.commons.compose.animation.AutoCollapse
import org.beatonma.commons.compose.animation.ExpandCollapseState
import org.beatonma.commons.compose.animation.isExpanded
import org.beatonma.commons.compose.animation.rememberExpandCollapseState
import org.beatonma.commons.compose.animation.toggle
import org.beatonma.commons.compose.modifiers.onlyWhen
import org.beatonma.commons.compose.modifiers.wrapContentWidth
import org.beatonma.commons.compose.padding.padding
import org.beatonma.commons.core.extensions.progressIn
import org.beatonma.commons.themed.themedAnimation
import org.beatonma.commons.themed.themedSize


@Composable
fun CollapsibleChip(
    text: AnnotatedString,
    contentDescription: String?,
    @DrawableRes drawableId: Int,
    modifier: Modifier = Modifier,
    cancelIcon: ImageVector = UiIcon.Close,
    tint: Color = LocalContentColor.current.copy(alpha = ContentAlpha.medium),
    autoCollapse: Long = AutoCollapse.Default,
    confirmAction: () -> Unit,
) {
    BaseCollapsibleChip(
        text,
        contentDescription,
        painterResource(drawableId),
        rememberVectorPainter(cancelIcon),
        modifier,
        tint,
        autoCollapse,
        confirmAction = confirmAction
    )
}

@Composable
fun CollapsibleChip(
    text: AnnotatedString,
    contentDescription: String?,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    cancelIcon: ImageVector = UiIcon.Close,
    tint: Color = LocalContentColor.current.copy(alpha = ContentAlpha.medium),
    autoCollapse: Long = AutoCollapse.Default,
    confirmAction: () -> Unit,
) {
    BaseCollapsibleChip(
        text,
        contentDescription,
        rememberVectorPainter(icon),
        rememberVectorPainter(cancelIcon),
        modifier,
        tint,
        autoCollapse,
        confirmAction = confirmAction,
    )
}

@Composable
fun CollapsibleChip(
    text: @Composable (Modifier) -> Unit,
    contentDescription: String?,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    cancelIcon: ImageVector = UiIcon.Close,
    tint: Color = LocalContentColor.current.copy(alpha = ContentAlpha.medium),
    autoCollapse: Long = AutoCollapse.Default,
    confirmAction: () -> Unit,
) {
    BaseCollapsibleChip(
        text,
        contentDescription,
        rememberVectorPainter(icon),
        rememberVectorPainter(cancelIcon),
        modifier,
        tint,
        autoCollapse,
        confirmAction = confirmAction,
    )
}


@Composable
private fun BaseCollapsibleChip(
    text: AnnotatedString,
    contentDescription: String?,
    icon: Painter,
    cancelIcon: Painter,
    modifier: Modifier,
    tint: Color,
    autoCollapse: Long,
    confirmAction: () -> Unit,
) {
    BaseCollapsibleChip(
        { textModifier -> Text(text, textModifier, maxLines = 1) },
        contentDescription,
        icon,
        cancelIcon,
        modifier,
        tint,
        autoCollapse,
        confirmAction = confirmAction,
    )
}

@Composable
private fun BaseCollapsibleChip(
    text: @Composable (Modifier) -> Unit,
    contentDescription: String?,
    icon: Painter,
    cancelIcon: Painter,
    modifier: Modifier,
    tint: Color,
    autoCollapse: Long,
    confirmAction: () -> Unit,
) {
    if (LocalAccessibility.current) {
        AccessibleChipLayout(
            text = text,
            contentDescription = contentDescription,
            icon = icon,
            modifier = modifier,
            tint = tint,
            confirmAction = confirmAction
        )
        return
    }

    val state = rememberExpandCollapseState()

    val scope = rememberCoroutineScope()
    var job: Job? by remember { mutableStateOf(null) }

    val toggleState = {
        job?.cancel()
        state.toggle()
        if (autoCollapse > 0) {
            if (state.isExpanded) {
                job = scope.launch {
                    delay(autoCollapse)
                    if (isActive) {
                        state.value = ExpandCollapseState.Collapsed
                    }
                }
            }
        }
    }

    val animationProgress by themedAnimation.animateFloatAsState(
        if (state.isExpanded) 1F else 0F
    )

    CollapsibleChipLayout(
        animationProgress = animationProgress,
        state = state.value,
        text = text,
        contentDescription = contentDescription,
        icon = icon,
        cancelIcon = cancelIcon,
        modifier = modifier,
        tint = tint,
        confirmAction = confirmAction,
        toggleState = toggleState,
    )
}

/**
 * Non-collapsible implementation, maximising touch target size and always displaying full content.
 */
@Composable
private fun AccessibleChipLayout(
    text: @Composable (Modifier) -> Unit,
    contentDescription: String?,
    icon: Painter,
    modifier: Modifier,
    tint: Color,
    confirmAction: () -> Unit,
) {
    Row(
        modifier
            .semantics(mergeDescendants = true) {
                if (contentDescription != null) {
                    this.contentDescription = contentDescription
                    testTag = TestTag.Confirm
                }
            }
            .clip(CircleShape)
            .clickable(onClick = confirmAction)
            .border(2.dp, tint, CircleShape)
            .padding(2.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier
                .padding(all = IconPadding, start = ExpandedPadding)
                .size(themedSize.IconSmall),
            tint = tint
        )

        text(Modifier.padding(end = ExpandedPadding))
    }
}

@Composable
private fun CollapsibleChipLayout(
    animationProgress: Float,
    state: ExpandCollapseState,
    text: @Composable (Modifier) -> Unit,
    contentDescription: String?,
    icon: Painter,
    cancelIcon: Painter,
    modifier: Modifier,
    tint: Color,
    confirmAction: () -> Unit,
    toggleState: () -> Unit,
) {
    Row(
        modifier
            .clip(CircleShape)
            .clickable(
                enabled = state.isCollapsed,
                onClick = toggleState,
                role = Role.Button,
            )
            .semantics(mergeDescendants = true) {
                if (contentDescription != null) {
                    this.contentDescription = contentDescription
                }
                testTag = TestTag.Chip
            }
            .border(2.dp, tint, CircleShape),

        verticalAlignment = Alignment.CenterVertically,
    ) {
        CancelIcon(state, cancelIcon, animationProgress, toggleState)

        // Separator
        Spacer(
            Modifier
                .wrapContentWidth(animationProgress)
                .alpha(animationProgress)
                .background(tint)
                .height(20.dp)
                .width(1.dp)
        )
//
        ExpandedContent(
            state,
            icon,
            tint,
            animationProgress,
            confirmAction,
            text,
        )
    }
}

@Composable
private fun CancelIcon(
    state: ExpandCollapseState,
    cancelIcon: Painter,
    animationProgress: Float,
    toggleState: () -> Unit,
) {
    Box(
        Modifier
            .clickable(
                enabled = state.isExpanded,
                onClick = toggleState,
            )
            .testTag(TestTag.Cancel),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            cancelIcon,
            contentDescription = stringResource(R.string.content_description_cancel),
            modifier = Modifier
                .wrapContentWidth(animationProgress)
                .padding(all = IconPadding, start = ExpandedPadding)
                .size(themedSize.IconSmall)
                .alpha(animationProgress),
            tint = colors.error
        )
    }
}

@Composable
private fun ExpandedContent(
    state: ExpandCollapseState,
    icon: Painter,
    tint: Color,
    animationProgress: Float,
    confirmAction: () -> Unit,
    text: @Composable (Modifier) -> Unit,
) {
    Row(
        Modifier
            .onlyWhen(state.isExpanded) {
                clickable(onClick = confirmAction)
            }
            .testTag(TestTag.Confirm),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier
                .padding(IconPadding)
                .size(themedSize.IconSmall),
            tint = tint,
        )

        text(
            Modifier
                .wrapContentWidth(animationProgress.progressIn(0F, 0.6F))
                .padding(end = ExpandedPadding)
                .alpha(animationProgress.progressIn(0.3F, 1F))
        )
    }
}


/**
 * Padding around icons.
 */
private val IconPadding = 15.dp

/**
 * Padding at start/end of expanded layout
 */
private val ExpandedPadding = IconPadding * 2
