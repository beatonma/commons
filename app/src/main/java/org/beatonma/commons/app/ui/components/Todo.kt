package org.beatonma.commons.app.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import org.beatonma.commons.compose.ambient.colors

@Composable
fun Todo(message: String, modifier: Modifier = Modifier) {
    val text =  "// todo: $message"
    Text(
        text,
        modifier.padding(vertical = 8.dp),
        color = colors.error,
    )
}

@Composable
fun Todo(message: AnnotatedString, modifier: Modifier = Modifier) {
    val text = buildAnnotatedString {
        append("// todo: ")
        append(message)
    }

    Text(
        text,
        modifier.padding(vertical = 8.dp),
        color = colors.error,
    )
}
