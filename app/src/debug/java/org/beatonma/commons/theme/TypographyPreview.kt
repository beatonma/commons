package org.beatonma.commons.theme

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
internal fun TypographyPreview() {
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
private fun SampleText(
    name: String,
    modifier: Modifier = Modifier,
    content: @Composable (Modifier) -> Unit,
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
