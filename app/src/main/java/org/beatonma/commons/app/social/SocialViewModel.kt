package org.beatonma.commons.app.social

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import dagger.hilt.android.qualifiers.ApplicationContext
import org.beatonma.commons.BuildConfig
import org.beatonma.commons.annotations.SignInRequired
import org.beatonma.commons.app
import org.beatonma.commons.context
import org.beatonma.commons.data.IoResult
import org.beatonma.commons.data.LiveDataIoResult
import org.beatonma.commons.data.NotSignedInError
import org.beatonma.commons.data.core.repository.*
import org.beatonma.commons.data.core.room.entities.user.UserToken
import org.beatonma.commons.data.core.social.CreatedComment
import org.beatonma.commons.data.core.social.CreatedVote
import org.beatonma.commons.data.core.social.SocialContent
import org.beatonma.commons.data.core.social.SocialVoteType

private const val TAG = "SocialViewModel"

class SocialViewModel @ViewModelInject constructor(
    private val userRepository: UserRepository,
    private val socialRepository: SocialRepository,
    @ApplicationContext application: Context,
): AndroidViewModel(application.app) {

    lateinit var livedata: LiveDataIoResult<SocialContent>
    lateinit var socialTarget: SocialTarget

    suspend fun forTarget(target: SocialTarget) {
        withOptionalUserToken { token ->
            livedata = socialRepository.getSocialContent(target, token?.snommocToken ).asLiveData()
            socialTarget = target
        }
    }

    suspend fun refresh() {
        if (socialTarget != null) {
            withOptionalUserToken { token ->
                livedata = socialRepository.getSocialContent(socialTarget, token?.snommocToken).asLiveData()
            }
        }
    }

    @SignInRequired
    suspend fun postComment(text: String): IoResult<*> {
        return withUserAccount { account ->
            val token = userRepository.getActiveUserToken(account)
                ?: return NotSignedInError("", null)

            val comment = CreatedComment(
                userToken = token,
                target = socialTarget,
                text = text,
            )

            socialRepository.postComment(comment)
        }
    }

    @SignInRequired
    suspend fun postVote(voteType: SocialVoteType): IoResult<*> {
        return withUserAccount { account ->
            val token = userRepository.getActiveUserToken(account)
                ?: return NotSignedInError("User must be signed in to vote", null)

            val vote = CreatedVote(
                userToken = token,
                target = socialTarget,
                voteType = voteType
            )

            socialRepository.postVote(vote)
        }
    }

    internal fun validateComment(text: String): CommentValidation {
        val length = text.length

        return when {
            length < 10 -> CommentValidation.INVALID_TOO_SHORT
            length > BuildConfig.SOCIAL_COMMENT_MAX_LENGTH -> CommentValidation.INVALID_TOO_LONG

            // Further validation on server side
            else -> CommentValidation.VALID
        }
    }

    /**
     * Called when a user submits a vote.
     * Returns true if the voteType is the same as that already registered.
     */
    internal fun shouldRemoveVote(voteType: SocialVoteType) =
        livedata.value?.data?.userVote == voteType

    private fun getUserAccount(): UserAccount? {
        val currentGoogleAccount = GoogleSignIn.getLastSignedInAccount(context)
        return currentGoogleAccount?.toUserAccount()
    }

    private suspend inline fun withUserAccount(block: (UserAccount) -> IoResult<*>):IoResult<*> {
        val userAccount = getUserAccount()

        return if (userAccount == null) {
            NotSignedInError("Current Google Account is null")
        }
        else {
            block.invoke(userAccount)
        }
    }

    private suspend inline fun withOptionalUserAccount(block: (UserAccount?) -> IoResult<*>):IoResult<*> {
        return block.invoke(getUserAccount())
    }

    private suspend inline fun withOptionalUserToken(block: (UserToken?) -> Unit) {
        val account = getUserAccount()

        if (account == null) {
            block(null)
        }
        else {
            val userToken = userRepository.getActiveUserToken(account)
            block(userToken)
        }
    }
}


internal enum class CommentValidation {
    VALID,
    INVALID_TOO_SHORT,
    INVALID_TOO_LONG,
}
