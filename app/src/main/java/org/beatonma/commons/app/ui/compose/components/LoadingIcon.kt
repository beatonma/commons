package org.beatonma.commons.app.ui.compose.components

import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HdrWeak
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import org.beatonma.commons.ActionBlock
import org.beatonma.commons.R
import org.beatonma.commons.compose.ambient.animation
import org.beatonma.commons.compose.util.rememberBoolean


@Composable
fun LoadingIcon(
    modifier: Modifier = Modifier,
    onClick: ActionBlock? = null,
    tint: Color = LocalContentColor.current,
    state: MutableState<Boolean> = rememberBoolean(false),
    animSpec: InfiniteRepeatableSpec<Float> = animation.infiniteRepeatable(
        duration = 600,
        repeatMode = RepeatMode.Reverse,
    ),
) {
    val transition = rememberInfiniteTransition()
    val rotation by transition.animateFloat(
        initialValue = -30F,
        targetValue = 220F,
        animationSpec = animSpec
    )
    val iconModifier = modifier
        .rotate(rotation)
        .testTag("loading_icon")

    if (onClick == null) {
        Icon(
            Icons.Default.HdrWeak,
            contentDescription = stringResource(R.string.content_description_loading),
            tint = tint,
            modifier = iconModifier
        )
    }
    else {
        IconButton(onClick = onClick) {
            Icon(
                Icons.Default.HdrWeak,
                contentDescription = stringResource(R.string.content_description_loading),
                tint = tint,
                modifier = iconModifier)
        }
    }

    state.value = true // Immediately change state value to start the animation.
}
