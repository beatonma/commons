package org.beatonma.compose.themepreview

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp

@Composable
internal fun ColorsPreview() {
    ColorPreview("Background", MaterialTheme.colors.background)
    ColorPreview("Surface", MaterialTheme.colors.surface)
    ColorPreview("Primary", MaterialTheme.colors.primary)
    ColorPreview("Secondary", MaterialTheme.colors.secondary)
    ColorPreview("SecondaryVariant", MaterialTheme.colors.secondaryVariant)
    ColorPreview("Error", MaterialTheme.colors.error)
}

@Composable
private fun ColorPreview(name: String, colorBackground: Color) {
    Surface(Modifier.height(128.dp).fillMaxWidth(), color = colorBackground) {
        Row(Modifier.fillMaxWidth()) {
            Text(
                name,
                Modifier.padding(ContentPadding).weight(2f),
                style = MaterialTheme.typography.h6,
            )
            Text(
                "#${colorBackground.value.toString(16).substring(0, 8)}",
                Modifier.padding(ContentPadding),
                style = MaterialTheme.typography.h6,
                fontFamily = FontFamily.Monospace,
            )
        }
    }
}
