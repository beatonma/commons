package org.beatonma.commons.data.core.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.beatonma.commons.data.core.Converters
import org.beatonma.commons.data.core.room.dao.*
import org.beatonma.commons.data.core.room.entities.bill.*
import org.beatonma.commons.data.core.room.entities.constituency.Constituency
import org.beatonma.commons.data.core.room.entities.constituency.ConstituencyBoundary
import org.beatonma.commons.data.core.room.entities.division.Division
import org.beatonma.commons.data.core.room.entities.division.FeaturedDivision
import org.beatonma.commons.data.core.room.entities.division.Vote
import org.beatonma.commons.data.core.room.entities.member.*
import org.beatonma.commons.data.core.room.entities.user.UserToken

@Database(
    entities = [
        // Member
        MemberProfile::class,
        CommitteeChairship::class,
        CommitteeMembership::class,
        Election::class,
        Experience::class,
        FeaturedMember::class,
        FinancialInterest::class,
        HistoricalConstituency::class,
        HouseMembership::class,
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

        // Constituency
        Constituency::class,
        ConstituencyBoundary::class,

        // Divisions
        Division::class,
        FeaturedDivision::class,
        Vote::class,

        // Common/Generic
        ParliamentarySession::class,

        // User
        UserToken::class,
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class CommonsDatabase : RoomDatabase() {
    abstract fun memberDao(): MemberDao
    abstract fun billDao(): BillDao
    abstract fun constituencyDao(): ConstituencyDao
    abstract fun divisionDao(): DivisionDao
    abstract fun userDao(): UserDao
}

const val COMMONS_DB_FILENAME = "commons.db"
