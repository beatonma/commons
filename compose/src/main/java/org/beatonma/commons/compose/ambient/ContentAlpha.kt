package org.beatonma.commons.compose.ambient

import androidx.compose.material.AmbientContentAlpha
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers

@Composable
fun WithContentAlpha(alpha: Float, content: @Composable () -> Unit) {
    Providers(
        AmbientContentAlpha provides alpha,
        content = content
    )
}
