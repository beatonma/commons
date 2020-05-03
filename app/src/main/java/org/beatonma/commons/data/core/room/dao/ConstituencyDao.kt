package org.beatonma.commons.data.core.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import org.beatonma.commons.data.core.room.entities.constituency.Constituency
import org.beatonma.commons.data.core.room.entities.constituency.ConstituencyBoundary
import org.beatonma.commons.data.core.room.entities.constituency.ConstituencyWithBoundary
import org.beatonma.commons.data.core.room.entities.election.ConstituencyResult
import org.beatonma.commons.data.core.room.entities.election.ConstituencyResultWithDetails

@Dao
interface ConstituencyDao {
    @Transaction
    @Query("""SELECT * FROM constituencies WHERE constituency_parliamentdotuk = :parliamentdotuk""")
    fun getConstituencyWithBoundary(parliamentdotuk: Int): LiveData<ConstituencyWithBoundary>

    @Query("""SELECT * FROM constituency_results
        INNER JOIN member_profiles ON member_profiles.parliamentdotuk = constituency_results.result_member_id
        INNER JOIN elections ON elections.election_parliamentdotuk = constituency_results.result_election_id
        INNER JOIN parties ON parties.party_parliamentdotuk = member_profiles.party_id
        WHERE constituency_results.result_constituency_id = :parliamentdotuk""")
    fun getElectionResults(parliamentdotuk: Int): LiveData<List<ConstituencyResultWithDetails>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConstituencies(constituency: List<Constituency>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertConstituenciesIfNotExists(constituency: List<Constituency>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConstituency(constituency: Constituency)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertConstituencyIfNotExists(constituency: Constituency)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBoundary(boundary: ConstituencyBoundary)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertElectionResults(electionResults: List<ConstituencyResult>)
}
