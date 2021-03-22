package org.beatonma.commons.compose.animation

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import org.beatonma.commons.compose.modifiers.onlyWhen
import org.beatonma.commons.compose.modifiers.wrapContentHeight
import org.beatonma.commons.compose.modifiers.wrapContentWidth
import org.beatonma.commons.core.extensions.progressIn
import org.beatonma.commons.theme.compose.theme.DefaultAnimation

@Composable
fun DefaultAnimation.AnimatedVisibility(
    visible: Boolean,
    modifier: Modifier = Modifier,
    fade: Boolean = true,
    expand: Boolean = true,
    vertical: Boolean = expand,
    horizontal: Boolean = expand,
    easing: Easing = FastOutLinearInEasing,
    content: @Composable BoxScope.() -> Unit
) {
    val visibility by animateFloatAsState(if (visible) 1F else 0F)

    if (visibility > 0F) {
        Box(
            Modifier
                .onlyWhen(horizontal) {
                    wrapContentWidth(visibility.progressIn(0F, .4F).withEasing(easing))
                }
                .onlyWhen(vertical) {
                    wrapContentHeight(visibility.progressIn(.2F, .75F).withEasing(easing))
                }
                .onlyWhen(fade) {
                    alpha(visibility.progressIn(.5F, 1F).withEasing(easing))
                }
                .then(modifier)
            ,
            content = content,
        )
    }
}
