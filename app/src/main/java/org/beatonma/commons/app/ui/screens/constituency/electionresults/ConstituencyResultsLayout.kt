package org.beatonma.commons.app.ui.screens.constituency.electionresults

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.beatonma.commons.R
import org.beatonma.commons.app.data.resolution.PartyResolution
import org.beatonma.commons.app.ui.colors.PartyColors
import org.beatonma.commons.app.ui.components.Date
import org.beatonma.commons.app.ui.components.party.LocalPartyTheme
import org.beatonma.commons.app.ui.components.party.PartyBackground
import org.beatonma.commons.app.ui.components.party.PartyWithTheme
import org.beatonma.commons.app.ui.components.party.ProvidePartyImageConfig
import org.beatonma.commons.app.ui.components.party.partyWithTheme
import org.beatonma.commons.app.ui.util.WithResultData
import org.beatonma.commons.compose.animation.ExpandCollapseState
import org.beatonma.commons.compose.animation.animateExpansionAsState
import org.beatonma.commons.compose.animation.rememberExpandCollapseState
import org.beatonma.commons.compose.components.HorizontalSeparator
import org.beatonma.commons.compose.components.text.ComponentTitle
import org.beatonma.commons.compose.components.text.ResourceText
import org.beatonma.commons.compose.layout.itemWithState
import org.beatonma.commons.compose.layout.stickyHeaderWithInsets
import org.beatonma.commons.compose.modifiers.wrapContentHeight
import org.beatonma.commons.compose.padding.endOfContent
import org.beatonma.commons.compose.padding.padding
import org.beatonma.commons.compose.util.dot
import org.beatonma.commons.core.extensions.progressIn
import org.beatonma.commons.core.extensions.reversed
import org.beatonma.commons.data.core.room.entities.constituency.ConstituencyCandidate
import org.beatonma.commons.data.core.room.entities.constituency.ConstituencyCandidateWithParty
import org.beatonma.commons.data.core.room.entities.constituency.ConstituencyElectionDetails
import org.beatonma.commons.data.core.room.entities.constituency.ConstituencyElectionDetailsWithExtras
import org.beatonma.commons.data.core.room.entities.member.MemberProfile
import org.beatonma.commons.data.core.room.entities.member.Party
import org.beatonma.commons.repo.result.IoLoading
import org.beatonma.commons.theme.CommonsPadding
import org.beatonma.commons.theme.CommonsSize
import org.beatonma.commons.theme.componentTitle
import org.beatonma.commons.theme.formatting.formatPercent
import org.beatonma.commons.theme.formatting.formatted
import org.beatonma.commons.theme.onWarningSurface
import org.beatonma.commons.theme.warningSurface
import org.beatonma.commons.themed.themedPadding
import java.util.Locale


/**
 * Candidates pay a deposit when they register.
 * Candidates who win less than 5% of the vote lose their deposit.
 */
private const val VoteDepositCutoff = 0.05F

private const val CandidateVoteBarDelay = 150L
private val CandidateVoteBarHeight = 6.dp
private val TrailingSize = 60.dp

