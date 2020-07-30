package org.beatonma.commons.data.core.room.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.beatonma.commons.data.core.room.entities.user.UserToken

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserToken(userToken: UserToken)

    @Query("""SELECT * FROM user_tokens WHERE google_id = :googleId""")
    fun getUserTokenFlow(googleId: String): Flow<UserToken>

    @Query("""SELECT * FROM user_tokens WHERE google_id = :googleId""")
    suspend fun getUserToken(googleId: String): UserToken

    @Delete
    fun deleteUserTokens(vararg userTokens: UserToken)
}
