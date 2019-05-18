package org.beatonma.commons.data.core

import androidx.room.*
import org.beatonma.commons.political.organisation.Party

data class Person(
    val profile: Profile,
    val contactInfo: ContactInfo,
    val career: Career,
    val interests: Interests
)

@Entity
data class Profile(
    @PrimaryKey @ColumnInfo(name = "person_id") val personID: Int,
    @ColumnInfo(name = "parliamentary_id") val parliamentaryID: Int,
    val name: String,
    val constituency: String,
    val party: Party
)

@Entity(
    indices = [Index("person_id")],
    foreignKeys = [
        ForeignKey(
            entity = Profile::class,
            parentColumns = ["person_id"],
            childColumns = ["person_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ContactInfo(
    @ColumnInfo(name = "person_id") val personID: Int,
    @PrimaryKey(autoGenerate = true) val id: Long,
    val email: String?,
    val twitter: String?,
    val facebook: String?,
    val weblink: String?,
    @ColumnInfo(name = "phone_constituency") val phoneConstituency: String?,
    @ColumnInfo(name = "phone_parliamentary") val phoneParliamentary: String?
)

@Entity(
    indices = [Index("person_id")],
    foreignKeys = [
        ForeignKey(
            entity = Profile::class,
            parentColumns = ["person_id"],
            childColumns = ["person_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Career(
    @ColumnInfo(name = "person_id") val personID: Int,
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo val parliamentary: String?,
    @ColumnInfo val committee: String?,
    @ColumnInfo val other: String?
)

@Entity(
    indices = [Index("person_id")],
    foreignKeys = [
        ForeignKey(
            entity = Profile::class,
            parentColumns = ["person_id"],
            childColumns = ["person_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Interests(
    @ColumnInfo(name = "person_id") val personID: Int,
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo val political: String?,
    @ColumnInfo val countries: String?
)

@Dao
interface PeopleDao {
    @Query("SELECT * FROM profile WHERE person_id = :personID")
    suspend fun getProfile(personID: Int): Profile

    @Query("SELECT * FROM contactinfo WHERE person_id = :personID")
    suspend fun getContactInfo(personID: Int): ContactInfo

    @Query("SELECT * FROM career WHERE person_id = :personID")
    suspend fun getCareer(personID: Int): Career

    @Query("SELECT * FROM interests WHERE person_id = :personID")
    suspend fun getInterests(personID: Int): Interests

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(vararg profiles: Profile)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContactInfo(vararg contactInfo: ContactInfo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCareer(vararg career: Career)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInterests(vararg interests: Interests)

    @Transaction
    suspend fun insertPerson(person: Person) {
        insertProfile(person.profile)
        insertContactInfo(person.contactInfo)
        insertCareer(person.career)
        insertInterests(person.interests)
    }

    @Transaction
    suspend fun getPerson(personID: Int) = Person(
        profile = getProfile(personID),
        contactInfo = getContactInfo(personID),
        career = getCareer(personID),
        interests = getInterests(personID)
    )
}
