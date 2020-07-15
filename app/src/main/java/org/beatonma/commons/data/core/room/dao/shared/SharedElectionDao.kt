package org.beatonma.commons.data.core.room.dao.shared

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import org.beatonma.commons.data.core.room.entities.election.Election

interface SharedElectionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertElections(elections: List<Election>)
}
