package org.beatonma.commons.themed

import androidx.compose.material.ButtonColors
import androidx.compose.material.ContentAlpha
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalButtonTheme =
    staticCompositionLocalOf<ThemedButtons> { error("LocalButtonTheme has not been provided") }

val themedButtons: ThemedButtons
    @ReadOnlyComposable @Composable get() = LocalButtonTheme.current

interface ThemedButtons {
    @Composable
    fun outlineButtonColors(): ButtonColors

    @Composable
    fun warningButtonColors(): ButtonColors

    @Composable
    fun contentButtonColors(): ButtonColors

    @Composable
    fun buttonColors(
        contentColor: Color,
        backgroundColor: Color,
        disabledContentColor: Color
    ): ButtonColors

    @Composable
    fun buttonColors(
        contentColor: Color,
        backgroundColor: Color,
    ) = buttonColors(
        contentColor,
        backgroundColor,
        contentColor.copy(alpha = ContentAlpha.disabled)
    )
}
