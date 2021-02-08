package org.beatonma.commons.app.constituency.compose.electionresults

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.compose.WithResultData
import org.beatonma.commons.app.ui.compose.components.party.AmbientPartyTheme
import org.beatonma.commons.app.ui.compose.components.party.PartyBackground
import org.beatonma.commons.app.ui.compose.components.party.PartyWithTheme
import org.beatonma.commons.app.ui.compose.components.party.partyWithTheme
import org.beatonma.commons.app.ui.compose.components.party.providePartyImageConfig
import org.beatonma.commons.compose.ambient.colors
import org.beatonma.commons.compose.ambient.shapes
import org.beatonma.commons.compose.ambient.typography
import org.beatonma.commons.compose.animation.ExpandCollapseState
import org.beatonma.commons.compose.animation.rememberExpandCollapseState
import org.beatonma.commons.compose.animation.toggle
import org.beatonma.commons.compose.components.CollapsibleHeaderLayout
import org.beatonma.commons.compose.components.ResourceText
import org.beatonma.commons.compose.util.dot
import org.beatonma.commons.data.core.room.entities.constituency.ConstituencyCandidate
import org.beatonma.commons.data.core.room.entities.constituency.ConstituencyElectionDetails
import org.beatonma.commons.data.core.room.entities.constituency.ConstituencyElectionDetailsWithExtras
import org.beatonma.commons.data.core.room.entities.member.Party
import org.beatonma.commons.data.resolution.PartyResolution
import org.beatonma.commons.kotlin.extensions.formatPercent
import org.beatonma.commons.kotlin.extensions.formatted
import org.beatonma.commons.repo.result.IoLoading
import org.beatonma.commons.theme.compose.Padding
import org.beatonma.commons.theme.compose.Size
import org.beatonma.commons.theme.compose.components.ScreenTitle
import org.beatonma.commons.theme.compose.plus
import org.beatonma.commons.theme.compose.theme.componentTitle
import org.beatonma.commons.theme.compose.theme.onWarningSurface
import org.beatonma.commons.theme.compose.theme.systemui.statusBarsPadding
import org.beatonma.commons.theme.compose.theme.warningSurface

/**
 * Candidates pay a deposit when they register.
 * Candidates who win less than 5% of the vote lose their deposit.
 */
private const val VOTE_DEPOSIT_CUTOFF = 0.05F


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
    Providers(
        *providePartyImageConfig(),
    ) {
        CollapsibleHeaderLayout(
            collapsingHeader = {
                Header(data, it)
            },
            lazyListContent = {
                Candidates(details = data.details, candidates = data.candidates)
            }
        )
    }
}

@Composable
private fun Header(data: ConstituencyElectionDetailsWithExtras, expanded: Float) {
    Column {
        ScreenTitle(
            stringResource(R.string.constituency_election_header, data.election.name),
            Modifier.statusBarsPadding()
        )
        Text(data.constituency.name)
        Text(data.election.date.formatted())

        ResourceText(
            R.string.constituency_election_turnout,
            data.details.turnout.formatted(),
            data.details.electorate.formatted(),
            data.details.turnoutPercent(),
        )
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

        Providers(AmbientPartyTheme provides partyWithTheme(fuzzyParty)) {
            val votePercentage = candidate.votePercentage(details.turnout)

            if (index == 0) {
                WinningCandidate(candidate, details, votePercentage)
                return@Providers
            }

            if (!lostDepositDisplayed && votePercentage < VOTE_DEPOSIT_CUTOFF) {
                DepositLostMarker()
                lostDepositDisplayed = true
            }

            LosingCandidate(candidate, votePercentage)
        }
    }
}

@Composable
private fun WinningCandidate(
    candidate: ConstituencyCandidate,
    details: ConstituencyElectionDetails,
    votePercentage: Float,
) {
    PartyBackground(
        modifier = Modifier
            .padding(Padding.VerticalListItemLarge)
            .clip(shapes.small),
    ) {
        ListItem(
            text = {
                Text(candidate.name dot formattedName(candidate.partyName))
            },
            secondaryText = {
                Text(candidate.votes.formatted() dot votePercentage.formatPercent())
            },
            icon = { CandidatePosition(position = candidate.order) },
            modifier = Modifier.padding(Padding.VerticalListItemLarge)
        )
    }

    TODO("Show stuff from details ${details.majority} and ${details.result}")
}

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
            Text(candidate.votes.formatted() dot votePercentage.formatPercent())
        },
        icon = { CandidatePosition(position = candidate.order) }
    )
}

/**
 * Candidates that appear below this line did not win enough votes to get their deposit back.
 */
@Composable
private fun DepositLostMarker() {
    val state = rememberExpandCollapseState(ExpandCollapseState.Expanded)
    // TODO animate

    ResourceText(
        R.string.constituency_candidate_deposit_info,
        modifier = Modifier
            .background(colors.warningSurface)
            .fillMaxWidth()
            .clickable { state.toggle() }
            .padding(Padding.VerticalListItemLarge + Padding.ScreenHorizontal)
        ,
        color = colors.onWarningSurface,
    )
}

@Composable
private fun CandidatePosition(
    position: Int,
    partyWithTheme: PartyWithTheme = AmbientPartyTheme.current,
) {
    Box(
        modifier = Modifier
            .size(Size.IconButton)
            .clip(shapes.small)
            .background(partyWithTheme.theme.primary)
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
private fun formattedName(fuzzyName: String) = PartyResolution.getPartyName(fuzzyName)

private fun ConstituencyCandidate.votePercentage(turnout: Int): Float =
    this.votes.toFloat() / turnout.toFloat()
