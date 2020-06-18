package org.beatonma.commons.data.core.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import org.beatonma.commons.data.core.room.entities.user.UserToken

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserToken(userToken: UserToken)

    @Query("""SELECT * FROM user_tokens WHERE google_id = :googleId""")
    fun getUserToken(googleId: String): LiveData<UserToken>

    @Query("""SELECT * FROM user_tokens WHERE google_id = :googleId""")
    suspend fun getUserTokenSync(googleId: String): UserToken?

    @Delete
    fun deleteUserTokens(vararg userTokens: UserToken)
}
