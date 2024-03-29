package org.beatonma.commons.data.core.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.data.FlowList
import org.beatonma.commons.data.core.room.dao.shared.SharedConstituencyDao
import org.beatonma.commons.data.core.room.dao.shared.SharedElectionDao
import org.beatonma.commons.data.core.room.entities.constituency.Constituency
import org.beatonma.commons.data.core.room.entities.constituency.ConstituencyBoundary
import org.beatonma.commons.data.core.room.entities.constituency.ConstituencyCandidate
import org.beatonma.commons.data.core.room.entities.constituency.ConstituencyElectionDetails
import org.beatonma.commons.data.core.room.entities.constituency.ConstituencyElectionDetailsWithCandidates
import org.beatonma.commons.data.core.room.entities.constituency.ConstituencyWithBoundary
import org.beatonma.commons.data.core.room.entities.election.ConstituencyResult
import org.beatonma.commons.data.core.room.entities.election.ConstituencyResultWithDetails
import org.beatonma.commons.data.core.room.entities.election.Election

@Dao
interface ConstituencyDao : SharedConstituencyDao, SharedElectionDao {
    // Get operations
    @Transaction
    @Query("""SELECT * FROM constituencies WHERE constituency_id = :constituencyId""")
    fun getConstituencyWithBoundary(constituencyId: ParliamentID): Flow<ConstituencyWithBoundary>

    @Transaction
    @Query(
        """
        SELECT * FROM constituency_results
        INNER JOIN member_profiles ON member_profiles.member_id = constituency_results.result_member_id
        INNER JOIN elections ON elections.election_id = constituency_results.result_election_id
        INNER JOIN parties ON parties.party_id = member_party_id
        WHERE constituency_results.result_constituency_id = :constituencyId
        ORDER BY elections.election_date DESC
        """
    )
    fun getElectionResults(constituencyId: ParliamentID): FlowList<ConstituencyResultWithDetails>

    @Transaction
    @Query(
        """SELECT * FROM constituency_election_results
        WHERE c_e_r_constituency_id = :constituencyId
        AND c_e_r_election_id = :electionId"""
    )
    fun getDetailsAndCandidatesForElection(
        constituencyId: ParliamentID,
        electionId: ParliamentID,
    ): Flow<ConstituencyElectionDetailsWithCandidates>

    @Query("""SELECT * FROM constituencies WHERE constituency_id = :constituencyId""")
    fun getConstituency(constituencyId: ParliamentID): Flow<Constituency>

    @Query("""SELECT * FROM elections WHERE election_id = :electionId""")
    fun getElection(electionId: ParliamentID): Flow<Election>


    // Insert operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConstituencies(constituency: List<Constituency>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConstituency(constituency: Constituency)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBoundary(boundary: ConstituencyBoundary)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertElectionResults(electionResults: List<ConstituencyResult>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConstituencyElectionDetails(constituencyElectionDetails: ConstituencyElectionDetails)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCandidates(candidates: List<ConstituencyCandidate>)

    suspend fun safeInsertConstituency(constituency: Constituency?, ifNotExists: Boolean = false) {
        constituency ?: return

        if (ifNotExists) {
            insertConstituencyIfNotExists(constituency)
        } else {
            insertConstituency(constituency)
        }
    }

    suspend fun safeInsertConstituencies(
        constituencies: List<Constituency>,
        ifNotExists: Boolean = false,
    ) {
        if (ifNotExists) {
            insertConstituenciesIfNotExists(constituencies)
        } else {
            insertConstituencies(constituencies)
        }
    }

    @Transaction
    suspend fun safeInsertElectionResults(
        constituency: Constituency,
        election: Election,
        results: ConstituencyElectionDetails,
        candidates: List<ConstituencyCandidate>,
    ) {
        safeInsertConstituency(constituency, ifNotExists = true)
        insertElection(election)
        insertConstituencyElectionDetails(results)
        insertCandidates(candidates)
    }
}
