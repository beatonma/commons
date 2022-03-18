/**
 * Adapted from https://github.com/joen93/compose-html-text
 */
package org.beatonma.commons.compose.util

import android.graphics.Typeface
import android.text.Html
import android.text.ParcelableSpan
import android.text.Spanned
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.ScaleXSpan
import android.text.style.StrikethroughSpan
import android.text.style.StyleSpan
import android.text.style.SubscriptSpan
import android.text.style.SuperscriptSpan
import android.text.style.URLSpan
import android.text.style.UnderlineSpan
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextGeometricTransform
import androidx.compose.ui.unit.em
import androidx.core.text.getSpans
import org.beatonma.commons.compose.components.text.ClickableText


@Composable
fun HtmlText(
    text: String,
    modifier: Modifier = Modifier,
    htmlStyle: HtmlStyle = rememberHtmlStyle(),
    clickable: Boolean = true,
) {
    val uriHandler = LocalUriHandler.current
    val annotatedText by remember {
        mutableStateOf(text.asAnnotatedHtml(htmlStyle))
    }

    ClickableText(
        annotatedText,
        clickable = clickable,
        modifier = modifier,
    ) { offset ->
        annotatedText.getStringAnnotations(offset, offset)
            .firstOrNull()
            ?.let { annotation ->
                if (annotation.tag == URL_TAG) {
                    uriHandler.openUri(annotation.item)
                }
            }
    }
}

private fun CharSequence.asAnnotatedHtml(
    htmlStyle: HtmlStyle,
): AnnotatedString {
    return when (this) {
        is Spanned -> this.asAnnotatedHtml(htmlStyle = htmlStyle)
        is String -> {
            val htmlSpan = Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
            htmlSpan.asAnnotatedHtml(htmlStyle = htmlStyle)
        }
        else -> buildAnnotatedString { append(this@asAnnotatedHtml.toString()) }
    }
}

private fun Spanned.asAnnotatedHtml(htmlStyle: HtmlStyle): AnnotatedString {
    return buildAnnotatedString {
        append(this@asAnnotatedHtml.toString())
        this@asAnnotatedHtml.getSpanRanges<ParcelableSpan>(htmlStyle)
            .forEach { (span, style, start, end) ->
                when (span) {
                    is URLSpan -> {
                        addStyle(style, start, end)
                        span.url?.let { addStringAnnotation(ANNOTATION_TAG_URL, it, start, end) }
                    }
                    else -> addStyle(style, start, end)
                }
            }
    }
}

@Composable
fun rememberHtmlStyle(
    normalSpanStyle: SpanStyle = SpanStyle(
        fontWeight = FontWeight.Normal,
        fontStyle = FontStyle.Normal,
        textDecoration = TextDecoration.None,
    ),
    boldSpanStyle: SpanStyle = SpanStyle(
        fontWeight = FontWeight.Bold,
    ),
    italicSpanStyle: SpanStyle = SpanStyle(
        fontStyle = FontStyle.Italic,
    ),
    boldItalicSpanStyle: SpanStyle = SpanStyle(
        fontWeight = FontWeight.Bold,
        fontStyle = FontStyle.Italic,
    ),
    underlineSpanStyle: SpanStyle = SpanStyle(
        textDecoration = TextDecoration.Underline,
    ),
    strikethroughSpanStyle: SpanStyle = SpanStyle(
        textDecoration = TextDecoration.LineThrough,
    ),
    urlSpanStyle: SpanStyle = SpanStyle(
        color = colors.primary,
        textDecoration = TextDecoration.Underline,
    ),
    subscriptSpanStyle: SpanStyle = SpanStyle(
        baselineShift = BaselineShift.Subscript,
    ),
    superscriptSpanStyle: SpanStyle = SpanStyle(
        baselineShift = BaselineShift.Superscript,
    ),
    foregroundColorSpanStyle: SpanStyle = SpanStyle(
        color = colors.onPrimary,
    ),
    backgroundColorSpanStyle: SpanStyle = SpanStyle(
        background = colors.primarySurface,
    ),
    scaleXSpanStyle: SpanStyle = SpanStyle(
        textGeometricTransform = TextGeometricTransform(scaleX = 2f),
    ),
    relativeSizeSpanStyle: SpanStyle = SpanStyle(
        fontSize = 2.em,
    ),
): HtmlStyle = remember {
    mutableStateOf(
        HtmlStyle(
            normalSpanStyle = normalSpanStyle,
            boldSpanStyle = boldSpanStyle,
            italicSpanStyle = italicSpanStyle,
            boldItalicSpanStyle = boldItalicSpanStyle,
            underlineSpanStyle = underlineSpanStyle,
            strikethroughSpanStyle = strikethroughSpanStyle,
            urlSpanStyle = urlSpanStyle,
            subscriptSpanStyle = subscriptSpanStyle,
            superscriptSpanStyle = superscriptSpanStyle,
            foregroundColorSpanStyle = foregroundColorSpanStyle,
            backgroundColorSpanStyle = backgroundColorSpanStyle,
            scaleXSpanStyle = scaleXSpanStyle,
            relativeSizeSpanStyle = relativeSizeSpanStyle,
        )
    )
}.value

