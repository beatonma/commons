package org.beatonma.commons.data.core.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import org.beatonma.commons.data.core.ApiCompleteMember
import org.beatonma.commons.data.core.CompleteMember
import org.beatonma.commons.data.core.room.entities.constituency.Constituency
import org.beatonma.commons.data.core.room.entities.division.VoteWithDivision
import org.beatonma.commons.data.core.room.entities.member.*

@Dao
interface MemberDao {

    // Get operations
    @Transaction
    @Query("""SELECT * FROM featured_members""")
    fun getFeaturedProfiles(): LiveData<List<FeaturedMemberProfile>>

    @Transaction
    @Query("""SELECT * FROM member_profiles WHERE member_profiles.parliamentdotuk = :parliamentdotuk""")
    fun getCompleteMember(parliamentdotuk: Int): LiveData<CompleteMember>

    @Query("""SELECT * FROM member_profiles WHERE member_profiles.parliamentdotuk = :parliamentdotuk""")
    fun getMemberProfile(parliamentdotuk: Int): LiveData<MemberProfile>

    @Query("""SELECT * FROM constituencies
        LEFT JOIN member_profiles ON constituency_id = constituency_parliamentdotuk 
        WHERE parliamentdotuk = :parliamentdotuk""")
    fun getConstituency(parliamentdotuk: Int): LiveData<Constituency>

    @Query("""SELECT * FROM parties
        LEFT JOIN member_profiles ON party_id = party_parliamentdotuk 
        WHERE parliamentdotuk = :parliamentdotuk""")
    fun getParty(parliamentdotuk: Int): LiveData<Party>

    @Query("""SELECT * FROM physical_addresses WHERE physical_addresses.paddr_member_id = :parliamentdotuk""")
    fun getPhysicalAddresses(parliamentdotuk: Int): LiveData<List<PhysicalAddress>>

    @Query("""SELECT * FROM weblinks WHERE weblinks.waddr_member_id = :parliamentdotuk""")
    fun getWebAddresses(parliamentdotuk: Int): LiveData<List<WebAddress>>

    @Query("""SELECT * FROM posts WHERE post_member_id = :parliamentdotuk""")
    fun getPosts(parliamentdotuk: Int): LiveData<List<Post>>

    @Query("""SELECT * FROM committee_memberships WHERE committee_member_id = :parliamentdotuk""")
    fun getCommitteeMemberships(parliamentdotuk: Int): LiveData<List<CommitteeMembership>>

    @Query("""SELECT * FROM house_memberships WHERE house_member_id = :parliamentdotuk""")
    fun getHouseMemberships(parliamentdotuk: Int): LiveData<List<HouseMembership>>

    @Query("""SELECT * FROM financial_interests WHERE interest_member_id = :parliamentdotuk""")
    fun getFinancialInterests(parliamentdotuk: Int): LiveData<List<FinancialInterest>>

    @Query("""SELECT * FROM experiences WHERE experience_member_id = :parliamentdotuk""")
    fun getExperiences(parliamentdotuk: Int): LiveData<List<Experience>>

    @Query("""SELECT * FROM topics_of_interest WHERE topic_member_id = :parliamentdotuk""")
    fun getTopicsOfInterest(parliamentdotuk: Int): LiveData<List<TopicOfInterest>>

    @Transaction
    @Query("""SELECT * FROM committee_memberships WHERE committee_member_id = :parliamentdotuk""")
    fun getCommitteeMembershipWithChairship(parliamentdotuk: Int): LiveData<List<CommitteeMemberWithChairs>>

    @Transaction
    @Query("""SELECT * FROM historic_constituencies WHERE memberfor_member_id = :parliamentdotuk""")
    fun getHistoricalConstituencies(parliamentdotuk: Int): LiveData<List<HistoricalConstituencyWithElection>>

    @Transaction
    @Query("""SELECT * FROM party_associations WHERE partyacc_member_id = :parliamentdotuk""")
    fun getPartyAssociations(parliamentdotuk: Int): LiveData<List<PartyAssociationWithParty>>

    @Transaction
    @Query("""SELECT * FROM division_votes WHERE dvote_member_id = :parliamentdotuk""")
    fun getCommonsVotesForMember(parliamentdotuk: Int): LiveData<List<VoteWithDivision>>

    @Transaction
    @Query("""SELECT * FROM division_votes WHERE dvote_member_id = :parliamentdotuk""")
    fun getLordsVotesForMember(parliamentdotuk: Int): LiveData<List<VoteWithDivision>>

    // Insert operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhysicalAddresses(webaddresses: List<PhysicalAddress>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWebAddresses(webaddresses: List<WebAddress>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeaturedPeople(people: List<FeaturedMember>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfiles(people: List<MemberProfile>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: MemberProfile)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConstituency(constituency: Constituency)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConstituencies(constituencies: List<Constituency>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParty(party: Party)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertParties(parties: List<Party>)

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
     * Delete everything related to a Person via ForeignKey cascading
     */
    @Delete
    suspend fun deleteProfile(profile: MemberProfile)

    @Transaction
    suspend fun insertCompleteMember(parliamentdotuk: Int, member: ApiCompleteMember) {
        insertParties(member.parties.map { it.party })
        insertParty(member.profile.party)
        insertConstituencies(
            member.constituencies.map { it.constituency }
        )
        if (member.profile.constituency != null) {
            // Not necessarily included in member.constituencies list.
            insertConstituency(member.profile.constituency)
        }

        insertProfile(member.profile)

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

        insertPartyAssociations(member.parties.map {it.toPartyAssocation(parliamentdotuk) })
    }
}
