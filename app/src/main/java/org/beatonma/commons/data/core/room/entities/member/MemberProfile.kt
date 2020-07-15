package org.beatonma.commons.data.core.room.entities.member

import androidx.room.*
import com.squareup.moshi.Json
import org.beatonma.commons.data.PARLIAMENTDOTUK
import org.beatonma.commons.data.ParliamentID
import org.beatonma.commons.data.core.interfaces.*
import org.beatonma.commons.data.core.room.entities.constituency.Constituency
import org.beatonma.commons.data.core.social.SocialTargetType
import org.beatonma.commons.network.retrofit.Contract
import java.time.LocalDate

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
    @PrimaryKey @field:Json(name = Contract.PARLIAMENTDOTUK) @ColumnInfo(name = PARLIAMENTDOTUK) override val parliamentdotuk: ParliamentID,
    @field:Json(name = Contract.NAME) @ColumnInfo(name = "name") override val name: String,
    @field:Json(name = Contract.PARTY) @ColumnInfo(name = "party_id", index = true) val party: Party,  // Use Party object for api response, serialized to id for storage
    @field:Json(name = Contract.CONSTITUENCY) @ColumnInfo(name = "constituency_id", index = true) val constituency: Constituency?,  // Use Constituency object for api response, serialized to id for storage
    @field:Json(name = Contract.ACTIVE) @ColumnInfo(name = "active") val active: Boolean? = null,
    @field:Json(name = Contract.IS_MP) @ColumnInfo(name = "is_mp") val isMp: Boolean? = null,
    @field:Json(name = Contract.IS_LORD) @ColumnInfo(name = "is_lord") val isLord: Boolean? = null,
    @field:Json(name = Contract.AGE) @ColumnInfo(name = "age") val age: Int? = null,
    @field:Json(name = Contract.DATE_OF_BIRTH) @ColumnInfo(name = "date_of_birth") val dateOfBirth: LocalDate? = null,
    @field:Json(name = Contract.DATE_OF_DEATH) @ColumnInfo(name = "date_of_death") val dateOfDeath: LocalDate? = null,
    @field:Json(name = Contract.GENDER) @ColumnInfo(name = "gender") val gender: String? = null,
    @field:Json(name = Contract.PLACE_OF_BIRTH) @Embedded(prefix = "birth_") val placeOfBirth: Town? = null,
    @field:Json(name = Contract.PORTRAIT) @ColumnInfo(name = "portrait_url") val portraitUrl: String? = null,
    @field:Json(name = Contract.CURRENT_POST) @ColumnInfo(name = "current_post") val currentPost: String? = null,

    @ColumnInfo(name = Timestamped.FIELD_ACCESSED_AT) override var accessedAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = Timestamped.FIELD_CREATED_AT) override var createdAt: Long = System.currentTimeMillis(),

    ) : Parliamentdotuk,
    Named,
    Periodic,
    Commentable,
    Votable,
    Timestamped
{
    override fun getSocialContentTarget(): SocialTargetType = SocialTargetType.member

    @Ignore override val start: LocalDate? = dateOfBirth
    @Ignore override val end: LocalDate? = dateOfDeath
}

data class BasicProfile(
    @field:Json(name = PARLIAMENTDOTUK) @ColumnInfo(name = PARLIAMENTDOTUK) override val parliamentdotuk: ParliamentID,
    @field:Json(name = Contract.NAME) @ColumnInfo(name = "name") override val name: String,
    @field:Json(name = Contract.PORTRAIT) @ColumnInfo(name = "portrait_url") val portraitUrl: String? = null,
    @field:Json(name = Contract.CURRENT_POST) @ColumnInfo(name = "current_post") val currentPost: String? = null,
    @field:Json(name = Contract.PARTY) @ColumnInfo(name = "party_id", index = true) val party: Party,  // Use Party object for api response, serialized to id for storage
    @field:Json(name = Contract.CONSTITUENCY) @ColumnInfo(name = "constituency_id", index = true) val constituency: Constituency?  // Use Constituency object for api response, serialized to id for storage
): Parliamentdotuk,
    Named {
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
