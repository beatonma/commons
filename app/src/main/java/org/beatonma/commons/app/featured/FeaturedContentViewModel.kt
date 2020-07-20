package org.beatonma.commons.app.featured

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import org.beatonma.commons.data.core.repository.FeaturedRepository
import org.beatonma.commons.data.core.repository.SnommocRepository

class FeaturedContentViewModel @ViewModelInject constructor(
    snommocRepository: SnommocRepository,
    featuredRepository: FeaturedRepository,
): ViewModel() {
    val featuredPeople = featuredRepository.getFeaturedPeople().asLiveData()
    val featuredBills = featuredRepository.getFeaturedBills().asLiveData()
    val featuredDivisions = featuredRepository.getFeaturedDivisionsFlow().asLiveData()

    val motd = snommocRepository.observeMotd()
}
