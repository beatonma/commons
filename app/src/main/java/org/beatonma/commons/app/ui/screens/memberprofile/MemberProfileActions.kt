package org.beatonma.commons.app.ui.screens.memberprofile

import org.beatonma.commons.data.core.room.entities.constituency.Constituency

internal typealias ConstituencyAction = (Constituency) -> Unit

data class MemberProfileActions(
    val onConstituencyClick: ConstituencyAction = {},
)
