package org.beatonma.commons.theme.compose.theme

import androidx.compose.material.LocalContentColor
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object CommonsButtons {
    @Composable
    fun outlineButtonColors(
        contentColor: Color = MaterialTheme.colors.primary,
        backgroundColor: Color = Color.Transparent
    ) = buttonColors(
        contentColor = contentColor,
        backgroundColor = backgroundColor,
    )

    @Composable
    fun warningButtonColors(
        contentColor: Color = MaterialTheme.colors.onWarningSurface,
        backgroundColor: Color = MaterialTheme.colors.warningSurface
    ) =
        buttonColors(
            contentColor = contentColor,
            backgroundColor = backgroundColor,
        )

    @Composable
    fun contentButtonColors(backgroundColor: Color = Color.Transparent) =
        buttonColors(
            contentColor = LocalContentColor.current,
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
