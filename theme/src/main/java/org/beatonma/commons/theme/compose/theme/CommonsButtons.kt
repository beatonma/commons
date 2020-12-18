package org.beatonma.commons.theme.compose.theme

import androidx.compose.material.AmbientContentColor
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object CommonsButtons {
    @Composable
    fun outlineButtonColors(backgroundColor: Color = Color.Transparent) = buttonColors(
        contentColor = MaterialTheme.colors.primary,
        backgroundColor = backgroundColor,
    )

    @Composable
    fun warningButtonColors(backgroundColor: Color = Color.Transparent) =
        buttonColors(
            contentColor = MaterialTheme.colors.negative,
            backgroundColor = backgroundColor,
        )

    @Composable
    fun contentButtonColors(backgroundColor: Color = Color.Transparent) =
        buttonColors(
            contentColor = AmbientContentColor.current,
            backgroundColor = backgroundColor,
        )

    @Composable
    fun buttonColors(
        contentColor: Color,
        backgroundColor: Color = Color.Transparent,
        disabledContentColor: Color = contentColor.copy(alpha = ContentAlpha.disabled),
    ) = ButtonDefaults.outlinedButtonColors(
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        disabledContentColor = disabledContentColor
    )
}
