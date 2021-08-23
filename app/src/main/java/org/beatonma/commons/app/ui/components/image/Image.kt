package org.beatonma.commons.app.ui.components.image

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import org.beatonma.commons.app.ui.components.ErrorUi
import org.beatonma.commons.app.ui.components.LoadingIcon

internal const val AvatarTestTag = "avatar"


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
    @Composable
    fun Boxed(content: @Composable BoxScope.() -> Unit) {
        Box(modifier, contentAlignment = alignment, content = content)
    }

    if (source.isNullOrBlank()) {
        Boxed(fallback)
        return
    }

    val painter = rememberImagePainter(
        data = source,
        builder = builder,
    )

    Box(modifier) {
        when (val state = painter.state) {
            is ImagePainter.State.Loading -> {
                Boxed(loading)
            }
            is ImagePainter.State.Error -> {
                Boxed { error(state.throwable) }
            }
        }

        Image(
            painter = painter,
            contentDescription = contentDescription,
            modifier = modifier.testTag(AvatarTestTag),
            alignment = alignment,
            contentScale = contentScale,
        )
    }
}

private val DefaultBuilder: ImageRequest.Builder.() -> Unit = {
    crossfade(300)
}
