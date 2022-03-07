package org.beatonma.commons.app.ui.screens.division

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.components.charts.ChartItem
import org.beatonma.commons.app.ui.components.charts.ChartKeyItem
import org.beatonma.commons.app.ui.components.charts.HorizontalStackedBarChart
import org.beatonma.commons.app.ui.components.charts.selectionDecoration
import org.beatonma.commons.app.ui.components.image.AppIcon
import org.beatonma.commons.app.ui.components.loadingIcon
import org.beatonma.commons.app.ui.screens.signin.UserAccountViewModel
import org.beatonma.commons.app.ui.screens.social.ProvideSocial
import org.beatonma.commons.app.ui.screens.social.SocialScaffold
import org.beatonma.commons.app.ui.screens.social.SocialViewModel
import org.beatonma.commons.app.ui.uiDescription
import org.beatonma.commons.app.ui.util.WithResultData
import org.beatonma.commons.compose.components.FlowRow
import org.beatonma.commons.compose.components.text.Quote
import org.beatonma.commons.compose.components.text.ResourceText
import org.beatonma.commons.compose.layout.stickyHeaderWithInsets
import org.beatonma.commons.compose.padding.padding
import org.beatonma.commons.compose.systemui.statusBarsPadding
import org.beatonma.commons.compose.util.rememberBoolean
import org.beatonma.commons.compose.util.rememberListOf
import org.beatonma.commons.compose.util.rememberSetOf
import org.beatonma.commons.compose.util.rememberText
import org.beatonma.commons.core.House
import org.beatonma.commons.core.VoteType
import org.beatonma.commons.core.extensions.allEqualTo
import org.beatonma.commons.data.core.room.entities.division.Division
import org.beatonma.commons.data.core.room.entities.division.DivisionWithVotes
import org.beatonma.commons.data.core.room.entities.division.VoteWithParty
import org.beatonma.commons.repo.result.IoLoading
import org.beatonma.commons.theme.CommonsPadding
import org.beatonma.commons.theme.CommonsSize
import org.beatonma.commons.theme.formatting.formatted
import org.beatonma.commons.theme.politicalVotes
import org.beatonma.commons.themed.themedPadding

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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DivisionDetailLayout(
    divisionWithVotes: DivisionWithVotes,
) {
    var voteTypeFilters by rememberSetOf<VoteType>()
    var filteredVotes by rememberListOf<VoteWithParty>()
    var filterQuery by rememberText()

    LaunchedEffect(divisionWithVotes.votes, filterQuery, voteTypeFilters) {
        val filtered = applyFilter(filterQuery, voteTypeFilters, divisionWithVotes)
        withContext(Dispatchers.Main) {
            filteredVotes = filtered
        }
    }

    val toggleVoteTypeFilter: (VoteType) -> Unit = { voteType: VoteType ->
        voteTypeFilters = if (voteTypeFilters.contains(voteType)) {
            voteTypeFilters.filterNot { it == voteType }.toSet()
        } else {
            voteTypeFilters + voteType
        }
    }

    val (division, votes) = divisionWithVotes

    val (title, description) = when (division.house) {
        House.commons -> Pair(stringResource(R.string.division_title), division.title)
        House.lords -> Pair(
            division.title,
            division.description ?: stringResource(R.string.division_no_description)
        )
        House.unassigned -> throw Exception("Unhandled house ${division.house}")
    }

    val scrollState = rememberLazyListState()

    SocialScaffold(
        title = title,
        aboveSocial = {},
        scrollState = scrollState,
        content = { modifier ->
            item {
                HeaderAboveSocial(division, description, modifier)
            }

            stickyHeaderWithInsets(
                state = scrollState,
                key = "search_filters"
            ) { _, headerModifier ->
                HeaderBelowSocial(
                    division = division,
                    onVoteTypeClick = toggleVoteTypeFilter,
                    applyFilter = { filter -> filterQuery = filter },
                    modifier = modifier.then(headerModifier)
                )
            }

            voteList(
                filteredVotes,
                isLoading = votes.isEmpty(),
                modifier = modifier,
            )
        }
    )
}

