package org.beatonma.commons.app.bill.compose

import org.beatonma.commons.data.core.room.entities.bill.BillSponsorWithParty

internal typealias SponsorAction = (BillSponsorWithParty) -> Unit

class BillActions(
    val onSponsorClick: SponsorAction = {},
)
