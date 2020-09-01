package org.beatonma.commons.data.core.room.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.data.FlowList
import org.beatonma.commons.data.core.room.dao.shared.SharedPartyDao
import org.beatonma.commons.data.core.room.entities.division.*

@Dao
interface DivisionDao: SharedPartyDao {

    @Transaction
    @Query("""SELECT * FROM featured_divisions""")
    fun getFeaturedDivisionsFlow(): FlowList<FeaturedDivisionWithDivision>

    @Transaction
    @Query("""SELECT * FROM divisions WHERE division_parliamentdotuk = :parliamentdotuk""")
    fun getDivisionWithVotes(parliamentdotuk: ParliamentID): Flow<DivisionWithVotes>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeaturedDivisions(featuredDivisions: List<FeaturedDivision>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDivisions(divisions: List<Division>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDivision(division: Division)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVotes(votes: List<Vote>)

//    @Transaction
//    suspend fun insertApiDivision(parliamentdotuk: ParliamentID, apiDivision: Division) {
//        insertPartiesIfNotExists(apiDivision.votes.mapNotNull { it.party })
//        insertDivision(apiDivision.toDivision())
//
//        apiDivision.votes.map { apiVote -> apiVote.toVote(parliamentdotuk) }
//
//        insertVotes(
//            apiDivision.votes.map { apiVote -> apiVote.toVote(parliamentdotuk) }
//        )
//    }
}
