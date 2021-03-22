package org.beatonma.commons.compose.ambient

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import org.beatonma.commons.theme.compose.theme.LocalAnimationSpec
import org.beatonma.commons.theme.compose.theme.invertedColors

// TODO move to theme module
inline val colors
    @Composable get() = MaterialTheme.colors

inline val shapes
    @Composable get() = MaterialTheme.shapes

inline val typography
    @Composable get() = MaterialTheme.typography

inline val invertedColors
    @Composable get() = MaterialTheme.invertedColors

inline val animation
    @Composable get() = LocalAnimationSpec.current
