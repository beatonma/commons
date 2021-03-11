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
import org.beatonma.commons.theme.compose.padding.Padding
import org.beatonma.commons.theme.compose.formatting.formatted
import org.beatonma.commons.theme.compose.padding.padding
import org.beatonma.commons.theme.compose.theme.componentTitle
import org.beatonma.commons.theme.compose.theme.quote
import org.beatonma.commons.theme.compose.theme.screenTitle
import java.time.LocalDate
import java.time.LocalDateTime


@Composable
fun ScreenTitle(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    maxLines: Int = 3,
    autoPadding: Boolean = true,
) {
    Text(
        text,
        modifier
            .semantics { heading() }
            .apply {
                if (autoPadding) padding(Padding.ScreenHorizontal)
            }
        ,
        color,
        style = typography.screenTitle,
        maxLines = maxLines,
    )
}

@Composable
fun ComponentTitle(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    maxLines: Int = 3,
    autoPadding: Boolean = true,
) {
    Text(
        text,
        modifier
            .semantics { heading() }
            .apply {
                if (autoPadding) padding(Padding.ScreenHorizontal)
            },
        color,
        style = typography.componentTitle,
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
