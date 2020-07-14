package org.beatonma.commons.data.core.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import org.beatonma.commons.data.LiveDataList
import org.beatonma.commons.data.ParliamentID
import org.beatonma.commons.data.core.ApiCompleteMember
import org.beatonma.commons.data.core.interfaces.markAccessed
import org.beatonma.commons.data.core.interfaces.markAllAccessed
import org.beatonma.commons.data.core.room.dao.shared.SharedConstituencyDao
import org.beatonma.commons.data.core.room.dao.shared.SharedPartyDao
import org.beatonma.commons.data.core.room.entities.constituency.Constituency
import org.beatonma.commons.data.core.room.entities.division.VoteWithDivision
import org.beatonma.commons.data.core.room.entities.election.Election
import org.beatonma.commons.data.core.room.entities.member.*

@Dao
interface MemberDao: SharedPartyDao, SharedConstituencyDao {

    // Get operations
    @Transaction
    @Query("""SELECT * FROM featured_members""")
    fun getFeaturedProfiles(): LiveDataList<FeaturedMemberProfile>

    @Query("""SELECT * FROM member_profiles WHERE member_profiles.parliamentdotuk = :parliamentdotuk""")
    fun getMemberProfile(parliamentdotuk: ParliamentID): LiveData<MemberProfile>

    @Query("""SELECT * FROM constituencies
        LEFT JOIN member_profiles ON constituency_id = constituency_parliamentdotuk 
        WHERE parliamentdotuk = :parliamentdotuk""")
    fun getConstituency(parliamentdotuk: ParliamentID): LiveData<Constituency>

    @Query("""SELECT * FROM parties
        LEFT JOIN member_profiles ON party_id = party_parliamentdotuk 
        WHERE parliamentdotuk = :parliamentdotuk""")
    fun getParty(parliamentdotuk: ParliamentID): LiveData<Party>

    @Query("""SELECT * FROM physical_addresses WHERE physical_addresses.paddr_member_id = :parliamentdotuk""")
    fun getPhysicalAddresses(parliamentdotuk: ParliamentID): LiveDataList<PhysicalAddress>

    @Query("""SELECT * FROM weblinks WHERE weblinks.waddr_member_id = :parliamentdotuk""")
    fun getWebAddresses(parliamentdotuk: ParliamentID): LiveDataList<WebAddress>

    @Query("""SELECT * FROM posts WHERE post_member_id = :parliamentdotuk""")
    fun getPosts(parliamentdotuk: ParliamentID): LiveDataList<Post>

    @Query("""SELECT * FROM committee_memberships WHERE committee_member_id = :parliamentdotuk""")
    fun getCommitteeMemberships(parliamentdotuk: ParliamentID): LiveDataList<CommitteeMembership>

    @Query("""SELECT * FROM house_memberships WHERE house_member_id = :parliamentdotuk""")
    fun getHouseMemberships(parliamentdotuk: ParliamentID): LiveDataList<HouseMembership>

    @Query("""SELECT * FROM financial_interests WHERE interest_member_id = :parliamentdotuk""")
    fun getFinancialInterests(parliamentdotuk: ParliamentID): LiveDataList<FinancialInterest>

    @Query("""SELECT * FROM experiences WHERE experience_member_id = :parliamentdotuk""")
    fun getExperiences(parliamentdotuk: ParliamentID): LiveDataList<Experience>

    @Query("""SELECT * FROM topics_of_interest WHERE topic_member_id = :parliamentdotuk""")
    fun getTopicsOfInterest(parliamentdotuk: ParliamentID): LiveDataList<TopicOfInterest>

    @Transaction
    @Query("""SELECT * FROM committee_memberships WHERE committee_member_id = :parliamentdotuk""")
    fun getCommitteeMembershipWithChairship(parliamentdotuk: ParliamentID): LiveDataList<CommitteeMemberWithChairs>

