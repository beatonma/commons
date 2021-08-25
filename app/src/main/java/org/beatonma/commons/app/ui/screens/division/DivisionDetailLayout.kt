package org.beatonma.commons.app.ui.screens.division

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.components.LoadingIcon
import org.beatonma.commons.app.ui.components.charts.ChartItem
import org.beatonma.commons.app.ui.components.charts.ChartKeyItem
import org.beatonma.commons.app.ui.components.charts.HorizontalStackedBarChart
import org.beatonma.commons.app.ui.screens.signin.UserAccountViewModel
import org.beatonma.commons.app.ui.screens.social.HeaderExpansion
import org.beatonma.commons.app.ui.screens.social.ProvideSocial
import org.beatonma.commons.app.ui.screens.social.SocialViewModel
import org.beatonma.commons.app.ui.screens.social.StickySocialScaffold
import org.beatonma.commons.app.ui.util.WithResultData
import org.beatonma.commons.compose.components.text.Quote
import org.beatonma.commons.compose.components.text.ResourceText
import org.beatonma.commons.compose.components.text.ScreenTitle
import org.beatonma.commons.compose.modifiers.wrapContentHeight
import org.beatonma.commons.compose.util.dot
import org.beatonma.commons.compose.util.mapUpdate
import org.beatonma.commons.compose.util.rememberBoolean
import org.beatonma.commons.compose.util.rememberListOf
import org.beatonma.commons.compose.util.rememberSetOf
import org.beatonma.commons.compose.util.rememberText
import org.beatonma.commons.compose.util.toggle
import org.beatonma.commons.core.House
import org.beatonma.commons.core.VoteType
import org.beatonma.commons.core.extensions.allEqualTo
import org.beatonma.commons.core.extensions.fastForEach
import org.beatonma.commons.core.extensions.fastForEachIndexed
import org.beatonma.commons.data.core.room.entities.division.Division
import org.beatonma.commons.data.core.room.entities.division.DivisionWithVotes
import org.beatonma.commons.data.core.room.entities.division.VoteWithParty
import org.beatonma.commons.data.resolution.uiDescription
import org.beatonma.commons.repo.result.IoLoading
import org.beatonma.commons.theme.compose.Size
import org.beatonma.commons.theme.compose.formatting.formatted
import org.beatonma.commons.theme.compose.padding.Padding
import org.beatonma.commons.theme.compose.padding.padding
import org.beatonma.commons.theme.compose.theme.politicalVotes
import org.beatonma.commons.theme.compose.theme.systemui.statusBarsPadding

internal val LocalDivisionActions: ProvidableCompositionLocal<DivisionActions> =
    compositionLocalOf { DivisionActions() }

