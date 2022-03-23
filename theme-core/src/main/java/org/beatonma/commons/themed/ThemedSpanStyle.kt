package org.beatonma.commons.themed

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.SpanStyle

val LocalSpanStyle =
    staticCompositionLocalOf<ThemedSpanStyle> { error("LocalSpanStyle has not been provided") }

inline val spanStyle: ThemedSpanStyle
    @ReadOnlyComposable @Composable get() = LocalSpanStyle.current

interface ThemedSpanStyle {
    val italic: SpanStyle @Composable get
    val bold: SpanStyle @Composable get
    val quote: SpanStyle @Composable get
    val hyperlink: SpanStyle @Composable get
}
