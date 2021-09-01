package org.beatonma.commons.compose.components.text

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalUriHandler
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
import org.beatonma.commons.compose.util.URL_TAG
import org.beatonma.commons.compose.util.linkify
import org.beatonma.commons.themed.themedSpanStyle

@Composable
fun LinkedText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    linkStyle: SpanStyle = themedSpanStyle.hyperlink,
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
    style: TextStyle = LocalTextStyle.current,
    clickable: Boolean = true,
    action: ((String) -> Unit)? = null
) {
    val uriHandler = LocalUriHandler.current
    val resolvedAction = action ?: { uri ->
        uriHandler.openUri(uri)
    }

    val layoutResult: MutableState<TextLayoutResult?> = remember { mutableStateOf(null) }
    val annotatedText = text.linkify(style = linkStyle)

    Text(
        annotatedText,
        modifier.pointerInput(text) {
            detectTapGestures { position ->
                if (clickable) {
                    layoutResult.value?.let { textLayoutResult ->
                        val offset = textLayoutResult.getOffsetForPosition(position)
                        annotatedText.getStringAnnotations(offset, offset)
                            .firstOrNull()
                            ?.let { annotation ->
                                if (annotation.tag == URL_TAG) {
                                    resolvedAction(annotation.item)
                                }
                            }
                    }
                }
            }
        },
        color = color,
        fontSize = fontSize,
        fontStyle = fontStyle,
        fontWeight = fontWeight,
        fontFamily = fontFamily,
        letterSpacing = letterSpacing,
        textDecoration = textDecoration,
        textAlign = textAlign,
        lineHeight = lineHeight,
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
        inlineContent = inlineContent,
        onTextLayout = {
            onTextLayout(it)
            layoutResult.value = it
        },
        style = style
    )
}
