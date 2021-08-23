package org.beatonma.commons.app.ui.screens.frontpage

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.beatonma.commons.repo.repository.ZeitgeistRepository
import javax.inject.Inject

@HiltViewModel
class ZeitgeistViewModel @Inject constructor(
    zeitgeistRepository: ZeitgeistRepository,
) : ViewModel() {
    val zeitgeist = zeitgeistRepository.getZeitgeist()
}
