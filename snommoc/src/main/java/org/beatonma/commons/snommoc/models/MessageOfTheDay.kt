package org.beatonma.commons.snommoc.models

import com.squareup.moshi.Json
import org.beatonma.commons.snommoc.Contract

data class MessageOfTheDay(
    @field:Json(name = Contract.TITLE) val title: String,
    @field:Json(name = Contract.DESCRIPTION) val description: String?,
    @field:Json(name = Contract.URL) val url: String?,  // Optional link
)
