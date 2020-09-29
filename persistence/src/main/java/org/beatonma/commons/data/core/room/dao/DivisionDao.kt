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
    @Query("""SELECT * FROM zeitgeist_divisions""")
    fun getZeitgeistDivisions(): FlowList<ResolvedZeitgeistDivision>

    @Deprecated("Use zeitgeist")
    @Transaction
    @Query("""SELECT * FROM featured_divisions""")
    fun getFeaturedDivisionsFlow(): FlowList<FeaturedDivisionWithDivision>

    @Transaction
    @Query("""SELECT * FROM divisions WHERE division_parliamentdotuk = :parliamentdotuk""")
    fun getDivisionWithVotes(parliamentdotuk: ParliamentID): Flow<DivisionWithVotes>

    @Deprecated("Use zeitgeist")
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeaturedDivisions(featuredDivisions: List<FeaturedDivision>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertZeitgeistDivisions(zeitgeistDivisions: List<ZeitgeistDivision>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDivisions(divisions: List<Division>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDivision(division: Division)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVotes(votes: List<Vote>)
}
