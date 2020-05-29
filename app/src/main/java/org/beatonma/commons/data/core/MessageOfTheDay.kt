package org.beatonma.commons.data.core

import com.squareup.moshi.Json


data class MessageOfTheDay(
    @field:Json(name = "title") val title: String,
    @field:Json(name = "description") val description: String?,
    @field:Json(name = "url") val url: String?,  // Optional link
)
