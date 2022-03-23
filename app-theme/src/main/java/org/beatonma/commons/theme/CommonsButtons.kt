package org.beatonma.commons.theme

import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import org.beatonma.commons.themed.ThemedButtons

internal object CommonsButtons : ThemedButtons {
    @Composable
    override fun outlineButtonColors() = buttonColors(
        contentColor = colors.primary,
        backgroundColor = Color.Transparent,
    )

    @Composable
    override fun warningButtonColors() =
        buttonColors(
            contentColor = colors.onWarningSurface,
            backgroundColor = colors.warningSurface,
        )

    @Composable
    override fun surfaceButtonColors() =
        buttonColors(
            contentColor = colors.onSurface,
            backgroundColor = colors.surface,
        )

    @Composable
    override fun accentButtonColors() = buttonColors(
        contentColor = colors.onSelectedSurface,
        backgroundColor = colors.selectedSurface,
    )

    @Composable
    override fun buttonColors(
        contentColor: Color,
        backgroundColor: Color,
        disabledContentColor: Color,
    ) = ButtonDefaults.outlinedButtonColors(
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        disabledContentColor = disabledContentColor
    )
}
