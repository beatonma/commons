package org.beatonma.commons.ukparliament.models

import com.squareup.moshi.Json
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.ukparliament.api.Contract

data class UkApiBillPublication(
    @field:Json(name = Contract.BILL_PUBLICATION_URL) val url: String?,
    @field:Json(name = Contract.BILL_PUBLICATION_CONTENT_TYPE) val contentType: String,
    @field:Json(name = Contract.BILL_PUBLICATION_CONTENT_LENGTH) val contentLength: Int,
    @field:Json(name = Contract.BILL_ABOUT) val about: String,
) {
    val parliamentdotuk: ParliamentID
        get() = about.split("/").last().toInt()
}
