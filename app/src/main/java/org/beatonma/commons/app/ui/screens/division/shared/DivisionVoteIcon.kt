package org.beatonma.commons.app.ui.screens.division.shared

import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import org.beatonma.commons.core.DivisionVoteType
import org.beatonma.commons.theme.color.color
import org.beatonma.commons.theme.icon
import org.beatonma.commons.themed.themedSize


@Composable
internal fun <T : DivisionVoteType> DivisionVoteIcon(
    vote: T,
    modifier: Modifier = Modifier,
) {
    DivisionVoteIcon(vote.icon, vote.color, modifier)
}

@Composable
private fun DivisionVoteIcon(
    icon: ImageVector,
    tint: Color,
    modifier: Modifier,
) {
    Icon(
        icon,
        contentDescription = null,
        tint = tint,
        modifier = modifier
            .size(themedSize.IconSmall)
    )
}
