package org.beatonma.commons.theme.compose.theme

import androidx.compose.material.ButtonConstants
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

object CommonsButtons {
    @Composable
    fun outlineButtonColors() = ButtonConstants.defaultOutlinedButtonColors(
        contentColor = MaterialTheme.colors.primary,
        disabledContentColor = MaterialTheme.colors.primary.copy(alpha = ContentAlpha.disabled)
    )
}
