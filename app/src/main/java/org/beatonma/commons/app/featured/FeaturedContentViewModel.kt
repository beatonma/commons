package org.beatonma.commons.app.featured

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import org.beatonma.commons.data.core.repository.FeaturedRepository
import org.beatonma.commons.data.core.repository.SnommocRepository

class FeaturedContentViewModel @ViewModelInject constructor(
    snommocRepository: SnommocRepository,
    featuredRepository: FeaturedRepository,
): ViewModel() {
    val featuredPeople = featuredRepository.observeFeaturedPeople()
    val featuredBills = featuredRepository.observeFeaturedBills()
    val featuredDivisions = featuredRepository.observeFeaturedDivisions()

    val motd = snommocRepository.observeMotd()
}
