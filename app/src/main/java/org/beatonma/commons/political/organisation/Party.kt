@file:JvmName("Party")

package org.beatonma.commons.political.organisation

import android.content.Context
import androidx.core.content.ContextCompat
import org.beatonma.commons.R
import org.beatonma.commons.kotlin.data.Color
import org.beatonma.commons.ui.svg.*
import org.beatonma.lib.util.kotlin.extensions.colorCompat

val partyNames get() =
    arrayListOf(
        CONSERVATIVE,
        DUP,
        GREEN,
        INDEPENDENT,
        LABOUR,
        LABOUR_COOP,
        LIB_DEM,
        PLAID_CYMRU,
        SDLP,
        SINN_FEIN,
        SNP,
        UKIP,
        UUP
    )

const val CONSERVATIVE = "Conservative"
const val DUP = "DUP"
const val GREEN = "Green"
const val INDEPENDENT = "Independent"
const val LABOUR = "Labour"
const val LABOUR_COOP = "Labour/Co-operative"
const val LIB_DEM = "Liberal Democrat"
const val PLAID_CYMRU = "Plaid Cymru"
const val SDLP = "Social Democratic and Labour Party"
const val SINN_FEIN = "Sinn Féin"  //"Sinn F&eacute;in"
const val SINN_FEIN_SIMPLE = "Sinn Fein"
const val SNP = "Scottish National Party"
const val UKIP = "UKIP"
const val UUP = "UUP"
const val SPEAKER = "Speaker"

// Party primary color resource IDs
const val DEFAULT_COLOR_PRIMARY = R.color.party_default
const val CONSERVATIVE_COLOR_PRIMARY = R.color.party_conservative
const val DUP_COLOR_PRIMARY = R.color.party_dup
const val GREEN_COLOR_PRIMARY = R.color.party_green
const val INDEPENDENT_COLOR_PRIMARY = R.color.party_independent
const val LABOUR_COLOR_PRIMARY = R.color.party_labour
const val LABOUR_COOP_COLOR_PRIMARY = R.color.party_labour_coop
const val LIB_DEM_COLOR_PRIMARY = R.color.party_lib_dem
const val PLAID_CYMRU_COLOR_PRIMARY = R.color.party_plaid_cymru
const val SDLP_COLOR_PRIMARY = R.color.party_sdlp
const val SINN_FEIN_COLOR_PRIMARY = R.color.party_sinn_fein
const val SNP_COLOR_PRIMARY = R.color.party_snp
const val UKIP_COLOR_PRIMARY = R.color.party_ukip
const val UUP_COLOR_PRIMARY = R.color.party_uup
const val SPEAKER_COLOR_PRIMARY  = R.color.party_speaker

// Party accent color resource IDs
const val DEFAULT_COLOR_ACCENT = R.color.party_accent_default
const val CONSERVATIVE_COLOR_ACCENT = R.color.party_accent_conservative
const val DUP_COLOR_ACCENT = R.color.party_accent_dup
const val GREEN_COLOR_ACCENT = R.color.party_accent_green
const val INDEPENDENT_COLOR_ACCENT = R.color.party_accent_independent
const val LABOUR_COLOR_ACCENT = R.color.party_accent_labour
const val LABOUR_COOP_COLOR_ACCENT = R.color.party_accent_labour_coop
const val LIB_DEM_COLOR_ACCENT = R.color.party_accent_lib_dem
const val PLAID_CYMRU_COLOR_ACCENT = R.color.party_accent_plaid_cymru
const val SDLP_COLOR_ACCENT = R.color.party_accent_sdlp
const val SINN_FEIN_COLOR_ACCENT = R.color.party_accent_sinn_fein
const val SNP_COLOR_ACCENT = R.color.party_accent_snp
const val UKIP_COLOR_ACCENT = R.color.party_accent_ukip
const val UUP_COLOR_ACCENT = R.color.party_accent_uup
const val SPEAKER_COLOR_ACCENT  = R.color.party_accent_speaker

data class PartyColors(val primary: Color, val accent: Color, val text: Color)
private fun Context.color(resID: Int) = ContextCompat.getColor(this, resID)
private fun Context.partyColors(primaryRes: Int, accentRes: Int, textRes: Int): PartyColors =
    PartyColors(
        Color(colorCompat(primaryRes)),
        Color(colorCompat(accentRes)),
        Color(colorCompat(textRes)))

/**
 * Get theming information for a given party.
 */
fun Context.getPartyColor(party: String): PartyColors {
    val textColor = when (party) {
        LIB_DEM, SNP -> R.color.TextPrimaryDark
        else -> R.color.TextPrimaryLight
    }

    return when (party) {
        CONSERVATIVE -> partyColors(CONSERVATIVE_COLOR_PRIMARY, CONSERVATIVE_COLOR_ACCENT, textColor)
        DUP -> partyColors(DUP_COLOR_PRIMARY, DUP_COLOR_ACCENT, textColor)
        GREEN -> partyColors(GREEN_COLOR_PRIMARY, GREEN_COLOR_ACCENT, textColor)
        INDEPENDENT -> partyColors(INDEPENDENT_COLOR_PRIMARY, INDEPENDENT_COLOR_ACCENT, textColor)
        LABOUR -> partyColors(LABOUR_COLOR_PRIMARY, LABOUR_COLOR_ACCENT, textColor)
        LABOUR_COOP -> partyColors(LABOUR_COOP_COLOR_PRIMARY, LABOUR_COOP_COLOR_ACCENT, textColor)
        LIB_DEM -> partyColors(LIB_DEM_COLOR_PRIMARY, LIB_DEM_COLOR_ACCENT, textColor)
        PLAID_CYMRU -> partyColors(PLAID_CYMRU_COLOR_PRIMARY, PLAID_CYMRU_COLOR_ACCENT, textColor)
        SDLP -> partyColors(SDLP_COLOR_PRIMARY, SDLP_COLOR_ACCENT, textColor)
        SINN_FEIN -> partyColors(SINN_FEIN_COLOR_PRIMARY, SINN_FEIN_COLOR_ACCENT, textColor)
        SNP -> partyColors(SNP_COLOR_PRIMARY, SNP_COLOR_ACCENT, textColor)
        UKIP -> partyColors(UKIP_COLOR_PRIMARY, UKIP_COLOR_ACCENT, textColor)
        UUP -> partyColors(UUP_COLOR_PRIMARY, UUP_COLOR_ACCENT, textColor)
        SPEAKER -> partyColors(SPEAKER_COLOR_PRIMARY, SPEAKER_COLOR_ACCENT, textColor)
        else -> partyColors(DEFAULT_COLOR_PRIMARY, DEFAULT_COLOR_ACCENT, textColor)
    }
}

fun getPartySvg(party: String) =
    when (party) {
        CONSERVATIVE -> ConservativeSvg()
        DUP -> DupSvg()
        GREEN -> GreenSvg()
        INDEPENDENT -> DefaultSvg()
        LABOUR -> LabourSvg()
        LABOUR_COOP -> LabourCoopSvg()
        LIB_DEM -> LibDemSvg()
        PLAID_CYMRU -> PlaidCymruSvg()
        SDLP -> SdlpSvg()
        SINN_FEIN -> SinnFeinSvg()
        SNP -> SnpSvg()
        UKIP -> UkipSvg()
        UUP -> UupSvg()
        SPEAKER -> DefaultSvg()
        else -> DefaultSvg()
}
