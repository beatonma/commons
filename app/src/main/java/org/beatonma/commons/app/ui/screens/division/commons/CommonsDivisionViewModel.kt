package org.beatonma.commons.app.ui.screens.division.commons

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import org.beatonma.commons.app.ui.screens.division.DivisionViewModel
import org.beatonma.commons.core.House
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.core.VoteType
import org.beatonma.commons.data.core.room.entities.division.CommonsDivision
import org.beatonma.commons.repo.repository.DivisionRepository
import org.beatonma.commons.repo.result.IoResult
import org.beatonma.commons.snommoc.models.social.SocialTarget
import org.beatonma.commons.snommoc.models.social.SocialTargetType
import javax.inject.Inject

@HiltViewModel
class CommonsDivisionViewModel @Inject constructor(
    repository: DivisionRepository,
    savedStateHandle: SavedStateHandle,
) : DivisionViewModel<CommonsDivision, VoteType>(repository, savedStateHandle) {
    override val house get() = House.commons
    override val socialTarget: SocialTarget
        get() = SocialTarget(
            targetType = SocialTargetType.division_commons,
            parliamentdotuk = divisionID,
        )

    override val repoFunc: DivisionRepository.(ParliamentID) -> Flow<IoResult<CommonsDivision>>
        get() = DivisionRepository::getCommonsDivision

    override fun applySorting(division: CommonsDivision): CommonsDivision = division.copy(
        votes = division.votes.sortedVotes()
    )
}
