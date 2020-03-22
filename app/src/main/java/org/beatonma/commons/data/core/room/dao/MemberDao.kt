package org.beatonma.commons.data.core.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import org.beatonma.commons.data.core.room.entities.*


@Dao
interface MemberDao {
//    @Query("SELECT * FROM profile WHERE parliamentdotuk = :parliamentdotuk")
//    suspend fun getProfile(parliamentdotuk: Int): Profile
//
//    @Query("SELECT * FROM party WHERE parliamentdotuk = :parliamentdotuk")
//    suspend fun getParty(parliamentdotuk: Int): LiveData<Party>

    @Query("""SELECT * FROM featuredmember, memberprofile, party, constituency
        WHERE memberprofile.parliamentdotuk = featuredmember.member_id
        AND party.party_parliamentdotuk = memberprofile.party_id
        AND constituency.constituency_parliamentdotuk = memberprofile.constituency_id""")
    fun getFeaturedProfiles(): LiveData<List<FeaturedMemberProfile>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeaturedPeople(people: List<FeaturedMember>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfiles(people: List<MemberProfile>)

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertProfile(profile: Profile)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertConstituency(constituency: Constituency?)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertParty(party: Party?)

//    @Query("SELECT * FROM profile where profile.parliamentdotuk = :parliamentdotuk")
//    suspend fun getProfileWithRelatedObjects(parliamentdotuk: Int)

//    @Transaction
//    suspend fun insertProfileWithRelatedObjects(profile: ProfileWithRelatedObjects) {
//        insertParty(profile.party)
//    }

//
//    @Transaction
//    suspend fun getPerson(personID: Int) = Person(
//        profile = getProfile(personID),
//        contactInfo = getContactInfo(personID),
//        career = getCareer(personID),
//        interests = getInterests(personID)
//    )
//
    /**
     * Delete everything related to a Person via ForeignKey cascading
     */
    @Delete
    suspend fun deleteProfile(profile: MemberProfile)
}
