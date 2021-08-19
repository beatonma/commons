package org.beatonma.commons.app.division

import org.beatonma.commons.data.core.room.entities.division.Vote

internal typealias MemberVoteAction = (Vote) -> Unit

class DivisionActions(
    val onMemberClick: MemberVoteAction = {},
)
