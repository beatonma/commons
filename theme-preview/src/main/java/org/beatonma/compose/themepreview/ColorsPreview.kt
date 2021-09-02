package org.beatonma.compose.themepreview

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp

@Composable
internal fun ColorsPreview() {
    val colors = MaterialTheme.colors
    ColorPreview("Background", colors.background, colors.onBackground)
    ColorPreview("Surface", colors.surface, colors.onSurface)
    ColorPreview("Primary", colors.primary, colors.onPrimary)
    ColorPreview("PrimaryVariant", colors.primaryVariant, colors.onPrimary)
    ColorPreview("Secondary", colors.secondary, colors.onSecondary)
    ColorPreview("SecondaryVariant", colors.secondaryVariant, colors.onSecondary)
    ColorPreview("Error", colors.error, colors.onError)
    ColorPreview("PrimarySurface", colors.primarySurface)
}

@OptIn(ExperimentalUnsignedTypes::class)
@Composable
private fun ColorPreview(
    name: String,
    backgroundColor: Color,
    contentColor: Color = LocalContentColor.current
) {
    Surface(
        Modifier
            .height(128.dp)
            .fillMaxWidth(), color = backgroundColor,
        contentColor = contentColor,
    ) {
        Row(Modifier.fillMaxWidth()) {
            Text(
                name,
                Modifier
                    .padding(ContentPadding)
                    .weight(2f),
                style = MaterialTheme.typography.h6,
            )
            Text(
                "#${backgroundColor.value.toString(16).substring(0, 8)}",
                Modifier.padding(ContentPadding),
                style = MaterialTheme.typography.h6,
                fontFamily = FontFamily.Monospace,
            )
        }
    }
}
