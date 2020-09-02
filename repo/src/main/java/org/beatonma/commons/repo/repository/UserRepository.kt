package org.beatonma.commons.repo.repository

import org.beatonma.commons.data.core.room.dao.UserDao
import org.beatonma.commons.data.core.room.entities.user.UserToken
import org.beatonma.commons.repo.CommonsApi
import org.beatonma.commons.repo.FlowIoResult
import org.beatonma.commons.repo.converters.composeToUserToken
import org.beatonma.commons.repo.result.cachedResultFlow
import org.beatonma.commons.repo.result.resultFlowLocalPreferred
import org.beatonma.commons.repo.result.resultFlowNoCache
import org.beatonma.commons.snommoc.models.social.ApiUserToken
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "UserRepository"

@Singleton
class UserRepository @Inject constructor(
    private val remoteSource: CommonsApi,
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

    /**
     * Refresh account token from the server, even if we have a cached token.
     */
    fun forceGetTokenForAccount(account: UserAccount): FlowIoResult<UserToken> = cachedResultFlow(
        databaseQuery = { userDao.getUserToken(account.googleId) },
        networkCall = { remoteSource.registerUser(account.googleIdToken) },
        saveCallResult = { apiToken -> saveApiToken(account, apiToken) }
    )

    fun requestRenameAccount(token: UserToken, newName: String) = resultFlowNoCache {
        remoteSource.requestRenameAccount(token, newName)
    }

    fun deleteAccount(token: UserToken) = resultFlowNoCache {
        remoteSource.deleteUserAccount(token)
    }

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
