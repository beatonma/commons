package org.beatonma.commons.app.ui.compose.components.image

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.imageResource
import dev.chrisbanes.accompanist.coil.CoilImage
import dev.chrisbanes.accompanist.imageloading.ImageLoadState
import org.beatonma.commons.app.ui.compose.components.ErrorUi
import org.beatonma.commons.app.ui.compose.components.LoadingIcon

@Composable
fun Avatar(
    source: String?,
    modifier: Modifier = Modifier,
    fadeIn: Boolean = true,
    contentScale: ContentScale = ContentScale.Crop,
    alignment: Alignment = Alignment.Center,
    loading: @Composable () -> Unit = { LoadingIcon() },
    error: @Composable (ImageLoadState) -> Unit = { ErrorUi() },
    @DrawableRes fallback: Int = 0,
) {
    CoilImage(
        source ?: imageResource(fallback),
        modifier = modifier,
        fadeIn = fadeIn,
        contentScale = contentScale,
        alignment = alignment,
        loading = loading,
        error = error,
    )
}
