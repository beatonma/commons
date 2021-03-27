package org.beatonma.commons.compose.ambient

import androidx.compose.runtime.staticCompositionLocalOf

/**
 * When true, UI elements should be optimised for navigation with a screen reader.
 *
 * Currently implemented in [org.beatonma.commons.compose.components.CollapsibleChip].
 *
 * e.g. Don't used timed state changes (like auto-collapse).
 *      Don't hide functionality for the sake of visual tidiness.
 *
 * May be expanded later for different types of optimisation.
 */
val LocalAccessibility = staticCompositionLocalOf { false }
