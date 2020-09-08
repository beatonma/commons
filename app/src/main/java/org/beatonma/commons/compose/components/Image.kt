package org.beatonma.commons.compose.components

import androidx.compose.foundation.Text
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import dev.chrisbanes.accompanist.coil.CoilImage
import org.beatonma.commons.BuildConfig
import org.beatonma.commons.theme.compose.Whitespace
import org.beatonma.commons.theme.compose.theme.CommonsShapes

@Composable
fun Avatar(
    source: Any?,
    placeholder: Any = "https://beatonma.org/static/images/mb.png",
    size: Dp = 72.dp,
    modifier: Modifier = Modifier
) {
    CoilImage(
        data = if (BuildConfig.DEBUG) placeholder else source ?: placeholder,
        contentScale = ContentScale.Crop,
        loading = {
            Text("LOADING")
        },
        modifier = modifier
            .padding(Whitespace.Image.around)
            .size(size),
    )
}

@Composable
fun AvatarWithBorder(
    source: Any?,
    placeholder: Any = "https://beatonma.org/static/images/mb.png",
    size: Dp = 72.dp,
    color: Color,
    width: Dp = 4.dp,
    modifier: Modifier = Modifier
) {
    Avatar(
        source = source,
        placeholder = placeholder,
        size = size,
        modifier = modifier
            .border(width, color, CommonsShapes.small)
            .padding(width * 2)
            .clip(CommonsShapes.small)
    )
}

@Composable
@Preview
fun PreviewAvatar() {
    AvatarWithBorder(
        "https://beatonma.org/static/images/mb.png",
        color = Color.Red
    )
}
