package org.beatonma.commons.data.core.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import org.beatonma.commons.data.LiveDataList
import org.beatonma.commons.data.ParliamentID
import org.beatonma.commons.data.core.room.dao.shared.SharedConstituencyDao
import org.beatonma.commons.data.core.room.dao.shared.SharedElectionDao
import org.beatonma.commons.data.core.room.entities.constituency.*
import org.beatonma.commons.data.core.room.entities.election.ConstituencyResult
import org.beatonma.commons.data.core.room.entities.election.ConstituencyResultWithDetails
import org.beatonma.commons.data.core.room.entities.election.Election
import org.beatonma.commons.data.core.room.entities.member.Party

@Dao
interface ConstituencyDao: SharedConstituencyDao, SharedElectionDao {

    @Transaction
    @Query("""SELECT * FROM constituencies WHERE constituency_parliamentdotuk = :parliamentdotuk""")
    fun getConstituencyWithBoundary(parliamentdotuk: ParliamentID): LiveData<ConstituencyWithBoundary>

    @Transaction
    @Query("""SELECT * FROM constituency_results
        INNER JOIN member_profiles ON member_profiles.parliamentdotuk = constituency_results.result_member_id
        INNER JOIN elections ON elections.election_parliamentdotuk = constituency_results.result_election_id
        INNER JOIN parties ON parties.party_parliamentdotuk = member_profiles.party_id
        WHERE constituency_results.result_constituency_id = :parliamentdotuk""")
    fun getElectionResults(parliamentdotuk: ParliamentID): LiveDataList<ConstituencyResultWithDetails>

    @Query("""SELECT * FROM constituency_election_results
        WHERE c_e_r_constituency_id = :constituencyId
        AND c_e_r_election_id = :electionId""")
    fun getDetailsForElection(
        constituencyId: ParliamentID,
        electionId: ParliamentID,
    ): LiveData<ConstituencyElectionDetails>

    @Transaction
    @Query("""SELECT * FROM constituency_election_results
        WHERE c_e_r_constituency_id = :constituencyId
        AND c_e_r_election_id = :electionId""")
    fun getDetailsAndCandidatesForElection(
        constituencyId: ParliamentID,
        electionId: ParliamentID,
    ): LiveData<ConstituencyElectionDetailsWithCandidates?>
    
    @Query("""SELECT * FROM parties WHERE party_name LIKE :fuzzyName""")
    fun getPartyByName(fuzzyName: String): LiveData<Party?>

    @Query("""SELECT * FROM constituencies WHERE constituency_parliamentdotuk = :parliamentdotuk""")
    fun getConstituency(parliamentdotuk: ParliamentID): LiveData<Constituency>

    @Query("""SELECT * FROM elections WHERE election_parliamentdotuk = :parliamentdotuk""")
    fun getElection(parliamentdotuk: ParliamentID): LiveData<Election>

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
        }
        else {
            insertConstituency(constituency)
        }
    }

    suspend fun safeInsertConstituencies(constituencies: List<Constituency>, ifNotExists: Boolean = false) {
        if (ifNotExists) {
            insertConstituenciesIfNotExists(constituencies)
        }
        else {
            insertConstituencies(constituencies)
        }
    }
}
