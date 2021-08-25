package org.beatonma.commons.app.ui.components

import androidx.annotation.VisibleForTesting
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.text
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.beatonma.commons.compose.animation.AnimatedItemVisibility
import org.beatonma.commons.compose.animation.AnimatedVisibility
import org.beatonma.commons.compose.animation.ExpandCollapseState
import org.beatonma.commons.compose.animation.animateExpansionAsState
import org.beatonma.commons.compose.animation.rememberExpandCollapseState
import org.beatonma.commons.compose.animation.toggle
import org.beatonma.commons.compose.components.Dot
import org.beatonma.commons.compose.modifiers.wrapContentWidth
import org.beatonma.commons.core.extensions.clipToLength
import org.beatonma.commons.core.extensions.fastForEach
import org.beatonma.commons.core.extensions.fastForEachIndexed
import org.beatonma.commons.core.extensions.modGet
import org.beatonma.commons.core.extensions.pairs
import org.beatonma.commons.core.extensions.roundDown
import org.beatonma.commons.core.extensions.roundUp
import org.beatonma.commons.data.core.interfaces.Named
import org.beatonma.commons.data.core.interfaces.Temporal
import org.beatonma.commons.data.resolution.uiDescription
import org.beatonma.commons.theme.compose.formatting.dateRange
import org.beatonma.commons.theme.compose.formatting.formatted
import org.beatonma.commons.theme.compose.formatting.formattedPeriod
import org.beatonma.commons.theme.compose.theme.animation
import org.beatonma.commons.theme.compose.theme.graphPrimaryColors
import org.beatonma.commons.theme.compose.theme.graphSecondaryColors
import java.time.LocalDate
import java.time.Period

/**
 * Months added to beginning/end of timeline
 */
private const val MonthsPadding = 12 * 5 // 5 years
private const val LabelMaxChars = 100
private val BarHeight = 16.dp

private data class TimelineColors(
    val surface: Color,
    val content: Color,

    /**
     * Color of decade markers.
     */
    val lines: Color = content.copy(alpha = .1F),

    /**
     * Background color for foreground to ensure readability over background content.
     */
    val overlay: Color = surface.copy(alpha = .8F),

    /**
     * Background color of bars in the graph - only visible when the bar is discontinuous.
     */
    val barBackground: Color = content.copy(alpha = .15F),
    val bars: List<Color>
)

@Composable
fun Timeline(
    data: List<Temporal>,
    modifier: Modifier = Modifier,
    barModifier: Modifier = Modifier,
    surfaceColor: Color = colors.background,
    contentColor: Color = LocalContentColor.current,
    barColors: List<Color> = colors.graphPrimaryColors + colors.graphSecondaryColors,
    scrollState: ScrollState = rememberScrollState(),
) {
    val renderData = renderData(data)
    val colors = remember(surfaceColor, contentColor, barColors) {
        TimelineColors(surfaceColor, contentColor, bars = barColors)
    }

    TimelineBackground(
        renderData.decades,
        modifier
            .wrapContentWidth()
            .horizontalScroll(state = scrollState)
            .clipToBounds(),
        colors,
    ) {
        TimelineLayout(renderData, barModifier, colors)
    }
}

/**
 * Render the timeline data.
 */
@Composable
private fun TimelineLayout(
    renderData: RenderData,
    modifier: Modifier,
    colors: TimelineColors,
) {
    Layout(
        content = {
            renderData.groups.fastForEachIndexed { index, group ->
                animation.AnimatedItemVisibility(position = index) { visibility ->
                    val color = colors.bars.modGet(index)
                    GroupLayout(group, visibility, colors, color, modifier)
                }
            }
        }
    ) { measurables, constraints ->
        val placeables = measurables.map { it.measure(constraints) }
        val xPositions = renderData.groups.map { it.startDp.roundToPx() }

        val width = placeables.zip(xPositions)
            .maxOf { (placeable, position) ->
                position + placeable.width
            }

        val height = placeables.sumOf(Placeable::height)

        layout(width, height) {
            var y = 0
            placeables.fastForEachIndexed { index, placeable ->
                placeable.placeRelative(xPositions[index], y)
                y += placeable.height
            }
        }
    }
}

