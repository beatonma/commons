package org.beatonma.commons.ui.colors

import android.content.Context
import org.beatonma.commons.BuildConfig.*
import org.beatonma.commons.data.core.room.entities.member.Party
import org.beatonma.commons.isNightMode
import org.beatonma.commons.kotlin.data.Color

interface Themed {
    var theme: PartyColors?
}

fun Party.getTheme(context: Context?): PartyColors {
    val theme = getPartyTheme(parliamentdotuk)
    return if (context?.isNightMode() == true) theme.coerce(
        minSaturation = .2F,
        maxSaturation = .8F,
        minLuminance = .2F,
        maxLuminance = .8F
    )
    else theme

}

private fun getPartyTheme(partyID: Int): PartyColors = when (getCanonicalParty(partyID)) {
    // Alliance
    PARTY_ALLIANCE_PARLIAMENTDOTUK -> PartyColors(
        COLOR_PARTY_ALLIANCE_PRIMARY,
        COLOR_PARTY_ALLIANCE_ACCENT,
        COLOR_PARTY_ALLIANCE_PRIMARY_TEXT,
        COLOR_PARTY_ALLIANCE_ACCENT_TEXT
    )

    // ChangeUK
    PARTY_THE_INDEPENDENT_GROUP_FOR_CHANGE_PARLIAMENTDOTUK -> PartyColors(
        COLOR_PARTY_CHANGEUK_PRIMARY,
        COLOR_PARTY_CHANGEUK_ACCENT,
        COLOR_PARTY_CHANGEUK_PRIMARY_TEXT,
        COLOR_PARTY_CHANGEUK_ACCENT_TEXT
    )

    // Conservative
    PARTY_CONSERVATIVE_PARLIAMENTDOTUK -> PartyColors(
        COLOR_PARTY_CONSERVATIVE_PRIMARY,
        COLOR_PARTY_CONSERVATIVE_ACCENT,
        COLOR_PARTY_CONSERVATIVE_PRIMARY_TEXT,
        COLOR_PARTY_CONSERVATIVE_ACCENT_TEXT
    )

    // DUP
    PARTY_DEMOCRATIC_UNIONIST_PARTY_PARLIAMENTDOTUK -> PartyColors(
        COLOR_PARTY_DUP_PRIMARY,
        COLOR_PARTY_DUP_ACCENT,
        COLOR_PARTY_DUP_PRIMARY_TEXT,
        COLOR_PARTY_DUP_ACCENT_TEXT
    )

    // Green
    PARTY_GREEN_PARTY_PARLIAMENTDOTUK -> PartyColors(
        COLOR_PARTY_GREEN_PRIMARY,
        COLOR_PARTY_GREEN_ACCENT,
        COLOR_PARTY_GREEN_PRIMARY_TEXT,
        COLOR_PARTY_GREEN_ACCENT_TEXT
    )

    // Labour
    PARTY_LABOUR_PARLIAMENTDOTUK -> PartyColors(
        COLOR_PARTY_LABOUR_PRIMARY,
        COLOR_PARTY_LABOUR_ACCENT,
        COLOR_PARTY_LABOUR_PRIMARY_TEXT,
        COLOR_PARTY_LABOUR_ACCENT_TEXT
    )

    // Labour Co-op
    PARTY_LABOUR_COOP_PARLIAMENTDOTUK -> PartyColors(
        COLOR_PARTY_LABOURCOOP_PRIMARY,
        COLOR_PARTY_LABOURCOOP_ACCENT,
        COLOR_PARTY_LABOURCOOP_PRIMARY_TEXT,
        COLOR_PARTY_LABOURCOOP_ACCENT_TEXT
    )

    // Liberal Democrat
    PARTY_LIBERAL_DEMOCRAT_PARLIAMENTDOTUK -> PartyColors(
        COLOR_PARTY_LIBDEM_PRIMARY,
        COLOR_PARTY_LIBDEM_ACCENT,
        COLOR_PARTY_LIBDEM_PRIMARY_TEXT,
        COLOR_PARTY_LIBDEM_ACCENT_TEXT
    )

    // Plaid Cymru
    PARTY_PLAID_CYMRU_PARLIAMENTDOTUK -> PartyColors(
        COLOR_PARTY_PLAIDCYMRU_PRIMARY,
        COLOR_PARTY_PLAIDCYMRU_ACCENT,
        COLOR_PARTY_PLAIDCYMRU_PRIMARY_TEXT,
        COLOR_PARTY_PLAIDCYMRU_ACCENT_TEXT
    )

    // Respect
    PARTY_RESPECT_PARLIAMENTDOTUK -> PartyColors(
        COLOR_PARTY_RESPECT_PRIMARY,
        COLOR_PARTY_RESPECT_ACCENT,
        COLOR_PARTY_RESPECT_PRIMARY_TEXT,
        COLOR_PARTY_RESPECT_ACCENT_TEXT
    )

    // SDLP
    PARTY_SOCIAL_DEMOCRATIC_LABOUR_PARTY_PARLIAMENTDOTUK -> PartyColors(
        COLOR_PARTY_SDLP_PRIMARY,
        COLOR_PARTY_SDLP_ACCENT,
        COLOR_PARTY_SDLP_PRIMARY_TEXT,
        COLOR_PARTY_SDLP_ACCENT_TEXT
    )

    // SDP
    PARTY_SOCIAL_DEMOCRATIC_PARTY_PARLIAMENTDOTUK -> PartyColors(
        COLOR_PARTY_SDP_PRIMARY,
        COLOR_PARTY_SDP_ACCENT,
        COLOR_PARTY_SDP_PRIMARY_TEXT,
        COLOR_PARTY_SDP_ACCENT_TEXT
    )

    // Sinn Fein
    PARTY_SINN_FEIN_PARLIAMENTDOTUK -> PartyColors(
        COLOR_PARTY_SINNFEIN_PRIMARY,
        COLOR_PARTY_SINNFEIN_ACCENT,
        COLOR_PARTY_SINNFEIN_PRIMARY_TEXT,
        COLOR_PARTY_SINNFEIN_ACCENT_TEXT
    )

    // SNP
    PARTY_SCOTTISH_NATIONAL_PARTY_PARLIAMENTDOTUK -> PartyColors(
        COLOR_PARTY_SNP_PRIMARY,
        COLOR_PARTY_SNP_ACCENT,
        COLOR_PARTY_SNP_PRIMARY_TEXT,
        COLOR_PARTY_SNP_ACCENT_TEXT
    )

    // Speaker
    PARTY_SPEAKER_PARLIAMENTDOTUK -> PartyColors(
        COLOR_PARTY_SPEAKER_PRIMARY,
        COLOR_PARTY_SPEAKER_ACCENT,
        COLOR_PARTY_SPEAKER_PRIMARY_TEXT,
        COLOR_PARTY_SPEAKER_ACCENT_TEXT
    )

    // UKIP
    PARTY_UK_INDEPENDENCE_PARTY_PARLIAMENTDOTUK -> PartyColors(
        COLOR_PARTY_UKIP_PRIMARY,
        COLOR_PARTY_UKIP_ACCENT,
        COLOR_PARTY_UKIP_PRIMARY_TEXT,
        COLOR_PARTY_UKIP_ACCENT_TEXT
    )

    // UUP
    PARTY_ULSTER_UNIONIST_PARTY_PARLIAMENTDOTUK -> PartyColors(
        COLOR_PARTY_UUP_PRIMARY,
        COLOR_PARTY_UUP_ACCENT,
        COLOR_PARTY_UUP_PRIMARY_TEXT,
        COLOR_PARTY_UUP_ACCENT_TEXT
    )

    // Default
    else -> PartyColors(
        COLOR_PARTY_DEFAULT_PRIMARY,
        COLOR_PARTY_DEFAULT_ACCENT,
        COLOR_PARTY_DEFAULT_PRIMARY_TEXT,
        COLOR_PARTY_DEFAULT_ACCENT_TEXT
    )
}

