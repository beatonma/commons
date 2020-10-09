package org.beatonma.commons.compose.modifiers

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.beatonma.commons.compose.ambient.shapes
import org.beatonma.commons.theme.compose.Whitespace


@Composable
fun Modifier.withBorder(
    color: Color,
    width: Dp = 2.dp,
    shape: Shape = shapes.small,
): Modifier = this.padding(horizontal = Whitespace.Image.around)
    .border(width, color, shape)
    .padding(width)
    .clip(shape)

@Composable
fun Modifier.withBorder(
    colors: Array<Color>,
    width: Dp = 2.dp,
    shape: Shape = shapes.small,
    paddingAround: Dp = Whitespace.Image.around,
): Modifier = padding(paddingAround)
    .forEachOf(colors) { color ->
        border(width, color, shape)
        padding(width)
    }
    .clip(shape)
