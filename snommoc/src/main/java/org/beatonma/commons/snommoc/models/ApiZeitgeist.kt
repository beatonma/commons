package org.beatonma.commons.snommoc.models

import com.squareup.moshi.Json
import org.beatonma.commons.snommoc.Contract

typealias ZeitgeistItems<T> = List<ZeitgeistItem<T>>

data class ApiZeitgeist(
    @field:Json(name = Contract.MOTD) val motd: List<MessageOfTheDay>,
    @field:Json(name = Contract.ZEITGEIST_PEOPLE) val people: ZeitgeistItems<ApiMemberProfile>,
    @field:Json(name = Contract.ZEITGEIST_DIVISIONS) val divisions: ZeitgeistItems<ApiDivision>,
    @field:Json(name = Contract.ZEITGEIST_BILLS) val bills: ZeitgeistItems<ApiBill>,
)

@Suppress("EnumEntryName") // Lowercase names to match API values
enum class ZeitgeistReason {
    feature,
    social,
    unspecified,
    ;
}

data class ZeitgeistItem<T>(
    @field:Json(name = Contract.ZEITGEIST_REASON) val reason: ZeitgeistReason?,
    @field:Json(name = Contract.ZEITGEIST_PRIORITY) val priority: Int,
    @field:Json(name = Contract.ZEITGEIST_TARGET) val target: T,
)

data class MessageOfTheDay(
    @field:Json(name = Contract.TITLE) val title: String,
    @field:Json(name = Contract.DESCRIPTION) val description: String?,
    @field:Json(name = Contract.URL) val url: String?,
)
