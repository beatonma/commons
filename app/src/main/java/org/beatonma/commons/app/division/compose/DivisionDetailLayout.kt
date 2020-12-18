package org.beatonma.commons.app.division.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayout
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.ProvidableAmbient
import androidx.compose.runtime.ambientOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.compose.components.LoadingIcon
import org.beatonma.commons.app.ui.compose.components.charts.ChartItem
import org.beatonma.commons.app.ui.compose.components.charts.ChartKeyItem
import org.beatonma.commons.app.ui.compose.components.charts.HorizontalStackedBarChart
import org.beatonma.commons.compose.ambient.colors
import org.beatonma.commons.compose.ambient.typography
import org.beatonma.commons.compose.components.Hint
import org.beatonma.commons.compose.util.dotted
import org.beatonma.commons.compose.util.rememberList
import org.beatonma.commons.compose.util.rememberText
import org.beatonma.commons.compose.util.update
import org.beatonma.commons.compose.util.withAnnotatedStyle
import org.beatonma.commons.core.House
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.core.VoteType
import org.beatonma.commons.data.core.room.entities.division.Division
import org.beatonma.commons.data.core.room.entities.division.DivisionWithVotes
import org.beatonma.commons.data.core.room.entities.division.VoteWithParty
import org.beatonma.commons.data.resolution.description
import org.beatonma.commons.kotlin.extensions.formatted
import org.beatonma.commons.kotlin.extensions.withMainContext
import org.beatonma.commons.theme.compose.EndOfContent
import org.beatonma.commons.theme.compose.Padding
import org.beatonma.commons.theme.compose.Size
import org.beatonma.commons.theme.compose.theme.politicalVotes
import org.beatonma.commons.theme.compose.theme.quote
import org.beatonma.commons.theme.compose.theme.screenTitle
import org.beatonma.commons.theme.compose.theme.systemui.systemBarsPadding

internal val AmbientDivisionActions: ProvidableAmbient<DivisionActions> =
    ambientOf(defaultFactory = ::DivisionActions)

class DivisionActions(
    val onMemberClick: (ParliamentID) -> Unit = {},
)

@Composable
fun DivisionDetailLayout(
    divisionWithVotes: DivisionWithVotes,
) {
    Column(Modifier.systemBarsPadding()) {
        Header(divisionWithVotes.division)
        VoteList(divisionWithVotes.votes)
        EndOfContent()
    }
}

@OptIn(ExperimentalLayout::class)
@Composable
private fun Header(
    division: Division,
) {
    val (title, description) = when (division.house) {
        House.commons -> Pair(stringResource(R.string.division_title), division.title)
        House.lords -> Pair(division.title,
            division.description ?: stringResource(R.string.division_no_description))
    }

    Column(Modifier.padding(Padding.Screen)) {
        Text(title, style = typography.screenTitle)

        Text(dotted(division.house.description(), division.date.formatted()))

        Text(
            description,
            style = typography.quote,
            modifier = Modifier.padding(Padding.Card))

        FlowRow {
            GraphKey(VoteType.AyeVote, division.ayes)
            GraphKey(VoteType.NoVote, division.noes)
            GraphKey(VoteType.Abstains, division.abstentions ?: 0)
            GraphKey(VoteType.DidNotVote, division.didNotVote ?: 0)
            GraphKey(VoteType.SuspendedOrExpelledVote, division.suspendedOrExpelled ?: 0)
        }

        Chart(division, Modifier.fillMaxWidth())
    }
}

@Composable
fun VoteList(
    votes: List<VoteWithParty>,
    modifier: Modifier = Modifier,
    maxVisible: Int = 50,
) {
    val query = rememberText()
    val coroutineScope = rememberCoroutineScope()
    val filteredVotes = rememberList<VoteWithParty>()

    if (votes.isEmpty()) {
        LoadingIcon(Modifier.fillMaxWidth())
        return
    }

    if (query.value.isBlank()) {
        filteredVotes.update(votes)
    }

    Column(modifier.fillMaxWidth()) {
        SearchField(
            query = query,
            modifier = Modifier.fillMaxWidth()
                .padding(Padding.ScreenHorizontal)
        ) { queryText ->
            coroutineScope.launch {
                val results = votes.filter {
                    it.vote.memberName.contains(queryText, ignoreCase = true)
                }.take(maxVisible)
                withMainContext { filteredVotes.update(results) }
            }
        }

        LazyColumn {
            items(items = filteredVotes.value, itemContent = { vote ->
                Vote(vote)
            })
        }
    }
}

@Composable
private fun Vote(
    voteWithParty: VoteWithParty,
    modifier: Modifier = Modifier,
    onMemberClick: (ParliamentID) -> Unit = AmbientDivisionActions.current.onMemberClick,
) {
    ListItem(
        modifier = modifier.clickable(onClick = { onMemberClick(voteWithParty.vote.memberId) }),
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
) {
    TextField(
        value = query.value,
        placeholder = { Hint(stringResource(R.string.division_search_member_hint)) },
        onValueChange = { queryText ->
            query.update(queryText)
            onQueryChange(queryText)
        },
        onImeActionPerformed = { _, controller -> controller?.hideSoftwareKeyboard() },
        modifier = modifier,
    )
}

@Composable
private fun VoteIcon(voteType: VoteType) = VoteIcon(voteType.icon, voteType.color)

@Composable
private fun VoteIcon(
    icon: ImageVector,
    tint: Color,
) {
    Icon(
        icon,
        tint = tint,
        modifier = Modifier
            .padding(Padding.IconSmall)
            .size(Size.IconSmall)
    )
}

@Composable
private fun GraphKey(voteType: VoteType, voteCount: Int, modifier: Modifier = Modifier) {
    if (voteCount == 0) return

    ChartKeyItem(
        { VoteIcon(voteType.icon, voteType.color) },
        { Text(stringResource(voteType.descriptionRes, voteCount).withAnnotatedStyle()) },
        modifier = modifier,
    )
}

@Composable
private val VoteType.color
    get() = when (this) {
        VoteType.AyeVote -> colors.politicalVotes.Aye
        VoteType.NoVote -> colors.politicalVotes.No
        VoteType.DidNotVote -> colors.politicalVotes.DidNotVote
        VoteType.Abstains -> colors.politicalVotes.Abstain
        VoteType.SuspendedOrExpelledVote -> colors.politicalVotes.SuspendedOrExpelled
    }

@Composable
private val VoteType.icon
    get() = when (this) {
        VoteType.AyeVote -> Icons.Default.AddCircle
        VoteType.NoVote -> Icons.Default.RemoveCircle
        else -> Icons.Default.Cancel
    }

@Composable
private val VoteType.descriptionRes
    get() = when (this) {
        VoteType.AyeVote -> R.string.division_ayes
        VoteType.NoVote -> R.string.division_noes
        VoteType.DidNotVote -> R.string.division_abstained
        VoteType.Abstains -> R.string.division_did_not_vote
        VoteType.SuspendedOrExpelledVote -> R.string.division_suspended_or_expelled
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
