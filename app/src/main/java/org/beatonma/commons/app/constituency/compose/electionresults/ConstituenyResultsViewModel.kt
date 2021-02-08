package org.beatonma.commons.app.constituency.compose.electionresults

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.beatonma.commons.app.ui.base.IoLiveDataViewModel
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.data.SavedStateKey
import org.beatonma.commons.data.core.room.entities.constituency.ConstituencyCandidate
import org.beatonma.commons.data.core.room.entities.constituency.ConstituencyElectionDetails
import org.beatonma.commons.data.core.room.entities.constituency.ConstituencyElectionDetailsWithExtras
import org.beatonma.commons.data.get
import org.beatonma.commons.data.set
import org.beatonma.commons.kotlin.extensions.BundledConstituencyResult
import org.beatonma.commons.kotlin.extensions.formatPercent
import org.beatonma.commons.repo.repository.ConstituencyRepository
import org.beatonma.commons.repo.result.map
import javax.inject.Inject

private val ConstituencyKey = SavedStateKey("constituency_id")
private val ElectionKey = SavedStateKey("election_id")

@HiltViewModel
class ConstituencyResultsViewModel @Inject constructor(
    private val repository: ConstituencyRepository,
    private val savedStateHandle: SavedStateHandle,
): IoLiveDataViewModel<ConstituencyElectionDetailsWithExtras>() {
    val constituencyID: ParliamentID get() = savedStateHandle[ConstituencyKey]!!
    val electionID: ParliamentID get() = savedStateHandle[ElectionKey]!!

    fun forConstituencyInElection(bundled: BundledConstituencyResult) {
        forConstituencyInElection(bundled.constituencyId, bundled.electionId)
    }

    private fun forConstituencyInElection(constituencyId: ParliamentID, electionId: ParliamentID) {
        savedStateHandle[ConstituencyKey] = constituencyId
        savedStateHandle[ElectionKey] = electionId

        viewModelScope.launch(Dispatchers.IO) {
            repository.getConstituencyResultsForElection(constituencyID, electionID)
                .collect { result ->
                    postValue(
                        result.map {
                            it.copy(
                                candidates = it.candidates.sortedBy(ConstituencyCandidate::order),
                            )
                        }
                    )
                }
        }
    }
}

internal fun ConstituencyElectionDetails.turnoutPercent() = formatPercent(turnout, electorate)
