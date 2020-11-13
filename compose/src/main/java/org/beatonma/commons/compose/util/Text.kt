package org.beatonma.commons.compose.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.annotatedString
import org.beatonma.commons.theme.compose.theme.CommonsSpanStyle

private const val DOT = '·'

@Composable
fun dotted(vararg components: String?) =
    components.filterNot(String?::isNullOrEmpty)
        .joinToString(
            separator = " $DOT "
        )

/**
 * Styles supported by [String.withAnnotatedStyle]
 */
private enum class MarkdownStyle {
    Normal,
    Italic,
    Bold,
    BoldItalic,
    Quote,
    ;
}

@Composable
fun String.withAnnotatedStyle() = withAnnotatedStyle(
    CommonsSpanStyle.italic,
    CommonsSpanStyle.bold,
    CommonsSpanStyle.quote(),
)

/**
 * Render basic asterisk-based markdown-like styling as [AnnotatedString].
 * Currently only handles italic(*) and bold(**) styles.
 *
 * e.g. It can handle
 *      *this* or **this**
 *      or combinations like ***this***
 *      or **nesting *like* this!**
 */
fun String.withAnnotatedStyle(
    italicStyle: SpanStyle,
    boldStyle: SpanStyle,
    quoteStyle: SpanStyle,
) = annotatedString {

    var totalStyleSymbols = 0
    var pendingStyle = MarkdownStyle.Normal
    var previousPendingStyle = MarkdownStyle.Normal

    val styleMap = mutableMapOf<Int, MarkdownStyle>()

    fun consumePendingStyle(position: Int) {
        if (pendingStyle == MarkdownStyle.Normal) return
        styleMap[position - totalStyleSymbols] = pendingStyle
        pendingStyle = MarkdownStyle.Normal
        previousPendingStyle = MarkdownStyle.Normal
    }

    this@withAnnotatedStyle.forEachIndexed { i, c ->
        when (c) {
            '*' -> {
                totalStyleSymbols += 1

                pendingStyle = when (pendingStyle) {
                    MarkdownStyle.Normal -> MarkdownStyle.Italic
                    MarkdownStyle.Italic -> MarkdownStyle.Bold
                    MarkdownStyle.Bold -> MarkdownStyle.BoldItalic

                    MarkdownStyle.Quote -> {
                        consumePendingStyle(i)
                        MarkdownStyle.Italic
                    }

                    else -> pendingStyle
                }
                if (pendingStyle == previousPendingStyle) {
                    consumePendingStyle(i)
                }

                previousPendingStyle = pendingStyle
            }

            '`', '\'', '"' -> {
                totalStyleSymbols += 1
                pendingStyle = MarkdownStyle.Quote
            }

            else -> {
                consumePendingStyle(i)
                append(c)
            }
        }
    }
    if (pendingStyle != MarkdownStyle.Normal) {
        styleMap[length] = pendingStyle
    }

    if (styleMap.isEmpty()) {
        return@annotatedString
    }

    var italicPosition = -1
    var boldPosition = -1
    var boldItalicPosition = -1
    var quotePosition = -1

    // Traverse styleMap to find pairs of each style type and apply them to the AnnotatedString.
    styleMap.forEach { (position, style) ->
        when (style) {
            MarkdownStyle.Italic -> {
                italicPosition = if (italicPosition < 0) position
                else {
                    addStyle(italicStyle, italicPosition, position)
                    -1
                }
            }

            MarkdownStyle.Bold -> {
                boldPosition = if (boldPosition < 0) position
                else {
                    addStyle(boldStyle, boldPosition, position)
                    -1
                }
            }

            MarkdownStyle.BoldItalic -> {
                boldItalicPosition = if (boldItalicPosition < 0) position
                else {
                    addStyle(italicStyle + boldStyle, boldItalicPosition, position)
                    -1
                }
            }

            MarkdownStyle.Quote -> {
                quotePosition = if (quotePosition < 0) position
                else {
                    addStyle(quoteStyle, quotePosition, position)
                    -1
                }
            }
        }
    }
}

/**
 * Take the list in (i, i+1) pairs. If list size is odd the last item will be ignored.
 */
private fun <T> List<T>.pairwise(): List<Pair<T, T>> {
    val s = size
    val result = mutableListOf<Pair<T, T>>()

    for (i in 0 until s step 2) {
        if (i + 1 < s) {
            result.add(this[i] to this[i + 1])
        }
    }
    return result
}
