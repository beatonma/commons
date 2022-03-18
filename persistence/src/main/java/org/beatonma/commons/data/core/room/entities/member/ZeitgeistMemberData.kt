package org.beatonma.commons.data.core.room.entities.member

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.core.ZeitgeistReason
import org.beatonma.commons.data.core.MinimalMember
import org.beatonma.commons.data.core.room.entities.ZeitgeistContent

@Entity(tableName = "zeitgeist_members")
data class ZeitgeistMemberData(
    @ColumnInfo(name = "zeitgeist_member_id") @PrimaryKey override val id: ParliamentID,
    @ColumnInfo(name = "zeitgeist_member_reason") override val reason: ZeitgeistReason,
    @ColumnInfo(name = "zeitgeist_member_priority") override val priority: Int = 50,
    @ColumnInfo(name = "zeitgeist_member_name") val name: String,
    @ColumnInfo(name = "zeitgeist_member_portrait_url") val portraitUrl: String?,
    @ColumnInfo(name = "zeitgeist_member_party_id") val partyId: ParliamentID,
    @ColumnInfo(name = "zeitgeist_member_constituency_id") val constituencyId: ParliamentID?,
    @ColumnInfo(name = "zeitgeist_member_current_post") val currentPost: String?,
) : ZeitgeistContent

data class ZeitgeistMember(
    @Embedded val data: ZeitgeistMemberData,
    @Relation(
        parentColumn = "zeitgeist_member_id",
        entityColumn = "member_id",
        entity = MemberProfile::class
    )
    val member: MinimalMember,
) : ZeitgeistContent by data
