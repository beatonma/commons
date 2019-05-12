@file:JvmName("ProfileImage")

package org.beatonma.commons.data.wikipedia

import androidx.room.*
import org.beatonma.commons.data.twfy.Profile

@Entity(
    indices = [Index("person_id")],
    tableName = "profile_images",
    foreignKeys = [
        ForeignKey(
            entity = Profile::class,
            parentColumns = ["person_id"],
            childColumns = ["person_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ProfileImage(
    @ColumnInfo(name = "person_id") val personID: Int,
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo val url: String,
    @ColumnInfo val attributionUrl: String,
    @ColumnInfo val attributionText: String?
)

@Dao
interface ImageDao {
    @Query("SELECT * FROM profile_images WHERE person_id = :personID")
    suspend fun getProfileImage(personID: Int): ProfileImage

    @Insert
    suspend fun insert(image: ProfileImage)

    @Delete
    suspend fun delete(image: ProfileImage)
}
