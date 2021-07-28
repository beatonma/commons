package org.beatonma.commons.data.core.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.data.FlowList
import org.beatonma.commons.data.core.interfaces.markAccessed
import org.beatonma.commons.data.core.interfaces.markAllAccessed
import org.beatonma.commons.data.core.room.dao.shared.SharedConstituencyDao
import org.beatonma.commons.data.core.room.dao.shared.SharedElectionDao
import org.beatonma.commons.data.core.room.dao.shared.SharedPartyDao
import org.beatonma.commons.data.core.room.entities.constituency.Constituency
import org.beatonma.commons.data.core.room.entities.member.CommitteeChairship
import org.beatonma.commons.data.core.room.entities.member.CommitteeMemberWithChairs
import org.beatonma.commons.data.core.room.entities.member.CommitteeMembership
import org.beatonma.commons.data.core.room.entities.member.Experience
import org.beatonma.commons.data.core.room.entities.member.FinancialInterest
import org.beatonma.commons.data.core.room.entities.member.HistoricalConstituency
import org.beatonma.commons.data.core.room.entities.member.HistoricalConstituencyWithElection
import org.beatonma.commons.data.core.room.entities.member.HouseMembership
import org.beatonma.commons.data.core.room.entities.member.MemberProfile
import org.beatonma.commons.data.core.room.entities.member.Party
import org.beatonma.commons.data.core.room.entities.member.PartyAssociation
import org.beatonma.commons.data.core.room.entities.member.PartyAssociationWithParty
import org.beatonma.commons.data.core.room.entities.member.PhysicalAddress
import org.beatonma.commons.data.core.room.entities.member.Post
import org.beatonma.commons.data.core.room.entities.member.ResolvedZeitgeistMember
import org.beatonma.commons.data.core.room.entities.member.TopicOfInterest
import org.beatonma.commons.data.core.room.entities.member.WebAddress
import org.beatonma.commons.data.core.room.entities.member.ZeitgeistMember

@Dao
interface MemberDao: SharedPartyDao, SharedConstituencyDao, SharedElectionDao {

    // Get operations
    @Transaction
    @Query("""SELECT * FROM zeitgeist_members""")
    fun getZeitgeistMembers(): FlowList<ResolvedZeitgeistMember>

    @Query("""SELECT * FROM member_profiles WHERE member_profiles.parliamentdotuk = :memberId""")
    fun getMemberProfile(memberId: ParliamentID): Flow<MemberProfile>

    @Query("""SELECT * FROM constituencies
        LEFT JOIN member_profiles ON constituency_id = constituency_parliamentdotuk 
        WHERE parliamentdotuk = :memberId""")
    fun getConstituency(memberId: ParliamentID): Flow<Constituency>

    @Query("""SELECT * FROM parties
        LEFT JOIN member_profiles ON party_id = party_parliamentdotuk 
        WHERE parliamentdotuk = :memberId""")
    fun getParty(memberId: ParliamentID): Flow<Party>

    @Query("""SELECT * FROM physical_addresses WHERE physical_addresses.paddr_member_id = :memberId""")
    fun getPhysicalAddresses(memberId: ParliamentID): FlowList<PhysicalAddress>

    @Query("""SELECT * FROM weblinks WHERE weblinks.waddr_member_id = :memberId""")
    fun getWebAddresses(memberId: ParliamentID): FlowList<WebAddress>

    @Query("""SELECT * FROM posts WHERE post_member_id = :memberId""")
    fun getPosts(memberId: ParliamentID): FlowList<Post>

    @Query("""SELECT * FROM house_memberships WHERE house_member_id = :memberId""")
    fun getHouseMemberships(memberId: ParliamentID): FlowList<HouseMembership>

    @Query("""SELECT * FROM financial_interests WHERE interest_member_id = :memberId""")
    fun getFinancialInterests(memberId: ParliamentID): FlowList<FinancialInterest>

    @Query("""SELECT * FROM experiences WHERE experience_member_id = :memberId""")
    fun getExperiences(memberId: ParliamentID): FlowList<Experience>

    @Query("""SELECT * FROM topics_of_interest WHERE topic_member_id = :memberId""")
    fun getTopicsOfInterest(memberId: ParliamentID): FlowList<TopicOfInterest>

    @Transaction
    @Query("""SELECT * FROM committee_memberships WHERE committee_member_id = :memberId""")
    fun getCommitteeMembershipWithChairship(memberId: ParliamentID): FlowList<CommitteeMemberWithChairs>

    @Transaction
    @Query("""SELECT * FROM historic_constituencies WHERE memberfor_member_id = :memberId""")
    fun getHistoricalConstituencies(memberId: ParliamentID): FlowList<HistoricalConstituencyWithElection>

    @Transaction
    @Query("""SELECT * FROM party_associations WHERE partyacc_member_id = :memberId""")
    fun getPartyAssociations(memberId: ParliamentID): FlowList<PartyAssociationWithParty>

    // Insert operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhysicalAddresses(webaddresses: List<PhysicalAddress>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWebAddresses(webaddresses: List<WebAddress>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertZeitgeistMembers(zeitgeistMembers: List<ZeitgeistMember>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: MemberProfile)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProfileIfNotExists(profile: MemberProfile)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfiles(people: List<MemberProfile>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProfilesIfNotExists(people: List<MemberProfile>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosts(posts: List<Post>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCommitteeMemberships(committeeMemberships: List<CommitteeMembership>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCommitteeChairships(committeeChairships: List<CommitteeChairship>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHouseMemberships(houseMemberships: List<HouseMembership>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFinancialInterests(financialInterests: List<FinancialInterest>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExperiences(experiences: List<Experience>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTopicsOfInterest(topicOfInterests: List<TopicOfInterest>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemberForConstituencies(memberForConstituencies: List<HistoricalConstituency>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPartyAssociations(partyAssociations: List<PartyAssociation>)

    /**
     * Retrieve the profile and update its accessed_at timestamp.
     */
    suspend fun getMemberProfileTimestampedFlow(parliamentdotuk: ParliamentID): Flow<MemberProfile> =
        getMemberProfile(parliamentdotuk).also {
            safeInsertProfile(it.first())
        }

    @Transaction
    suspend fun safeInsertProfile(profile: MemberProfile?, ifNotExists: Boolean = false) {
        profile ?: return

        // The embedded party/constituency are low-detail so we do not want to overwrite any
        // high-detail records we might already hold.
        if (profile.constituency != null) {
            insertConstituencyIfNotExists(profile.constituency)
        }
        insertPartyIfNotExists(profile.party)

        if (ifNotExists) {
            insertProfileIfNotExists(profile.markAccessed())
        }
        else {
            insertProfile(profile.markAccessed())
        }
    }

    @Transaction
    suspend fun safeInsertProfiles(profiles: List<MemberProfile>, ifNotExists: Boolean = false) {
        insertConstituenciesIfNotExists(profiles.mapNotNull { it.constituency })
        insertPartiesIfNotExists(profiles.map { it.party })

        if (ifNotExists) {
            insertProfilesIfNotExists(profiles.markAllAccessed())
        }
        else {
            insertProfiles(profiles.markAllAccessed())
        }
    }
}
