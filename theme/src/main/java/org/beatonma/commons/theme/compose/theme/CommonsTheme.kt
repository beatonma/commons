package org.beatonma.commons.theme.compose.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun CommonsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = if (darkTheme) CommonsDarkThemeColors else CommonsLightThemeColors,
        typography = CommonsTypography,
        shapes = CommonsShapes,
        content = content,
    )
}