/**
 * Styling configuration for a [HtmlText].
 *
 * This class basically groups multiple [SpanStyle]s for every by the
 * [HtmlText] composable supported [ParcelableSpan].
 *
 * For each supported [ParcelableSpan] there's a [SpanStyle] parameter.
 *
 * @param normalSpanStyle The [SpanStyle] for [StyleSpan] with style [Typeface.NORMAL]
 * @param boldSpanStyle The [SpanStyle] for [StyleSpan] with style [Typeface.BOLD]
 * @param italicSpanStyle The [SpanStyle] for [StyleSpan] with style [Typeface.ITALIC]
 * @param boldItalicSpanStyle The [SpanStyle] for [StyleSpan] with style [Typeface.BOLD_ITALIC]
 * @param underlineSpanStyle The [SpanStyle] for [UnderlineSpan]
 * @param strikethroughSpanStyle The [SpanStyle] for [StrikethroughSpan]
 * @param urlSpanStyle the [SpanStyle] for [URLSpan]
 * @param subscriptSpanStyle The [SpanStyle] for [SubscriptSpan]
 * @param superscriptSpanStyle The [SpanStyle] for [SuperscriptSpan]
 * @param foregroundColorSpanStyle The [SpanStyle] for [ForegroundColorSpan]
 * @param backgroundColorSpanStyle The [SpanStyle] for [BackgroundColorSpan]
 * @param scaleXSpanStyle The [SpanStyle] for [ScaleXSpan]
 * @param relativeSizeSpanStyle The [SpanStyle] for [RelativeSizeSpan]
 *
 * @see SpanStyle
 * @see ParcelableSpan
 * @see AnnotatedString
 * @see HtmlText
 */
