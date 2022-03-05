package org.beatonma.commons.app.ui.screens.bill

import org.beatonma.commons.data.core.room.entities.member.MemberProfile

internal typealias SponsorAction = (MemberProfile) -> Unit

class BillActions(
    val onClickProfile: SponsorAction = {},
)
