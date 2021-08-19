package org.beatonma.commons.app.ui.compose.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import org.beatonma.commons.compose.ambient.colors
import org.beatonma.commons.compose.ambient.typography
import org.beatonma.commons.repo.result.ResponseCode
import org.beatonma.commons.theme.compose.theme.systemui.systemBarsPadding

@Composable
fun ErrorUi(
    modifier: Modifier = Modifier,
    error: Throwable,
) {
    ErrorUi(modifier, "$error")
}

@Composable
fun ErrorUi(
    modifier: Modifier = Modifier,
    error: ResponseCode,
) {
    ErrorUi(modifier, "$error")
}


@Composable
fun ErrorUi(
    modifier: Modifier = Modifier,
    message: String? = "Error",
) {
    Box(modifier.systemBarsPadding(), contentAlignment = Alignment.Center) {
        Text(
            message ?: "Error",
            style = typography.h3,
            color = colors.error,
            textAlign = TextAlign.Center,
        )
    }
}
