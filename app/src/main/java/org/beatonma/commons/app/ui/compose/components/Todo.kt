package org.beatonma.commons.app.ui.compose.components

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.beatonma.commons.compose.ambient.colors

@Composable
fun Todo(message: String? = null, modifier: Modifier = Modifier) {
    val text = if (message == null) "// todo" else "// todo: $message"
    Text(
        text,
        modifier.padding(vertical = 8.dp),
        color = colors.error,
    )
}
