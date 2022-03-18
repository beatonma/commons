package org.beatonma.commons.compose.util

import android.util.Patterns
import androidx.annotation.PluralsRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import org.beatonma.commons.themed.themedSpanStyle
import java.util.regex.Pattern

private const val DOT = '•'
private const val SEPARATOR = " $DOT "
internal const val ANNOTATION_TAG_URL = "url"

@Composable
fun dotted(vararg components: String?) =
    components.filterNot(String?::isNullOrEmpty)
        .joinToString(
            separator = SEPARATOR
        )

@Composable
fun Collection<String?>.dotted(): String =
    filterNot(String?::isNullOrEmpty)
        .joinToString(separator = SEPARATOR)

@Composable
infix fun String?.dot(other: String?): String = dotted(this, other)

@Composable
fun pluralResource(
    @PluralsRes resId: Int,
    quantity: Int,
    formatArgs: Array<out Any> = arrayOf(quantity)
): String {
    return LocalContext.current.resources.getQuantityString(resId, quantity, *formatArgs)
}


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
    themedSpanStyle.italic,
    themedSpanStyle.bold,
    themedSpanStyle.quote,
)

/**
 * Render basic markdown-like styling as [AnnotatedString].
 * See [MarkdownStyle] for supported styles
 *
 * Supports:
 * - *italic* text
 * - **bold text**
 * - "quotations"
 *
 *
 * e.g. It can handle
 *      *this* or **this** or 'this'
 *      or combinations like ***this***
 *      or **nesting *like* this!**
 */
fun String.withAnnotatedStyle(
    italicStyle: SpanStyle,
    boldStyle: SpanStyle,
    quoteStyle: SpanStyle,
) = buildAnnotatedString {

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
        return@buildAnnotatedString
    }

    var italicPosition = -1
    var boldPosition = -1
    var boldItalicPosition = -1
    var quotePosition = -1

    // Traverse styleMap to find pairs of each style type and apply them to the AnnotatedString.
    styleMap.forEach { (position, style) ->
        when (style) {
            MarkdownStyle.Normal -> {
            }

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

@Composable
fun String.rememberLinkifiedText(
    style: SpanStyle = themedSpanStyle.hyperlink,
): MutableState<AnnotatedString> = remember { mutableStateOf(withAnnotatedUrls(style)) }

internal fun String.withAnnotatedUrls(
    style: SpanStyle,
    pattern: Pattern = Patterns.WEB_URL
) = buildAnnotatedString {
    append(this@withAnnotatedUrls)

    pattern.toRegex().findAll(this@withAnnotatedUrls).forEach { result ->
        val first = result.range.first
        val last = result.range.last

        addStyle(style, first, last + 1)
        addStringAnnotation(ANNOTATION_TAG_URL, result.value, first, last)
    }
}
