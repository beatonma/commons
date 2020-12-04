package org.beatonma.commons.app.ui.compose.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import org.beatonma.commons.compose.ambient.typography

@Composable
fun Loading(
    modifier: Modifier = Modifier,
) {
    Box(modifier, contentAlignment = Alignment.Center) {
        Text(
            "Loading",
            style = typography.h3,
            textAlign = TextAlign.Center
        )
    }
}
