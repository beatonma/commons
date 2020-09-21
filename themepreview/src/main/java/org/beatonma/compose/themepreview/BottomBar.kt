package org.beatonma.compose.themepreview

import androidx.compose.animation.AnimatedFloatModel
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.BottomAppBar
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.onCommit
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Layout
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawOpacity
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.VectorAsset
import androidx.compose.ui.platform.AnimationClockAmbient
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp

private val BottomBarHeight = 56.dp

@Composable
internal fun BottomBar(
    selectedIndex: Int,
    itemCount: Int,
    animSpec: AnimationSpec<Float>,
    modifier: Modifier = Modifier,
    content: @Composable (List<AnimatedFloatModel>) -> Unit,
) {
    val clock = AnimationClockAmbient.current
    val selectionFractions = remember(itemCount) {
        // 'selectedness' of each item.
        List(itemCount) { i ->
            AnimatedFloatModel(if (i == selectedIndex) 1F else 0F, clock)
        }
    }

    onCommit(selectedIndex) {
        selectionFractions.forEachIndexed { index, fraction ->
            val target = if (index == selectedIndex) 1F else 0F
            if (fraction.targetValue != target) {
                fraction.animateTo(target, animSpec)
            }
        }
    }

    BottomAppBar(
        Modifier.fillMaxWidth()
            .preferredHeight(BottomBarHeight),
        cutoutShape = MaterialTheme.shapes.small,
        backgroundColor = MaterialTheme.colors.surface,
    ) {
        Layout(
            modifier = modifier.fillMaxSize(),
            children = {
                content(selectionFractions)
            }
        ) { measurables, constraints ->
            check(itemCount == measurables.size)

            val unselectedWidth = constraints.maxWidth / (itemCount + 1)
            val selectedWidth = constraints.maxWidth - (itemCount - 1) * unselectedWidth

            val itemPlaceables = measurables
                .mapIndexed { index, measurable ->
                    val width =
                        lerp(unselectedWidth, selectedWidth, selectionFractions[index].value)
                    measurable.measure(
                        constraints.copy(
                            minWidth = width,
                            maxWidth = width
                        )
                    )
                }

            layout(
                width = constraints.maxWidth,
                height = itemPlaceables.maxByOrNull { it.height }?.height ?: 0
            ) {
                var x = 0
                itemPlaceables.forEach { placeable ->
                    placeable.place(x = x, y = 0)
                    x += placeable.width
                }
            }
        }
    }
}

@Composable
internal fun SectionIcon(screen: Screen, progress: Float, onSelected: (Screen) -> Unit) {
    SectionIcon(screen, screen.title, screen.icon, progress, onSelected)
}

@Composable
internal fun SectionIcon(
    screen: Screen,
    title: String,
    icon: VectorAsset,
    progress: Float,
    onSelected: (Screen) -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable { onSelected(screen) }
            .padding(16.dp)
            .clip(MaterialTheme.shapes.small),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier.size(BottomBarHeight),
            gravity = ContentGravity.Center,
        ) {
            Icon(icon,
                tint = MaterialTheme.colors.onSurface.interpolateHue(MaterialTheme.colors.secondary,
                    progress))
        }

        Text(
            title,
            style = MaterialTheme.typography.caption,
            modifier = Modifier.drawOpacity(progress),
            color = MaterialTheme.colors.secondary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

private fun Color.interpolateHue(other: Color, progress: Float) = Color(
    alpha = alpha,
    red = interpolate(this.red, other.red, progress).coerceIn(0F, 1F),
    green = interpolate(this.green, other.green, progress).coerceIn(0F, 1F),
    blue = interpolate(this.blue, other.blue, progress).coerceIn(0F, 1F),
)

private fun interpolate(start: Float, end: Float, progress: Float) =
    start + ((end - start) * progress)
