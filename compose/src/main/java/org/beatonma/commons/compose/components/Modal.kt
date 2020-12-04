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
import org.beatonma.commons.theme.compose.Layer
import org.beatonma.commons.theme.compose.theme.modalScrim

@Composable
fun ModalScrim(
    onClickAction: () -> Unit,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.TopStart,
    alpha: Float = 1F,
    content: @Composable BoxScope.() -> Unit,
) {
    val backgroundColor = colors.modalScrim
    Box(
        modifier
            .zIndex(Layer.ModalScrim)
            .fillMaxSize()
            .clickable(enabled = alpha > 0F, indication = null, onClick = onClickAction)
            .background(backgroundColor.copy(alpha = alpha * backgroundColor.alpha)),
        contentAlignment = alignment,
        content = content
    )
}
