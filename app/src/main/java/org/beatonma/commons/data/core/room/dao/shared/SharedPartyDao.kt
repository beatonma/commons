package org.beatonma.commons.data.core.room.dao.shared

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import org.beatonma.commons.data.core.room.entities.member.Party

interface SharedPartyDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPartyIfNotExists(party: Party)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPartiesIfNotExists(parties: List<Party>)
}
