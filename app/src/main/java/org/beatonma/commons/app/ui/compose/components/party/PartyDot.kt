package org.beatonma.commons.app.ui.compose.components.party

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.beatonma.commons.app.ui.colors.partyTheme
import org.beatonma.commons.app.ui.compose.components.Dot
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.data.core.room.entities.member.Party

@Composable
fun PartyDot(
    party: Party?,
    modifier: Modifier = Modifier,
    size: Dp = 8.dp,
    shape: Shape = CircleShape,
) =
    PartyDot(
        party?.parliamentdotuk ?: -1,
        modifier.semantics {
            contentDescription = party?.name ?: ""
        },
        size,
        shape
    )

@Composable
fun PartyDot(
    parliamentID: ParliamentID,
    modifier: Modifier = Modifier,
    size: Dp = 8.dp,
    shape: Shape = CircleShape,
) =
    Dot(
        color = partyTheme(parliamentID).primary,
        modifier,
        size,
        shape
    )
