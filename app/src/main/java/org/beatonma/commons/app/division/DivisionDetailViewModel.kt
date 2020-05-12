package org.beatonma.commons.app.division

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.beatonma.commons.CommonsApplication
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.Snippet
import org.beatonma.commons.app.ui.SnippetGeneratorAndroidViewModel
import org.beatonma.commons.app.ui.views.BarChartCategory
import org.beatonma.commons.data.IoResult
import org.beatonma.commons.data.ParliamentID
import org.beatonma.commons.data.core.repository.CommonsRepository
import org.beatonma.commons.data.core.room.entities.division.Division
import org.beatonma.commons.data.core.room.entities.division.DivisionWithVotes
import org.beatonma.commons.data.core.room.entities.division.Vote
import org.beatonma.commons.data.core.room.entities.member.House
import org.beatonma.commons.kotlin.extensions.colorCompat
import javax.inject.Inject

class DivisionDetailViewModel @Inject constructor(
    private val repository: CommonsRepository,
    application: CommonsApplication,
) : SnippetGeneratorAndroidViewModel<DivisionWithVotes>(application) {

    private val votesObserver = Observer<IoResult<DivisionWithVotes>> {
        viewModelScope.launch {
            voteLiveData.postValue(
                sortedVotes(it.data?.votes ?: listOf())
            )
        }
    }
    var voteLiveData: MutableLiveData<List<Vote>> = MutableLiveData()

    fun forDivision(house: House, parliamentdotuk: ParliamentID) {
        liveData = repository.observeDivision(house, parliamentdotuk)
        observe()
    }

    override fun observe() {
        super.observe()
        liveData.observeForever(votesObserver)
    }

    override fun onCleared() {
        liveData.removeObserver(votesObserver)
        super.onCleared()
    }

    override suspend fun generateSnippets(data: DivisionWithVotes): List<Snippet> = listOfNotNull(

    )

    fun getVoteChartData(division: Division) = listOf(
        BarChartCategory(division.ayes, colorCompat(R.color.vote_aye), "Ayes"),
        BarChartCategory(division.abstentions ?: 0, colorCompat(R.color.vote_abstain), "Abstentions"),
        BarChartCategory(division.didNotVote ?: 0, colorCompat(R.color.vote_didnotvote), "Did not vote/not present"),
        BarChartCategory(division.noes, colorCompat(R.color.vote_no), "Noes"),
    ).filter { it.value > 0 }

    suspend fun sortedVotes(votes: List<Vote>): List<Vote> = votes.sortedWith(
        compareBy({ it.voteType }, { it.memberName })
    )
}
