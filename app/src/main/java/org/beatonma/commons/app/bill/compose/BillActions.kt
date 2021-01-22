package org.beatonma.commons.app.bill.compose

import org.beatonma.commons.data.core.room.entities.bill.BillSponsorWithParty

class BillActions(
    val onSponsorClick: (BillSponsorWithParty) -> Unit = {},
)
