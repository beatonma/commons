package org.beatonma.commons.app.constituency.electionresults

import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.colors.ComposePartyColors
import org.beatonma.commons.app.ui.compose.WithResultData
import org.beatonma.commons.app.ui.compose.components.party.LocalPartyTheme
import org.beatonma.commons.app.ui.compose.components.party.PartyBackground
import org.beatonma.commons.app.ui.compose.components.party.PartyWithTheme
import org.beatonma.commons.app.ui.compose.components.party.partyWithTheme
import org.beatonma.commons.app.ui.compose.components.party.providePartyImageConfig
import org.beatonma.commons.compose.ambient.colors
import org.beatonma.commons.compose.ambient.shapes
import org.beatonma.commons.compose.ambient.typography
import org.beatonma.commons.compose.animation.ExpandCollapseState
import org.beatonma.commons.compose.animation.animateExpansion
import org.beatonma.commons.compose.animation.rememberExpandCollapseState
import org.beatonma.commons.compose.components.HorizontalSeparator
import org.beatonma.commons.compose.components.Tag
import org.beatonma.commons.compose.components.collapsibleheader.CollapsibleHeaderLayout
import org.beatonma.commons.compose.components.text.ComponentTitle
import org.beatonma.commons.compose.components.text.Date
import org.beatonma.commons.compose.components.text.ResourceText
import org.beatonma.commons.compose.modifiers.wrapContentHeight
import org.beatonma.commons.compose.util.dot
import org.beatonma.commons.core.extensions.progressIn
import org.beatonma.commons.core.extensions.reversed
import org.beatonma.commons.data.core.room.entities.constituency.ConstituencyCandidate
import org.beatonma.commons.data.core.room.entities.constituency.ConstituencyElectionDetails
import org.beatonma.commons.data.core.room.entities.constituency.ConstituencyElectionDetailsWithExtras
import org.beatonma.commons.data.core.room.entities.member.Party
import org.beatonma.commons.data.resolution.PartyResolution
import org.beatonma.commons.repo.result.IoLoading
import org.beatonma.commons.theme.compose.Size
import org.beatonma.commons.theme.compose.formatting.formatPercent
import org.beatonma.commons.theme.compose.formatting.formatted
import org.beatonma.commons.theme.compose.padding.Padding
import org.beatonma.commons.theme.compose.padding.padding
import org.beatonma.commons.theme.compose.theme.componentTitle
import org.beatonma.commons.theme.compose.theme.onWarningSurface
import org.beatonma.commons.theme.compose.theme.systemui.statusBarsPadding
import org.beatonma.commons.theme.compose.theme.warningSurface
import java.util.*

/**
 * Candidates pay a deposit when they register.
 * Candidates who win less than 5% of the vote lose their deposit.
 */
private const val VOTE_DEPOSIT_CUTOFF = 0.05F

private const val CandidateVoteBarDelay = 150L
private val CandidateVoteBarHeight = 6.dp
private val TrailingSize = 60.dp

@Composable
fun ConstituencyResultsLayout(
    viewmodel: ConstituencyResultsViewModel,
) {
    val resultState by viewmodel.livedata.observeAsState(IoLoading)

    WithResultData(resultState) { data ->
        ConstituencyResultsLayout(data)
    }
}

@Composable
fun ConstituencyResultsLayout(
    data: ConstituencyElectionDetailsWithExtras,
) {
    CompositionLocalProvider(
        *providePartyImageConfig(),
    ) {
        CollapsibleHeaderLayout(
            collapsingHeader = { Header(data, it) },
            lazyListContent = {
                Candidates(details = data.details, candidates = data.candidates)
            }
        )
    }
}

@Composable
private fun Header(data: ConstituencyElectionDetailsWithExtras, expanded: Float) {
    Column(
        Modifier.statusBarsPadding()
    ) {
        ComponentTitle(
            stringResource(R.string.constituency_election_header, data.election.name) dot
                    data.constituency.name,
        )

        Column(
            Modifier
                .padding(Padding.ScreenHorizontal)
                .wrapContentHeight(expanded.progressIn(0.7F, 1F))
                .alpha(expanded.progressIn(.7F, 1F))
        ) {
            ResourceText(
                R.string.constituency_election_turnout,
                data.details.turnoutPercent(),
                data.details.turnout.formatted(),
                data.details.electorate.formatted(),
                withAnnotatedStyle = true,
            )
            Date(data.election.date)
        }
    }
}

