package org.beatonma.commons.data.core.repository

import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import org.beatonma.commons.data.CommonsRemoteDataSource
import org.beatonma.commons.data.FlowIoResult
import org.beatonma.commons.data.core.room.dao.UserDao
import org.beatonma.commons.data.core.room.entities.user.ApiUserToken
import org.beatonma.commons.data.core.room.entities.user.UserToken
import org.beatonma.commons.data.resultFlowLocalPreferred
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "UserRepository"

@Singleton
class UserRepository @Inject constructor(
    private val remoteSource: CommonsRemoteDataSource,
    private val userDao: UserDao,
) {

    /**
     * Return local token if exists, otherwise contact web service to get token.
     */
    fun getTokenForAccount(account: UserAccount): FlowIoResult<UserToken> = resultFlowLocalPreferred(
        databaseQuery = { userDao.getUserToken(account.googleId) },
        networkCall = { remoteSource.registerUser(account.googleIdToken) },
        saveCallResult = { apiToken -> saveApiToken(account, apiToken) }
    )

    private suspend fun saveApiToken(account: UserAccount, apiToken: ApiUserToken) {
        val googleTokenStub = account.googleIdToken.substring(0..31)
        if (googleTokenStub == apiToken.googleTokenStub) {
            userDao.insertUserToken(apiToken.composeToUserToken(account))
        }
        else {
            throw Exception("Returned token does not match user: $googleTokenStub !~ ${apiToken.googleTokenStub}")
        }
    }
}


data class UserAccount(
    val name: String?,
    val photoUrl: String?,
    val email: String?,
    val googleId: String,
    val googleIdToken: String,
)


fun GoogleSignInAccount.toUserAccount(): UserAccount? =
    try {
        UserAccount(
            name = displayName,
            photoUrl = photoUrl?.toString(),
            email = email,
            googleId = id!!,
            googleIdToken = idToken!!,
        )
    }
    catch (e: NullPointerException) {
        Log.w(TAG, "Unable to retrieve required ID from account.")
        null
    }
