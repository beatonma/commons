package org.beatonma.commons.theme.compose.theme

import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import org.beatonma.commons.theme.compose.color.CommonsColor

val CommonsLightThemeColors = lightColors(
    primary = CommonsColor.Primary,
    primaryVariant = CommonsColor.PrimaryVariant,
    onPrimary = CommonsColor.Text.PrimaryLight,

    secondary = CommonsColor.Light.Secondary,
    secondaryVariant = CommonsColor.Light.SecondaryVariant,
    onSecondary = CommonsColor.Text.PrimaryLight,

    background = CommonsColor.Light.Background,
    onBackground = CommonsColor.Text.PrimaryDark,

    error = CommonsColor.Negative,
    onError = CommonsColor.Text.PrimaryLight,

    surface = Color.White,
    onSurface = CommonsColor.Text.PrimaryDark
)

val CommonsDarkThemeColors: Colors = darkColors(
    primary = CommonsColor.Primary,
    primaryVariant = CommonsColor.PrimaryVariant,
    onPrimary = CommonsColor.Text.PrimaryLight,

    secondary = CommonsColor.Dark.Secondary,
    onSecondary = CommonsColor.Text.SecondaryDark,

    background = CommonsColor.Dark.Background,
    onBackground = CommonsColor.Text.PrimaryLight,

    error = CommonsColor.Negative,
    onError = CommonsColor.Text.PrimaryLight,

    surface = CommonsColor.Dark.Surface,
    onSurface = CommonsColor.Text.PrimaryLight
)

val Colors.SystemBars get() = background.copy(alpha = 0.4f)

@Composable
val MaterialTheme.invertedColors
    get() = when (colors.isLight) {
        true -> CommonsDarkThemeColors
        false -> CommonsLightThemeColors
    }
