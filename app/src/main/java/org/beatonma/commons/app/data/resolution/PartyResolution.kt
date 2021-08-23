package org.beatonma.commons.app.data.resolution

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.beatonma.commons.BuildConfig.*
import org.beatonma.commons.R
import org.beatonma.commons.core.ParliamentID
import java.util.*

private fun getFuzzyNames(partyId: ParliamentID): Array<String> = when (partyId) {
    // Alliance
    PARTY_ALLIANCE_PARLIAMENTDOTUK -> arrayOf(
        "alliance",
    )

    // ChangeUK
    PARTY_THE_INDEPENDENT_GROUP_FOR_CHANGE_PARLIAMENTDOTUK -> arrayOf(
        "change",
        "change uk",
        "tig",
    )

    // Conservative
    PARTY_CONSERVATIVE_PARLIAMENTDOTUK -> arrayOf(
        "con",
        "conservative",
        "conservative party",
        "conservatives",
        "tory",
        "tories",
    )

    // DUP
    PARTY_DEMOCRATIC_UNIONIST_PARTY_PARLIAMENTDOTUK -> arrayOf(
        "dup",
        "democratic unionist party",
    )

    // Green
    PARTY_GREEN_PARTY_PARLIAMENTDOTUK -> arrayOf(
        "green",
        "greens",
        "green party",
    )

    // Labour
    PARTY_LABOUR_PARLIAMENTDOTUK -> arrayOf(
        "lab",
        "labour",
        "labour party",
    )

    // Labour Co-op
    PARTY_LABOUR_COOP_PARLIAMENTDOTUK -> arrayOf(
        "lab coop",
        "lab co-op",
        "labour cooperative",
        "labour co-operative",
    )

    // Liberal Democrat
    PARTY_LIBERAL_DEMOCRAT_PARLIAMENTDOTUK -> arrayOf(
        "ld",
        "lib dem",
        "liberal democrat",
        "liberal democrats",
    )

    // Plaid Cymru
    PARTY_PLAID_CYMRU_PARLIAMENTDOTUK -> arrayOf(
        "plaid",
        "plaid cymru",
    )

    // Respect
    PARTY_RESPECT_PARLIAMENTDOTUK -> arrayOf(
        "respect",
    )

    // SDLP
    PARTY_SOCIAL_DEMOCRATIC_LABOUR_PARTY_PARLIAMENTDOTUK -> arrayOf(
        "sdlp",
        "social democratic and labour party",
    )

    // SDP
    PARTY_SOCIAL_DEMOCRATIC_PARTY_PARLIAMENTDOTUK -> arrayOf(
        "sdp",
        "social democratic party",
    )

    // Sinn Fein
    PARTY_SINN_FEIN_PARLIAMENTDOTUK -> arrayOf(
        "sinn fein",
        "sinn féin",
    )

    // SNP
    PARTY_SCOTTISH_NATIONAL_PARTY_PARLIAMENTDOTUK -> arrayOf(
        "snp",
        "scottish national party",
    )
    // Speaker
    PARTY_SPEAKER_PARLIAMENTDOTUK -> arrayOf(
        "speaker",
    )

    // UKIP
    PARTY_UK_INDEPENDENCE_PARTY_PARLIAMENTDOTUK -> arrayOf(
        "ukip",
    )

    // UUP
    PARTY_ULSTER_UNIONIST_PARTY_PARLIAMENTDOTUK -> arrayOf(
        "uup",
    )

    // Default
    else -> arrayOf()
}


/**
 * Given an inexact/unofficial/shorthand party name, try to return the ParliamentID of the
 * party it refers to.
 */
object PartyResolution {
    fun getPartyId(fuzzyName: String): ParliamentID {
        val lowerFuzzyName = fuzzyName.lowercase(Locale.ROOT)
        arrayOf(
            PARTY_ALLIANCE_PARLIAMENTDOTUK,
            PARTY_THE_INDEPENDENT_GROUP_FOR_CHANGE_PARLIAMENTDOTUK,
            PARTY_CONSERVATIVE_PARLIAMENTDOTUK,
            PARTY_DEMOCRATIC_UNIONIST_PARTY_PARLIAMENTDOTUK,
            PARTY_GREEN_PARTY_PARLIAMENTDOTUK,
            PARTY_LABOUR_PARLIAMENTDOTUK,
            PARTY_LABOUR_COOP_PARLIAMENTDOTUK,
            PARTY_LIBERAL_DEMOCRAT_PARLIAMENTDOTUK,
            PARTY_PLAID_CYMRU_PARLIAMENTDOTUK,
            PARTY_RESPECT_PARLIAMENTDOTUK,
            PARTY_SOCIAL_DEMOCRATIC_LABOUR_PARTY_PARLIAMENTDOTUK,
            PARTY_SOCIAL_DEMOCRATIC_PARTY_PARLIAMENTDOTUK,
            PARTY_SINN_FEIN_PARLIAMENTDOTUK,
            PARTY_SCOTTISH_NATIONAL_PARTY_PARLIAMENTDOTUK,
            PARTY_SPEAKER_PARLIAMENTDOTUK,
            PARTY_UK_INDEPENDENCE_PARTY_PARLIAMENTDOTUK,
            PARTY_ULSTER_UNIONIST_PARTY_PARLIAMENTDOTUK,
        ).forEach { party ->
            if (lowerFuzzyName in getFuzzyNames(party)) {
                return party
            }
        }
        return -1
    }

    @Composable
    fun getPartyName(fuzzyName: String): String = when (getPartyId(fuzzyName)) {
        PARTY_THE_INDEPENDENT_GROUP_FOR_CHANGE_PARLIAMENTDOTUK -> stringResource(R.string.party_change_uk)
        PARTY_CONSERVATIVE_PARLIAMENTDOTUK -> stringResource(R.string.party_conservative)
        PARTY_DEMOCRATIC_UNIONIST_PARTY_PARLIAMENTDOTUK -> stringResource(R.string.party_dup)
        PARTY_GREEN_PARTY_PARLIAMENTDOTUK -> stringResource(R.string.party_green)
        PARTY_LABOUR_PARLIAMENTDOTUK -> stringResource(R.string.party_labour)
        PARTY_LABOUR_COOP_PARLIAMENTDOTUK -> stringResource(R.string.party_labour_coop)
        PARTY_LIBERAL_DEMOCRAT_PARLIAMENTDOTUK -> stringResource(R.string.party_lib_dem)
        PARTY_PLAID_CYMRU_PARLIAMENTDOTUK -> stringResource(R.string.party_plaid_cymru)
        PARTY_SOCIAL_DEMOCRATIC_LABOUR_PARTY_PARLIAMENTDOTUK -> stringResource(R.string.party_sdlp)
        PARTY_SINN_FEIN_PARLIAMENTDOTUK -> stringResource(R.string.party_sinn_fein)
        PARTY_SCOTTISH_NATIONAL_PARTY_PARLIAMENTDOTUK -> stringResource(R.string.party_snp)
        PARTY_SPEAKER_PARLIAMENTDOTUK -> stringResource(R.string.party_speaker)
        PARTY_UK_INDEPENDENCE_PARTY_PARLIAMENTDOTUK -> stringResource(R.string.party_ukip)
        PARTY_ULSTER_UNIONIST_PARTY_PARLIAMENTDOTUK -> stringResource(R.string.party_uup)
        else -> fuzzyName
    }
}
