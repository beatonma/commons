package org.beatonma.commons.app.division.compose

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.beatonma.commons.core.House
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.data.SavedStateKey
import org.beatonma.commons.data.core.room.entities.division.DivisionWithVotes
import org.beatonma.commons.data.core.room.entities.division.VoteWithParty
import org.beatonma.commons.data.get
import org.beatonma.commons.data.set
import org.beatonma.commons.kotlin.extensions.BundledDivision
import org.beatonma.commons.repo.repository.DivisionRepository
import org.beatonma.commons.repo.result.BaseResult
import org.beatonma.commons.repo.result.IoLoading
import org.beatonma.commons.repo.result.IoResult
import org.beatonma.commons.repo.result.map
import org.beatonma.commons.snommoc.models.social.SocialTarget
import org.beatonma.commons.snommoc.models.social.SocialTargetType

private val HouseKey = SavedStateKey("house")
private val DivisionKey = SavedStateKey("division_id")

class DivisionDetailViewModel @ViewModelInject constructor(
    private val repository: DivisionRepository,
    @Assisted private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _livedata: MutableLiveData<IoResult<DivisionWithVotes>> =
        MutableLiveData(IoLoading)
    val livedata: LiveData<IoResult<DivisionWithVotes>> = _livedata

    val house: House get() = savedStateHandle[HouseKey]!!
    val divisionID: ParliamentID get() = savedStateHandle[DivisionKey]!!

    init {
        val house: House? = savedStateHandle[HouseKey]
        val divisionId: Int? = savedStateHandle[DivisionKey]

        if (house != null && divisionId != null) {
            forDivision(house, divisionId)
        }
    }

    val socialTarget: SocialTarget
        get() = SocialTarget(
            targetType = when(house) {
                House.commons -> SocialTargetType.division_commons
                House.lords -> SocialTargetType.division_lords
            },
            parliamentdotuk = divisionID,
        )

    fun forDivision(bundled: BundledDivision) = forDivision(bundled.house, bundled.parliamentdotuk)

    private fun forDivision(house: House, divisionId: ParliamentID) {
        savedStateHandle[HouseKey] = house
        savedStateHandle[DivisionKey] = divisionId

        viewModelScope.launch(Dispatchers.IO) {
            repository.getDivision(house, divisionId)
                .collect { result: BaseResult<DivisionWithVotes, Throwable> ->
                    val sorted = result.map { divisionWithVotes ->
                        divisionWithVotes.copy(votes = sortedVotes(divisionWithVotes.votes))
                    }
                    _livedata.postValue(sorted)
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
