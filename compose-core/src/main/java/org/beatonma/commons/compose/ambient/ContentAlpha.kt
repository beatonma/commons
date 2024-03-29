package org.beatonma.commons.compose.ambient

import androidx.compose.material.LocalContentAlpha
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun WithContentAlpha(alpha: Float, content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalContentAlpha provides alpha,
        content = content
    )
}