@Composable
fun ConstituencyResultsLayout(
    viewmodel: ConstituencyResultsViewModel = hiltViewModel(),
    onClickCandidate: (name: String, MemberProfile?) -> Unit,
) {
    val resultState by viewmodel.livedata.observeAsState(IoLoading)

    WithResultData(resultState) { data ->
        ConstituencyResultsLayout(
            data,
            onClickCandidate = onClickCandidate,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ConstituencyResultsLayout(
    data: ConstituencyElectionDetailsWithExtras,
    onClickCandidate: (name: String, MemberProfile?) -> Unit,
) {
    ProvidePartyImageConfig {
        var depositLostPosition by remember { mutableStateOf(-1) }
        val state = rememberLazyListState()

        LazyColumn(state = state) {
            stickyHeaderWithInsets(state, "header") { headerState, headerModifier ->
                val elevation by animateDpAsState(targetValue = if (headerState.overlapsNext) 4.dp else 0.dp)
                Header(
                    data,
                    Modifier
                        .shadow(elevation)
                        .fillMaxWidth()
                        .onlyWhen(headerState.overlapsNext) {
                            shadow(4.dp)
                        }
                        .background(colors.background)
                        .then(headerModifier)
                )
            }

            itemWithState(state, "collapsing") { itemState ->
                Column(
                    Modifier
                        .padding(themedPadding.ScreenHorizontal)
                        .alpha(itemState.visibility)
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

            Candidates(
                details = data.details,
                candidates = data.candidates,
                depositLostPosition = depositLostPosition,
                onDepositionLostPositionChange = { depositLostPosition = it },
                onClickCandidate = onClickCandidate,
            )

            endOfContent()
        }
    }
}

@Composable
private fun Header(data: ConstituencyElectionDetailsWithExtras, modifier: Modifier = Modifier) {
    Column(modifier) {
        ComponentTitle(
            stringResource(
                R.string.constituency_election_header,
                data.election.name
            )
        )

        ComponentTitle(data.constituency.name)
    }
}

private fun LazyListScope.Candidates(
    details: ConstituencyElectionDetails,
    candidates: List<ConstituencyCandidateWithParty>,
    depositLostPosition: Int,
    onDepositionLostPositionChange: (Int) -> Unit,
    onClickCandidate: (name: String, MemberProfile?) -> Unit,
) {
    itemsIndexed(candidates) { index, candidateWithParty ->
        val (candidate, profile, party) = candidateWithParty

        val onClickModifier =
            Modifier.clickable { onClickCandidate(candidate.name, profile?.profile) }
        // Party lookup using (probably) abbreviated name - may be inaccurate but worst case
        // will have an invalid ID and use name as given, giving the default party theme.
        val fuzzyParty = party ?: Party(
            name = PartyResolution.getPartyName(candidate.partyName),
            parliamentdotuk = PartyResolution.getPartyId(candidate.partyName)
        )

        val votePercentage = candidate.votePercentage(details.turnout)

        if (depositLostPosition < 0 && votePercentage < VoteDepositCutoff) {
            onDepositionLostPositionChange(index)
        }

        CompositionLocalProvider(LocalPartyTheme provides partyWithTheme(fuzzyParty)) {
            when (index) {
                0 -> WinningCandidate(candidate, party, details, votePercentage, onClickModifier)
                depositLostPosition -> {
                    DepositLostMarker()
                    LosingCandidate(candidate, party, votePercentage, onClickModifier)
                }
                else -> LosingCandidate(candidate, party, votePercentage, onClickModifier)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun WinningCandidate(
    candidate: ConstituencyCandidate,
    party: Party?,
    details: ConstituencyElectionDetails,
    votePercentage: Float,
    modifier: Modifier,
) {
    PartyBackground(
        modifier = modifier
            .padding(themedPadding.VerticalListItemLarge),
    ) {
        ListItem(
            text = {
                Text(candidate.name dot formattedName(party?.name ?: candidate.partyName))
            },
            secondaryText = {
                Column(Modifier.fillMaxWidth()) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            candidate.votes.formatted() dot votePercentage.formatPercent(),
                            Modifier.padding(CommonsPadding.LinkItem)
                        )
                        ResourceText(
                            R.string.constituency_winner_majority,
                            details.majority.formatted(),
                            style = typography.caption
                        )
                    }
                    CandidateVotesBar(votePercentage = votePercentage, position = candidate.order)
                }
            },
            icon = { CandidatePosition(position = candidate.order, withBackgroundColor = false) },
            trailing = { ResultSummary(details, Modifier.width(TrailingSize)) },
            modifier = Modifier.padding(themedPadding.VerticalListItemLarge),
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun LosingCandidate(
    candidate: ConstituencyCandidate,
    party: Party?,
    votePercentage: Float,
    modifier: Modifier,
) {
    ListItem(
        modifier = modifier,
        text = {
            Text(candidate.name dot formattedName(party?.name ?: candidate.partyName))
        },
        secondaryText = {
            Column(Modifier.fillMaxWidth()) {
                Text(candidate.votes.formatted() dot votePercentage.formatPercent())
                CandidateVotesBar(votePercentage = votePercentage, position = candidate.order)
            }
        },
        icon = { CandidatePosition(position = candidate.order) },
        trailing = { Spacer(Modifier.width(TrailingSize)) }
    )
}

/**
 * Candidates that appear below this line did not win enough votes to get their deposit back.
 */
@Composable
private fun DepositLostMarker() {
    var state by rememberExpandCollapseState(ExpandCollapseState.Collapsed)
    val expandedness by state.animateExpansionAsState()

    Box(
        Modifier
            .clickable { state = state.toggle() }
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
                    .padding(themedPadding.VerticalListItemLarge)
                    .padding(themedPadding.ScreenHorizontal),
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
            .size(CommonsSize.IconButton)
            .clip(shapes.small)
            .background(if (withBackgroundColor) partyWithTheme.theme.primary else Color.Transparent),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            "$position",
            style = typography.componentTitle,
            color = partyWithTheme.theme.onPrimary
        )
    }
}

@Composable
private fun CandidateVotesBar(
    votePercentage: Float,
    position: Int,
    theme: PartyColors = LocalPartyTheme.current.theme,
) {
    var state by rememberExpandCollapseState(ExpandCollapseState.Collapsed)
    val expandedness by state.animateExpansionAsState()

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
    theme: PartyColors = LocalPartyTheme.current.theme,
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
        Text(resultText, color = theme.onPrimary)
    }
}

@Composable
private fun formattedName(fuzzyName: String) = PartyResolution.getPartyName(fuzzyName)

private fun ConstituencyCandidate.votePercentage(turnout: Int): Float =
    this.votes.toFloat() / turnout.toFloat()