data class HtmlStyle internal constructor(
    val normalSpanStyle: SpanStyle,
    val boldSpanStyle: SpanStyle,
    val italicSpanStyle: SpanStyle,
    val boldItalicSpanStyle: SpanStyle,
    val underlineSpanStyle: SpanStyle,
    val strikethroughSpanStyle: SpanStyle,
    val urlSpanStyle: SpanStyle,
    val subscriptSpanStyle: SpanStyle,
    val superscriptSpanStyle: SpanStyle,
    val foregroundColorSpanStyle: SpanStyle,
    val backgroundColorSpanStyle: SpanStyle,
    val scaleXSpanStyle: SpanStyle,
    val relativeSizeSpanStyle: SpanStyle,
) {
    /**
     * Get the matching [SpanStyle] for the given [ParcelableSpan]
     * @param parcelableSpan The span for which a [SpanStyle] is needed
     * @return a matching [SpanStyle] that was defined upon creation of the [HtmlStyle], or null if
     * there's no [SpanStyle] defined for the given [ParcelableSpan]
     */
    fun getStyleFor(parcelableSpan: ParcelableSpan): SpanStyle? =
        when (parcelableSpan) {
            is StyleSpan -> {
                when (parcelableSpan.style) {
                    Typeface.NORMAL -> normalSpanStyle
                    Typeface.BOLD -> boldSpanStyle
                    Typeface.ITALIC -> italicSpanStyle
                    Typeface.BOLD_ITALIC -> boldItalicSpanStyle
                    else -> normalSpanStyle
                }
            }
            is UnderlineSpan -> underlineSpanStyle
            is StrikethroughSpan -> strikethroughSpanStyle
            is URLSpan -> urlSpanStyle
            is SubscriptSpan -> subscriptSpanStyle
            is SuperscriptSpan -> superscriptSpanStyle
            is ForegroundColorSpan -> foregroundColorSpanStyle
            is BackgroundColorSpan -> backgroundColorSpanStyle
            is ScaleXSpan -> scaleXSpanStyle
            is RelativeSizeSpan -> relativeSizeSpanStyle
            else -> null
        }

    /**
     * Returns a new html style that is a combination of this style and the given [other] style.
     *
     * [other] html style's null or inherit properties are replaced with the non-null properties of
     * this html style. Another way to think of it is that the "missing" properties of the [other]
     * style are _filled_ by the properties of this style.
     *
     * If the given html style is null, returns this html style.
     */
    fun merge(other: HtmlStyle? = null): HtmlStyle {
        if (other == null) return this

        return HtmlStyle(
            normalSpanStyle = other.normalSpanStyle.merge(this.normalSpanStyle),
            boldSpanStyle = other.boldSpanStyle.merge(this.boldSpanStyle),
            italicSpanStyle = other.italicSpanStyle.merge(this.italicSpanStyle),
            boldItalicSpanStyle = other.boldItalicSpanStyle.merge(this.boldItalicSpanStyle),
            underlineSpanStyle = other.underlineSpanStyle.merge(this.underlineSpanStyle),
            strikethroughSpanStyle = other.strikethroughSpanStyle.merge(this.strikethroughSpanStyle),
            urlSpanStyle = other.urlSpanStyle.merge(this.urlSpanStyle),
            subscriptSpanStyle = other.subscriptSpanStyle.merge(this.subscriptSpanStyle),
            superscriptSpanStyle = other.superscriptSpanStyle.merge(this.superscriptSpanStyle),
            foregroundColorSpanStyle = other.foregroundColorSpanStyle.merge(this.foregroundColorSpanStyle),
            backgroundColorSpanStyle = other.backgroundColorSpanStyle.merge(this.backgroundColorSpanStyle),
            scaleXSpanStyle = other.scaleXSpanStyle.merge(this.scaleXSpanStyle),
            relativeSizeSpanStyle = other.relativeSizeSpanStyle.merge(this.relativeSizeSpanStyle),
        )
    }

    /**
     * Plus operator overload that applies a [merge].
     */
    operator fun plus(other: HtmlStyle): HtmlStyle = this.merge(other)

    @Composable
    fun disabled(): HtmlStyle = this.copy(
        normalSpanStyle = this.normalSpanStyle.disabled(),
        boldSpanStyle = this.boldSpanStyle.disabled(),
        italicSpanStyle = this.italicSpanStyle.disabled(),
        boldItalicSpanStyle = this.boldItalicSpanStyle.disabled(),
        underlineSpanStyle = this.underlineSpanStyle.disabled(),
        strikethroughSpanStyle = this.strikethroughSpanStyle.disabled(),
        urlSpanStyle = this.urlSpanStyle.disabled(),
        subscriptSpanStyle = this.subscriptSpanStyle.disabled(),
        superscriptSpanStyle = this.superscriptSpanStyle.disabled(),
        foregroundColorSpanStyle = this.foregroundColorSpanStyle.disabled(),
        backgroundColorSpanStyle = this.backgroundColorSpanStyle.disabled(),
        scaleXSpanStyle = this.scaleXSpanStyle.disabled(),
        relativeSizeSpanStyle = this.relativeSizeSpanStyle.disabled(),
    )

    @Composable
    private fun SpanStyle.disabled() = this.copy(color = this.color.disabled())

    @Composable
    private fun Color.disabled(): Color = this.copy(alpha = ContentAlpha.disabled)
}


internal const val ANNOTATION_TAG_URL = "url"

private inline fun <reified T : ParcelableSpan> Spanned.getSpanRanges(
    htmlStyle: HtmlStyle,
    start: Int = 0,
    end: Int = length,
): List<SpanRange<T>> =
    getSpans<T>(start, end)
        .mapNotNull {
            SpanRange(
                span = it,
                style = htmlStyle.getStyleFor(it) ?: return@mapNotNull null,
                start = getSpanStart(it),
                end = getSpanEnd(it)
            )
        }

/**
 * Utility data class to combine the [ParcelableSpan], [SpanStyle], start and end of the Span.
 */
private data class SpanRange<T : ParcelableSpan>(
    val span: T,
    val style: SpanStyle,
    val start: Int,
    val end: Int,
) {
    init {
        require((start <= end)) { "Start can't be bigger than end: start=$start end=$end" }
    }
}
