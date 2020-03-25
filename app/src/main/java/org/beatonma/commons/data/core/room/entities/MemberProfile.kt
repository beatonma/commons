package org.beatonma.commons.data.core.room.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import org.beatonma.commons.data.PARLIAMENTDOTUK


@Entity
data class MemberProfile(
    @field:Json(name = PARLIAMENTDOTUK) @PrimaryKey val parliamentdotuk: Int,
    @field:Json(name = "name") val name: String,
    @field:Json(name = "party") @ColumnInfo(name = "party_id") val party: Party,
    @field:Json(name = "constituency") @ColumnInfo(name = "constituency_id") val constituency: Constituency,
    @field:Json(name = "active") val active: Boolean? = null,
    @field:Json(name = "is_mp") @ColumnInfo(name = "is_mp") val isMp: Boolean? = null,
    @field:Json(name = "is_lord") @ColumnInfo(name = "is_lord") val isLord: Boolean? = null,
    @field:Json(name = "age") val age: Int? = null,
    @field:Json(name = "date_of_birth") @ColumnInfo(name = "date_of_birth") val dateOfBirth: String? = null,
    @field:Json(name = "date_of_death") @ColumnInfo(name = "date_of_death") val dateOfDeath: String? = null,
    @field:Json(name = "gender") val gender: String? = null,
    @Embedded @field:Json(name = "place_of_birth") val placeOfBirth: Birthplace? = null,
    @field:Json(name = "portrait") @ColumnInfo(name = "portrait_url") val portraitUrl: String? = null
)

data class MemberProfileWithRelatedObjects(
    @Embedded val profile: MemberProfile,
    @Embedded val party: Party,
    @Embedded val constituency: Constituency
)
