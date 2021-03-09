package org.beatonma.commons.compose.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import org.beatonma.commons.compose.ambient.colors
import org.beatonma.commons.compose.util.toggle
import org.beatonma.commons.theme.compose.Layer
import org.beatonma.commons.theme.compose.theme.CommonsSpring
import org.beatonma.commons.theme.compose.theme.modalScrim

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ModalScrim(
    onClickAction: () -> Unit,
    modifier: Modifier = Modifier,
    visible: Boolean = true,
    alignment: Alignment = Alignment.TopStart,
    alpha: Float = 1F,
    interactionSource: MutableInteractionSource = remember(::MutableInteractionSource),
    content: @Composable BoxScope.() -> Unit,
) {
    val backgroundColor = colors.modalScrim
    AnimatedVisibility(
        visible = visible,
        initiallyVisible = !visible,
        enter = fadeIn(animationSpec = CommonsSpring()),
        exit = fadeOut(animationSpec = CommonsSpring()),
    ) {
        Box(
            modifier
                .zIndex(Layer.ModalScrim)
                .fillMaxSize()
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    enabled = alpha > 0F,
                    onClick = onClickAction,
                )
                .background(backgroundColor.copy(alpha = alpha * backgroundColor.alpha)),
            contentAlignment = alignment,
            content = content
        )
    }
}


@Preview
@Composable
fun ModalPreview() {
    val state = remember { mutableStateOf(true) }

    Box(
        Modifier
            .background(Color.Blue)
            .fillMaxSize()
    ) {
        Spacer(
            Modifier
                .clickable { state.value = true }
                .size(128.dp)
                .background(Color.Green)
        )

        ModalScrim(
            visible = state.value,
            onClickAction = { state.value = false }
        ) {
            Spacer(
                Modifier
                    .size(64.dp)
                    .background(Color.Red)
            )
        }
    }
}
