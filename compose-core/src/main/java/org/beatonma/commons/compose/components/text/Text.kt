package org.beatonma.commons.compose.components.text

import androidx.annotation.StringRes
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import org.beatonma.commons.compose.modifiers.onlyWhen
import org.beatonma.commons.compose.padding.padding
import org.beatonma.commons.themed.padding


@Composable
fun ScreenTitle(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = LocalContentColor.current,
    maxLines: Int = 3,
    textAlign: TextAlign? = null,
    autoPadding: Boolean = true,
) {
    Text(
        text,
        modifier
            .semantics { heading() }
            .onlyWhen(autoPadding) {
                padding(padding.ScreenHorizontal)
            },
        color,
        style = typography.h4,
        maxLines = maxLines,
        textAlign = textAlign,
    )
}

@Composable
fun ComponentTitle(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = LocalContentColor.current,
    maxLines: Int = 3,
    autoPadding: Boolean = true,
) {
    Text(
        text,
        modifier
            .semantics { heading() }
            .onlyWhen(autoPadding) {
                padding(padding.ScreenHorizontal)
            },
        color,
        style = typography.h5,
        maxLines = maxLines,
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
        modifier.padding(padding.Card),
        style = typography.body1,
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
fun Hint(
    @StringRes resId: Int,
    vararg formatArgs: Any,
    modifier: Modifier = Modifier,
    color: Color = LocalContentColor.current.copy(alpha = 0.4F),
) {
    Hint(stringResource(resId, *formatArgs), modifier, color)
}

@Composable
fun Hint(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = LocalContentColor.current.copy(alpha = 0.4F),
) {
    Text(text, modifier, color = color)
}
