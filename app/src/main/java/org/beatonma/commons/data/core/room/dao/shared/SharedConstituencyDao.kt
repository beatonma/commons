package org.beatonma.commons.data.core.room.dao.shared

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import org.beatonma.commons.data.core.room.entities.constituency.Constituency


interface SharedConstituencyDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertConstituencyIfNotExists(constituency: Constituency)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertConstituenciesIfNotExists(constituencies: List<Constituency>)
}
