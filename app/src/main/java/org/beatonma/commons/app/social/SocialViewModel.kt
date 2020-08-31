package org.beatonma.commons.app.social

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import org.beatonma.commons.BuildConfig
import org.beatonma.commons.app
import org.beatonma.commons.app.signin.BaseUserAccountViewModel
import org.beatonma.commons.data.IoResult
import org.beatonma.commons.data.LiveDataIoResult
import org.beatonma.commons.data.NotSignedInError
import org.beatonma.commons.data.core.repository.SocialRepository
import org.beatonma.commons.data.core.repository.SocialTarget
import org.beatonma.commons.data.core.repository.UserRepository
import org.beatonma.commons.data.core.room.entities.user.UserToken
import org.beatonma.commons.data.core.social.CreatedComment
import org.beatonma.commons.data.core.social.CreatedVote
import org.beatonma.commons.data.core.social.SocialContent
import org.beatonma.commons.data.core.social.SocialVoteType
import org.beatonma.commons.snommoc.annotations.SignInRequired

private const val TAG = "SocialViewModel"

class SocialViewModel @ViewModelInject constructor(
    userRepository: UserRepository,
    private val socialRepository: SocialRepository,
    @ApplicationContext application: Context,
): BaseUserAccountViewModel(userRepository, application.app) {

    lateinit var livedata: LiveDataIoResult<SocialContent>
    lateinit var socialTarget: SocialTarget

    private var token: UserToken? = null
    private val tokenObserver = Observer<IoResult<UserToken>> { token = it.data }

    fun forTarget(target: SocialTarget) {
        getTokenForCurrentSignedInAccount()
        activeUserToken?.observeForever(tokenObserver)
        livedata = socialRepository.getSocialContent(target, token?.snommocToken ).asLiveData()
        socialTarget = target
    }

    fun refresh() {
        if (::socialTarget.isInitialized) {
            livedata = socialRepository.getSocialContent(socialTarget, token?.snommocToken).asLiveData()
        }
    }

    override fun onCleared() {
        activeUserToken?.removeObserver(tokenObserver)
        super.onCleared()
    }

    @SignInRequired
    suspend fun postComment(text: String): IoResult<*> {
        val token = token ?: return NotSignedInError("User must be signed in to post a comment")

        val comment = CreatedComment(
            userToken = token,
            target = socialTarget,
            text = text,
        )

        return socialRepository.postComment(comment)
    }

    @SignInRequired
    suspend fun postVote(voteType: SocialVoteType): IoResult<*> {
        val token = token ?: return NotSignedInError("User must be signed in to vote")

        val vote = CreatedVote(
            userToken = token,
            target = socialTarget,
            voteType = voteType
        )

        return socialRepository.postVote(vote)
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
}


internal enum class CommentValidation {
    VALID,
    INVALID_TOO_SHORT,
    INVALID_TOO_LONG,
}
