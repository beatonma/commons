package org.beatonma.commons.app.timeline

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.beatonma.commons.app.ui.compose.components.Dot
import org.beatonma.commons.compose.ambient.animation
import org.beatonma.commons.compose.ambient.colors
import org.beatonma.commons.compose.ambient.shapes
import org.beatonma.commons.compose.ambient.typography
import org.beatonma.commons.compose.animation.AnimatedItemVisibility
import org.beatonma.commons.compose.modifiers.wrapContentWidth
import org.beatonma.commons.core.extensions.clipToLength
import org.beatonma.commons.core.extensions.fastForEachIndexed
import org.beatonma.commons.core.extensions.modGet
import org.beatonma.commons.data.core.interfaces.Named
import org.beatonma.commons.data.core.interfaces.Temporal
import org.beatonma.commons.data.resolution.description
import org.beatonma.commons.kotlin.extensions.roundDown
import org.beatonma.commons.kotlin.extensions.roundUp
import org.beatonma.commons.theme.compose.theme.graphPrimaryColors
import java.time.LocalDate
import java.time.Period

/**
 * Months added to beginning/end of timeline
 */
private const val MonthsPadding = 12 * 5 // 5 years
private const val LabelMaxChars = 100

@Composable
fun Timeline(
    data: List<Temporal>,
    modifier: Modifier = Modifier,
    groupModifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
) {
    val renderData = renderData(data)

    Layout(
        modifier = modifier
            .wrapContentWidth()
            .horizontalScroll(
                state = scrollState,
            )
            .clipToBounds(),
        content = { TimelineLayout(renderData, groupModifier) }
    ) { measurables, constraints ->
        val placeables = measurables.map { it.measure(constraints) }
        val xPositions = renderData.groups.map { it.startDp.roundToPx() }

        val width = placeables.zip(xPositions)
            .maxOf { (placeable, position) ->
                position + placeable.width
            }

        val height = placeables.sumBy(Placeable::height)

        layout(width, height) {
            var y = 0
            placeables.fastForEachIndexed { index, placeable ->
                placeable.placeRelative(xPositions[index], y)
                y += placeable.height
            }
        }
    }
}

@Composable
private fun TimelineLayout(renderData: RenderData, modifier: Modifier) {
    val colors = colors.graphPrimaryColors

    renderData.groups.fastForEachIndexed { index, group ->
        animation.AnimatedItemVisibility(position = index) { visibility ->
            Bar(group, visibility, colors.modGet(index), modifier)
        }
    }
}

@Composable
private fun Bar(group: Group, visibility: Float, color: Color, modifier: Modifier) {
    Column(
        modifier.padding(end = MonthsPadding.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        Layout(
            content = {
                for (item in group.items) {
                    Spacer(
                        Modifier
                            .width(item.widthDp)
                            .height(16.dp)
                            .clip(shapes.small)
                            .background(color)
                    )
                }
            },
            Modifier
                .clip(shapes.small)
                .wrapContentWidth(visibility)
                .background(LocalContentColor.current.copy(alpha = 0.15F)),
        ) { measurables, constraints ->
            val positions =
                group.items.map { it.startDp - group.startDp } // Normalise relative to start of group

            val placeables = measurables.map { it.measure(constraints) }

            val width = group.widthDp.roundToPx()
            val height = placeables.maxOf { it.height }

            layout(width, height) {
                placeables.fastForEachIndexed { index, placeable ->
                    placeable.placeRelative(positions[index].roundToPx(), 0)
                }
            }
        }

        Text(
            group.label,
            maxLines = 2,
            style = typography.caption,
        )
    }
}

@Composable
private fun Instant() {
    Dot(Color.Red)
}

@Composable
private fun TimelineBackground(decades: List<Decade>) {
    TODO()
}

@Composable
private fun DecadeMarker(decade: Decade, modifier: Modifier) {
    Column(
        modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("${decade.year}")

        Spacer(
            Modifier
                .background(LocalContentColor.current.copy(alpha = .3F))
                .width(2.dp)
                .fillMaxSize()
        )

        Text("${decade.year}")
    }
}

@Composable
private fun renderData(rawData: List<Temporal>): RenderData {
    require(rawData.isNotEmpty())

    val data = rawData.sortedBy { it.startOf() }

    val now: LocalDate = LocalDate.now()
    val start: LocalDate = data.first().startOf().minusMonths(MonthsPadding.toLong())

    val grouped = data.groupBy {
        when (it) {
            is Named -> it.description()
            else -> "noname"
        }
    }
    val groups = grouped
        .map { (label, items) ->
            Group(
                label.clipToLength(LabelMaxChars),
                items.map { item -> Item(item, start, now) }
            )
        }
        .sortedWith(
            compareBy(Group::startInEpoch)
                .thenByDescending(Group::durationMonths)
                .thenBy(Group::label)
        )

    return RenderData(groups)
}

@VisibleForTesting
internal interface TimelineData {
    val start: LocalDate
    val end: LocalDate

    val startInEpoch: Int
    val endInEpoch: Int

    val durationMonths: Int

    val startDp: Dp get() = startInEpoch.dp
    val endDp: Dp get() = endInEpoch.dp
    val widthDp: Dp get() = durationMonths.dp
}

@VisibleForTesting
internal class RenderData(val groups: List<Group>) : TimelineData {
    override val start: LocalDate
    override val end: LocalDate

    override val startInEpoch: Int
    override val endInEpoch: Int

    override val durationMonths: Int

    init {
        val first = groups.first()
        val last = groups.maxByOrNull { it.end }!!

        start = first.start
        end = last.end

        startInEpoch = first.startInEpoch
        endInEpoch = last.endInEpoch + MonthsPadding

        durationMonths = endInEpoch - startInEpoch
    }

    val decades = decadesBetween(start, end)
}

@VisibleForTesting
internal class Item(item: Temporal, epochStart: LocalDate, now: LocalDate) : TimelineData {
    override val start: LocalDate = item.startOf()
    override val end: LocalDate = item.endOf(now)

    override val startInEpoch: Int = Period.between(epochStart, start).toTotalMonths().toInt()
    override val endInEpoch: Int = Period.between(epochStart, end).toTotalMonths().toInt()

    override val durationMonths: Int = endInEpoch - startInEpoch
}

@VisibleForTesting
internal class Group(
    val label: String,
    val items: List<Item>
) : TimelineData {
    override val start: LocalDate
    override val end: LocalDate

    override val startInEpoch: Int
    override val endInEpoch: Int

    override val durationMonths: Int

    init {
        val first = items.minByOrNull { it.start }!!
        val last = items.maxByOrNull { it.end }!!

        start = first.start
        end = last.end

        startInEpoch = first.startInEpoch
        endInEpoch = last.endInEpoch

        durationMonths = endInEpoch - startInEpoch
    }
}

@VisibleForTesting
internal data class Decade(val year: Int, val inEpoch: Long)

/**
 * Return decades (years where it % 10 == 0) between start and end, for showing helper lines.
 */
@VisibleForTesting
internal fun decadesBetween(start: LocalDate, end: LocalDate): List<Decade> {
    val firstDecade = start.withYear(start.year.roundUp(10)).withDayOfYear(1)
    val lastDecade = end.withYear(end.year.roundDown(10)).withDayOfYear(1)

    val decadeYears = firstDecade.year..lastDecade.year step 10
    return decadeYears.map { year ->
        val date = LocalDate.of(year, 1, 1)
        Decade(year, Period.between(start, date).toTotalMonths())
    }
}
