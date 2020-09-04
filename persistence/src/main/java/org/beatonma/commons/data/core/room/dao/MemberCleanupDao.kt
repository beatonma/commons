package org.beatonma.commons.data.core.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import org.beatonma.commons.data.core.room.entities.member.MemberProfile

@Dao
interface MemberCleanupDao {
    @Query("""SELECT * FROM member_profiles""")
    fun getAllMemberProfilesSync(): List<MemberProfile>

    @Delete
    suspend fun deleteProfile(profile: MemberProfile)

    @Delete
    suspend fun deleteProfiles(profiles: List<MemberProfile>)
}
