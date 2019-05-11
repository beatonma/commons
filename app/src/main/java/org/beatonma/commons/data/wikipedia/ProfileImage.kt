@file:JvmName("ProfileImage")

package org.beatonma.commons.data.wikipedia

import androidx.room.*

@Entity
data class ProfileImage(
    @PrimaryKey val person_id: Int,
    @ColumnInfo(name = "url") val url: String,
    @ColumnInfo(name = "attribution_url") val attribution_url: String,
    @ColumnInfo(name = "attribution_text") val attribution_text: String?
)

@Dao
interface ImageDao {
    @Query("SELECT * FROM profileimage WHERE person_id = :id")
    fun getProfileImage(id: Int): ProfileImage

    @Insert
    fun insert(image: ProfileImage)

    @Delete
    fun delete(image: ProfileImage)
}
