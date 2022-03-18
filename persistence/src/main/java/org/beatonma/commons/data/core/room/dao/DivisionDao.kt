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
import org.beatonma.commons.data.core.room.entities.division.CommonsDivision
import org.beatonma.commons.data.core.room.entities.division.CommonsDivisionData
import org.beatonma.commons.data.core.room.entities.division.CommonsDivisionVoteData
import org.beatonma.commons.data.core.room.entities.division.LordsDivision
import org.beatonma.commons.data.core.room.entities.division.LordsDivisionData
import org.beatonma.commons.data.core.room.entities.division.LordsDivisionVoteData
import org.beatonma.commons.data.core.room.entities.division.ZeitgeistDivision

@Dao
interface DivisionDao: SharedPartyDao {

    @Transaction
    @Query("""SELECT * FROM zeitgeist_divisions""")
    fun getZeitgeistDivisions(): FlowList<ZeitgeistDivision>

    @Transaction
    @Query("""SELECT * FROM commons_divisions WHERE division_id = :parliamentdotuk""")
    fun getDivisionWithVotes(parliamentdotuk: ParliamentID): Flow<CommonsDivision>

    @Transaction
    @Query("""SELECT * FROM lords_divisions WHERE ldivision_id = :divisionId""")
    fun getLordsDivision(divisionId: ParliamentID): Flow<LordsDivision>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertZeitgeistDivisions(zeitgeistDivisions: List<ZeitgeistDivision>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDivisions(divisions: List<CommonsDivisionData>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDivision(division: CommonsDivisionData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVotes(votes: List<CommonsDivisionVoteData>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLordsDivisionData(data: LordsDivisionData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLordsDivisionVotes(votes: List<LordsDivisionVoteData>)

    @Transaction
    suspend fun safeInsertLordsDivision(
        data: LordsDivisionData,
        votes: List<LordsDivisionVoteData>,
    ) {
        insertLordsDivisionData(data)
        insertLordsDivisionVotes(votes)
    }
}
