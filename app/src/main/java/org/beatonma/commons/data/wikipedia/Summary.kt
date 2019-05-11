@file:JvmName("Summary")

package org.beatonma.commons.data.wikipedia

import androidx.room.*

@Entity
data class WikiSummary(
    @PrimaryKey val url: String,
    @ColumnInfo(name = "person_id") val personID: Int?,
    @ColumnInfo(name = "summary_text") val text: String?,
    @ColumnInfo(name = "timestamp") val timestamp: Long
)

@Dao
interface WikiWummaryDao {
    @Query("SELECT * FROM wikisummary WHERE person_id = :person_id")
    fun getWikiSummary(person_id: Int): WikiSummary
}
