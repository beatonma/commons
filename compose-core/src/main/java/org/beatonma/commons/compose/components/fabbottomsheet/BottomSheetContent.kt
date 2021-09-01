package org.beatonma.commons.compose.components.fabbottomsheet

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import org.beatonma.commons.compose.components.CardText
import org.beatonma.commons.compose.modifiers.wrapContentSize
import org.beatonma.commons.compose.systemui.imeOrNavigationBarsPadding
import org.beatonma.commons.compose.systemui.navigationBarsPadding
import org.beatonma.commons.core.extensions.progressIn

@Composable
fun BottomSheetContent(
    progress: Float,
    modifier: Modifier = Modifier,
    handleImeInsets: Boolean = true,
    content: @Composable BoxScope.() -> Unit,
) {
    val paddingModifier = when (handleImeInsets) {
        true -> Modifier.imeOrNavigationBarsPadding(scale = progress)
        false -> Modifier.navigationBarsPadding(scale = progress)
    }

    Box(
        modifier
            .wrapContentSize(
                horizontalProgress = progress.progressIn(0F, 0.4F),
                verticalProgress = progress.progressIn(0F, 0.8F)
            )
            .alpha(progress.progressIn(0.8F, 1F))
            .then(paddingModifier),
        content = content,
    )
}

@Composable
fun BottomSheetText(
    progress: Float,
    modifier: Modifier = Modifier,
    handleImeInsets: Boolean = true,
    content: @Composable BoxScope.() -> Unit,
) {
    BottomSheetContent(progress, modifier, handleImeInsets = handleImeInsets) {
        CardText(content = content)
    }
}
