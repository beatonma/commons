package org.beatonma.commons.data.core.room.dao

import androidx.room.*
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
import org.beatonma.commons.data.core.room.entities.member.*

@Dao
interface MemberDao: SharedPartyDao, SharedConstituencyDao, SharedElectionDao {

    // Get operations
    @Transaction
    @Query("""SELECT * FROM featured_members""")
    fun getFeaturedProfiles(): FlowList<FeaturedMemberProfile>

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
    suspend fun insertFeaturedPeople(people: List<FeaturedMember>)

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
    suspend fun getMemberProfileTimestampedFlow(parliamentdotuk: ParliamentID): Flow<MemberProfile> {
        val result = getMemberProfile(parliamentdotuk)

        safeInsertProfile(result.first())

        return result
    }

    @Transaction
    suspend fun saveFeaturedPeople(featuredPeople: List<MemberProfile>) {
        safeInsertProfiles(featuredPeople, ifNotExists = true)

        insertFeaturedPeople(
            featuredPeople.map { profile -> FeaturedMember(profile.parliamentdotuk) }
        )
    }

//    @Transaction
//    suspend fun insertCompleteMember(parliamentdotuk: ParliamentID, member: ApiCompleteMember) {
//        insertPartiesIfNotExists(member.parties.map { it.party })
//        insertPartyIfNotExists(member.profile.party)
//        insertConstituenciesIfNotExists(
//            member.constituencies.map { it.constituency }
//        )
//        if (member.profile.constituency != null) {
//            // Not necessarily included in member.constituencies list.
//            insertConstituencyIfNotExists(member.profile.constituency)
//        }
//
//        safeInsertProfile(member.profile.toMemberProfile())
//
//        insertPhysicalAddresses(
//            member.addresses.physical.map { it.toPhysicalAddress(memberId = parliamentdotuk) }
//        )
//        insertWebAddresses(
//            member.addresses.web.map { it.toWebAddress(memberId = parliamentdotuk) }
//        )
//
//        insertPosts(member.posts.governmental.map { post ->
//            post.toPost(memberId = parliamentdotuk, postType = Post.PostType.GOVERNMENTAL)
//        })
//        insertPosts(member.posts.parliamentary.map { post ->
//            post.toPost(memberId = parliamentdotuk, postType = Post.PostType.PARLIAMENTARY)
//        })
//        insertPosts(member.posts.opposition.map { post ->
//            post.toPost(memberId = parliamentdotuk, postType = Post.PostType.OPPOSITION)
//        })
//
//        insertCommitteeMemberships(member.committees.map { membership ->
//            membership.toCommitteeMembership(parliamentdotuk)
//        })
//
//        member.committees.forEach { committee ->
//            insertCommitteeChairships(committee.chairs.map { chair ->
//                chair.toCommitteeChairship(
//                    committeeId = committee.parliamentdotuk,
//                    memberId = parliamentdotuk
//                )
//            })
//        }
//
//        insertHouseMemberships(member.houses.map { house ->
//            house.toHouseMembership(memberId = parliamentdotuk)
//        })
//
//        insertFinancialInterests(member.financialInterests.map { it.toFinancialInterest(memberId = parliamentdotuk) })
//        insertExperiences(member.experiences.map { it.toExperience(memberId = parliamentdotuk) })
//        insertTopicsOfInterest(member.topicsOfInterest.map { it.toTopicOfInterest(memberId = parliamentdotuk) })
//
//        insertElections(member.constituencies.map { it.election.toElection() })
//        insertMemberForConstituencies(member.constituencies.map {
//            it.toHistoricalConstituency(parliamentdotuk)
//        } )
//
//        insertPartyAssociations(member.parties.map {it.toPartyAssociation(parliamentdotuk) })
//    }

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
