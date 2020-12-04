package org.beatonma.commons.app.ui.compose.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import org.beatonma.commons.compose.ambient.colors
import org.beatonma.commons.compose.ambient.typography

@Composable
fun Error(
    errorText: String? = "Error",
    modifier: Modifier = Modifier.fillMaxSize(),
) {
    Box(modifier, alignment = Alignment.Center) {
        Text(
            errorText ?: "Error",
            style = typography.h3,
            color = colors.error,
            textAlign = TextAlign.Center,
        )
    }
}
