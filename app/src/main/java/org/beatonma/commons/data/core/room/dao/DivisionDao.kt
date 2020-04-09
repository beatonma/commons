package org.beatonma.commons.data.core.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import org.beatonma.commons.data.core.room.entities.division.*

@Dao
interface DivisionDao {
    @Query("""SELECT * FROM featured_divisions""")
    fun getFeaturedDivisions(): LiveData<List<FeaturedDivisionWithDivision>>

    @Query("""SELECT * FROM divisions WHERE division_parliamentdotuk = :parliamentdotuk""")
    fun getDivision(parliamentdotuk: Int): LiveData<Division>

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
    suspend fun insertApiDivision(parliamentdotuk: Int, division: ApiDivision) {
        insertDivision(
            Division(
                parliamentdotuk = division.parliamentdotuk,
                title = division.title,
                date = division.date,
                ayes = division.ayes,
                noes = division.noes,
                passed = division.passed,
                house = division.house
        ))

        insertVotes(
            division.votes.map { vote ->
                Vote(
                    divisionId = parliamentdotuk,
                    memberId = vote.memberId,
                    memberName = vote.memberName,
                    voteType = vote.voteType
                )
            }
        )
    }
}
