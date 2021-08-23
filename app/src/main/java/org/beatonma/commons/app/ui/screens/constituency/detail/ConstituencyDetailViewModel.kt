package org.beatonma.commons.app.ui.screens.constituency.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.beatonma.commons.app.data.SavedStateKey
import org.beatonma.commons.app.data.get
import org.beatonma.commons.app.data.set
import org.beatonma.commons.app.ui.base.IoLiveDataViewModel
import org.beatonma.commons.app.ui.base.SocialTargetProvider
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.data.core.room.entities.constituency.CompleteConstituency
import org.beatonma.commons.repo.repository.ConstituencyRepository
import org.beatonma.commons.snommoc.models.social.SocialTarget
import org.beatonma.commons.snommoc.models.social.SocialTargetType
import javax.inject.Inject

private val ConstituencyKey = SavedStateKey("constituency_id")

@HiltViewModel
class ConstituencyDetailViewModel @Inject constructor(
    private val repository: ConstituencyRepository,
    private val savedStateHandle: SavedStateHandle,
): IoLiveDataViewModel<CompleteConstituency>(), SocialTargetProvider {
    val constituencyID: ParliamentID get() = savedStateHandle[ConstituencyKey]!!

    override val socialTarget: SocialTarget
        get() = SocialTarget(
            SocialTargetType.constituency,
            parliamentdotuk = constituencyID
        )

    @OptIn(InternalCoroutinesApi::class)
    fun forConstituency(constituencyId: ParliamentID) {
        savedStateHandle[ConstituencyKey] = constituencyId

        viewModelScope.launch(Dispatchers.IO) {
            repository.getConstituency(constituencyID)
                .collect { result -> postValue(result) }
        }
    }
}
