package org.beatonma.compose.themepreview

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
    Sample("h1") { Text("Header", style = MaterialTheme.typography.h1) }
    Sample("h2") { Text("Header", style = MaterialTheme.typography.h2) }
    Sample("h3") { Text("Header", style = MaterialTheme.typography.h3) }
    Sample("h4") { Text("Header", style = MaterialTheme.typography.h4) }
    Sample("h5") { Text("Header", style = MaterialTheme.typography.h5) }
    Sample("h6") { Text("Header", style = MaterialTheme.typography.h6) }

    Sample("subtitle1") { Text("Subtitle", style = MaterialTheme.typography.subtitle1) }
    Sample("subtitle2") { Text("Subtitle", style = MaterialTheme.typography.subtitle2) }

    Sample("body1") { modifier ->
        Text(
            lorem,
            modifier,
            style = MaterialTheme.typography.body1,
            maxLines = 3
        )
    }
    Sample("body2") { modifier ->
        Text(
            lorem,
            modifier,
            style = MaterialTheme.typography.body2,
            maxLines = 3
        )
    }
    Sample("overline") { modifier ->
        Text(
            lorem,
            modifier,
            style = MaterialTheme.typography.overline,
            maxLines = 3
        )
    }
    Sample("button") { modifier ->
        Text(
            lorem,
            modifier,
            style = MaterialTheme.typography.button,
            maxLines = 1
        )
    }
    Sample("caption") { modifier ->
        Text(
            lorem,
            modifier,
            style = MaterialTheme.typography.caption,
            maxLines = 3
        )
    }
}

@Composable
private fun Sample(
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
