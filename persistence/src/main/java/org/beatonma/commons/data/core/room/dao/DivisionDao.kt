package org.beatonma.commons.data.core.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.data.FlowList
import org.beatonma.commons.data.core.room.dao.shared.SharedPartyDao
import org.beatonma.commons.data.core.room.entities.division.Division
import org.beatonma.commons.data.core.room.entities.division.DivisionWithVotes
import org.beatonma.commons.data.core.room.entities.division.ResolvedZeitgeistDivision
import org.beatonma.commons.data.core.room.entities.division.Vote
import org.beatonma.commons.data.core.room.entities.division.ZeitgeistDivision

@Dao
interface DivisionDao: SharedPartyDao {

    @Transaction
    @Query("""SELECT * FROM zeitgeist_divisions""")
    fun getZeitgeistDivisions(): FlowList<ResolvedZeitgeistDivision>

    @Transaction
    @Query("""SELECT * FROM divisions WHERE division_id = :parliamentdotuk""")
    fun getDivisionWithVotes(parliamentdotuk: ParliamentID): Flow<DivisionWithVotes>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertZeitgeistDivisions(zeitgeistDivisions: List<ZeitgeistDivision>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDivisions(divisions: List<Division>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDivision(division: Division)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVotes(votes: List<Vote>)
}
