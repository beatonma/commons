package org.beatonma.commons.data.core.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import org.beatonma.commons.data.core.room.entities.division.*

@Dao
interface DivisionDao {
    @Transaction
    @Query("""SELECT * FROM featured_divisions""")
    fun getFeaturedDivisions(): LiveData<List<FeaturedDivisionWithDivision>>

    @Query("""SELECT * FROM divisions WHERE division_parliamentdotuk = :parliamentdotuk""")
    fun getDivision(parliamentdotuk: Int): LiveData<Division>

    @Transaction
    @Query("""SELECT * FROM divisions WHERE division_parliamentdotuk = :parliamentdotuk""")
    fun getDivisionWithVotes(parliamentdotuk: Int): LiveData<DivisionWithVotes>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeaturedDivisions(featuredDivisions: List<FeaturedDivision>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDivisions(divisions: List<Division>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDivision(division: Division)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVotes(votes: List<Vote>)


    @Transaction
    suspend fun insertApiDivision(parliamentdotuk: Int, apiDivision: ApiDivision) {
        insertDivision(apiDivision.toDivision())
        insertVotes(
            apiDivision.votes.map { apiVote -> apiVote.toVote(parliamentdotuk) }
        )
    }
}
