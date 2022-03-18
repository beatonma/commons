package org.beatonma.commons.app.ui.screens.division.lords

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import org.beatonma.commons.app.ui.screens.division.DivisionViewModel
import org.beatonma.commons.core.House
import org.beatonma.commons.core.LordsVoteType
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.data.core.room.entities.division.LordsDivision
import org.beatonma.commons.repo.repository.DivisionRepository
import org.beatonma.commons.repo.result.IoResult
import org.beatonma.commons.snommoc.models.social.SocialTarget
import org.beatonma.commons.snommoc.models.social.SocialTargetType
import javax.inject.Inject

@HiltViewModel
class LordsDivisionViewModel @Inject constructor(
    repository: DivisionRepository,
    savedStateHandle: SavedStateHandle,
) : DivisionViewModel<LordsDivision, LordsVoteType>(repository, savedStateHandle) {
    override val house get() = House.lords
    override val socialTarget: SocialTarget
        get() = SocialTarget(
            targetType = SocialTargetType.division_lords,
            parliamentdotuk = divisionID,
        )

    override val repoFunc: DivisionRepository.(ParliamentID) -> Flow<IoResult<LordsDivision>>
        get() = DivisionRepository::getLordsDivision

    override fun applySorting(division: LordsDivision): LordsDivision = division.copy(
        votes = division.votes.sortedVotes()
    )
}
