package org.beatonma.commons.app.ui.screens.bill

import org.beatonma.commons.data.core.room.entities.bill.BillSponsorWithProfile

internal typealias SponsorAction = (BillSponsorWithProfile) -> Unit

class BillActions(
    val onSponsorClick: SponsorAction = {},
)
