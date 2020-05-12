package org.beatonma.commons.data.core.room.entities.member

import androidx.room.*
import com.squareup.moshi.Json
import org.beatonma.commons.data.PARLIAMENTDOTUK
import org.beatonma.commons.data.ParliamentID
import org.beatonma.commons.data.core.room.entities.constituency.Constituency
import java.util.*

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Party::class,
            parentColumns = ["party_$PARLIAMENTDOTUK"],
            childColumns = ["party_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Constituency::class,
            parentColumns = ["constituency_$PARLIAMENTDOTUK"],
            childColumns = ["constituency_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    tableName = "member_profiles"
)
data class MemberProfile(
    @PrimaryKey @field:Json(name = PARLIAMENTDOTUK) @ColumnInfo(name = PARLIAMENTDOTUK) val parliamentdotuk: ParliamentID,
    @field:Json(name = "name") @ColumnInfo(name = "name") val name: String,
    @field:Json(name = "party") @ColumnInfo(name = "party_id", index = true) val party: Party,  // Use Party object for api response, serialized to id for storage
    @field:Json(name = "constituency") @ColumnInfo(name = "constituency_id", index = true) val constituency: Constituency?,  // Use Constituency object for api response, serialized to id for storage
    @field:Json(name = "active") @ColumnInfo(name = "active") val active: Boolean? = null,
    @field:Json(name = "is_mp") @ColumnInfo(name = "is_mp") val isMp: Boolean? = null,
    @field:Json(name = "is_lord") @ColumnInfo(name = "is_lord") val isLord: Boolean? = null,
    @field:Json(name = "age") @ColumnInfo(name = "age") val age: Int? = null,
    @field:Json(name = "date_of_birth") @ColumnInfo(name = "date_of_birth") val dateOfBirth: Date? = null,
    @field:Json(name = "date_of_death") @ColumnInfo(name = "date_of_death") val dateOfDeath: Date? = null,
    @field:Json(name = "gender") @ColumnInfo(name = "gender") val gender: String? = null,
    @Embedded(prefix = "birth_") @field:Json(name = "place_of_birth") val placeOfBirth: Town? = null,
    @field:Json(name = "portrait") @ColumnInfo(name = "portrait_url") val portraitUrl: String? = null,
    @field:Json(name = "current_post") @ColumnInfo(name = "current_post") val currentPost: String? = null
)

data class BasicProfile(
    @field:Json(name = PARLIAMENTDOTUK) @ColumnInfo(name = PARLIAMENTDOTUK) val parliamentdotuk: ParliamentID,
    @field:Json(name = "name") @ColumnInfo(name = "name") val name: String,
    @field:Json(name = "portrait_url") @ColumnInfo(name = "portrait_url") val portraitUrl: String? = null,
    @field:Json(name = "current_post") @ColumnInfo(name = "current_post") val currentPost: String? = null,
    @field:Json(name = "party") @ColumnInfo(name = "party_id", index = true) val party: Party,  // Use Party object for api response, serialized to id for storage
    @field:Json(name = "constituency") @ColumnInfo(name = "constituency_id", index = true) val constituency: Constituency?  // Use Constituency object for api response, serialized to id for storage
) {
    fun toMemberProfile() = MemberProfile(
        parliamentdotuk = parliamentdotuk,
        name = name,
        portraitUrl = portraitUrl,
        currentPost = currentPost,
        party = party,
        constituency = constituency
    )
}

data class BasicProfileWithParty(
    @Embedded
    val profile: BasicProfile,

    @Relation(parentColumn = "party_id", entityColumn = "party_$PARLIAMENTDOTUK")
    val party: Party
)
