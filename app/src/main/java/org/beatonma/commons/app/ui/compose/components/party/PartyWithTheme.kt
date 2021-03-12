package org.beatonma.commons.app.ui.compose.components.party

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import org.beatonma.commons.app.ui.colors.ComposePartyColors
import org.beatonma.commons.app.ui.colors.theme
import org.beatonma.commons.data.core.room.entities.member.NoParty
import org.beatonma.commons.data.core.room.entities.member.Party

val LocalPartyTheme: ProvidableCompositionLocal<PartyWithTheme> = compositionLocalOf {
    error("Party not set")
}

data class PartyWithTheme(val party: Party, val theme: ComposePartyColors)

@Composable
fun partyWithTheme(party: Party?): PartyWithTheme {
    val resolvedParty = party ?: NoParty
    return PartyWithTheme(resolvedParty, resolvedParty.theme())
}
