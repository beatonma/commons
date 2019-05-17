package org.beatonma.commons.data.wikipedia

import androidx.room.*
import org.beatonma.commons.data.core.Profile

@Entity(
    indices = [Index("person_id")],
    tableName = "wiki_summaries",
    foreignKeys = [
        ForeignKey(
            entity = Profile::class,
            parentColumns = ["person_id"],
            childColumns = ["person_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class WikiSummary(
    @ColumnInfo(name = "person_id") val personID: Int?,
    @PrimaryKey val url: String,
    @ColumnInfo val text: String?,
    @ColumnInfo val timestamp: Long
)

@Dao
interface WikiSummaryDao {
    @Query("SELECT * FROM wiki_summaries WHERE person_id = :personID")
    suspend fun getWikiSummary(personID: Int): WikiSummary
}
