package org.beatonma.commons.data.core.room.entities.division

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import org.beatonma.commons.core.PARLIAMENTDOTUK
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.data.core.room.entities.ResolvedZeitgeistContent
import org.beatonma.commons.data.core.room.entities.ZeitgeistContent

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Division::class,
            parentColumns = ["division_$PARLIAMENTDOTUK"],
            childColumns = ["zeitgeist_division_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    tableName = "zeitgeist_divisions"
)
data class ZeitgeistDivision(
    @ColumnInfo(name = "zeitgeist_division_id") @PrimaryKey override val id: ParliamentID,
    @ColumnInfo(name = "zeitgeist_division_reason") override val reason: String? = null,
    @ColumnInfo(name = "zeitgeist_division_priority") override val priority: Int = 50,
) : ZeitgeistContent

data class ResolvedZeitgeistDivision(
    @Embedded override val zeitgeistContent: ZeitgeistDivision,
    @Relation(
        parentColumn = "zeitgeist_division_id",
        entityColumn = "division_$PARLIAMENTDOTUK",
        entity = Division::class
    )
    val division: Division,
) : ResolvedZeitgeistContent<ZeitgeistDivision>