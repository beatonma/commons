package org.beatonma.commons.compose.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

typealias ComposableBlock = @Composable () -> Unit
typealias TypedComportBlock<T> = @Composable (T) -> Unit

typealias ModifierBlock = @Composable Modifier.() -> Modifier
typealias TypedModifierBlock<T> = @Composable Modifier.(T) -> Modifier