@Composable
fun DivisionDetailLayout(
    viewmodel: DivisionDetailViewModel,
    socialViewModel: SocialViewModel,
    userAccountViewModel: UserAccountViewModel,
) {
    val resultState by viewmodel.livedata.observeAsState(IoLoading)

    WithResultData(resultState) { divisionData ->
        ProvideSocial(
            viewmodel,
            socialViewModel,
            userAccountViewModel
        ) {
            DivisionDetailLayout(divisionData)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DivisionDetailLayout(
    divisionWithVotes: DivisionWithVotes,
) {
    val voteTypeFilters = rememberSetOf<VoteType>()

    val coroutineScope = rememberCoroutineScope()
    val filteredVotes = rememberListOf<VoteWithParty>()

    val applyFilter: (String) -> Unit = { queryText: String ->
        coroutineScope.launch {
            val filtered = applyFilter(queryText, voteTypeFilters.value, divisionWithVotes)

            withContext(Dispatchers.Main) {
                filteredVotes.value = filtered
            }
        }
    }

    val toggleVoteTypeFilter: (VoteType) -> Unit = { voteType: VoteType ->
        voteTypeFilters.mapUpdate { filters ->
            val exists = filters.contains(voteType)
            if (exists) {
                filters.filterNot { it == voteType }.toSet()
            } else {
                filters + setOf(voteType)
            }
        }
        applyFilter("")
    }
    applyFilter("")

    StickySocialScaffold(
        aboveSocial = { headerExpansion: HeaderExpansion, headerModifier: Modifier ->
            HeaderAboveSocial(
                divisionWithVotes.division,
                expandProgress = headerExpansion,
                modifier = headerModifier,
            )
        },
        belowSocial = { headerExpansion: HeaderExpansion, headerModifier: Modifier ->
            HeaderBelowSocial(
                division = divisionWithVotes.division,
                expandProgress = headerExpansion,
                onVoteTypeClick = toggleVoteTypeFilter,
                applyFilter = applyFilter,
                modifier = headerModifier,
            )
        },
        lazyListContent = {
            voteList(
                filteredVotes = filteredVotes.value,
                isLoading = divisionWithVotes.votes.isEmpty()
            )
        },
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun HeaderAboveSocial(
    division: Division,
    expandProgress: HeaderExpansion,
    modifier: Modifier = Modifier,
) {
    val (title, description) = when (division.house) {
        House.commons -> Pair(stringResource(R.string.division_title), division.title)
        House.lords -> Pair(
            division.title,
            division.description ?: stringResource(R.string.division_no_description)
        )
    }

    Column(
        modifier
            .padding(Padding.Screen)
            .statusBarsPadding()
            .wrapContentHeight(expandProgress.value)
            .alpha(expandProgress.value)
    ) {
        ScreenTitle(title)

        Text(division.house.uiDescription() dot division.date.formatted())

        Quote(description)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun HeaderBelowSocial(
    division: Division,
    expandProgress: HeaderExpansion,
    onVoteTypeClick: (VoteType) -> Unit,
    applyFilter: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        Chart(division, Modifier.fillMaxWidth())

        GraphKey(division, onVoteTypeClick)

        SearchField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Padding.ScreenHorizontal),
            onQueryChange = applyFilter
        )
    }
}

private fun LazyListScope.voteList(
    filteredVotes: List<VoteWithParty>,
    isLoading: Boolean,
) {
    if (isLoading) {
        item {
            LoadingIcon(Modifier.fillMaxWidth())
        }
        return
    }

    items(
        items = filteredVotes,
        itemContent = { vote ->
            Vote(vote)
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun Vote(
    voteWithParty: VoteWithParty,
    modifier: Modifier = Modifier,
    onMemberClick: MemberVoteAction = LocalDivisionActions.current.onMemberClick,
) {
    ListItem(
        modifier = modifier.clickable(onClick = { onMemberClick(voteWithParty.vote) }),
        icon = { VoteIcon(voteWithParty.vote.voteType) },
        text = { Text(voteWithParty.vote.memberName, maxLines = 2) },
        trailing = { Text(voteWithParty.party?.name ?: "") }
    )
}

@Composable
private fun SearchField(
    modifier: Modifier = Modifier,
    query: MutableState<String> = rememberText(),
    onQueryChange: (String) -> Unit,
) =
    org.beatonma.commons.compose.components.text.SearchField(
        modifier = modifier,
        query = query,
        onQueryChange = onQueryChange,
        hint = R.string.division_search_member_hint,
    )

@Composable
private fun VoteIcon(voteType: VoteType) = VoteIcon(
    voteType.icon,
    voteType.uiDescription(),
    voteType.color
)

@Composable
private fun VoteIcon(
    icon: ImageVector,
    contentDescription: String?,
    tint: Color,
) {
    Icon(
        icon,
        contentDescription = contentDescription,
        tint = tint,
        modifier = Modifier
            .padding(Padding.IconSmall)
            .size(Size.IconSmall)
    )
}

@Composable
private fun GraphKey(
    division: Division,
    onVoteTypeClick: (VoteType) -> Unit,
) {
    if (division.isEmpty()) return

    val keyModifier = Modifier.padding(Padding.HorizontalListItem)

    // Simple FlowRow
    Layout(content = {
        GraphKeyItems(division, onVoteTypeClick, keyModifier)
    }) { measurables, constraints ->
        val maxWidth = constraints.maxWidth

        val rows = mutableListOf<List<Placeable>>()
        val currentRow = mutableListOf<Placeable>()
        val rowSizes = mutableListOf<IntSize>()
        val rowStartY = mutableListOf(0)

        val placeables = measurables.map { it.measure(constraints) }

        var rowHeight = 0
        var rowWidth = 0

        fun storeRowAndReset() {
            // Remember this row and start a new row
            rows += currentRow.toList()
            rowSizes += IntSize(rowWidth, rowHeight)
            rowStartY += rowHeight
            rowHeight = 0
            rowWidth = 0
            currentRow.clear()
        }

        placeables.fastForEach {
            if (rowWidth + it.width > maxWidth) {
                storeRowAndReset()
            }

            currentRow += it
            rowHeight = maxOf(rowHeight, it.height)
            rowWidth += it.width
        }

        if (currentRow.isNotEmpty()) {
            storeRowAndReset()
        }

        val totalWidth = rowSizes.maxOf(IntSize::width)
        val totalHeight = rowSizes.sumOf(IntSize::height)

        check(rows.size < rowStartY.size)
        check(currentRow.isEmpty())

        layout(totalWidth, totalHeight) {
            rows.fastForEachIndexed { rowIndex, rowItems ->
                var left = 0
                val top = rowStartY[rowIndex]

                rowItems.fastForEach { item ->
                    item.placeRelative(left, top)
                    left += item.width
                }
            }
        }
    }
}

@Composable
private fun GraphKeyItems(
    division: Division,
    onVoteTypeClick: (VoteType) -> Unit,
    modifier: Modifier,
) {
    GraphKeyItem(
        VoteType.AyeVote,
        division.ayes,
        onClick = onVoteTypeClick,
        modifier = modifier
    )
    GraphKeyItem(
        VoteType.NoVote,
        division.noes,
        onClick = onVoteTypeClick,
        modifier = modifier
    )
    GraphKeyItem(
        VoteType.Abstains,
        division.abstentions ?: 0,
        onClick = onVoteTypeClick,
        modifier = modifier
    )
    GraphKeyItem(
        VoteType.DidNotVote,
        division.didNotVote ?: 0,
        onClick = onVoteTypeClick,
        modifier = modifier
    )
    GraphKeyItem(
        VoteType.SuspendedOrExpelledVote,
        division.suspendedOrExpelled ?: 0,
        onClick = onVoteTypeClick,
        modifier = modifier
    )
}

@Composable
private fun GraphKeyItem(
    voteType: VoteType,
    voteCount: Int,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource = remember(::MutableInteractionSource),
    onClick: (VoteType) -> Unit,
) {
    val selected = rememberBoolean(false)
    if (voteCount == 0) return

    ChartKeyItem(
        { VoteIcon(voteType.icon, null, voteType.color) },
        { ResourceText(voteType.descriptionRes, voteCount, withAnnotatedStyle = true) },
        modifier = modifier.selectable(
            selected = selected.value,
            onClick = {
                selected.toggle()
                onClick(voteType)
            },
            interactionSource = interactionSource,
            indication = rememberRipple(),
        ),
    )
}

@Composable
private fun Chart(
    division: Division,
    modifier: Modifier = Modifier,
    height: Dp = 16.dp,
) {
    HorizontalStackedBarChart(
        ChartItem(division.ayes, colors.politicalVotes.Aye, ""),
        ChartItem(division.didNotVote ?: 0, colors.politicalVotes.DidNotVote, ""),
        ChartItem(division.abstentions ?: 0, colors.politicalVotes.Abstain, ""),
        ChartItem(division.suspendedOrExpelled ?: 0, colors.politicalVotes.SuspendedOrExpelled, ""),
        ChartItem(division.noes, colors.politicalVotes.No, ""),
        modifier = modifier,
        height = height,
    )
}

private val VoteType.color
    @Composable get() = when (this) {
        VoteType.AyeVote -> colors.politicalVotes.Aye
        VoteType.NoVote -> colors.politicalVotes.No
        VoteType.DidNotVote -> colors.politicalVotes.DidNotVote
        VoteType.Abstains -> colors.politicalVotes.Abstain
        VoteType.SuspendedOrExpelledVote -> colors.politicalVotes.SuspendedOrExpelled
    }

private val VoteType.icon
    @Composable get() = when (this) {
        VoteType.AyeVote -> Icons.Default.AddCircle
        VoteType.NoVote -> Icons.Default.RemoveCircle
        else -> Icons.Default.Cancel
    }

private val VoteType.descriptionRes
    @Composable get() = when (this) {
        VoteType.AyeVote -> R.string.division_ayes
        VoteType.NoVote -> R.string.division_noes
        VoteType.DidNotVote -> R.string.division_abstained
        VoteType.Abstains -> R.string.division_did_not_vote
        VoteType.SuspendedOrExpelledVote -> R.string.division_suspended_or_expelled
    }

private fun applyFilter(
    queryText: String,
    voteTypeFilters: Set<VoteType>,
    division: DivisionWithVotes,
) =
    when {
        voteTypeFilters.isEmpty() -> division.votes
        else -> division.votes.filter { it.vote.voteType in voteTypeFilters }
    }
        .filter { it.vote.memberName.contains(queryText, ignoreCase = true) }

private fun Division.isEmpty() =
    listOf(ayes, noes, didNotVote, abstentions, suspendedOrExpelled).allEqualTo(
        expected = 0,
        orNull = true
    )
