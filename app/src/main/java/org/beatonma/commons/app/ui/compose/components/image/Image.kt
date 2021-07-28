package org.beatonma.commons.app.ui.compose.components.image

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import org.beatonma.commons.app.ui.compose.components.ErrorUi
import org.beatonma.commons.app.ui.compose.components.LoadingIcon


@OptIn(ExperimentalCoilApi::class)
@Composable
fun Avatar(
    source: String?,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    builder: ImageRequest.Builder.() -> Unit = DefaultBuilder,
    contentScale: ContentScale = ContentScale.Crop,
    alignment: Alignment = Alignment.Center,
    loading: @Composable BoxScope.() -> Unit = { LoadingIcon() },
    error: @Composable BoxScope.(Throwable) -> Unit = { throwable ->
        ErrorUi(message = (throwable.message ?: ""))
    },
    fallback: @Composable BoxScope.() -> Unit = {},
) {
    val painter = rememberImagePainter(
        data = source,
        builder = builder,
    )

    when (val state = painter.state) {
        is ImagePainter.State.Loading -> Box(contentAlignment = alignment, content = loading)
        is ImagePainter.State.Empty -> Box(contentAlignment = alignment, content = fallback)
        is ImagePainter.State.Error -> {
            Box(contentAlignment = alignment) {
                error(state.throwable)
            }
        }
        is ImagePainter.State.Success -> {
            Image(
                painter = painter,
                contentDescription = contentDescription,
                modifier = modifier,
                alignment = alignment,
                contentScale = contentScale,
            )
        }
    }

//    CoilImage(
//        data = source ?: fallback,
//        contentDescription = contentDescription,
//        modifier = modifier.testTag("avatar"),
//        fadeIn = fadeIn,
//        contentScale = contentScale,
//        alignment = alignment,
//        loading = loading,
//        error = error,
//    )
}

private val DefaultBuilder: ImageRequest.Builder.() -> Unit = {
    crossfade(300)
}
