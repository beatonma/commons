package org.beatonma.commons.theme

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
import androidx.compose.ui.draw.drawShadow
import androidx.compose.ui.platform.AnimationClockAmbient
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp

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
            .preferredHeight(BottomBarHeight)
            .drawShadow(elevation = 8.dp),
        cutoutShape = MaterialTheme.shapes.small,
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
    Row(
        Modifier
            .fillMaxWidth()
            .clickable { onSelected(screen) }
            .padding(16.dp)
            .clip(MaterialTheme.shapes.small),
        verticalGravity = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier.size(BottomBarHeight),
            gravity = ContentGravity.Center,
            children = { Icon(screen.icon) }
        )

        Text(screen.name,
            style = MaterialTheme.typography.caption,
            modifier = Modifier.fillMaxWidth(progress)
                .drawOpacity(progress)
        )
    }
}
