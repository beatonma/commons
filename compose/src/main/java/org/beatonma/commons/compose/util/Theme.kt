package org.beatonma.commons.compose.util

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
inline val colors
    get() = MaterialTheme.colors

@Composable
inline val shapes
    get() = MaterialTheme.shapes

@Composable
inline val typography
    get() = MaterialTheme.typography
