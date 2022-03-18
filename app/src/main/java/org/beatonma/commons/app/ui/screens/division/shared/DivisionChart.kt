package org.beatonma.commons.app.ui.screens.division.shared

import androidx.annotation.StringRes
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.components.charts.ChartItem
import org.beatonma.commons.app.ui.components.charts.ChartKeyItem
import org.beatonma.commons.app.ui.components.charts.HorizontalStackedBarChart
import org.beatonma.commons.app.ui.components.charts.selectionDecoration
import org.beatonma.commons.compose.components.FlowRow
import org.beatonma.commons.compose.components.text.ResourceText
import org.beatonma.commons.compose.padding.padding
import org.beatonma.commons.core.DivisionVoteType
import org.beatonma.commons.core.LordsVoteType
import org.beatonma.commons.core.VoteType
import org.beatonma.commons.data.core.room.entities.division.CommonsDivision
import org.beatonma.commons.data.core.room.entities.division.HouseDivision
import org.beatonma.commons.data.core.room.entities.division.LordsDivision
import org.beatonma.commons.theme.color.color
import org.beatonma.commons.themed.themedPadding

private class VoteChartItem<T : DivisionVoteType>(
    val voteType: T,
    val count: Int,
) {
    @Composable
    fun toChartItem() =
        ChartItem(
            count,
            voteType.color,
            stringResource(voteType.descriptionRes, count)
        )
}

@Composable
internal fun <T : HouseDivision<V>, V : DivisionVoteType> DivisionVoteChart(
    division: T,
    voteTypeFilter: Set<V>,
    onVoteTypeClick: (V, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    @Suppress("UNCHECKED_CAST")
    when (division) {
        is CommonsDivision -> DivisionVoteChart(
            division,
            voteTypeFilter as Set<VoteType>,
            onVoteTypeClick as (VoteType, Boolean) -> Unit,
            modifier
        )
        is LordsDivision -> DivisionVoteChart(
            division,
            voteTypeFilter as Set<LordsVoteType>,
            onVoteTypeClick as (LordsVoteType, Boolean) -> Unit,
            modifier
        )
    }
}

@Composable
private fun DivisionVoteChart(
    division: CommonsDivision,
    voteTypeFilter: Set<VoteType>,
    onVoteTypeClick: (VoteType, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val voteTypes by remember {
        mutableStateOf(
            setOf(
                VoteChartItem(VoteType.AyeVote, division.data.ayes),
                VoteChartItem(VoteType.NoVote, division.data.noes),
                VoteChartItem(VoteType.DidNotVote, division.data.didNotVote),
                VoteChartItem(VoteType.Abstains, division.data.abstentions),
                VoteChartItem(VoteType.SuspendedOrExpelledVote, division.data.suspendedOrExpelled),
            )
        )
    }

    DivisionVoteChart(
        voteTypes,
        voteTypeFilter,
        onVoteTypeClick,
        modifier,
    )
}

@Composable
private fun DivisionVoteChart(
    division: LordsDivision,
    voteTypeFilter: Set<LordsVoteType>,
    onVoteTypeClick: (LordsVoteType, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val voteTypes by remember {
        mutableStateOf(
            setOf(
                VoteChartItem(LordsVoteType.content, division.data.ayes),
                VoteChartItem(LordsVoteType.not_content, division.data.noes),
            )
        )
    }

    DivisionVoteChart(
        voteTypes,
        voteTypeFilter,
        onVoteTypeClick,
        modifier,
    )
}

@Composable
private fun <T : DivisionVoteType> DivisionVoteChart(
    voteTypes: Set<VoteChartItem<T>>,
    voteTypeFilter: Set<T>,
    onSelectChanged: (T, Boolean) -> Unit,
    modifier: Modifier,
) {
    Column(modifier) {
        Chart(
            voteTypes,
            Modifier.padding(themedPadding.VerticalListItem)
        )

        ChartKey(
            voteTypes,
            voteTypeFilter,
            onSelectChanged,
            Modifier.padding(themedPadding.HorizontalListItem),
        )
    }
}

@Composable
private fun <T : DivisionVoteType> Chart(
    voteTypes: Set<VoteChartItem<T>>,
    modifier: Modifier,
) {
    val items = voteTypes.map { it.toChartItem() }.toTypedArray()
    HorizontalStackedBarChart(
        *items,
        modifier = modifier
    )
}


@Composable
private fun <T : DivisionVoteType> ChartKey(
    voteTypes: Set<VoteChartItem<T>>,
    filter: Set<T>,
    onSelectChanged: (T, Boolean) -> Unit,
    modifier: Modifier,
) {
    FlowRow(modifier) {
        voteTypes.forEach { type ->
            ChartKeyItem(
                type.voteType,
                type.count,
                modifier = Modifier
                    .padding(themedPadding.HorizontalListItem)
                    .padding(themedPadding.VerticalListItem),
                selected = filter.contains(type.voteType),
                onSelectChanged = { selected -> onSelectChanged(type.voteType, selected) },
            )
        }
    }
}

@Composable
private fun <T : DivisionVoteType> ChartKeyItem(
    voteType: T,
    voteCount: Int,
    modifier: Modifier = Modifier,
    selected: Boolean,
    onSelectChanged: (Boolean) -> Unit,
) {
    if (voteCount == 0) return

    ChartKeyItem(
        icon = { DivisionVoteIcon(voteType, Modifier.padding(themedPadding.IconSmall)) },
        description = {
            ResourceText(
                voteType.descriptionRes,
                voteCount,
                withAnnotatedStyle = true
            )
        },
        modifier = modifier
            .clip(shapes.small)
            .selectable(
                selected = selected,
                interactionSource = remember(::MutableInteractionSource),
                indication = rememberRipple(color = colors.primary),
                role = Role.Checkbox,
                onClick = { onSelectChanged(!selected) },
            )
            .selectionDecoration(selected, color = colors.primary)
            .padding(4.dp)
            .padding(end = 8.dp),
    )
}

private val DivisionVoteType.descriptionRes: Int
    @StringRes get() = when (this) {
        is LordsVoteType -> descriptionRes
        is VoteType -> descriptionRes
    }

private val LordsVoteType.descriptionRes: Int
    @StringRes get() = when (this) {
        LordsVoteType.content -> R.string.division_ayes
        LordsVoteType.not_content -> R.string.division_noes
    }

private val VoteType.descriptionRes: Int
    @StringRes get() = when (this) {
        VoteType.AyeVote -> R.string.division_ayes
        VoteType.NoVote -> R.string.division_noes
        VoteType.DidNotVote -> R.string.division_abstained
        VoteType.Abstains -> R.string.division_did_not_vote
        VoteType.SuspendedOrExpelledVote -> R.string.division_suspended_or_expelled
    }
