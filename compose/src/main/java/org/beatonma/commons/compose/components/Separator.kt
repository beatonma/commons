package org.beatonma.commons.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material.AmbientContentColor
import androidx.compose.material.AmbientTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.AmbientLayoutDirection
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import org.beatonma.commons.compose.ambient.typography
import org.beatonma.commons.compose.util.ComposableBlock
import org.beatonma.commons.compose.util.size
import org.beatonma.commons.theme.compose.Padding
import org.beatonma.commons.theme.compose.plus

private const val ALPHA = 0.1F

@Composable
fun HorizontalSeparator(
    modifier: Modifier = Modifier,
    color: Color = AmbientContentColor.current.copy(alpha = ALPHA),
    text: ComposableBlock? = null,
) {
    if (text == null) {
        Box(
            Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            Separator(
                color = color,
                modifier = Modifier
                    .padding(Padding.HorizontalSeparator)
                    .preferredHeight(1.dp)
                    .fillMaxWidth(0.25F)
                    .then(modifier),
            )
        }
    }
    else {
        HorizontalSeparatorWithContent(color, modifier, content = text)
    }
}

@Composable
fun VerticalSeparator(
    modifier: Modifier = Modifier,
    color: Color = AmbientContentColor.current.copy(alpha = ALPHA),
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
                .padding(Padding.VerticalSeparator)
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
    layoutDirection: LayoutDirection = AmbientLayoutDirection.current,
    content: @Composable () -> Unit,
) {
    val alignment = Alignment.Center
    val lineModifier = Modifier
        .padding(Padding.HorizontalSeparator + Padding.VerticalSeparator)
        .preferredHeight(1.dp)

    Providers(
        AmbientTextStyle provides typography.caption,
        AmbientContentColor provides color,
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

            val contentConstraints = constraints.copy(maxWidth = (constraints.maxWidth.toFloat() * .7F).toInt())
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
