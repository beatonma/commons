package org.beatonma.commons.data.core.room.entities.division

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import org.beatonma.commons.core.PARLIAMENTDOTUK
import org.beatonma.commons.core.ParliamentID

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
    @ColumnInfo(name = "zeitgeist_division_id") @PrimaryKey val divisionId: ParliamentID,
    @ColumnInfo(name = "zeitgeist_division_reason") val reason: String? = null,
)

data class ResolvedZeitgeistDivision(
    @Embedded val zeitgeistDivision: ZeitgeistDivision,
    @Relation(
        parentColumn = "zeitgeist_division_id",
        entityColumn = "division_$PARLIAMENTDOTUK",
        entity = Division::class
    )
    val division: Division,
)
