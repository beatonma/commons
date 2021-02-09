package org.beatonma.commons.theme.compose.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.beatonma.commons.theme.compose.Padding
import org.beatonma.commons.theme.compose.formatting.formatted
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
