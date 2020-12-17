package org.beatonma.commons.app.search

import androidx.compose.runtime.ProvidableAmbient
import androidx.compose.runtime.ambientOf

val AmbientSearchActions: ProvidableAmbient<SearchActions> =
    ambientOf { error("SearchActions have not been registered") }