    @Transaction
    @Query("""SELECT * FROM historic_constituencies WHERE memberfor_member_id = :parliamentdotuk""")
    fun getHistoricalConstituencies(parliamentdotuk: ParliamentID): LiveDataList<HistoricalConstituencyWithElection>

    @Transaction
    @Query("""SELECT * FROM party_associations WHERE partyacc_member_id = :parliamentdotuk""")
    fun getPartyAssociations(parliamentdotuk: ParliamentID): LiveDataList<PartyAssociationWithParty>

    @Transaction
    @Query("""SELECT * FROM division_votes WHERE dvote_member_id = :parliamentdotuk""")
    fun getCommonsVotesForMember(parliamentdotuk: ParliamentID): LiveDataList<VoteWithDivision>

    @Transaction
    @Query("""SELECT * FROM division_votes WHERE dvote_member_id = :parliamentdotuk""")
    fun getLordsVotesForMember(parliamentdotuk: ParliamentID): LiveDataList<VoteWithDivision>


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
    suspend fun insertElections(elections: List<Election>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMemberForConstituencies(memberForConstituencies: List<HistoricalConstituency>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPartyAssociation(partyAssociation: PartyAssociation)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPartyAssociations(partyAssociations: List<PartyAssociation>)

    /**
     * Retrieve the profile and update its accessed_at timestamp.
     */
    suspend fun getMemberProfileTimestamped(parliamentdotuk: ParliamentID): LiveData<MemberProfile> {
        val result = getMemberProfile(parliamentdotuk)

        safeInsertProfile(result.value)

        return result
    }

    @Transaction
    suspend fun insertCompleteMember(parliamentdotuk: ParliamentID, member: ApiCompleteMember) {
        insertPartiesIfNotExists(member.parties.map { it.party })
        insertPartyIfNotExists(member.profile.party)
        insertConstituenciesIfNotExists(
            member.constituencies.map { it.constituency }
        )
        if (member.profile.constituency != null) {
            // Not necessarily included in member.constituencies list.
            insertConstituencyIfNotExists(member.profile.constituency)
        }

        safeInsertProfile(member.profile)

        insertPhysicalAddresses(
            member.addresses.physical.map { it.copy(memberId = parliamentdotuk) }
        )
        insertWebAddresses(
            member.addresses.web.map { it.copy(memberId = parliamentdotuk) }
        )

        insertPosts(member.posts.governmental.map { post ->
            post.copy(memberId = parliamentdotuk, postType = Post.PostType.GOVERNMENTAL)
        })
        insertPosts(member.posts.parliamentary.map { post ->
            post.copy(memberId = parliamentdotuk, postType = Post.PostType.PARLIAMENTARY)
        })
        insertPosts(member.posts.opposition.map { post ->
            post.copy(memberId = parliamentdotuk, postType = Post.PostType.OPPOSITION)
        })

        insertCommitteeMemberships(member.committees.map { membership ->
            membership.toCommitteeMembership(parliamentdotuk)
        })

        member.committees.forEach { committee ->
            insertCommitteeChairships(committee.chairs.map { chair ->
                chair.copy(
                    committeeId = committee.parliamentdotuk,
                    memberId = parliamentdotuk)
            })
        }

        insertHouseMemberships(member.houses.map { house ->
            house.copy(memberId = parliamentdotuk)
        })

        insertFinancialInterests(member.financialInterests.map { it.copy(memberId = parliamentdotuk) })
        insertExperiences(member.experiences.map { it.copy(memberId = parliamentdotuk) })
        insertTopicsOfInterest(member.topicsOfInterest.map { it.copy(memberId = parliamentdotuk) })

        insertElections(member.constituencies.map { it.election })
        insertMemberForConstituencies(member.constituencies.map {
            it.toHistoricalConstituency(parliamentdotuk)
        } )

        insertPartyAssociations(member.parties.map {it.toPartyAssociation(parliamentdotuk) })
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
