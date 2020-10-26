package org.beatonma.commons.compose.components

import androidx.compose.animation.transition
import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.foundation.layout.preferredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawOpacity
import androidx.compose.ui.graphics.vector.VectorAsset
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import org.beatonma.commons.compose.ambient.colors
import org.beatonma.commons.compose.animation.ExpandCollapseState
import org.beatonma.commons.compose.animation.progress
import org.beatonma.commons.compose.modifiers.onlyWhen
import org.beatonma.commons.compose.modifiers.wrapContentWidth
import org.beatonma.commons.compose.util.withVectorResource
import org.beatonma.commons.core.extensions.dump
import org.beatonma.commons.theme.compose.IconDimen
import org.beatonma.commons.theme.compose.color.CommonsColor
import org.beatonma.commons.theme.compose.theme.themed

@Composable
fun CollapsibleChip(
    displayText: AnnotatedString,
    drawableId: Int,
    cancelDrawableId: Int,
    modifier: Modifier = Modifier,
    confirmAction: () -> Unit,
) {
    withVectorResource(drawableId) { primaryIcon ->
        withVectorResource(cancelDrawableId) { cancelIcon ->
            CollapsibleChip(displayText,
                primaryIcon,
                cancelIcon,
                modifier,
                confirmAction = confirmAction)
        }
    }
}

@Composable
fun CollapsibleChip(
    displayText: AnnotatedString,
    icon: VectorAsset,
    cancelIcon: VectorAsset,
    modifier: Modifier = Modifier,
    state: MutableState<ExpandCollapseState> = remember { mutableStateOf(ExpandCollapseState.Collapsed) },
    confirmAction: () -> Unit,
) {
    val transition = remember { ExpandCollapseState.defaultTransition() }
    val transitionState = transition(transition, toState = state.value)
    val progress = transitionState.progress
    val toggleState = { state.value = state.value.toggle().dump() }

    val tint = colors.themed.TextSecondary

    Row(
        modifier
            .clip(CircleShape)
            .onlyWhen(condition = state.value == ExpandCollapseState.Collapsed) {
                clickable(onClick = toggleState)
            }
            .border(2.dp, tint, CircleShape)
            .padding(2.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier.onlyWhen(state.value == ExpandCollapseState.Expanded) {
                clickable(onClick = toggleState)
            }
        ) {
            Icon(
                cancelIcon,
                Modifier
                    .wrapContentWidth(progress)
                    .padding(start = 16.dp, top = 8.dp, end = 8.dp, bottom = 8.dp)
                    .size(IconDimen.Small)
                    .drawOpacity(progress),
                tint = CommonsColor.Negative
            )
        }

        // Separator
        Spacer(
            Modifier
                .drawOpacity(progress)
                .background(tint)
                .preferredHeight(20.dp)
                .preferredWidth(1.dp)
                .padding(horizontal = 4.dp)
                .wrapContentWidth(progress)
        )

        Row(
            Modifier.onlyWhen(state.value == ExpandCollapseState.Expanded) {
                clickable(onClick = confirmAction)
            },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                icon,
                Modifier
                    .padding(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 8.dp)
                    .size(IconDimen.Small),
                tint = tint
            )

            Text(
                displayText,
                Modifier
                    .wrapContentWidth(progress)
                    .padding(top = 8.dp, end = 16.dp, bottom = 8.dp)
                    .drawOpacity(progress)
            )
        }
    }
}
