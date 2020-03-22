package org.beatonma.commons.app.featured

import androidx.lifecycle.ViewModel
import org.beatonma.commons.data.core.CommonsRepository
import javax.inject.Inject

class FeaturedContentViewModel @Inject constructor(
    repository: CommonsRepository
): ViewModel() {
    val featuredPeople = repository.observeFeaturedPeople()
}
