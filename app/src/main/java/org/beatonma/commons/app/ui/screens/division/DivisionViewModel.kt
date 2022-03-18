package org.beatonma.commons.app.ui.screens.division

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.beatonma.commons.app.data.SavedStateKey
import org.beatonma.commons.app.data.get
import org.beatonma.commons.app.data.set
import org.beatonma.commons.app.ui.base.IoLiveDataViewModel
import org.beatonma.commons.app.ui.base.SocialTargetProvider
import org.beatonma.commons.app.util.BundledDivision
import org.beatonma.commons.core.DivisionVoteType
import org.beatonma.commons.core.House
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.data.core.room.entities.division.HouseDivision
import org.beatonma.commons.data.core.room.entities.division.ResolvedHouseVote
import org.beatonma.commons.repo.repository.DivisionRepository
import org.beatonma.commons.repo.result.IoResult
import org.beatonma.commons.repo.result.map

private val DivisionKey = SavedStateKey("division_id")

abstract class DivisionViewModel<T : HouseDivision<V>, V : DivisionVoteType>(
    protected val repository: DivisionRepository,
    protected val savedStateHandle: SavedStateHandle,
) : IoLiveDataViewModel<T>(), SocialTargetProvider {
    val divisionID: ParliamentID get() = savedStateHandle[DivisionKey]!!
    abstract val house: House

    init {
        forSavedDivision()
    }

    fun forDivision(bundled: BundledDivision) = forDivision(bundled.parliamentdotuk)

    private fun forSavedDivision() {
        val divisionId: ParliamentID? = savedStateHandle[DivisionKey]

        if (divisionId != null) {
            forDivision(divisionId)
        }
    }

    abstract val repoFunc: (DivisionRepository.(ParliamentID) -> Flow<IoResult<T>>)

    fun forDivision(divisionId: ParliamentID) {
        savedStateHandle[DivisionKey] = divisionId

        viewModelScope.launch(Dispatchers.IO) {
            repository.repoFunc(divisionId)
                .map { result ->
                    result.map(this@DivisionViewModel::applySorting)
                }
                .collectLatest(this@DivisionViewModel::postValue)
        }
    }

    abstract fun applySorting(division: T): T

    inline fun <reified V : ResolvedHouseVote<T>, T : DivisionVoteType> List<V>.sortedVotes() =
        this.sortedBy { it.data.memberName }
}
