package org.beatonma.commons.app.featured

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import org.beatonma.commons.data.IoResult
import org.beatonma.commons.data.core.MessageOfTheDay
import org.beatonma.commons.data.core.repository.FeaturedRepository
import org.beatonma.commons.data.core.repository.SnommocRepository

class FeaturedContentViewModel @ViewModelInject constructor(
    snommocRepository: SnommocRepository,
    featuredRepository: FeaturedRepository,
) : ViewModel() {

    val featuredPeople =
        featuredRepository.getFeaturedPeople()
            .asLiveData()

    val featuredBills =
        featuredRepository.getFeaturedBills()
            .asLiveData()

    val featuredDivisions =
        featuredRepository.getFeaturedDivisions()
            .asLiveData()

    val motd: LiveData<IoResult<List<MessageOfTheDay>>> = snommocRepository.getMotd().asLiveData()
}
