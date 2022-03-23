package org.beatonma.commons.app.ui.screens.division.shared

import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.beatonma.commons.core.DivisionVoteType
import org.beatonma.commons.theme.color.color
import org.beatonma.commons.theme.icon


@Composable
internal fun <T : DivisionVoteType> DivisionVoteIcon(
    vote: T,
    modifier: Modifier = Modifier,
) {
    Icon(
        vote.icon,
        contentDescription = null,
        tint = vote.color,
        modifier = modifier
    )
}
