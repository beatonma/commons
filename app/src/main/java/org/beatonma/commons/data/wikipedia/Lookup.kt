@file:JvmName("Lookup")

package org.beatonma.commons.data.wikipedia

import androidx.room.*
import org.beatonma.commons.data.twfy.Profile

/**
 * Sometimes we have to guess at a Wikipedia URL path and resolve to the
 * most likely value if it leads to a disambiguation page.
 *
 * After resolution, we store the resolved path locally.
 */
@Entity(
    indices = [Index("person_id")],
    foreignKeys = [
        ForeignKey(
            entity = Profile::class,
            parentColumns = ["person_id"],
            childColumns = ["person_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Lookup(
    @ColumnInfo(name = "person_id") val personID: Int?,
    @PrimaryKey @ColumnInfo(name = "naive_path") val naivePath: String,
    @ColumnInfo(name = "confirmed_path") val confirmedPath: String?
)

@Dao
interface LookupDao {
    @Query("SELECT * FROM lookup WHERE person_id = :personID")
    suspend fun getLookupInfo(personID: Int): Lookup

    @Insert
    suspend fun insert(lookup: Lookup)
}
