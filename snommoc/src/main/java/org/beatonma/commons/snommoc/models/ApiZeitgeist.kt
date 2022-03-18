package org.beatonma.commons.snommoc.models

import com.squareup.moshi.Json
import org.beatonma.commons.core.House
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.core.ZeitgeistReason
import org.beatonma.commons.snommoc.Contract
import java.time.LocalDate

private typealias ZeitgeistItems<T> = List<ZeitgeistItem<T>>

data class ApiZeitgeist(
    @field:Json(name = Contract.MOTD) val motd: List<MessageOfTheDay>,
    @field:Json(name = Contract.ZEITGEIST_PEOPLE) val people: ZeitgeistItems<ApiMemberProfile>,
    @field:Json(name = Contract.ZEITGEIST_DIVISIONS) val divisions: ZeitgeistItems<ApiZeitgeistDivision>,
    @field:Json(name = Contract.ZEITGEIST_BILLS) val bills: ZeitgeistItems<ApiBill>,
)

data class ApiZeitgeistDivision(
    @field:Json(name = Contract.PARLIAMENTDOTUK) val parliamentdotuk: ParliamentID,
    @field:Json(name = Contract.TITLE) val title: String,
    @field:Json(name = Contract.DATE) val date: LocalDate,
    @field:Json(name = Contract.PASSED) val passed: Boolean,
    @field:Json(name = Contract.HOUSE) val house: House,
)

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
