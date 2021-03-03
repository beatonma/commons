package org.beatonma.commons.compose.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.beatonma.commons.compose.R
import org.beatonma.commons.compose.ambient.colors
import org.beatonma.commons.compose.animation.ExpandCollapseState
import org.beatonma.commons.compose.animation.isExpanded
import org.beatonma.commons.compose.animation.rememberExpandCollapseState
import org.beatonma.commons.compose.animation.toggle
import org.beatonma.commons.compose.modifiers.design.centerOverlay
import org.beatonma.commons.compose.modifiers.wrapContentWidth
import org.beatonma.commons.core.extensions.progressIn
import org.beatonma.commons.theme.compose.Size
import org.beatonma.commons.theme.compose.theme.CommonsSpring
import org.beatonma.commons.theme.compose.theme.CommonsTheme
import org.beatonma.commons.theme.compose.theme.negative
import org.beatonma.commons.theme.compose.theme.textSecondary

@Composable
fun CollapsibleChip(
    text: AnnotatedString,
    contentDescription: String?,
    drawableId: Int,
    cancelIcon: ImageVector = Icons.Default.Close,
    modifier: Modifier = Modifier,
    tint: Color = colors.textSecondary,
    autoCollapse: Long = 2000,
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
    cancelIcon: ImageVector = Icons.Default.Close,
    modifier: Modifier = Modifier,
    tint: Color = colors.textSecondary,
    autoCollapse: Long = 2000,
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
    cancelIcon: ImageVector = Icons.Default.Close,
    modifier: Modifier = Modifier,
    tint: Color = colors.textSecondary,
    autoCollapse: Long = 2000,
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
    val state = rememberExpandCollapseState()

    val scope = rememberCoroutineScope()
    var job: Job? = remember { null }

    val toggleState = {
        state.toggle()
        if (autoCollapse > 0) {
            job?.cancel()
            if (state.isExpanded) {
                job = scope.launch {
                    delay(autoCollapse)
                    state.value = ExpandCollapseState.Collapsed
                }
            }
        }
    }

    val animationProgress by animateFloatAsState(
        targetValue = if (state.isExpanded) 1F else 0F,
        animationSpec = CommonsSpring()
    )

    CollapsibleChipLayout(
        animationProgress,
        state.value,
        text,
        contentDescription,
        icon,
        cancelIcon,
        modifier,
        tint,
        confirmAction,
        toggleState,
    )
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
    toggleState: () -> Unit
) {
    Row(
        modifier
            .clickable(enabled = state.isCollapsed) {
                toggleState()
            }
            .clip(CircleShape)
            .border(2.dp, tint, CircleShape)
            .padding(2.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier
                .clickable(enabled = state.isExpanded) {
                    toggleState()
                }
                .testTag("clickable_cancel")
            ,
            contentAlignment = Alignment.Center
        ) {
            Icon(
                cancelIcon,
                contentDescription = stringResource(R.string.content_description_cancel),
                modifier = Modifier
                    .wrapContentWidth(animationProgress)
                    .padding(start = 16.dp, end = 8.dp)
                    .size(Size.IconSmall)
                    .alpha(animationProgress),
                tint = colors.negative
            )
        }

        // Separator
        Spacer(
            Modifier
                .wrapContentWidth(animationProgress)
                .alpha(animationProgress)
                .background(tint)
                .height(20.dp)
                .width(1.dp)
        )

        Row(
            Modifier
                .clickable(enabled = state.isExpanded) {
                    confirmAction()
                }
                .testTag("clickable_confirm")
            ,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                icon,
                contentDescription = contentDescription,
                modifier = Modifier
                    .padding(8.dp)
                    .size(Size.IconSmall),
                tint = tint
            )

            text(
                Modifier
                    .wrapContentWidth(animationProgress.progressIn(0F, 0.6F))
                    .padding(end = 16.dp)
                    .alpha(animationProgress.progressIn(0.3F, 1F))
            )
        }
    }
}

@Preview
@Composable
private fun CollapsibleChipPreview() {
    var counter by remember { mutableStateOf(0) }
    CommonsTheme{
        Surface {
            Column {
                CollapsibleChip(
                    text = AnnotatedString("Compose email"),
                    contentDescription = "Compose an email to fake@snommoc.org",
                    icon = Icons.Default.Email,
                    modifier = Modifier
                        .centerOverlay(color = Color.Black)
                ) {
                    counter += 1
                }

                Text(counter.toString())
            }
        }
    }
}
