package org.beatonma.commons.theme

import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.material.Button
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.OutlinedButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.runtime.Composable

@Composable
internal fun WidgetsPreview() {
    Button(onClick = {}) { Text("Button") }
    OutlinedButton(onClick = {}) { Text("Button") }
    FloatingActionButton(onClick = {}) { Icon(Icons.Default.Android) }
}
