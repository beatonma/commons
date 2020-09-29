package org.beatonma.commons.data.core.room.entities.member

import androidx.room.*
import org.beatonma.commons.core.PARLIAMENTDOTUK
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.data.core.interfaces.*
import org.beatonma.commons.data.core.room.entities.constituency.Constituency
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
}


data class BasicProfile(
    @ColumnInfo(name = PARLIAMENTDOTUK) override val parliamentdotuk: ParliamentID,
    @ColumnInfo(name = "name") override val name: String,
    @ColumnInfo(name = "portrait_url") val portraitUrl: String? = null,
    @ColumnInfo(name = "current_post") val currentPost: String? = null,
    @ColumnInfo(name = "party_id", index = true) val party: Party,  // Use Party object for api response, serialized to id for storage
    @ColumnInfo(name = "constituency_id", index = true) val constituency: Constituency?  // Use Constituency object for api response, serialized to id for storage
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
