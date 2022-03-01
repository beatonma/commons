package org.beatonma.commons.data.core.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.beatonma.commons.data.core.Converters
import org.beatonma.commons.data.core.room.dao.BillDao
import org.beatonma.commons.data.core.room.dao.ConstituencyDao
import org.beatonma.commons.data.core.room.dao.DivisionDao
import org.beatonma.commons.data.core.room.dao.MemberCleanupDao
import org.beatonma.commons.data.core.room.dao.MemberDao
import org.beatonma.commons.data.core.room.dao.UserDao
import org.beatonma.commons.data.core.room.entities.bill.BillData
import org.beatonma.commons.data.core.room.entities.bill.BillPublicationData
import org.beatonma.commons.data.core.room.entities.bill.BillPublicationLink
import org.beatonma.commons.data.core.room.entities.bill.BillSponsorData
import org.beatonma.commons.data.core.room.entities.bill.BillStage
import org.beatonma.commons.data.core.room.entities.bill.BillType
import org.beatonma.commons.data.core.room.entities.bill.BillXPublication
import org.beatonma.commons.data.core.room.entities.bill.BillXSession
import org.beatonma.commons.data.core.room.entities.bill.BillXStage
import org.beatonma.commons.data.core.room.entities.bill.ParliamentarySession
import org.beatonma.commons.data.core.room.entities.bill.ZeitgeistBill
import org.beatonma.commons.data.core.room.entities.constituency.Constituency
import org.beatonma.commons.data.core.room.entities.constituency.ConstituencyBoundary
import org.beatonma.commons.data.core.room.entities.constituency.ConstituencyCandidate
import org.beatonma.commons.data.core.room.entities.constituency.ConstituencyElectionDetails
import org.beatonma.commons.data.core.room.entities.division.Division
import org.beatonma.commons.data.core.room.entities.division.Vote
import org.beatonma.commons.data.core.room.entities.division.ZeitgeistDivision
import org.beatonma.commons.data.core.room.entities.election.ConstituencyResult
import org.beatonma.commons.data.core.room.entities.election.Election
import org.beatonma.commons.data.core.room.entities.member.CommitteeChairship
import org.beatonma.commons.data.core.room.entities.member.CommitteeMembership
import org.beatonma.commons.data.core.room.entities.member.Experience
import org.beatonma.commons.data.core.room.entities.member.FinancialInterest
import org.beatonma.commons.data.core.room.entities.member.HistoricalConstituency
import org.beatonma.commons.data.core.room.entities.member.HouseMembership
import org.beatonma.commons.data.core.room.entities.member.MemberProfile
import org.beatonma.commons.data.core.room.entities.member.Party
import org.beatonma.commons.data.core.room.entities.member.PartyAssociation
import org.beatonma.commons.data.core.room.entities.member.PhysicalAddress
import org.beatonma.commons.data.core.room.entities.member.Post
import org.beatonma.commons.data.core.room.entities.member.TopicOfInterest
import org.beatonma.commons.data.core.room.entities.member.Town
import org.beatonma.commons.data.core.room.entities.member.WebAddress
import org.beatonma.commons.data.core.room.entities.member.ZeitgeistMember
import org.beatonma.commons.data.core.room.entities.user.UserToken

@Database(
    entities = [
        // Member
        MemberProfile::class,
        CommitteeChairship::class,
        CommitteeMembership::class,
        Experience::class,
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
        ZeitgeistMember::class,

        // Bills
        BillData::class,
        BillPublicationData::class,
        BillPublicationLink::class,
        BillXPublication::class,
        BillXSession::class,
        BillXStage::class,
        BillSponsorData::class,
        BillStage::class,
        BillType::class,
        ParliamentarySession::class,
        ZeitgeistBill::class,

        // Constituency
        Constituency::class,
        ConstituencyBoundary::class,
        ConstituencyElectionDetails::class,
        ConstituencyCandidate::class,

        // Elections
        Election::class,
        ConstituencyResult::class,

        // Divisions
        Division::class,
        ZeitgeistDivision::class,
        Vote::class,

        // User
        UserToken::class,
    ],
    version = 1,
)
@TypeConverters(Converters::class)
abstract class CommonsDatabase : RoomDatabase() {
    abstract fun memberDao(): MemberDao
    abstract fun billDao(): BillDao
    abstract fun constituencyDao(): ConstituencyDao
    abstract fun divisionDao(): DivisionDao
    abstract fun userDao(): UserDao

    abstract fun memberCleanupDao(): MemberCleanupDao
}

const val COMMONS_DB_FILENAME = "commons.db"
