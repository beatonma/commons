package org.beatonma.commons.theme

import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import org.beatonma.commons.themed.ThemedSpanStyle

internal object CommonsSpanStyle : ThemedSpanStyle {
    override val italic: SpanStyle @Composable get() = SpanStyle(fontStyle = FontStyle.Italic)
    override val bold: SpanStyle @Composable get() = SpanStyle(fontWeight = FontWeight.Bold)
    override val quote: SpanStyle
        @Composable get() = SpanStyle(
            fontFamily = FontFamily.Monospace,
            background = LocalContentColor.current.copy(alpha = 0.2F)
        )

    override val hyperlink: SpanStyle
        @Composable get() =
            SpanStyle(
                color = MaterialTheme.colors.primary,
                textDecoration = TextDecoration.Underline
            )
}
