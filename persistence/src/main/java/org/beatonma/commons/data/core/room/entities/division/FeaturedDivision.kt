package org.beatonma.commons.data.core.room.entities.division

import androidx.room.*
import org.beatonma.commons.core.PARLIAMENTDOTUK
import org.beatonma.commons.core.ParliamentID

@Entity(
    tableName = "featured_divisions",
)
data class FeaturedDivision(
    @ColumnInfo(name = "featured_division_id") @PrimaryKey val divisionId: ParliamentID,
    @ColumnInfo(name = "featured_about") val about: String? = null,
)

data class FeaturedDivisionWithDivision(
    @Embedded val featured: FeaturedDivision,
    @Relation(
        parentColumn = "featured_division_id",
        entityColumn = "division_$PARLIAMENTDOTUK",
        entity = Division::class
    )
    val division: Division,
)
