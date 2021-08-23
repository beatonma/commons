package org.beatonma.commons.app.ui.components.party

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import org.beatonma.commons.app.ui.colors.PartyColors
import org.beatonma.commons.app.ui.colors.partyTheme
import org.beatonma.commons.data.core.room.entities.member.NoParty
import org.beatonma.commons.data.core.room.entities.member.Party

val LocalPartyTheme: ProvidableCompositionLocal<PartyWithTheme> = compositionLocalOf {
    error("Party not set")
}

data class PartyWithTheme(val party: Party, val theme: PartyColors) {
    val primary = theme.primary
    val onPrimary = theme.onPrimary
    val accent = theme.accent
    val onAccent = theme.onAccent
}

@Composable
fun partyWithTheme(party: Party?): PartyWithTheme {
    val resolvedParty = party ?: NoParty
    return PartyWithTheme(resolvedParty, resolvedParty.theme())
}

@Composable
fun Party.theme(): PartyColors = partyTheme(parliamentdotuk = parliamentdotuk)
