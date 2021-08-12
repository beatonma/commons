package org.beatonma.commons.data.resolution

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import org.beatonma.commons.R
import org.beatonma.commons.app.bill.viewmodel.BillStageCategory
import org.beatonma.commons.core.House
import org.beatonma.commons.core.VoteType
import org.beatonma.commons.core.extensions.dump
import org.beatonma.commons.data.core.interfaces.Named
import org.beatonma.commons.data.core.room.entities.member.*
import org.beatonma.commons.kotlin.extensions.stringCompat

@Composable
fun House.description(): String = when (this) {
    House.commons -> stringResource(R.string.house_of_commons)
    House.lords -> stringResource(R.string.house_of_lords)
}

@Composable
fun BillStageCategory.description(): String = when (this) {
    BillStageCategory.Commons -> House.commons.description()
    BillStageCategory.Lords -> House.lords.description()
    BillStageCategory.ConsiderationOfAmendments -> stringResource(R.string.bill_category_consideration)
    BillStageCategory.RoyalAssent -> stringResource(R.string.bill_category_royal_assent)
}

@Composable
fun VoteType.description(): String = when (this) {
    VoteType.AyeVote -> stringResource(R.string.division_votetype_aye)
    VoteType.NoVote -> stringResource(R.string.division_votetype_no)
    VoteType.Abstains -> stringResource(R.string.division_votetype_abstain)
    VoteType.SuspendedOrExpelledVote -> stringResource(R.string.division_votetype_suspended_or_expelled)
    VoteType.DidNotVote -> stringResource(R.string.division_votetype_did_not_vote)
}


/**
 * Helper for getting more verbose descriptions of [Named] instances.
 */
fun Named.description(context: Context): String = when (this) {
    is CommitteeMemberWithChairs -> {
        if (chairs.isNotEmpty()) context.stringCompat(
            R.string.named_description_committee_chairship,
            name
        )
        else context.stringCompat(R.string.named_description_committee_member, name)
    }
    is Experience -> {
        if (organisation == null) name
        else context.stringCompat(R.string.named_description_experience, name, organisation)
    }
    is HistoricalConstituencyWithElection -> context.stringCompat(R.string.named_description_constituency_mp, name)
    is HouseMembership -> context.stringCompat(R.string.named_description_house_membership, name)
    is PartyAssociationWithParty -> context.stringCompat(R.string.named_description_party_member, name)

    // Classes that use the default handling
    is FinancialInterest,
    is Post,
    -> name

    else -> {
        this.javaClass.canonicalName.dump("no description for class ($name)")
        name
    }
}

@Composable
fun Named.description(): String = this.description(LocalContext.current)
