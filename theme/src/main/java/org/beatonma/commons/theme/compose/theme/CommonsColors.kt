package org.beatonma.commons.theme.compose.theme

import androidx.compose.material.Colors
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import org.beatonma.commons.theme.compose.color.CommonsColor
import org.beatonma.commons.theme.compose.color.PoliticalColor
import org.beatonma.commons.theme.compose.color.resolveColor

/**
 * Standard color definitions for MaterialTheme.
 */
val CommonsLightThemeColors = lightColors(
    primary = CommonsColor.Primary.light,
    primaryVariant = CommonsColor.PrimaryVariant.light,
    onPrimary = CommonsColor.Text.PrimaryLight,

    secondary = CommonsColor.Secondary.light,
    secondaryVariant = CommonsColor.SecondaryVariant.light,
    onSecondary = CommonsColor.Text.PrimaryDark,

    background = CommonsColor.Background.light,
    onBackground = CommonsColor.Text.PrimaryDark,

    error = CommonsColor.Negative,
    onError = CommonsColor.Text.PrimaryLight,

    surface = Color.White,
    onSurface = CommonsColor.Text.PrimaryDark,
)

/**
 * Standard color definitions for MaterialTheme.
 */
val CommonsDarkThemeColors: Colors = darkColors(
    primary = CommonsColor.Primary.dark,
    primaryVariant = CommonsColor.PrimaryVariant.dark,
    onPrimary = CommonsColor.Text.PrimaryDark,

    secondary = CommonsColor.Secondary.dark,
    onSecondary = CommonsColor.Text.SecondaryLight,

    background = CommonsColor.Background.dark,
    onBackground = CommonsColor.Text.PrimaryLight,

    error = CommonsColor.Negative,
    onError = CommonsColor.Text.PrimaryLight,

    surface = CommonsColor.Surface.dark,
    onSurface = CommonsColor.Text.PrimaryLight
)

/*
 * Custom color accessor extensions.
 */

val Colors.SystemBars get() = background.copy(alpha = 0.4f)

val Colors.searchBar get() = resolveColor(CommonsColor.SearchBar)
val Colors.onSearchBar get() = resolveColor(CommonsColor.OnSearchBar)

val Colors.positive get() = resolveColor(CommonsColor.Positive)
val Colors.negative get() = resolveColor(CommonsColor.Negative)

val Colors.warningSurface: Color
    @Composable get() = resolveColor(CommonsColor.WarningSurface)

val Colors.onWarningSurface: Color
    @Composable get() = resolveColor(CommonsColor.OnWarningSurface)

val Colors.politicalVotes get() = PoliticalColor.Vote
val Colors.house get() = PoliticalColor.House
val Colors.graphPrimaryColors get() = CommonsColor.Graph.Primary
val Colors.graphSecondaryColors get() = CommonsColor.Graph.Secondary

val Colors.modalScrim get() = resolveColor(CommonsColor.ModalScrim)

val invertedColors
    @Composable get() = when (MaterialTheme.colors.isLight) {
        true -> CommonsDarkThemeColors
        false -> CommonsLightThemeColors
    }

val Colors.textPrimary: Color
    @Composable get() = resolveColor(CommonsColor.TextPrimary)

val Colors.textSecondary: Color
    @Composable get() = resolveColor(CommonsColor.TextSecondary)

val Colors.textTertiary: Color
    @Composable get() = resolveColor(CommonsColor.TextTertiary)

val Colors.textPrimaryLight: Color get() = resolveColor(CommonsColor.Text.PrimaryLight)
val Colors.textSecondaryLight: Color get() = resolveColor(CommonsColor.Text.SecondaryLight)
val Colors.textTertiaryLight: Color get() = resolveColor(CommonsColor.Text.TertiaryLight)

val Colors.textPrimaryDark: Color get() = resolveColor(CommonsColor.Text.PrimaryDark)
val Colors.textSecondaryDark: Color get() = resolveColor(CommonsColor.Text.SecondaryDark)
val Colors.textTertiaryDark: Color get() = resolveColor(CommonsColor.Text.TertiaryDark)

val Colors.searchBarColors @Composable get() =
    TextFieldDefaults.textFieldColors(
        textColor = onSearchBar,
        placeholderColor = onSearchBar.copy(ContentAlpha.medium),
        cursorColor = secondary,
        focusedIndicatorColor = secondary,
        trailingIconColor = onSearchBar,
    )
