package org.beatonma.commons.app.preview

import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import com.google.accompanist.insets.ProvideWindowInsets
import org.beatonma.commons.theme.compose.theme.CommonsTheme

@Composable
fun InAppPreview(content: @Composable () -> Unit) {
    CommonsTheme {
        ProvideWindowInsets {
            Surface(content = content)
        }
    }
}
