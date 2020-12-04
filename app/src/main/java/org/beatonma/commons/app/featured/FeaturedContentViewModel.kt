package org.beatonma.commons.app.featured

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import org.beatonma.commons.repo.repository.SnommocRepository
import org.beatonma.commons.repo.repository.ZeitgeistRepository
import org.beatonma.commons.repo.result.IoResult
import org.beatonma.commons.snommoc.models.MessageOfTheDay

class FeaturedContentViewModel @ViewModelInject constructor(
    snommocRepository: SnommocRepository,
    zeitgeistRepository: ZeitgeistRepository,
) : ViewModel() {
    val zeitgeist = zeitgeistRepository.getZeitgeist()
    val motd: LiveData<IoResult<List<MessageOfTheDay>>> = snommocRepository.getMotd().asLiveData()
}