/**
 * Chrome - draw labelled reference lines to mark decades behind the [foreground].
 */
@Composable
private fun TimelineBackground(
    decades: List<Decade>,
    modifier: Modifier,
    colors: TimelineColors,
    labelStyle: TextStyle = typography.caption,
    foreground: @Composable () -> Unit
) {
    val clearSemanticsModifier = Modifier.clearAndSetSemantics {
        /* Screen readers should ignore background markers */
    }

    Layout(
        content = {
            decades.fastForEach { decade ->
                // Year markers along top and bottom, connected with vertical line.
                Text("${decade.year}", clearSemanticsModifier, style = labelStyle)
                Spacer(
                    Modifier
                        .fillMaxHeight()
                        .background(colors.lines)
                        .width(2.dp)
                )
                Text("${decade.year}", clearSemanticsModifier, style = labelStyle)
            }

            foreground()
        },
        modifier = modifier,
    ) { measurables, constraints ->
        val decadeMeasurables = measurables.take(measurables.size - 1)
        val textMeasurables = decadeMeasurables.filterIndexed { i, _ -> i % 3 != 1 }

        val textPlaceables = textMeasurables.map { it.measure(constraints) }

        val foregroundPlaceable = measurables.last().measure(constraints)

        val textHeight = textPlaceables[0].height
        val foregroundHeight = foregroundPlaceable.height

        val width = foregroundPlaceable.width
        val height = foregroundHeight + (textHeight * 2)

        val lineMeasurables = decadeMeasurables.filterIndexed { i, _ -> i % 3 == 1 }
        val linePlaceables = lineMeasurables.map {
            it.measure(
                constraints.copy(
                    minHeight = foregroundHeight,
                    maxHeight = foregroundHeight
                )
            )
        }

        check(textMeasurables.size + lineMeasurables.size == decadeMeasurables.size)
        check(measurables.size == decadeMeasurables.size + 1)

        layout(width, height) {
            val labels = textPlaceables.pairs()

            decades.forEachIndexed { index, decade ->
                val center = decade.startDp.roundToPx()
                fun centered(placeable: Placeable) =
                    (center - (placeable.width / 2)).coerceAtLeast(0)

                if (center > 0) {
                    val (top, bottom) = labels[index]
                    top.placeRelative(centered(top), 0)
                    bottom.placeRelative(centered(bottom), height - bottom.height)

                    val line = linePlaceables[index]
                    line.placeRelative(centered(line), textHeight)
                }
            }

            foregroundPlaceable.placeRelative(0, textHeight)
        }
    }
}

@Composable
private fun GroupLayout(
    group: TimelineGroup,
    visibility: Float,
    colors: TimelineColors,
    color: Color,
    modifier: Modifier
) {
    val state = rememberExpandCollapseState()
    val expansion by state.value.animateExpansionAsState()

    val dates = group.items.mapNotNull {
        if (it.durationMonths == 0) {
            it.start.formatted()
        } else {
            dateRange(it.start, it.end)
        }
    }
    val durations = group.items.mapNotNull {
        if (it.durationMonths > 0) {
            formattedPeriod(it.start, it.end)
        } else {
            ""
        }
    }

    val allDatesText = dates
        .zip(durations)
        .joinToString("\n") { (date, duration) ->
            if (duration.isEmpty()) date
            else "$date ($duration)"
        }

    Column(
        Modifier
            .padding(end = MonthsPadding.dp)
            .padding(vertical = 12.dp * expansion)
            .clearAndSetSemantics {
                text = AnnotatedString("${group.label}: $allDatesText")
            }
            .clickable { state.toggle() }
            .animateContentSize(animation.spec())
            .then(modifier),
        horizontalAlignment = Alignment.Start,
    ) {
        if (group.durationMonths < 12) {
            Instant(visibility, color)
        } else {
            Bar(group, visibility, colors, color)
        }

        GroupLabel(group.label, allDatesText, state.value, colors)
    }
}

