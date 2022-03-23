package org.beatonma.commons.themed

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp

val LocalSize =
    staticCompositionLocalOf<ThemedSize> { error("LocalSize has not been provided") }

inline val size: ThemedSize
    @ReadOnlyComposable @Composable get() = LocalSize.current

interface ThemedSize {
    val IconButton: Dp
    val IconSmall: Dp
    val IconLarge: Dp
}
