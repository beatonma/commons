package org.beatonma.commons.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import org.beatonma.commons.compose.padding.padding
import org.beatonma.commons.compose.util.size
import org.beatonma.commons.themed.padding

private const val ALPHA = 0.1F

@Composable
fun HorizontalSeparator(
    modifier: Modifier = Modifier,
    color: Color = LocalContentColor.current.copy(alpha = ALPHA),
    text: (@Composable () -> Unit)? = null,
) {
    if (text == null) {
        Box(
            Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            Separator(
                color = color,
                modifier = Modifier
                    .padding(padding.HorizontalSeparator)
                    .height(1.dp)
                    .fillMaxWidth(0.25F)
                    .then(modifier),
            )
        }
    } else {
        HorizontalSeparatorWithContent(color, modifier, content = text)
    }
}

@Composable
fun VerticalSeparator(
    modifier: Modifier = Modifier,
    color: Color = LocalContentColor.current.copy(alpha = ALPHA),
) {
    Box(
        Modifier.fillMaxHeight(),
        contentAlignment = Alignment.Center,
    ) {
        Separator(
            color = color,
            modifier = Modifier
                .width(1.dp)
                .fillMaxHeight(0.6F)
                .padding(padding.VerticalSeparator)
                .then(modifier)
        )
    }
}

@Composable
private fun Separator(color: Color, modifier: Modifier) {
    Spacer(modifier.background(color))
}

@Composable
private fun HorizontalSeparatorWithContent(
    color: Color,
    modifier: Modifier,
    layoutDirection: LayoutDirection = LocalLayoutDirection.current,
    content: @Composable () -> Unit,
) {
    val alignment = Alignment.Center
    val lineModifier = Modifier
        .padding(padding.HorizontalSeparator)
        .padding(padding.VerticalSeparator)
        .height(1.dp)

    CompositionLocalProvider(
        LocalTextStyle provides typography.caption,
        LocalContentColor provides color,
    ) {
        Layout(
            modifier = modifier,
            content = {
                Separator(color, lineModifier)
                content()
                Separator(color, lineModifier)
            }
        ) { measurables, constraints ->
            check(measurables.size == 3)

            val contentConstraints =
                constraints.copy(maxWidth = (constraints.maxWidth.toFloat() * .7F).toInt())
            val contentPlaceable = measurables[1].measure(contentConstraints)

            val lineLength = (constraints.maxWidth - contentPlaceable.width) / 2
            val lineConstraints = Constraints.fixedWidth(lineLength)
            val before = measurables[0].measure(lineConstraints)
            val after = measurables[2].measure(lineConstraints)

            val width: Int = constraints.maxWidth
            val height: Int = contentPlaceable.height
            val availableSize = IntSize(width, height)

            val lineY = alignment.align(
                before.size,
                availableSize,
                layoutDirection
            ).y

            layout(width, height) {
                before.placeRelative(0, lineY)

                contentPlaceable.placeRelative(lineLength, 0)

                after.placeRelative(lineLength + contentPlaceable.width, lineY)
            }
        }
    }
}
