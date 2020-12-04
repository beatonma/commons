package org.beatonma.commons.theme.compose.theme

import androidx.compose.material.AmbientContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp

private val default = Typography()

val CommonsTypography = Typography(
    h1 = default.h1.copy(fontSize = 60.sp, fontWeight = FontWeight.Black),
    h2 = default.h2.copy(fontSize = 48.sp),
    h3 = default.h3.copy(fontSize = 42.sp)
)

object CommonsSpanStyle {
    val italic: SpanStyle = SpanStyle(fontStyle = FontStyle.Italic)
    val bold: SpanStyle = SpanStyle(fontWeight = FontWeight.Bold)
    val quote: SpanStyle = SpanStyle(fontFamily = FontFamily.Monospace)

    @Composable
    val hyperlink: SpanStyle
        get() =
            SpanStyle(color = MaterialTheme.colors.primary,
                textDecoration = TextDecoration.Underline)

    @Composable
    fun quote(): SpanStyle = SpanStyle(
        background = AmbientContentColor.current.copy(alpha = 0.2F),
        fontFamily = FontFamily.Monospace,
    )
}
