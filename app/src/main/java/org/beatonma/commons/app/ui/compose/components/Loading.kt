package org.beatonma.commons.app.ui.compose.components

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import org.beatonma.commons.compose.ambient.typography

@Composable
fun Loading(
    modifier: Modifier = Modifier.fillMaxSize(),
) {
    Box(modifier, alignment = Alignment.Center) {
        Text(
            "Loading",
            style = typography.h3,
            textAlign = TextAlign.Center
        )
    }
}
