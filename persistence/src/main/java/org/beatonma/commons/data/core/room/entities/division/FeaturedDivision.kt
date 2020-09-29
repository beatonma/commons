package org.beatonma.commons.data.core.room.entities.division

import androidx.room.*
import org.beatonma.commons.core.PARLIAMENTDOTUK
import org.beatonma.commons.core.ParliamentID

@Deprecated("Use zeitgeist")
@Entity(
    tableName = "featured_divisions",
)
data class FeaturedDivision(
    @ColumnInfo(name = "featured_division_id") @PrimaryKey val divisionId: ParliamentID,
    @ColumnInfo(name = "featured_about") val about: String? = null,
)

@Deprecated("Use zeitgeist")
data class FeaturedDivisionWithDivision(
    @Embedded val featured: FeaturedDivision,
    @Relation(
        parentColumn = "featured_division_id",
        entityColumn = "division_$PARLIAMENTDOTUK",
        entity = Division::class
    )
    val division: Division,
)

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
