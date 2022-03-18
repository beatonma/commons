package org.beatonma.commons.app.ui.screens.frontpage

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import org.beatonma.commons.data.core.room.entities.ZeitgeistContent
import org.beatonma.commons.repo.models.Zeitgeist
import org.beatonma.commons.repo.repository.ZeitgeistRepository
import org.beatonma.commons.repo.result.BaseResult
import org.beatonma.commons.repo.result.Success
import javax.inject.Inject

@HiltViewModel
class ZeitgeistViewModel @Inject constructor(
    zeitgeistRepository: ZeitgeistRepository,
) : ViewModel() {
    val zeitgeist: Flow<BaseResult<Zeitgeist, Throwable>> =
        zeitgeistRepository.getZeitgeist().transform { v: BaseResult<Zeitgeist, Throwable> ->
            when (v) {
                is Success -> {
                    val zeitgeist = v.data

                    emit(
                        Success(
                            Zeitgeist(
                                zeitgeistShuffler(zeitgeist.members),
                                zeitgeistShuffler(zeitgeist.divisions),
                                zeitgeistShuffler(zeitgeist.bills),
                            ),
                            v.message,
                        )
                    )
                }
                else -> emit(v)
            }
        }

    /**
     * Reorder zeitgeist content lists while respecting priority settings.
     */
    private fun <T : ZeitgeistContent> zeitgeistShuffler(content: List<T>): List<T> =
        content.shuffled().sortedByDescending { it.priority }
}
