package org.beatonma.commons.data.core.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import org.beatonma.commons.data.core.CompleteMember
import org.beatonma.commons.data.core.room.entities.*

@Dao
interface MemberDao {

    // Get operations
    @Transaction
    @Query("""SELECT * FROM member_profiles WHERE member_profiles.parliamentdotuk = :parliamentdotuk""")
    fun getCompleteMember(parliamentdotuk: Int): LiveData<CompleteMember>

    @Transaction
    @Query("""SELECT * FROM featured_members""")
    fun getFeaturedProfiles(): LiveData<List<FeaturedMemberProfile>>

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
    suspend fun insertParty(party: Party)

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

    /**
     * Delete everything related to a Person via ForeignKey cascading
     */
    @Delete
    suspend fun deleteProfile(profile: MemberProfile)
}
