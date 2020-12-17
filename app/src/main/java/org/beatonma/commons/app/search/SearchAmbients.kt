package org.beatonma.commons.app.search.compose

import androidx.compose.runtime.ProvidableAmbient
import androidx.compose.runtime.ambientOf
import org.beatonma.commons.app.search.SearchActions

val AmbientSearchActions: ProvidableAmbient<SearchActions> =
    ambientOf { error("SearchActions have not been registered") }
