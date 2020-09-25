package org.beatonma.commons.compose.components

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bumptech.glide.request.RequestOptions

const val DEV_AVATAR = "https://beatonma.org/static/images/profile2.jpg"

@Composable
fun Avatar(
    source: String?,
    @DrawableRes fallback: Int = 0,
    @DrawableRes placeholder: Int = 0,
    modifier: Modifier = Modifier,
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
