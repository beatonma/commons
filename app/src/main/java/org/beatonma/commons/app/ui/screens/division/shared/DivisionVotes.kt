package org.beatonma.commons.app.ui.screens.division.shared

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.components.EmptyList
import org.beatonma.commons.app.ui.screens.division.MemberVoteAction
import org.beatonma.commons.compose.components.ListItem
import org.beatonma.commons.compose.components.text.SearchField
import org.beatonma.commons.compose.layout.itemsOrEmpty
import org.beatonma.commons.compose.layout.stickyHeaderWithInsets
import org.beatonma.commons.core.DivisionVoteType
import org.beatonma.commons.data.core.room.entities.division.HouseDivision
import org.beatonma.commons.data.core.room.entities.division.ResolvedHouseVote
import org.beatonma.commons.data.core.room.entities.member.Party


inline fun <reified V : ResolvedHouseVote<T>, T : DivisionVoteType> List<V>.sortedVotes() =
    this.sortedBy { it.data.memberName }


@Composable
fun <T : DivisionVoteType> DivisionVotesLaunchedEffect(
    query: String,
    filters: Set<T>,
    votes: List<ResolvedHouseVote<T>>,
    onChange: (List<ResolvedHouseVote<T>>) -> Unit,
) {
    LaunchedEffect(query, filters) {
        val filtered = votes
            .filter { it.data.vote in filters }
            .filter { it.data.memberName.contains(query, ignoreCase = true) }
        onChange(filtered)
    }
}


@OptIn(ExperimentalFoundationApi::class)
fun <T : DivisionVoteType> LazyListScope.DivisionVotes(
    state: LazyListState,
    division: HouseDivision<T>,
    displayVotes: List<ResolvedHouseVote<T>>,
    query: MutableState<String>,
    filter: MutableState<Set<T>>,
    onMemberClick: MemberVoteAction,
    modifier: Modifier = Modifier,
) {
    DivisionVotesHeader(state, division, query, filter, modifier)

    itemsOrEmpty(
        displayVotes, key = { it.data.memberId },
        emptyContent = { EmptyList() },
    ) { vote ->
        DivisionMemberVote(vote = vote.data.vote, vote.data.memberName, vote.party) {
            onMemberClick(vote.data.memberId)
        }
    }
}

private fun <T : HouseDivision<V>, V : DivisionVoteType> LazyListScope.DivisionVotesHeader(
    state: LazyListState,
    division: T,
    query: MutableState<String>,
    filter: MutableState<Set<V>>,
    modifier: Modifier,
) {
    stickyHeaderWithInsets(state, "division_votes") { _, headerModifier ->
        Column(
            modifier
                .background(colors.surface)
                .then(headerModifier)
        ) {
            DivisionVoteChart(
                division = division,
                voteTypeFilter = filter.value,
                onVoteTypeClick = { type: V, selected: Boolean ->
                    filter.value = when {
                        selected -> filter.value + type
                        else -> filter.value - type
                    }
                },
                modifier = Modifier,
            )

            DivisionVoteSearchField(
                query = query.value,
                onQueryChange = { query.value = it },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun <T : DivisionVoteType> DivisionMemberVote(
    vote: T,
    memberName: String,
    party: Party,
    onClick: () -> Unit,
) {
    ListItem(
        text = memberName,
        icon = { DivisionVoteIcon(vote) },
        trailing = party.name,
        modifier = Modifier.clickable(onClick = onClick),
    )
}

@Composable
private fun DivisionVoteSearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier,
) {
    SearchField(
        query = query,
        onQueryChange = onQueryChange,
        hint = stringResource(R.string.division_search_member_hint),
        modifier = modifier,
    )
}
