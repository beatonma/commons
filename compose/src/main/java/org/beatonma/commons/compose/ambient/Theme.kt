package org.beatonma.commons.compose.ambient

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import org.beatonma.commons.theme.compose.theme.invertedColors

inline val colors
    @Composable get() = MaterialTheme.colors

inline val shapes
    @Composable get() = MaterialTheme.shapes

inline val typography
    @Composable get() = MaterialTheme.typography

inline val invertedColors
    @Composable get() = MaterialTheme.invertedColors
