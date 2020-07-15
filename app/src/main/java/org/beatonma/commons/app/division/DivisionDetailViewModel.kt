package org.beatonma.commons.app.division

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import dagger.hilt.android.qualifiers.ApplicationContext
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.BaseIoAndroidViewModel
import org.beatonma.commons.app.ui.views.BarChartCategory
import org.beatonma.commons.data.core.repository.DivisionRepository
import org.beatonma.commons.data.core.room.entities.division.Division
import org.beatonma.commons.data.core.room.entities.division.DivisionWithVotes
import org.beatonma.commons.data.core.room.entities.division.VoteWithParty
import org.beatonma.commons.kotlin.extensions.BundledDivision
import org.beatonma.commons.kotlin.extensions.colorCompat

class DivisionDetailViewModel @ViewModelInject constructor(
    private val repository: DivisionRepository,
    @ApplicationContext context: Context,
) : BaseIoAndroidViewModel<DivisionWithVotes>(context) {

    fun forDivision(bundled: BundledDivision) {
        liveData = repository.observeDivision(bundled.house, bundled.parliamentdotuk)
    }

    fun getVoteChartData(division: Division) = listOf(
        BarChartCategory(division.ayes, colorCompat(R.color.vote_aye), "Ayes"),
        BarChartCategory(division.abstentions ?: 0, colorCompat(R.color.vote_abstain), "Abstentions"),
        BarChartCategory(division.didNotVote ?: 0, colorCompat(R.color.vote_didnotvote), "Did not vote/not present"),
        BarChartCategory(division.noes, colorCompat(R.color.vote_no), "Noes"),
    ).filter { it.value > 0 }

    suspend fun sortedVotes(votes: List<VoteWithParty>): List<VoteWithParty> = votes.sortedWith(
        compareBy(
            { it.vote.voteType },
            { it.vote.memberName }
        )
    )
}