@Composable
private fun Bar(
    group: TimelineGroup,
    visibility: Float,
    colors: TimelineColors,
    color: Color,
    shape: Shape = CircleShape
) {
    Layout(
        content = {
            for (item in group.items) {
                Spacer(
                    Modifier
                        .width(item.widthDp)
                        .height(BarHeight)
                        .clip(shape)
                        .background(color)
                )
            }
        },
        Modifier
            .clip(shape)
            .background(colors.overlay)
            .wrapContentWidth(visibility)
            .background(colors.barBackground),
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
}

@Composable
private fun Instant(visibility: Float, color: Color) {
    Dot(color, size = BarHeight * visibility)
}

@Composable
private fun GroupLabel(
    label: String,
    expandedText: String,
    state: ExpandCollapseState,
    colors: TimelineColors,
) {
    Column(Modifier.padding(top = 2.dp)) {
        Text(
            label,
            Modifier
                .background(colors.overlay)
                .requiredWidthIn(0.dp, 256.dp),
            maxLines = if (state.isExpanded) 10 else 1,
            style = typography.caption,
            overflow = TextOverflow.Ellipsis,
        )

        animation.AnimatedVisibility(visible = state.isExpanded, horizontal = false) {
            Text(
                expandedText,
                Modifier.background(colors.overlay),
                style = typography.caption,
            )
        }
    }
}

@Composable
private fun renderData(rawData: List<Temporal>): RenderData {
    require(rawData.isNotEmpty())

    val data = rawData.sortedBy { it.startOf() }

    val now: LocalDate = LocalDate.now()
    val start: LocalDate = data.first().startOf()

    val grouped = data.groupBy {
        when (it) {
            is Named -> it.uiDescription()
            else -> "NO DESCRIPTION"
        }
    }
    val groups = grouped
        .map { (label, items) ->
            TimelineGroup(
                label.clipToLength(LabelMaxChars),
                items.map { item -> TimelineItem(item, epochStart = start, now = now) }
            )
        }
        .sortedWith(
            compareBy(TimelineGroup::startInEpoch)
                .thenByDescending(TimelineGroup::durationMonths)
                .thenBy(TimelineGroup::label)
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

    val startDp: Dp get() = startInEpoch.dp + MonthsPadding.dp
    val endDp: Dp get() = endInEpoch.dp + MonthsPadding.dp
    val widthDp: Dp get() = durationMonths.dp
}

@VisibleForTesting
internal class RenderData(val groups: List<TimelineGroup>) : TimelineData {
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
        endInEpoch = last.endInEpoch

        durationMonths = endInEpoch - startInEpoch
    }

    val decades = decadesBetween(
        start.minusMonths(MonthsPadding.toLong()), end.plusMonths(
            MonthsPadding.toLong()
        )
    )
}

@VisibleForTesting
internal class TimelineItem(item: Temporal, epochStart: LocalDate, now: LocalDate) : TimelineData {
    override val start: LocalDate = item.startOf()
    override val end: LocalDate = item.endOf(now)

    override val startInEpoch: Int = Period.between(epochStart, start).toTotalMonths().toInt()
    override val endInEpoch: Int = Period.between(epochStart, end).toTotalMonths().toInt()

    override val durationMonths: Int = endInEpoch - startInEpoch
}

@VisibleForTesting
internal class TimelineGroup(
    val label: String,
    val items: List<TimelineItem>
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
internal data class Decade(val year: Int, val inEpoch: Int) {
    val startDp: Dp get() = inEpoch.dp// + MonthsPadding.dp
}

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
        Decade(year, Period.between(start, date).toTotalMonths().toInt())
    }
}
