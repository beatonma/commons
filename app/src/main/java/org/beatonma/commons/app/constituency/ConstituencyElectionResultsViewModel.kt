package org.beatonma.commons.app.constituency

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import dagger.hilt.android.qualifiers.ApplicationContext
import org.beatonma.commons.app.ui.BaseIoAndroidViewModel
import org.beatonma.commons.context
import org.beatonma.commons.data.ParliamentID
import org.beatonma.commons.data.core.repository.ConstituencyRepository
import org.beatonma.commons.data.core.room.entities.constituency.ConstituencyCandidate
import org.beatonma.commons.data.core.room.entities.constituency.ConstituencyElectionDetails
import org.beatonma.commons.data.core.room.entities.constituency.ConstituencyElectionDetailsWithExtras
import org.beatonma.commons.data.resolution.PartyResolution
import org.beatonma.commons.kotlin.extensions.formatPercent

class ConstituencyElectionResultsViewModel @ViewModelInject constructor(
    private val repository: ConstituencyRepository,
    @ApplicationContext context: Context,
): BaseIoAndroidViewModel<ConstituencyElectionDetailsWithExtras>(context) {

    fun forConstituencyInElection(constituencyId: ParliamentID, electionId: ParliamentID) {
        liveData = repository.observeConstituencyResultsForElection(constituencyId, electionId)
    }

    private fun getPercentage(value: Int, outOf: Int): Float =
        value.coerceAtLeast(1).toFloat() / outOf.coerceAtLeast(1).toFloat()

    fun getFormattedPercentage(value: Int, outOf: Int): String = getPercentage(value, outOf).formatPercent()

    fun getTurnoutPercentage(details: ConstituencyElectionDetails?): String? {
        details ?: return null
        return getFormattedPercentage(details.turnout, details.electorate)
    }

    fun getFormattedParty(candidate: ConstituencyCandidate): String =
        PartyResolution.getPartyName(
            context,
            candidate.partyName
                .split(" ")
                .first()
        )

    fun getFormattedName(candidate: ConstituencyCandidate): String =
        candidate.name.split(",")
            .reversed()
            .joinToString(separator = " ") {
                it.trim()
            }

    internal fun composeCandidateData(details: ConstituencyElectionDetailsWithExtras): List<CandidateData>? {
        val totalVotes = details.details?.turnout ?: return null
        val candidates = details.candidates ?: return null
        if (candidates.isEmpty()) return null
        val (depositReturned, depositLost) = candidates.partition {
            // Candidates with less than 5% of the vote lose their £500 deposit
            getPercentage(it.votes, totalVotes) > .05
        }
        return when {
            depositReturned.isEmpty() -> depositLost.toCandidates()
            depositLost.isEmpty() -> depositReturned.toCandidates()
            else -> listOf(
                depositReturned.toCandidates(),
                listOf(DepositSeparator),
                depositLost.toCandidates()
            ).flatten()
        }
    }
}


internal sealed class CandidateData
internal data class Candidate(val candidate: ConstituencyCandidate): CandidateData()
internal object DepositSeparator : CandidateData()

private fun List<ConstituencyCandidate>.toCandidates() =
    this.sortedBy { it.order }.map { Candidate(it) }
