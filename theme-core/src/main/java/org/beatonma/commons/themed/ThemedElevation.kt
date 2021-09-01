package org.beatonma.commons.themed

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp

val LocalElevation =
    staticCompositionLocalOf<ThemedElevation> { error("LocalElevation has not been provided") }

val themedElevation: ThemedElevation
    @ReadOnlyComposable @Composable get() = LocalElevation.current

interface ThemedElevation {
    val Card: Dp
    val ModalSurface: Dp
}
