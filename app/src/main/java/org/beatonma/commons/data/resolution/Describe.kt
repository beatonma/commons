package org.beatonma.commons.data.resolution

import android.content.Context
import android.util.Log
import org.beatonma.commons.R
import org.beatonma.commons.data.core.interfaces.Named
import org.beatonma.commons.data.core.room.entities.member.*
import org.beatonma.commons.kotlin.extensions.dump
import org.beatonma.commons.kotlin.extensions.stringCompat

/**
 * Helper for getting more verbose descriptions of various object types
 */
fun <T: Any> Context.describe(obj: T?): String = when (obj) {
    is Named -> obj.description(this)
    is House -> obj.description(this)
    null -> ""
    else -> {
        Log.d("ctx.describe", "Unable to provide description for object of class=${obj.javaClass.canonicalName}")
        "ctx.describe"
    }
}


fun House.description(context: Context) = when(this) {
    House.commons -> context.stringCompat(R.string.house_of_commons)
    House.lords -> context.stringCompat(R.string.house_of_lords)
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
