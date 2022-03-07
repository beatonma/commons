package org.beatonma.commons.app.ui.components

import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import org.beatonma.commons.app.ui.accessibility.ContentDescription
import org.beatonma.commons.app.ui.components.image.AppIcon
import org.beatonma.commons.compose.util.rememberBoolean
import org.beatonma.commons.themed.themedAnimation

internal const val LoadingTestTag = "loading_icon"

@Composable
fun LoadingIcon(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    tint: Color = LocalContentColor.current,
    state: MutableState<Boolean> = rememberBoolean(false),
    animSpec: InfiniteRepeatableSpec<Float> = themedAnimation.infiniteRepeatable(
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
        .testTag(LoadingTestTag)

    if (onClick == null) {
        Icon(
            AppIcon.Loading,
            contentDescription = ContentDescription.Loading,
            tint = tint,
            modifier = iconModifier
        )
    } else {
        IconButton(onClick = onClick) {
            Icon(
                AppIcon.Loading,
                contentDescription = ContentDescription.Loading,
                tint = tint,
                modifier = iconModifier
            )
        }
    }

    LaunchedEffect(Unit) {
        state.value = true // Immediately change state value to start the animation.
    }
}

fun LazyListScope.loadingIcon(loading: Boolean, key: Any? = "loading_icon") {
    if (loading) {
        item(key = key) {
            LoadingIcon(Modifier.fillMaxWidth())
        }
    }
}
