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
    @PrimaryKey @ColumnInfo(name = PARLIAMENTDOTUK) override val parliamentdotuk: ParliamentID,
    @ColumnInfo(name = "name") override val name: String,
    @ColumnInfo(name = "party_id", index = true) val party: Party,  // Use Party object for api response, serialized to id for storage
    @ColumnInfo(name = "constituency_id", index = true) val constituency: Constituency?,  // Use Constituency object for api response, serialized to id for storage
    @ColumnInfo(name = "active") val active: Boolean? = null,
    @ColumnInfo(name = "is_mp") val isMp: Boolean? = null,
    @ColumnInfo(name = "is_lord") val isLord: Boolean? = null,
    @ColumnInfo(name = "age") val age: Int? = null,
    @ColumnInfo(name = "date_of_birth") val dateOfBirth: LocalDate? = null,
    @ColumnInfo(name = "date_of_death") val dateOfDeath: LocalDate? = null,
    @ColumnInfo(name = "gender") val gender: String? = null,
    @Embedded(prefix = "birth_") val placeOfBirth: Town? = null,
    @ColumnInfo(name = "portrait_url") val portraitUrl: String? = null,
    @ColumnInfo(name = "current_post") val currentPost: String? = null,

    ) : Parliamentdotuk,
    Named,
    Periodic,
    Commentable,
    Votable,
    Timestamped
{
    @ColumnInfo(name = Timestamped.FIELD_ACCESSED_AT) override var accessedAt: Long = System.currentTimeMillis()
    @ColumnInfo(name = Timestamped.FIELD_CREATED_AT) override var createdAt: Long = System.currentTimeMillis()

    @Ignore override val start: LocalDate? = dateOfBirth
    @Ignore override val end: LocalDate? = dateOfDeath

    override fun getSocialContentTarget(): SocialTargetType = SocialTargetType.member
}


data class ApiMemberProfile(
    @field:Json(name = Contract.PARLIAMENTDOTUK)val parliamentdotuk: ParliamentID,
    @field:Json(name = Contract.NAME) val name: String,
    @field:Json(name = Contract.PARTY) val party: Party,  // Use Party object for api response, serialized to id for storage
    @field:Json(name = Contract.CONSTITUENCY) val constituency: Constituency?,  // Use Constituency object for api response, serialized to id for storage
    @field:Json(name = Contract.ACTIVE) val active: Boolean? = null,
    @field:Json(name = Contract.IS_MP) val isMp: Boolean? = null,
    @field:Json(name = Contract.IS_LORD) val isLord: Boolean? = null,
    @field:Json(name = Contract.AGE) val age: Int? = null,
    @field:Json(name = Contract.DATE_OF_BIRTH) val dateOfBirth: LocalDate? = null,
    @field:Json(name = Contract.DATE_OF_DEATH) val dateOfDeath: LocalDate? = null,
    @field:Json(name = Contract.GENDER) val gender: String? = null,
    @field:Json(name = Contract.PLACE_OF_BIRTH) val placeOfBirth: Town? = null,
    @field:Json(name = Contract.PORTRAIT) val portraitUrl: String? = null,
    @field:Json(name = Contract.CURRENT_POST) val currentPost: String? = null,
) {
    fun toMemberProfile() = MemberProfile(
        parliamentdotuk = parliamentdotuk,
        name = name,
        party = party,
        constituency = constituency,
        active = active,
        isMp = isMp,
        isLord = isLord,
        age = age,
        dateOfBirth = dateOfBirth,
        dateOfDeath = dateOfDeath,
        gender = gender,
        placeOfBirth = placeOfBirth,
        portraitUrl = portraitUrl,
        currentPost = currentPost
    )
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
