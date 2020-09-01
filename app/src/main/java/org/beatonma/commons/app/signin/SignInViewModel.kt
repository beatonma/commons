package org.beatonma.commons.app.signin

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import dagger.hilt.android.qualifiers.ApplicationContext
import org.beatonma.commons.BuildConfig
import org.beatonma.commons.app
import org.beatonma.commons.data.LoadingResult
import org.beatonma.commons.data.NetworkError
import org.beatonma.commons.data.SuccessCodeResult
import org.beatonma.commons.data.await
import org.beatonma.commons.network.core.Http
import org.beatonma.commons.network.core.NetworkException
import org.beatonma.commons.repo.repository.UserAccount
import org.beatonma.commons.repo.repository.UserRepository

private const val TAG = "SignInViewModel"

/**
 * Implementation of [BaseUserAccountViewModel] which allows signing in/out.
 */
class SignInViewModel @ViewModelInject constructor(
    repository: UserRepository,
    private val googleSignInClient: GoogleSignInClient,
    @ApplicationContext application: Context,
): BaseUserAccountViewModel(repository, application.app) {

    val signInIntent: Intent get() = googleSignInClient.signInIntent

    /**
     * Handle the response from [signInIntent]
     */
    fun getTokenFromSignInResult(data: Intent?) {
        val account = getUserAccountFromSignInResult(data)
        if (account != null) {
            getTokenForAccount(account)
        }
    }

    internal suspend fun requestRename(newName: String): RenameResult {
        val token = activeUserToken?.value?.data ?: return RenameResult.NOT_SIGNED_IN
        val localValidationResult = quickValidate(token.username, newName)
        if (localValidationResult != RenameResult.OK) {
            return localValidationResult
        }

        val result = repository.requestRenameAccount(token, newName).await {
            it !is LoadingResult
        }

        if (result is SuccessCodeResult && Http.Status.isSuccess(result.responseCode)){
            return RenameResult.OK
        }

        else if (result is NetworkError && result.error is NetworkException) {
            val code = (result.error as NetworkException).code
            return when {
                code == Http.Status.FORBIDDEN_403 -> RenameResult.SERVER_DENIED
                Http.Status.isServerError(code) -> RenameResult.SERVER_ERROR
                else -> RenameResult.SERVER_BAD_REQUEST
            }
        }
        return RenameResult.ERROR
    }

    fun signOut(): Task<Void> {
        activeUserToken = null
        return googleSignInClient.signOut()
    }
}


internal enum class RenameResult {
    NO_CHANGE,  // New name is the same as the old one
    OK,  // Server renamed the account successfully.
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
private suspend fun quickValidate(oldName: String, newName: String): RenameResult = when {
    oldName == newName -> RenameResult.NO_CHANGE
    newName.length > BuildConfig.ACCOUNT_USERNAME_MAX_LENGTH -> RenameResult.TOO_LONG
    newName.length < BuildConfig.ACCOUNT_USERNAME_MIN_LENGTH -> RenameResult.TOO_SHORT
    !newName.first().isLetterOrDigit() -> RenameResult.BAD_START_OR_END
    !newName.last().isLetterOrDigit() -> RenameResult.BAD_START_OR_END
    else -> RenameResult.OK
}


private fun getUserAccountFromSignInResult(data: Intent?): UserAccount? {
    try {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        val account = task.getResult(ApiException::class.java)
        return account?.toUserAccount()
    }
    catch(e: ApiException) {
        Log.w(TAG, "Google sign in failed - code=${e.statusCode}")
    }
    return null
}
