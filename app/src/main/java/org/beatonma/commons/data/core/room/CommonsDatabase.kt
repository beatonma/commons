package org.beatonma.commons.data.core.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.beatonma.commons.data.core.Converters
import org.beatonma.commons.data.core.room.dao.BillDao
import org.beatonma.commons.data.core.room.dao.MemberDao
import org.beatonma.commons.data.core.room.entities.bill.*
import org.beatonma.commons.data.core.room.entities.member.*

const val COMMONS_DB_FILENAME = "commons.db"

@Database(
    entities = [
        // Member
        MemberProfile::class,
        CommitteeChairship::class,
        CommitteeMembership::class,
        Constituency::class,
        Election::class,
        Experience::class,
        FeaturedMember::class,
        FinancialInterest::class,
        HouseMembership::class,
        HistoricalConstituency::class,
        Party::class,
        PartyAssociation::class,
        PhysicalAddress::class,
        Post::class,
        TopicOfInterest::class,
        Town::class,
        WebAddress::class,

        // Bills
        Bill::class,
        BillPublication::class,
        BillSponsor::class,
        BillStage::class,
        BillStageSitting::class,
        BillType::class,
        FeaturedBill::class,

        // Common/Generic
        ParliamentarySession::class,
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class CommonsDatabase : RoomDatabase() {

    abstract fun memberDao(): MemberDao
    abstract fun billDao(): BillDao
}
