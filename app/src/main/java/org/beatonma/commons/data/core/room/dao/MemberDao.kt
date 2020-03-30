package org.beatonma.commons.data.core.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import org.beatonma.commons.data.core.Member
import org.beatonma.commons.data.core.room.entities.*

@Dao
interface MemberDao {

    // Get operations
    @Query("""
        SELECT parliamentdotuk, name, party_id, constituency_id, active, is_mp, is_lord, age,
                date_of_birth, date_of_death, gender, portrait_url, current_post, birth_town_name, birth_country_name
        FROM member_profiles
        INNER JOIN parties ON parties.party_parliamentdotuk = member_profiles.party_id
        INNER JOIN constituencies ON constituencies.constituency_parliamentdotuk = member_profiles.constituency_id
        WHERE member_profiles.parliamentdotuk = :parliamentdotuk
        """)
    fun getMember(parliamentdotuk: Int): LiveData<Member>

    @Query("""
        SELECT parliamentdotuk, name, party_id, constituency_id, active, is_mp, is_lord, age,
                date_of_birth, date_of_death, gender, portrait_url, current_post, birth_town_name, birth_country_name
        FROM member_profiles
        INNER JOIN parties ON parties.party_parliamentdotuk = member_profiles.party_id
        INNER JOIN constituencies ON constituencies.constituency_parliamentdotuk = member_profiles.constituency_id
        WHERE member_profiles.parliamentdotuk = :parliamentdotuk
        """)
    fun getMemberProfile(parliamentdotuk: Int): LiveData<MemberProfile>

    @Query("""
        SELECT f.member_id, f.about, prof.parliamentdotuk, prof.name, prof.portrait_url, prof.current_post,
                p.party_parliamentdotuk, p.party_name, c.constituency_parliamentdotuk, c.constituency_name 
        FROM featured_members f, member_profiles prof, parties p, constituencies c
        WHERE prof.parliamentdotuk = f.member_id
        AND p.party_parliamentdotuk = prof.party_id
        AND c.constituency_parliamentdotuk = prof.constituency_id
        """)
    fun getFeaturedProfiles(): LiveData<List<FeaturedMemberProfile>>

    @Query("""SELECT * FROM weblinks WHERE weblinks.waddr_person_id = :parliamentdotuk""")
    fun getWebAddresses(parliamentdotuk: Int): LiveData<List<WebAddress>>

    @Query("""SELECT * FROM posts WHERE post_member_id = :parliamentdotuk""")
    fun getPosts(parliamentdotuk: Int): LiveData<List<Post>>

    @Query("""SELECT * FROM committee_memberships WHERE committee_member_id = :parliamentdotuk""")
    fun getCommitteeMemberships(parliamentdotuk: Int): LiveData<List<CommitteeMembership>>

    @Query("""SELECT * FROM house_memberships WHERE house_member_id = :parliamentdotuk""")
    fun getHouseMemberships(parliamentdotuk: Int): LiveData<List<HouseMembership>>

    // Insert operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhysicalAddresses(webaddresses: List<PhysicalAddress>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWebAddresses(webaddresses: List<WebAddress>?)

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
    suspend fun insertHouseMemberships(houseMemberships: List<HouseMembership>)

    /**
     * Delete everything related to a Person via ForeignKey cascading
     */
    @Delete
    suspend fun deleteProfile(profile: MemberProfile)
}
