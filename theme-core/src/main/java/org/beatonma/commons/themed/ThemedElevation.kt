package org.beatonma.commons.themed

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp

val LocalElevation =
    staticCompositionLocalOf<ThemedElevation> { error("LocalElevation has not been provided") }

inline val elevation: ThemedElevation
    @ReadOnlyComposable @Composable get() = LocalElevation.current

interface ThemedElevation {
    val Card: Dp
    val Toolbar: Dp
    val ModalSurface: Dp
}
