package org.beatonma.commons.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import org.beatonma.commons.themed.LocalAnimationSpec
import org.beatonma.commons.themed.LocalButtonTheme
import org.beatonma.commons.themed.LocalElevation
import org.beatonma.commons.themed.LocalPadding
import org.beatonma.commons.themed.LocalSize
import org.beatonma.commons.themed.LocalSpanStyle

@Composable
fun CommonsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colors = if (darkTheme) CommonsDarkThemeColors else CommonsLightThemeColors,
        typography = CommonsTypography,
        shapes = CommonsShapes,
    ) {
        CompositionLocalProvider(
            LocalAnimationSpec provides CommonsAnimation,
            LocalButtonTheme provides CommonsButtons,
            LocalElevation provides CommonsElevation,
            LocalPadding provides CommonsPadding,
            LocalSize provides CommonsSize,
            LocalSpanStyle provides CommonsSpanStyle,
            content = content,
        )
    }
}
