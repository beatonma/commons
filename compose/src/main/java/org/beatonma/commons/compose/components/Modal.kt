package org.beatonma.commons.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import org.beatonma.commons.compose.ambient.colors
import org.beatonma.commons.compose.modifiers.onlyWhen
import org.beatonma.commons.theme.compose.Layer
import org.beatonma.commons.theme.compose.theme.modalScrim

@Composable
fun ModalScrim(
    onClickAction: () -> Unit,
    alignment: Alignment = Alignment.TopStart,
    alpha: Float = 1F,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    val backgroundColor = colors.modalScrim
    Box(
        modifier
            .fillMaxSize()
            .zIndex(Layer.ModalScrim)
            .background(backgroundColor.copy(alpha = alpha * backgroundColor.alpha))
            .onlyWhen(alpha > 0F) {
                clickable(onClick = onClickAction)
            },
        alignment = alignment,
        children = content
    )
}
