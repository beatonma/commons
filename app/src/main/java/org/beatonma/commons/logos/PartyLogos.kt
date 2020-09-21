package org.beatonma.commons.logos

import org.beatonma.commons.BuildConfig.*
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.svg.VectorGraphic

object PartyLogos {

    fun get(partyId: ParliamentID): VectorGraphic = when (partyId) {
        PARTY_ALLIANCE_PARLIAMENTDOTUK -> AllianceLogo()
        PARTY_CONSERVATIVE_PARLIAMENTDOTUK -> ConservativeLogo()
        PARTY_DEMOCRATIC_UNIONIST_PARTY_PARLIAMENTDOTUK -> DupLogo()
        PARTY_LABOUR_PARLIAMENTDOTUK -> LabourLogo()
        PARTY_LIBERAL_DEMOCRAT_PARLIAMENTDOTUK -> LibDemLogo()
        PARTY_GREEN_PARTY_PARLIAMENTDOTUK -> GreenLogo()
        PARTY_LABOUR_COOP_PARLIAMENTDOTUK -> LabourCoopLogo()
        PARTY_PLAID_CYMRU_PARLIAMENTDOTUK -> PlaidCymruLogo()
        PARTY_SCOTTISH_NATIONAL_PARTY_PARLIAMENTDOTUK -> SnpLogo()
        PARTY_SOCIAL_DEMOCRATIC_LABOUR_PARTY_PARLIAMENTDOTUK -> SdlpLogo()
        PARTY_SINN_FEIN_PARLIAMENTDOTUK -> SinnFeinLogo()
        PARTY_ULSTER_UNIONIST_PARTY_PARLIAMENTDOTUK -> UupLogo()
        else -> DefaultLogo()
    }.apply { buildPaths() }

    fun all() = listOf(
        PARTY_ALLIANCE_PARLIAMENTDOTUK,
        PARTY_CONSERVATIVE_PARLIAMENTDOTUK,
        PARTY_LABOUR_PARLIAMENTDOTUK,
        PARTY_LABOUR_COOP_PARLIAMENTDOTUK,
        PARTY_LIBERAL_DEMOCRAT_PARLIAMENTDOTUK,
        PARTY_DEMOCRATIC_UNIONIST_PARTY_PARLIAMENTDOTUK,
        PARTY_INDEPENDENT_PARLIAMENTDOTUK,
        PARTY_GREEN_PARTY_PARLIAMENTDOTUK,
        PARTY_PLAID_CYMRU_PARLIAMENTDOTUK,
        PARTY_SCOTTISH_NATIONAL_PARTY_PARLIAMENTDOTUK,
        PARTY_SOCIAL_DEMOCRATIC_LABOUR_PARTY_PARLIAMENTDOTUK,
        PARTY_SINN_FEIN_PARLIAMENTDOTUK,
        PARTY_ULSTER_UNIONIST_PARTY_PARLIAMENTDOTUK,
    ).map(::get)
}
