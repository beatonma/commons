package org.beatonma.commons.data.resolution

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.screens.bill.viewmodel.BillStageCategory
import org.beatonma.commons.core.House
import org.beatonma.commons.core.VoteType
import org.beatonma.commons.core.extensions.dump
import org.beatonma.commons.core.extensions.withNotNull
import org.beatonma.commons.data.core.interfaces.Named
import org.beatonma.commons.data.core.room.entities.member.CommitteeMemberWithChairs
import org.beatonma.commons.data.core.room.entities.member.Experience
import org.beatonma.commons.data.core.room.entities.member.FinancialInterest
import org.beatonma.commons.data.core.room.entities.member.HistoricalConstituencyWithElection
import org.beatonma.commons.data.core.room.entities.member.HouseMembership
import org.beatonma.commons.data.core.room.entities.member.MemberProfile
import org.beatonma.commons.data.core.room.entities.member.PartyAssociationWithParty
import org.beatonma.commons.data.core.room.entities.member.Post


@Composable
fun House.uiDescription(): String = when (this) {
    House.commons -> stringResource(R.string.house_of_commons)
    House.lords -> stringResource(R.string.house_of_lords)
}

@Composable
fun BillStageCategory.uiDescription(): String = when (this) {
    BillStageCategory.Commons -> House.commons.uiDescription()
    BillStageCategory.Lords -> House.lords.uiDescription()
    BillStageCategory.ConsiderationOfAmendments -> stringResource(R.string.bill_category_consideration)
    BillStageCategory.RoyalAssent -> stringResource(R.string.bill_category_royal_assent)
}

@Composable
fun VoteType.uiDescription(): String = when (this) {
    VoteType.AyeVote -> stringResource(R.string.division_votetype_aye)
    VoteType.NoVote -> stringResource(R.string.division_votetype_no)
    VoteType.Abstains -> stringResource(R.string.division_votetype_abstain)
    VoteType.SuspendedOrExpelledVote -> stringResource(R.string.division_votetype_suspended_or_expelled)
    VoteType.DidNotVote -> stringResource(R.string.division_votetype_did_not_vote)
}


/**
 * Helper for getting more verbose descriptions of [Named] instances.
 */
@Composable
fun Named.uiDescription(): String = when (this) {
    is CommitteeMemberWithChairs -> {
        if (chairs.isNotEmpty()) stringResource(
            R.string.named_description_committee_chairship,
            name
        )
        else stringResource(R.string.named_description_committee_member, name)
    }
    is Experience -> {
        val castableOrganisation = organisation
        if (castableOrganisation == null) name
        else stringResource(R.string.named_description_experience, name, castableOrganisation)
    }
    is HistoricalConstituencyWithElection -> stringResource(
        R.string.named_description_constituency_mp,
        name
    )
    is HouseMembership -> stringResource(R.string.named_description_house_membership, name)
    is PartyAssociationWithParty -> stringResource(R.string.named_description_party_member, name)

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
fun MemberProfile.currentPostUiDescription(): String {
    withNotNull(currentPost) { return it }

    return when {
        active == false -> ""
        isLord == true || constituency == null -> House.lords.uiDescription()
        isMp == true -> House.commons.uiDescription()
        else -> ""
    }
}
