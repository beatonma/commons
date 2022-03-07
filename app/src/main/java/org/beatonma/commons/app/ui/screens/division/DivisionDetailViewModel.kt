package org.beatonma.commons.app.ui.screens.division

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.beatonma.commons.app.data.SavedStateKey
import org.beatonma.commons.app.data.get
import org.beatonma.commons.app.data.set
import org.beatonma.commons.app.ui.base.IoLiveDataViewModel
import org.beatonma.commons.app.ui.base.SocialTargetProvider
import org.beatonma.commons.app.util.BundledDivision
import org.beatonma.commons.core.House
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.data.core.room.entities.division.DivisionWithVotes
import org.beatonma.commons.data.core.room.entities.division.VoteWithParty
import org.beatonma.commons.repo.repository.DivisionRepository
import org.beatonma.commons.repo.result.BaseResult
import org.beatonma.commons.repo.result.map
import org.beatonma.commons.snommoc.models.social.SocialTarget
import org.beatonma.commons.snommoc.models.social.SocialTargetType
import javax.inject.Inject

private val HouseKey = SavedStateKey("house")
private val DivisionKey = SavedStateKey("division_id")

@HiltViewModel
class DivisionDetailViewModel @Inject constructor(
    private val repository: DivisionRepository,
    private val savedStateHandle: SavedStateHandle,
) : IoLiveDataViewModel<DivisionWithVotes>(), SocialTargetProvider {

    val house: House get() = savedStateHandle[HouseKey]!!
    val divisionID: ParliamentID get() = savedStateHandle[DivisionKey]!!

    override val socialTarget: SocialTarget
        get() = SocialTarget(
            targetType = when(house) {
                House.commons -> SocialTargetType.division_commons
                House.lords -> SocialTargetType.division_lords
                House.unassigned -> throw Exception("Unhandled house ${house}")
            },
            parliamentdotuk = divisionID,
        )

    init {
        forSavedDivision()
    }

    fun forDivision(bundled: BundledDivision) = forDivision(bundled.house, bundled.parliamentdotuk)

    private fun forSavedDivision() {
        val house: House? = savedStateHandle[HouseKey]
        val divisionId: ParliamentID? = savedStateHandle[DivisionKey]

        if (house != null && divisionId != null) {
            forDivision(house, divisionId)
        }
    }

    private fun forDivision(house: House, divisionId: ParliamentID) {
        savedStateHandle[HouseKey] = house
        savedStateHandle[DivisionKey] = divisionId

        viewModelScope.launch(Dispatchers.IO) {
            repository.getDivision(house, divisionId)
                .collect { result: BaseResult<DivisionWithVotes, Throwable> ->
                    val sorted = result.map { divisionWithVotes ->
                        divisionWithVotes.copy(votes = sortedVotes(divisionWithVotes.votes))
                    }
                    postValue(sorted)
                }
        }
    }

    private fun sortedVotes(votes: List<VoteWithParty>): List<VoteWithParty> = votes.sortedWith(
        compareBy(
            { it.vote.voteType },
            { it.vote.memberName }
        )
    )
}