private fun LazyListScope.Candidates(
    details: ConstituencyElectionDetails,
    candidates: List<ConstituencyCandidate>
) {
    var lostDepositDisplayed = false
    itemsIndexed(candidates) { index, candidate ->
        // Party lookup using (probably) abbreviated name - may be inaccurate but worst case
        // will have an invalid ID and use name as given, giving the default party theme.
        val fuzzyParty = Party(
            name = PartyResolution.getPartyName(candidate.partyName),
            parliamentdotuk = PartyResolution.getPartyId(candidate.partyName)
        )

        CompositionLocalProvider(LocalPartyTheme provides partyWithTheme(fuzzyParty)) {
            val votePercentage = candidate.votePercentage(details.turnout)

            if (index == 0) {
                WinningCandidate(candidate, details, votePercentage)
                return@CompositionLocalProvider
            }

            if (!lostDepositDisplayed && votePercentage < VOTE_DEPOSIT_CUTOFF) {
                DepositLostMarker()
                lostDepositDisplayed = true
            }

            LosingCandidate(candidate, votePercentage)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun WinningCandidate(
    candidate: ConstituencyCandidate,
    details: ConstituencyElectionDetails,
    votePercentage: Float,
) {
    PartyBackground(
        modifier = Modifier
            .padding(Padding.VerticalListItemLarge),
    ) {
        ListItem(
            text = {
                Text(candidate.name dot formattedName(candidate.partyName))
            },
            secondaryText = {
                Column(Modifier.fillMaxWidth()) {
                    Row(
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            candidate.votes.formatted() dot votePercentage.formatPercent(),
                            Modifier.padding(Padding.LinkItem)
                        )
                        ResourceText(R.string.constituency_winner_majority, details.majority.formatted(), style = typography.caption)
                    }
                    CandidateVotesBar(votePercentage = votePercentage, position = candidate.order)
                }
            },
            icon = { CandidatePosition(position = candidate.order, withBackgroundColor = false) },
            trailing = { ResultSummary(details, Modifier.size(TrailingSize)) },
            modifier = Modifier.padding(Padding.VerticalListItemLarge),
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun LosingCandidate(
    candidate: ConstituencyCandidate,
    votePercentage: Float,
) {
    ListItem(
        text = {
            Text(candidate.name dot formattedName(candidate.partyName))
        },
        secondaryText = {
            Column(Modifier.fillMaxWidth()) {
                Text(candidate.votes.formatted() dot votePercentage.formatPercent())
                CandidateVotesBar(votePercentage = votePercentage, position = candidate.order)
            }
        },
        icon = { CandidatePosition(position = candidate.order) },
        trailing = { Spacer(Modifier.size(TrailingSize)) }
    )
}

/**
 * Candidates that appear below this line did not win enough votes to get their deposit back.
 */
@Composable
private fun DepositLostMarker() {
    val state by rememberExpandCollapseState(ExpandCollapseState.Collapsed)
    val transition = updateTransition(targetState = state)
    val expandedness by transition.animateExpansion()

    Box(
        Modifier
            .clickable { state.toggle() }
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        HorizontalSeparator(
            color = colors.warningSurface,
            modifier = Modifier.alpha(expandedness.reversed().progressIn(.9F, 1F))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.ArrowDropDown, contentDescription = "TODO")
                ResourceText(R.string.constituency_candidate_deposit_lost)
                Icon(Icons.Default.ArrowDropDown, contentDescription = "TODO")
            }
        }

        Box(
            Modifier
                .background(colors.warningSurface)
                .alpha(expandedness.progressIn(.2F, .4F))
                .wrapContentHeight(expandedness.progressIn(.2F, 1F))
        ) {
            ResourceText(
                R.string.constituency_candidate_deposit_info,
                modifier = Modifier
                    .alpha(expandedness.progressIn(.8F, 1F))
                    .padding(Padding.VerticalListItemLarge)
                    .padding(Padding.ScreenHorizontal),
                color = colors.onWarningSurface,
            )
        }
    }
}

@Composable
private fun CandidatePosition(
    position: Int,
    partyWithTheme: PartyWithTheme = LocalPartyTheme.current,
    withBackgroundColor: Boolean = true,
) {
    Box(
        modifier = Modifier
            .size(Size.IconButton)
            .clip(shapes.small)
            .background(if (withBackgroundColor) partyWithTheme.theme.primary else Color.Transparent)
        ,
        contentAlignment = Alignment.Center,
    ) {
        Text("$position",
            style = typography.componentTitle,
            color = partyWithTheme.theme.onPrimary
        )
    }
}

@Composable
private fun CandidateVotesBar(
    votePercentage: Float,
    position: Int,
    theme: ComposePartyColors = LocalPartyTheme.current.theme,
) {
    var state by rememberExpandCollapseState(ExpandCollapseState.Collapsed)
    val transition = updateTransition(targetState = state, label = "Candidate votes ExpandCollapse")
    val expandedness by transition.animateExpansion()

    LaunchedEffect(Unit) {
        delay(position * CandidateVoteBarDelay)
        withContext(Dispatchers.Main) {
            state = ExpandCollapseState.Expanded
        }
    }

    Box {
        Spacer(
            Modifier
                .padding(vertical = CandidateVoteBarHeight)
                .background(LocalContentColor.current.copy(alpha = .2F))
                .height(CandidateVoteBarHeight)
                .fillMaxWidth()
                .clip(shapes.small)
        )

        Spacer(
            Modifier
                .padding(vertical = CandidateVoteBarHeight)
                .background(theme.accent)
                .height(CandidateVoteBarHeight)
                .fillMaxWidth(votePercentage * expandedness)
                .clip(shapes.small)
        )
    }
}

@Composable
private fun ResultSummary(
    details: ConstituencyElectionDetails,
    modifier: Modifier = Modifier,
    theme: ComposePartyColors = LocalPartyTheme.current.theme,
) {
    val result = details.result
        .split(" ")
        .lastOrNull()
        ?.lowercase(Locale.getDefault())
        ?: return

    val resultText = when (result) {
        "gain" -> stringResource(R.string.constituency_result_gain)
        "hold" -> stringResource(R.string.constituency_result_hold)
        else -> null
    } ?: return

    Box(
        modifier,
        contentAlignment = Alignment.Center
    ) {
        Tag(resultText, color = theme.accent, contentColor = theme.onAccent)
    }
}

@Composable
private fun formattedName(fuzzyName: String) = PartyResolution.getPartyName(fuzzyName)

private fun ConstituencyCandidate.votePercentage(turnout: Int): Float =
    this.votes.toFloat() / turnout.toFloat()
