package org.beatonma.commons.data.resolution

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.res.stringResource
import org.beatonma.commons.R
import org.beatonma.commons.core.House
import org.beatonma.commons.core.extensions.dump
import org.beatonma.commons.data.core.interfaces.Named
import org.beatonma.commons.data.core.room.entities.member.CommitteeMemberWithChairs
import org.beatonma.commons.data.core.room.entities.member.Experience
import org.beatonma.commons.data.core.room.entities.member.FinancialInterest
import org.beatonma.commons.data.core.room.entities.member.HistoricalConstituencyWithElection
import org.beatonma.commons.data.core.room.entities.member.HouseMembership
import org.beatonma.commons.data.core.room.entities.member.PartyAssociationWithParty
import org.beatonma.commons.data.core.room.entities.member.Post
import org.beatonma.commons.kotlin.extensions.stringCompat

@Composable
fun House.description(): String = when (this) {
    House.commons -> stringResource(R.string.house_of_commons)
    House.lords -> stringResource(R.string.house_of_lords)
}


/**
 * Helper for getting more verbose descriptions of [Named] instances.
 */
fun Named.description(context: Context): String = when (this) {
    is CommitteeMemberWithChairs -> {
        if (chairs.isNotEmpty()) context.stringCompat(R.string.named_description_committee_chairship, name)
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
fun Named.description(): String = this.description(AmbientContext.current)
