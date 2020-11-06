package org.beatonma.commons.compose.ambient

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import org.beatonma.commons.theme.compose.theme.invertedColors

@Composable
inline val colors
    get() = MaterialTheme.colors

@Composable
inline val shapes
    get() = MaterialTheme.shapes

@Composable
inline val typography
    get() = MaterialTheme.typography

@Composable
inline val invertedColors
    get() = MaterialTheme.invertedColors
