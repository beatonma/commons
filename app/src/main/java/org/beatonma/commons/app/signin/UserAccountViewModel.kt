package org.beatonma.commons.app.signin

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.beatonma.commons.BuildConfig
import org.beatonma.commons.app
import org.beatonma.commons.context
import org.beatonma.commons.core.extensions.autotag
import org.beatonma.commons.data.core.room.entities.user.UserToken
import org.beatonma.commons.network.core.Http
import org.beatonma.commons.repo.ResultFlow
import org.beatonma.commons.repo.repository.GoogleAccount
import org.beatonma.commons.repo.repository.UserRepository
import org.beatonma.commons.repo.result.isComplete
import org.beatonma.commons.repo.result.onResponseCode
import org.beatonma.commons.repo.result.onSuccess
import javax.inject.Inject

private const val TAG = "SignInViewModel"

private const val SavedStateUserToken = "usertoken"

@HiltViewModel
class UserAccountViewModel @Inject constructor(
    private val repository: UserRepository,
    private val googleSignInClient: GoogleSignInClient,
    @ApplicationContext application: Context,
    private val savedStateHandle: SavedStateHandle,
) : AndroidViewModel(application.app) {

    val userTokenLiveData: LiveData<UserToken> =
        savedStateHandle.getLiveData(SavedStateUserToken, NullUserToken)
    val signInIntent: Intent get() = googleSignInClient.signInIntent

    init {
        getTokenForCurrentSignedInAccountFlow()
    }

    /**
     * Handle the response from [signInIntent]
     */
    fun getTokenFromSignInResult(data: Intent?) {
        val account = getGoogleAccountFromSignInResult(data)
        if (account != null) {
            getTokenForAccount(account)
        }
    }

    internal suspend fun requestRename(userToken: UserToken, newName: String): RenameResult {
        val localValidationResult = quickValidate(userToken.username, newName)
        if (localValidationResult != RenameResult.ACCEPTED) {
            return localValidationResult
        }

        repository.requestRenameAccount(userToken, newName)
            .filter { it.isComplete }
            .first()
            .onSuccess {
                refreshUsername(userToken)
                return RenameResult.ACCEPTED
            }
            .onResponseCode { responseCode ->
                val code = responseCode.code
                return when {
                    code == Http.Status.FORBIDDEN_403 -> RenameResult.SERVER_DENIED
                    Http.Status.isServerError(code) -> RenameResult.SERVER_ERROR
                    else -> RenameResult.SERVER_BAD_REQUEST
                }
            }

        return RenameResult.ERROR
    }

    suspend fun deleteAccount(userToken: UserToken): ResultFlow<Void> {
        signOut()
        return repository.deleteAccount(userToken)
    }

    fun signOut(): Task<Void> {
        savedStateHandle[SavedStateUserToken] = NullUserToken
        return googleSignInClient.signOut()
    }

    /**
     * Force a refresh from server.
     */
    private fun refreshUsername(userToken: UserToken) {
        repository.refreshUsername(userToken).cacheTokenResult()
    }

    /**
     * If user is signed in with their Google account, use it to retrieve a UserToken that
     * will enable interaction social features of Commons.
     */
    private fun getTokenForCurrentSignedInAccountFlow() =
        withCurrentGoogleAccount(default = null) { account ->
            repository.getTokenForAccount(account).cacheTokenResult()
        }

    private fun getTokenForAccount(account: GoogleAccount) {
        repository.getTokenForAccount(account).cacheTokenResult()
    }

    private fun ResultFlow<UserToken>.cacheTokenResult() {
        viewModelScope.launch {
            collect { result ->
                result.onSuccess { data ->
                    savedStateHandle[SavedStateUserToken] = data
                }
            }
        }
    }

    private inline fun <T> withCurrentGoogleAccount(
        default: T,
        block: (googleAccount: GoogleAccount) -> T,
    ): T {
        val currentGoogleAccount = GoogleSignIn.getLastSignedInAccount(context)
        val userAccount = currentGoogleAccount?.asGoogleAccount()
        if (userAccount != null) {
            return block.invoke(userAccount)
        }
        Log.i(autotag, "Unable to get user account")
        return default
    }
}

enum class RenameResult {
    NO_CHANGE,  // New name is the same as the old one
    ACCEPTED,  // Server renamed the account successfully.
    TOO_SHORT,
    TOO_LONG,
    BAD_START_OR_END,  // Name must start and end with alphanumeric characters.
    SERVER_DENIED,  // Server validation failed or the username is taken/reserved/blocked.
    SERVER_BAD_REQUEST,  // Request is missing some required content.
    SERVER_ERROR,  // HTTP response >= 500
    ERROR,  // Something else went wrong
    NOT_SIGNED_IN,
    ;
}

/**
 * Check basic validation rules before submitting to server.
 * The server runs its own validation but we can check some of the basics locally.
 */
private fun quickValidate(oldName: String, newName: String): RenameResult = when {
    oldName == newName -> RenameResult.NO_CHANGE
    newName.length > BuildConfig.ACCOUNT_USERNAME_MAX_LENGTH -> RenameResult.TOO_LONG
    newName.length < BuildConfig.ACCOUNT_USERNAME_MIN_LENGTH -> RenameResult.TOO_SHORT
    !newName.first().isLetterOrDigit() -> RenameResult.BAD_START_OR_END
    !newName.last().isLetterOrDigit() -> RenameResult.BAD_START_OR_END
    else -> RenameResult.ACCEPTED
}

private fun getGoogleAccountFromSignInResult(data: Intent?): GoogleAccount? {
    try {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        val account = task.getResult(ApiException::class.java)
        return account?.asGoogleAccount()
    }
    catch (e: ApiException) {
        Log.w(TAG, "Google sign in failed - code=${e.statusCode}")
    }
    Log.w(TAG, "Google sign in failed!")
    return null
}

private fun GoogleSignInAccount.asGoogleAccount(): GoogleAccount? =
    try {
        GoogleAccount(
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
