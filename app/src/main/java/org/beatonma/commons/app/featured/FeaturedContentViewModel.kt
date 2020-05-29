package org.beatonma.commons.app.featured

import androidx.lifecycle.ViewModel
import org.beatonma.commons.data.core.repository.CommonsRepository
import javax.inject.Inject

class FeaturedContentViewModel @Inject constructor(
    repository: CommonsRepository
): ViewModel() {
    val featuredPeople = repository.observeFeaturedPeople()
    val featuredBills = repository.observeFeaturedBills()
    val featuredDivisions = repository.observeFeaturedDivisions()

    val motd = repository.observeMotd()
}
