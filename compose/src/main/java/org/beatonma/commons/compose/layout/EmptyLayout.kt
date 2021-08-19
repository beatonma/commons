package org.beatonma.commons.compose.layout

import androidx.compose.runtime.Composable
import androidx.compose.ui.layout.Layout

@Composable
fun EmptyLayout() = Layout(content = {}) { _, _ -> layout(0, 0) {} }
