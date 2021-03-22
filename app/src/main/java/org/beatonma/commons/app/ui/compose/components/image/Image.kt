package org.beatonma.commons.app.ui.compose.components.image

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import dev.chrisbanes.accompanist.coil.CoilImage
import dev.chrisbanes.accompanist.imageloading.ImageLoadState
import org.beatonma.commons.app.ui.compose.components.ErrorUi
import org.beatonma.commons.app.ui.compose.components.LoadingIcon

@Composable
fun Avatar(
    source: String?,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    fadeIn: Boolean = true,
    contentScale: ContentScale = ContentScale.Crop,
    alignment: Alignment = Alignment.Center,
    loading: @Composable BoxScope.() -> Unit = { LoadingIcon() },
    error: @Composable BoxScope.(ImageLoadState) -> Unit = { imageLoadState ->
        ErrorUi(message = (imageLoadState as? ImageLoadState.Error)?.throwable?.message ?: "")
                                                           },
    @DrawableRes fallback: Int = 0,
) {
    CoilImage(
        data = source ?: fallback,
        contentDescription = contentDescription,
        modifier = modifier.testTag("avatar"),
        fadeIn = fadeIn,
        contentScale = contentScale,
        alignment = alignment,
        loading = loading,
        error = error,
    )
}
