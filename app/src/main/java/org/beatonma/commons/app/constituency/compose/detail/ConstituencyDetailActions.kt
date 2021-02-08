package org.beatonma.commons.app.constituency.compose.detail

import org.beatonma.commons.data.core.room.entities.constituency.Constituency
import org.beatonma.commons.data.core.room.entities.election.ConstituencyResultWithDetails

internal typealias ConstituencyMemberAction = (result: ConstituencyResultWithDetails) -> Unit
internal typealias ConstituencyResultsAction = (constituency: Constituency, result: ConstituencyResultWithDetails) -> Unit

internal class ConstituencyDetailActions(
    val onMemberClick: ConstituencyMemberAction = {},
    val onConstituencyResultsClick: ConstituencyResultsAction = { _, _ -> }
)
