package org.beatonma.commons

import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import org.beatonma.commons.theme.compose.theme.CommonsTheme

const val lorem =
    "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut " +
            "labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation " +
            "ullamco laboris nisi ut aliquip ex ea commodo consequat."

@Composable
@Preview
fun CommonsThemePreview() {
    CommonsTheme {
        ThemePreview()
    }
}

@Composable
fun ThemePreview() {
    Surface(Modifier.fillMaxWidth(), color = MaterialTheme.colors.surface) {
        ScrollableColumn(
            Modifier.fillMaxWidth(),
        ) {
            Section("Typography") {
                TextPreview()
            }

            Section("Colors") {
                ColorsPreview()
            }

            Section("Components") {
                Text("// TODO")
            }
        }
    }
}

@Composable
private fun Section(title: String, content: @Composable () -> Unit) {
    SectionHeader(title)
    content()
    Spacer(Modifier.height(64.dp))
}

@Composable
private fun TextPreview() {
    SampleText("h1") { Text("Header", style = MaterialTheme.typography.h1) }
    SampleText("h2") { Text("Header", style = MaterialTheme.typography.h2) }
    SampleText("h3") { Text("Header", style = MaterialTheme.typography.h3) }
    SampleText("h4") { Text("Header", style = MaterialTheme.typography.h4) }
    SampleText("h5") { Text("Header", style = MaterialTheme.typography.h5) }
    SampleText("h6") { Text("Header", style = MaterialTheme.typography.h6) }

    SampleText("subtitle1") { Text("Subtitle", style = MaterialTheme.typography.subtitle1) }
    SampleText("subtitle2") { Text("Subtitle", style = MaterialTheme.typography.subtitle2) }

    SampleText("body1") { modifier ->
        Text(
            lorem,
            modifier,
            style = MaterialTheme.typography.body1,
            maxLines = 3
        )
    }
    SampleText("body2") { modifier ->
        Text(
            lorem,
            modifier,
            style = MaterialTheme.typography.body2,
            maxLines = 3
        )
    }
    SampleText("overline") { modifier ->
        Text(
            lorem,
            modifier,
            style = MaterialTheme.typography.overline,
            maxLines = 3
        )
    }
    SampleText("button") { modifier ->
        Text(
            lorem,
            modifier,
            style = MaterialTheme.typography.button,
            maxLines = 1
        )
    }
    SampleText("caption") { modifier ->
        Text(
            lorem,
            modifier,
            style = MaterialTheme.typography.caption,
            maxLines = 3
        )
    }
}

@Composable
private fun ColorsPreview() {
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
                Modifier.padding(16.dp).weight(2f),
                style = MaterialTheme.typography.h6,
            )
            Text(
                "#${colorBackground.value.toString(16).substring(0, 8)}",
                Modifier.padding(16.dp),
                style = MaterialTheme.typography.h6,
                fontFamily = FontFamily.Monospace,
            )
        }
    }
}

@Composable
private fun SectionHeader(text: String, modifier: Modifier = Modifier) {
    Surface(Modifier.fillMaxWidth(), color = MaterialTheme.colors.secondary) {
        Text(
            text,
            modifier.padding(horizontal = 16.dp, vertical = 32.dp),
            fontFamily = FontFamily.Monospace,
            style = MaterialTheme.typography.h3,
        )
    }
}

@Composable
private fun SampleText(
    name: String,
    modifier: Modifier = Modifier,
    content: @Composable (Modifier) -> Unit
) {
    Column(
        Modifier.fillMaxWidth()
            .padding(16.dp),
    ) {
        Text(
            name,
            Modifier.width(72.dp),
            style = MaterialTheme.typography.caption
        )
        content(modifier)
    }
}
