package org.beatonma.commons.compose.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import org.beatonma.commons.compose.TestTag
import org.beatonma.commons.compose.ambient.animation
import org.beatonma.commons.compose.ambient.colors
import org.beatonma.commons.compose.animation.AnimatedVisibility
import org.beatonma.commons.theme.compose.Layer
import org.beatonma.commons.theme.compose.theme.modalScrim

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ModalScrim(
    onClickLabel: String?,
    onClickAction: () -> Unit,
    modifier: Modifier = Modifier,
    visible: Boolean = false,
    alignment: Alignment = Alignment.TopStart,
    backgroundColor: Color = colors.modalScrim,
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


@Preview
@Composable
fun ModalPreview() {
    var state by remember { mutableStateOf(true) }

    Box(
        Modifier
            .background(Color.Blue)
            .fillMaxSize()
    ) {
        Spacer(
            Modifier
                .clickable { state = true }
                .size(128.dp)
                .background(Color.Green)
        )

        ModalScrim(
            visible = state,
            onClickAction = { state = false },
            onClickLabel = "close"
        ) {
            Spacer(
                Modifier
                    .size(64.dp)
                    .background(Color.Red)
            )
        }
    }
}
