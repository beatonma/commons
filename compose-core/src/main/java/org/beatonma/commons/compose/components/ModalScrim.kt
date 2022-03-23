package org.beatonma.commons.compose.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.zIndex
import org.beatonma.commons.compose.Layer
import org.beatonma.commons.compose.TestTag
import org.beatonma.commons.compose.animation.AnimatedVisibility
import org.beatonma.commons.themed.animation

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ModalScrim(
    onClickLabel: String?,
    onClickAction: () -> Unit,
    modifier: Modifier = Modifier,
    visible: Boolean = false,
    alignment: Alignment = Alignment.TopStart,
    backgroundColor: Color = Color.Black.copy(alpha = .4F),
    interactionSource: MutableInteractionSource = remember(::MutableInteractionSource),
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        Modifier
            .fillMaxSize()
            .zIndex(Layer.ModalScrim),
        contentAlignment = alignment,
    ) {
        animation.AnimatedVisibility(
            visible = visible,
            expand = false,
        ) {
            Spacer(
                modifier
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null,
                        enabled = visible,
                        onClick = onClickAction,
                        onClickLabel = onClickLabel,
                    )
                    .fillMaxSize()
                    .background(backgroundColor)
                    .testTag(TestTag.ModalScrim)
            )
        }

        content()
    }
}