@Composable
private fun HeaderAboveSocial(
    division: Division,
    description: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier.statusBarsPadding(),
    ) {
        Row(verticalAlignment = Alignment.Bottom) {
            Text(division.house.uiDescription(), style = typography.h5)
            Text(division.date.formatted())
        }

        Quote(description)
    }
}

@Composable
private fun HeaderBelowSocial(
    division: Division,
    onVoteTypeClick: (VoteType) -> Unit,
    applyFilter: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        Chart(division, Modifier.fillMaxWidth())

        GraphKey(division, onVoteTypeClick, Modifier.padding(themedPadding.VerticalListItem))

        SearchField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(CommonsPadding.ScreenHorizontal),
            onQueryChange = applyFilter
        )
    }
}

private fun LazyListScope.voteList(
    filteredVotes: List<VoteWithParty>,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    loadingIcon(isLoading)

    items(filteredVotes, key = { it.vote.memberId }) { vote ->
        Vote(vote, modifier)
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun Vote(
    voteWithParty: VoteWithParty,
    modifier: Modifier = Modifier,
    onMemberClick: MemberVoteAction = LocalDivisionActions.current.onMemberClick,
) {
    ListItem(
        icon = { VoteIcon(voteWithParty.vote.voteType) },
        text = { Text(voteWithParty.vote.memberName, maxLines = 2) },
        trailing = { Text(voteWithParty.party?.name ?: "") },
        modifier = modifier.clickable(onClick = { onMemberClick(voteWithParty.vote) }),
    )
}

@Composable
private fun SearchField(
    modifier: Modifier = Modifier,
    query: MutableState<String> = rememberText(),
    onQueryChange: (String) -> Unit,
) {
    org.beatonma.commons.compose.components.text.SearchField(
        modifier = modifier,
        query = query,
        onQueryChange = onQueryChange,
        hint = R.string.division_search_member_hint,
    )
}

@Composable
private fun VoteIcon(voteType: VoteType) {
    VoteIcon(
        voteType.icon,
        voteType.uiDescription(),
        voteType.color
    )
}

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
            .padding(CommonsPadding.IconSmall)
            .size(CommonsSize.IconSmall)
    )
}

@Composable
private fun GraphKey(
    division: Division,
    onVoteTypeClick: (VoteType) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (division.isEmpty()) return

    val keyModifier = Modifier.padding(CommonsPadding.HorizontalListItem)

    FlowRow(modifier) {
        GraphKeyItem(
            VoteType.AyeVote,
            division.ayes,
            onClick = onVoteTypeClick,
            modifier = keyModifier
        )

        GraphKeyItem(
            VoteType.NoVote,
            division.noes,
            onClick = onVoteTypeClick,
            modifier = keyModifier
        )

        GraphKeyItem(
            VoteType.Abstains,
            division.abstentions ?: 0,
            onClick = onVoteTypeClick,
            modifier = keyModifier
        )

        GraphKeyItem(
            VoteType.DidNotVote,
            division.didNotVote ?: 0,
            onClick = onVoteTypeClick,
            modifier = keyModifier
        )

        GraphKeyItem(
            VoteType.SuspendedOrExpelledVote,
            division.suspendedOrExpelled ?: 0,
            onClick = onVoteTypeClick,
            modifier = keyModifier
        )
    }
}

@Composable
private fun GraphKeyItem(
    voteType: VoteType,
    voteCount: Int,
    modifier: Modifier = Modifier,
    onClick: (VoteType) -> Unit,
) {
    if (voteCount == 0) return
    var selected by rememberBoolean(false)

    ChartKeyItem(
        icon = { VoteIcon(voteType.icon, null, voteType.color) },
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
                onClick = {
                    selected = !selected
                    onClick(voteType)
                },
            )
            .selectionDecoration(selected, color = colors.primary)
            .padding(4.dp)
            .padding(end = 8.dp),
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
        VoteType.AyeVote -> AppIcon.VoteAye
        VoteType.NoVote -> AppIcon.VoteNo
        else -> AppIcon.CloseCircle
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
