package org.beatonma.commons.app.ui.compose.components.image

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bumptech.glide.request.RequestOptions

const val DEV_AVATAR = "https://beatonma.org/static/images/profile2.jpg"

@Composable
fun Avatar(
    source: String?,
    modifier: Modifier = Modifier,
    @DrawableRes fallback: Int = 0,
    @DrawableRes placeholder: Int = 0,
) {
    GlideImage(
        source,
        modifier = modifier,
        options = RequestOptions()
            .placeholder(placeholder)
            .fallback(fallback)
            .centerCrop()
    )
}