data class PartyColors(
    val primary: Int,
    val accent: Int,
    val primaryText: Int,
    val accentText: Int
) {
    fun coerce(
        minSaturation: Float = 0F,
        maxSaturation: Float = 1F,
        minLuminance: Float = 0F,
        maxLuminance: Float = 1F,
    ): PartyColors = PartyColors(
        Color(primary).coerce(minSaturation, maxSaturation, minLuminance, maxLuminance).color,
        Color(accent).coerce(minSaturation, maxSaturation, minLuminance, maxLuminance).color,
        primaryText,
        accentText,
    )
}

/**
 * Map small/historic/niche/relatively unestablished/loosely associated parties, or parties with that
 * do not have particular colors branding of their own, to a more well known party of a similar-ish
 * 'flavour'.
 * e.g. Independent Conservative -> Conservative
 *
 * For color theme purposes only, to avoid making separate themes for all ~40 parties registered
 * on data.parliament.uk. Maybe we can do that later but this will do for now.
 *
 * Not trying to imply associations/endorsements. Feel free to make suggestions or pull requests
 * for anything you find questionable.
 */
private fun getCanonicalParty(partyID: Int): Int = when (partyID) {
    // Conservative
    PARTY_CONSERVATIVE_PARLIAMENTDOTUK,
    PARTY_CONSERVATIVE_INDEPENDENT_PARLIAMENTDOTUK,
    PARTY_INDEPENDENT_CONSERVATIVE_PARLIAMENTDOTUK,
    PARTY_NATIONAL_LIBERAL_CONSERVATIVE_PARLIAMENTDOTUK
    -> PARTY_CONSERVATIVE_PARLIAMENTDOTUK

    // Labour
    PARTY_LABOUR_PARLIAMENTDOTUK,
    PARTY_INDEPENDENT_LABOUR_PARLIAMENTDOTUK,
    PARTY_LABOUR_INDEPENDENT_PARLIAMENTDOTUK
    -> PARTY_LABOUR_PARLIAMENTDOTUK

    // Liberal Democrats
    PARTY_LIBERAL_DEMOCRAT_INDEPENDENT_PARLIAMENTDOTUK,
    PARTY_LIBERAL_PARLIAMENTDOTUK
    -> PARTY_LIBERAL_DEMOCRAT_PARLIAMENTDOTUK

    // SDP
    PARTY_INDEPENDENT_SOCIAL_DEMOCRAT_PARLIAMENTDOTUK
    -> PARTY_SOCIAL_DEMOCRATIC_PARTY_PARLIAMENTDOTUK

    // Sinn Fein
    PARTY_ANTI_H_BLOCK_PARLIAMENTDOTUK -> PARTY_SINN_FEIN_PARLIAMENTDOTUK

    // Speakers
    PARTY_LORD_SPEAKER_PARLIAMENTDOTUK
    -> PARTY_SPEAKER_PARLIAMENTDOTUK

    // UUP
    PARTY_INDEPENDENT_ULSTER_UNIONIST_PARLIAMENTDOTUK,
    PARTY_UNITED_KINGDOM_UNIONIST_PARLIAMENTDOTUK
    -> PARTY_ULSTER_UNIONIST_PARTY_PARLIAMENTDOTUK

    else -> partyID
}
