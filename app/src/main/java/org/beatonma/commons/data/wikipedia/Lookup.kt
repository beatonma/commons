@file:JvmName("Lookup")

package org.beatonma.commons.data.wikipedia

import androidx.room.*

/**
 * Sometimes we have to guess at a Wikipedia URL path and resolve to the
 * most likely value if it leads to a disambiguation page.
 *
 * After resolution, we store the resolved path locally.
 */
@Entity
data class Lookup(
    @PrimaryKey val naive_path: String,
    @ColumnInfo(name = "person_id") val person_id: Int?,
    @ColumnInfo(name = "confirmed_path") val confirmedPath: String?
)

@Dao
interface LookupDao {
    @Query("SELECT confirmed_path from lookup WHERE person_id = :person_id")
    fun getWikiProfile(person_id: Int)

    @Insert
    fun insert(lookup: Lookup)
}
