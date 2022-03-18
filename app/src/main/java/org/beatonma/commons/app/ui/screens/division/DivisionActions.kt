package org.beatonma.commons.app.ui.screens.division

import org.beatonma.commons.core.ParliamentID

internal typealias MemberVoteAction = (memberId: ParliamentID) -> Unit

class DivisionActions(
    val onMemberClick: MemberVoteAction = {},
)
