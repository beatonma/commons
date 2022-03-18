package org.beatonma.commons.app.ui.components.charts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.beatonma.commons.core.extensions.fastForEach

data class ChartItem<T : Number>(val value: T, val color: Color, val description: String = "")

@Composable
fun <T : Number> HorizontalStackedBarChart(
    vararg items: ChartItem<T>,
    modifier: Modifier = Modifier,
    height: Dp = 16.dp,
) {
    val totalValue: Float by remember {
        mutableStateOf(
            items.map { it.value.toFloat() }
                .reduce { acc, t -> acc + t }
        )
    }
    if (totalValue == 0F) return

    val itemModifier = Modifier.height(height - 4.dp)

    Box(modifier, contentAlignment = Alignment.Center) {
        Layout(
            content = {
                StackedBarChartScope.apply {
                    items.fastForEach {
                        HorizontalStackedBar(it, totalValue, itemModifier)
                    }
                }
            }
        ) { measurables, constraints ->
            val placeables = measurables.map { it.measure(constraints) }

            val w = placeables.sumOf(Placeable::width)
            val h = placeables.maxOf(Placeable::height)

            layout(w, h) {
                var consumedWidth = 0

                placeables.fastForEach {
                    it.placeRelative(consumedWidth, 0)
                    consumedWidth += it.width
                }
            }
        }

        CenterMarker(height = height)
    }
}

interface StackedBarChartScope {
    companion object : StackedBarChartScope
}

@Composable
private fun <T : Number> StackedBarChartScope.HorizontalStackedBar(
    chartItem: ChartItem<T>,
    total: Float,
    modifier: Modifier = Modifier,
) {
    if (chartItem.value == 0) return
    val barWidthPercent = chartItem.value.toFloat() / total

    Spacer(
        modifier
            .fillMaxWidth(barWidthPercent)
            .background(chartItem.color)
    )
}

@Composable
private fun CenterMarker(height: Dp) {
    Spacer(
        Modifier
            .width(2.dp)
            .height(height)
            .background(LocalContentColor.current)
            .alpha(ContentAlpha.medium)
    )
}
