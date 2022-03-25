package org.beatonma.commons.app.ui.screens.frontpage

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.beatonma.commons.app.ui.base.IoLiveDataViewModel
import org.beatonma.commons.data.core.room.entities.ZeitgeistContent
import org.beatonma.commons.repo.models.Zeitgeist
import org.beatonma.commons.repo.repository.ZeitgeistRepository
import org.beatonma.commons.repo.result.Success
import org.beatonma.commons.repo.result.map
import org.beatonma.commons.repo.result.onSuccess
import javax.inject.Inject

@HiltViewModel
class ZeitgeistViewModel @Inject constructor(
    private val repository: ZeitgeistRepository,
) : IoLiveDataViewModel<Zeitgeist>() {
    init {
        loadZeitgeist()
    }

    fun loadZeitgeist() {
        viewModelScope.launch {
            repository.getZeitgeist()
                .collectLatest { result ->
                    result
                        .map { zeitgeist ->
                            Success(
                                Zeitgeist(
                                    zeitgeistShuffler(zeitgeist.members),
                                    zeitgeistShuffler(zeitgeist.divisions),
                                    zeitgeistShuffler(zeitgeist.bills),
                                )
                            )
                        }
                        .onSuccess(this@ZeitgeistViewModel::postValue)
                }
        }
    }

    /**
     * Reorder zeitgeist content lists while respecting priority settings.
     */
    private fun <T : ZeitgeistContent> zeitgeistShuffler(content: List<T>): List<T> =
        content.shuffled().sortedByDescending { it.priority }
}
