package org.beatonma.commons.compose.components.text

import androidx.annotation.PluralsRes
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import org.beatonma.commons.compose.components.OptionalText
import org.beatonma.commons.compose.util.withAnnotatedStyle

@Composable
fun PluralText(
    @PluralsRes resId: Int,
    vararg formatArgs: Any,
    quantity: Int,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
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
    withAnnotatedStyle: Boolean = false,
) {
    val resolvedText: AnnotatedString =
        pluralResource(resId, quantity, formatArgs).let {
            if (withAnnotatedStyle) it.withAnnotatedStyle()
            else AnnotatedString(it)
        }

    OptionalText(
        resolvedText,
        modifier,
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
        onTextLayout,
        style
    )
}

@Composable
private fun pluralResource(@PluralsRes resId: Int, quantity: Int, vararg formatArgs: Array<out Any>): String =
    LocalContext.current.resources.getQuantityString(resId, quantity, formatArgs)
