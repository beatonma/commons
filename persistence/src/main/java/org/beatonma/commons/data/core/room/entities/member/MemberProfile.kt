package org.beatonma.commons.data.core.room.entities.member

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.Relation
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.data.core.interfaces.Commentable
import org.beatonma.commons.data.core.interfaces.Named
import org.beatonma.commons.data.core.interfaces.Parliamentdotuk
import org.beatonma.commons.data.core.interfaces.Periodic
import org.beatonma.commons.data.core.interfaces.Timestamped
import org.beatonma.commons.data.core.interfaces.Votable
import org.beatonma.commons.data.core.room.entities.constituency.Constituency
import java.time.LocalDate

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Party::class,
            parentColumns = ["party_id"],
            childColumns = ["member_party_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Constituency::class,
            parentColumns = ["constituency_id"],
            childColumns = ["member_constituency_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    tableName = "member_profiles"
)
data class MemberProfile(
    @PrimaryKey @ColumnInfo(name = "member_id") override val parliamentdotuk: ParliamentID,
    @ColumnInfo(name = "name") override val name: String,
    @ColumnInfo(name = "member_party_id",
        index = true) val party: Party,  // Use Party object for api response, serialized to id for storage
    @ColumnInfo(name = "member_constituency_id",
        index = true) val constituency: Constituency?,  // Use Constituency object for api response, serialized to id for storage
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
    @ColumnInfo(name = "member_id") override val parliamentdotuk: ParliamentID,
    @ColumnInfo(name = "name") override val name: String,
    @ColumnInfo(name = "portrait_url") val portraitUrl: String? = null,
    @ColumnInfo(name = "current_post") val currentPost: String? = null,
    @ColumnInfo(
        name = "member_party_id",
        index = true
    ) val party: Party,  // Use Party object for api response, serialized to id for storage
    @ColumnInfo(
        name = "member_constituency_id",
        index = true
    ) val constituency: Constituency?,  // Use Constituency object for api response, serialized to id for storage
) : Parliamentdotuk,
    Named


data class MemberProfileWithPartyConstituency(
    @Embedded val profile: MemberProfile,

    @Relation(parentColumn = "member_party_id", entityColumn = "party_id")
    val party: Party?,

    @Relation(parentColumn = "member_constituency_id", entityColumn = "constituency_id")
    val constituency: Constituency?,
)
