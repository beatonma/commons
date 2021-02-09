package org.beatonma.commons.theme.compose.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material.AmbientContentColor
import androidx.compose.material.AmbientTextStyle
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.gesture.tapGestureFilter
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.AmbientUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import org.beatonma.commons.theme.compose.Padding
import org.beatonma.commons.theme.compose.formatting.formatted
import org.beatonma.commons.theme.compose.theme.CommonsSpanStyle
import org.beatonma.commons.theme.compose.theme.componentTitle
import org.beatonma.commons.theme.compose.theme.quote
import org.beatonma.commons.theme.compose.theme.screenTitle
import org.beatonma.commons.theme.compose.util.linkify
import java.time.LocalDate
import java.time.LocalDateTime


@Composable
fun ScreenTitle(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
) {
    Text(
        text,
        modifier.padding(Padding.ScreenHorizontal),
        color,
        style = typography.screenTitle,
    )
}

@Composable
fun ComponentTitle(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
) {
    Text(
        text,
        modifier.padding(Padding.ScreenHorizontal),
        color,
        style = typography.componentTitle,
    )
}

@Composable
fun Quote(
    text: String?,
    modifier: Modifier = Modifier,
) {
    if (text == null) return
    Text(
        text,
        modifier.padding(Padding.Card),
        style = typography.quote,
    )
}

@Composable
fun Quote(
    @StringRes resId: Int,
    vararg formatArgs: Any,
    modifier: Modifier = Modifier,
) {
    Quote(stringResource(resId, *formatArgs), modifier)
}

@Composable
fun Caption(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text,
        modifier,
        style = typography.caption,
    )
}

@Composable
fun Caption(
    @StringRes resId: Int,
    vararg formatArgs: Any,
    modifier: Modifier = Modifier,
) {
    Caption(stringResource(resId, *formatArgs), modifier)
}

@Composable
fun Date(
    date: LocalDate,
    modifier: Modifier = Modifier,
) {
    Caption(date.formatted(), modifier)
}


@Composable
fun DateTime(
    datetime: LocalDateTime,
    modifier: Modifier = Modifier,
) {
    Caption(datetime.formatted(), modifier)
}


@Composable
fun Hint(
    @StringRes resId: Int,
    vararg formatArgs: Any,
    modifier: Modifier = Modifier,
    color: Color = AmbientContentColor.current.copy(alpha = 0.4F),
) {
    Hint(stringResource(resId, *formatArgs), modifier, color)
}

@Composable
fun Hint(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = AmbientContentColor.current.copy(alpha = 0.4F),
) {
    Text(text, modifier, color = color)
}

@Composable
fun LinkedText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    linkStyle: SpanStyle = CommonsSpanStyle.hyperlink,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    inlineContent: Map<String, InlineTextContent> = mapOf(),
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = AmbientTextStyle.current,
    clickable: Boolean = true,
) {
    val uriHandler = AmbientUriHandler.current
    val layoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }
    val annotatedText = text.linkify(style = linkStyle)

    Text(
        annotatedText,
        modifier.tapGestureFilter { position ->
            if (clickable) {
                layoutResult.value?.let { textLayoutResult ->
                    val offset = textLayoutResult.getOffsetForPosition(position)
                    annotatedText.getStringAnnotations(offset, offset)
                        .firstOrNull()
                        ?.let { annotation ->
                            if (annotation.tag == "url") {
                                uriHandler.openUri(annotation.item)
                            }
                        }
                }
            }
        },
        color,
        fontSize,
        fontStyle,
        fontWeight,
        fontFamily,
        letterSpacing,
        textDecoration,
        textAlign,
        lineHeight,
        overflow,
        softWrap,
        maxLines,
        inlineContent,
        onTextLayout = {
            onTextLayout(it)
            layoutResult.value = it
        },
        style
    )
}
