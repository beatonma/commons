package org.beatonma.commons.app.memberprofile

import org.beatonma.commons.data.core.room.entities.constituency.Constituency

internal typealias ConstituencyAction = (Constituency) -> Unit

data class MemberProfileActions(
    val onConstituencyClick: ConstituencyAction = {},
)
