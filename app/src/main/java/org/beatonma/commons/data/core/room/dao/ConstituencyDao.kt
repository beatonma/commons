package org.beatonma.commons.data.core.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import org.beatonma.commons.data.core.room.entities.constituency.Constituency
import org.beatonma.commons.data.core.room.entities.constituency.ConstituencyBoundary
import org.beatonma.commons.data.core.room.entities.constituency.ConstituencyWithBoundary
import org.beatonma.commons.data.core.room.entities.member.BasicProfileWithParty

@Dao
interface ConstituencyDao {
    @Transaction
    @Query("""SELECT * FROM constituencies WHERE constituency_parliamentdotuk = :parliamentdotuk""")
    fun getConstituencyDetails(parliamentdotuk: Int): LiveData<ConstituencyWithBoundary>

    @Transaction
    @Query("""SELECT * FROM member_profiles WHERE constituency_id = :parliamentdotuk""")
    fun getMemberForConstituency(parliamentdotuk: Int): LiveData<BasicProfileWithParty>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConstituency(constituency: Constituency)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertConstituencyIfNotExists(constituency: Constituency)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBoundary(boundary: ConstituencyBoundary)
}
