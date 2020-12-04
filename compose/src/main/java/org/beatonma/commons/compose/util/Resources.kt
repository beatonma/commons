package org.beatonma.commons.compose.util

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.loadVectorResource

@Composable
inline fun withVectorResource(
    @DrawableRes resourceId: Int,
    content: @Composable (ImageVector) -> Unit,
) {
    loadVectorResource(resourceId).resource.resource?.let { asset ->
        content(asset)
    }
}
