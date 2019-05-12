package org.beatonma.commons.political.organisation

import android.content.Context
import org.beatonma.commons.R
import org.beatonma.commons.kotlin.data.Color
import org.beatonma.commons.ui.svg.*
import org.beatonma.lib.util.kotlin.extensions.colorCompat

val partyNames
    get() =
        arrayListOf(
            CHANGE_UK,
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

const val CHANGE_UK = "Change UK"
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


data class PartyColors(private val primaryRes: Int, private val accentRes: Int, private val textRes: Int) {
    var primary: Color? = null
    var accent: Color? = null
    var text: Color? = null

    fun resolve(context: Context) {
        primary = Color(context.colorCompat(primaryRes))
        accent = Color(context.colorCompat(accentRes))
        text = Color(context.colorCompat(textRes))
    }
}

fun getParty(name: String) = when(name) {
    CHANGE_UK -> ChangeUk()
    CONSERVATIVE -> Conservative()
    DUP -> Dup()
    GREEN -> Green()
    INDEPENDENT -> Independent()
    LABOUR -> Labour()
    LABOUR_COOP -> LabourCoop()
    LIB_DEM -> LibDem()
    PLAID_CYMRU -> PlaidCymru()
    SDLP -> Sdlp()
    SINN_FEIN -> SinnFein()
    SNP -> Snp()
    UKIP -> Ukip()
    UUP -> Uup()
    SPEAKER -> Speaker()
    else -> null
}

fun getPartySvg(party: String) = when (party) {
    CONSERVATIVE -> ConservativeSvg()
    CHANGE_UK -> DefaultSvg()  // TODO
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

interface Party {
    val id: String  // Name as used by TWFY API
    val name: Int  // Common name
    val shortName: Int  // Initials or abbreviation
    val longName: Int  // Full name
    val partyColors: PartyColors  // Color theme

    fun svg() = getPartySvg(id)
}

class ChangeUk : Party {
    override val id = CHANGE_UK
    override val name = R.string.party_change_uk
    override val shortName = R.string.party_short_change_uk
    override val longName = R.string.party_long_change_uk
    override val partyColors by lazy {
        PartyColors(R.color.party_change_uk, R.color.party_accent_change_uk, R.color.TextPrimaryDark)
    }
}

class Conservative : Party {
    override val id = CONSERVATIVE
    override val name = R.string.party_conservative
    override val shortName = R.string.party_short_conservative
    override val longName = R.string.party_long_conservative
    override val partyColors by lazy {
        PartyColors(R.color.party_conservative, R.color.party_accent_conservative, R.color.TextPrimaryLight)
    }
}

class Dup : Party {
    override val id = DUP
    override val name = R.string.party_dup
    override val shortName = R.string.party_short_dup
    override val longName = R.string.party_long_dup
    override val partyColors by lazy {
        PartyColors(R.color.party_dup, R.color.party_accent_dup, R.color.TextPrimaryLight)
    }
}

class Green : Party {
    override val id = GREEN
    override val name = R.string.party_green
    override val shortName = R.string.party_short_green
    override val longName = R.string.party_long_green
    override val partyColors by lazy {
        PartyColors(R.color.party_green, R.color.party_accent_green, R.color.TextPrimaryLight)
    }
}

class Independent : Party {
    override val id = INDEPENDENT
    override val name = R.string.party_independent
    override val shortName = R.string.party_short_independent
    override val longName = R.string.party_long_independent
    override val partyColors by lazy {
        PartyColors(R.color.party_independent, R.color.party_accent_independent, R.color.TextPrimaryLight)
    }
}

class Labour : Party {
    override val id = LABOUR
    override val name = R.string.party_labour
    override val shortName = R.string.party_short_labour
    override val longName = R.string.party_long_labour
    override val partyColors by lazy {
        PartyColors(R.color.party_labour, R.color.party_accent_labour, R.color.TextPrimaryLight)
    }
}

class LabourCoop : Party {
    override val id = LABOUR_COOP
    override val name = R.string.party_labour_coop
    override val shortName = R.string.party_short_labour_coop
    override val longName = R.string.party_long_labour_coop
    override val partyColors by lazy {
        PartyColors(R.color.party_labour_coop, R.color.party_accent_labour_coop, R.color.TextPrimaryLight)
    }
}

class LibDem : Party {
    override val id = LIB_DEM
    override val name = R.string.party_lib_dem
    override val shortName = R.string.party_short_lib_dem
    override val longName = R.string.party_long_lib_dem
    override val partyColors by lazy {
        PartyColors(R.color.party_lib_dem, R.color.party_accent_lib_dem, R.color.TextPrimaryDark)
    }
}

class PlaidCymru : Party {
    override val id = PLAID_CYMRU
    override val name = R.string.party_plaid_cymru
    override val shortName = R.string.party_short_plaid_cymru
    override val longName = R.string.party_long_plaid_cymru
    override val partyColors by lazy {
        PartyColors(R.color.party_plaid_cymru, R.color.party_accent_plaid_cymru, R.color.TextPrimaryLight)
    }
}

class Sdlp : Party {
    override val id = SDLP
    override val name = R.string.party_sdlp
    override val shortName = R.string.party_short_sdlp
    override val longName = R.string.party_long_sdlp
    override val partyColors by lazy {
        PartyColors(R.color.party_sdlp, R.color.party_accent_sdlp, R.color.TextPrimaryLight)
    }
}

class SinnFein : Party {
    override val id = SINN_FEIN
    override val name = R.string.party_sinn_fein
    override val shortName = R.string.party_short_sinn_fein
    override val longName = R.string.party_long_sinn_fein
    override val partyColors by lazy {
        PartyColors(R.color.party_sinn_fein, R.color.party_accent_sinn_fein, R.color.TextPrimaryLight)
    }
}

class Snp : Party {
    override val id = SNP
    override val name = R.string.party_snp
    override val shortName = R.string.party_short_snp
    override val longName = R.string.party_long_snp
    override val partyColors by lazy {
        PartyColors(R.color.party_snp, R.color.party_accent_snp, R.color.TextPrimaryDark)
    }
}

class Ukip : Party {
    override val id = UKIP
    override val name = R.string.party_ukip
    override val shortName = R.string.party_short_ukip
    override val longName = R.string.party_long_ukip
    override val partyColors by lazy {
        PartyColors(R.color.party_ukip, R.color.party_accent_ukip, R.color.TextPrimaryLight)
    }
}

class Uup : Party {
    override val id = UUP
    override val name = R.string.party_uup
    override val shortName = R.string.party_short_uup
    override val longName = R.string.party_long_uup
    override val partyColors by lazy {
        PartyColors(R.color.party_uup, R.color.party_accent_uup, R.color.TextPrimaryLight)
    }
}

/**
 * Not really a party but reasonable to treat it as one, since the Speaker
 * renounces party affiliations when they get the role.
 */
class Speaker: Party {
    override val id = SPEAKER
    override val name = R.string.party_speaker
    override val shortName = R.string.party_short_speaker
    override val longName = R.string.party_long_speaker
    override val partyColors by lazy {
        PartyColors(R.color.party_speaker, R.color.party_accent_speaker, R.color.TextPrimaryLight)
    }
}
