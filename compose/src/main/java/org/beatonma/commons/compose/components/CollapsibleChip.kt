package org.beatonma.commons.compose.components

import androidx.compose.animation.transition
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
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawOpacity
import androidx.compose.ui.graphics.vector.VectorAsset
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.beatonma.commons.compose.ambient.colors
import org.beatonma.commons.compose.animation.ExpandCollapseState
import org.beatonma.commons.compose.animation.isCollapsed
import org.beatonma.commons.compose.animation.isExpanded
import org.beatonma.commons.compose.animation.progressKey
import org.beatonma.commons.compose.animation.rememberExpandCollapseState
import org.beatonma.commons.compose.animation.rememberExpandCollapseTransition
import org.beatonma.commons.compose.animation.toggle
import org.beatonma.commons.compose.modifiers.wrapContentWidth
import org.beatonma.commons.compose.util.withVectorResource
import org.beatonma.commons.theme.compose.Size
import org.beatonma.commons.theme.compose.color.CommonsColor
import org.beatonma.commons.theme.compose.theme.themed

@Composable
fun CollapsibleChip(
    displayText: AnnotatedString,
    drawableId: Int,
    cancelDrawableId: Int,
    modifier: Modifier = Modifier,
    autoCollapse: Long = 2000,
    confirmAction: () -> Unit,
) {
    withVectorResource(drawableId) { primaryIcon ->
        withVectorResource(cancelDrawableId) { cancelIcon ->
            CollapsibleChip(displayText,
                primaryIcon,
                cancelIcon,
                modifier,
                autoCollapse,
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
    autoCollapse: Long = 2000,
    state: MutableState<ExpandCollapseState> = rememberExpandCollapseState(),
    confirmAction: () -> Unit,
) {
    val transition = rememberExpandCollapseTransition()
    val transitionState = transition(transition, toState = state.value)
    val progress = transitionState[progressKey]

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

    val tint = colors.themed.TextSecondary

    Row(
        modifier
            .clip(CircleShape)
            .clickable(enabled = state.isCollapsed) {
                toggleState()
            }
            .border(2.dp, tint, CircleShape)
            .padding(2.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier.clickable(enabled = state.isExpanded) {
                toggleState()
            }
        ) {
            Icon(
                cancelIcon,
                Modifier
                    .wrapContentWidth(progress)
                    .padding(start = 16.dp, top = 8.dp, end = 8.dp, bottom = 8.dp)
                    .size(Size.IconSmall)
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
                .wrapContentWidth(progress)
        )

        Row(
            Modifier.clickable(enabled = state.isExpanded) {
                confirmAction()
            },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                icon,
                Modifier
                    .padding(8.dp)
                    .size(Size.IconSmall),
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
