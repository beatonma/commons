package org.beatonma.commons.app.ui.screens.division

import org.beatonma.commons.data.core.room.entities.division.Vote

internal typealias MemberVoteAction = (Vote) -> Unit

class DivisionActions(
    val onMemberClick: MemberVoteAction = {},
)
