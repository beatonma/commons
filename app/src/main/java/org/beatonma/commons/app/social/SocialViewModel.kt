package org.beatonma.commons.app.social

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import org.beatonma.commons.BuildConfig
import org.beatonma.commons.app
import org.beatonma.commons.app.signin.BaseUserAccountViewModel
import org.beatonma.commons.data.core.room.entities.user.UserToken
import org.beatonma.commons.repo.FlowIoResult
import org.beatonma.commons.repo.LiveDataIoResult
import org.beatonma.commons.repo.models.CreatedComment
import org.beatonma.commons.repo.models.CreatedVote
import org.beatonma.commons.repo.repository.SocialRepository
import org.beatonma.commons.repo.repository.UserRepository
import org.beatonma.commons.repo.result.IoResult
import org.beatonma.commons.repo.result.NotSignedInError
import org.beatonma.commons.snommoc.annotations.SignInRequired
import org.beatonma.commons.snommoc.models.social.EmptySocialContent
import org.beatonma.commons.snommoc.models.social.SocialContent
import org.beatonma.commons.snommoc.models.social.SocialTarget
import org.beatonma.commons.snommoc.models.social.SocialVoteType

private const val TAG = "SocialViewModel"

class SocialViewModel @ViewModelInject constructor(
    userRepository: UserRepository,
    private val socialRepository: SocialRepository,
    @ApplicationContext application: Context,
): BaseUserAccountViewModel(userRepository, application.app) {

    lateinit var flow: FlowIoResult<SocialContent>
    val livedata: LiveDataIoResult<SocialContent> get() = flow.asLiveData()

    lateinit var socialTarget: SocialTarget

    // Keep a copy of the last populated content to avoid abrupt ui changes during refresh.
    var cachedSocialContent: SocialContent = EmptySocialContent

    private var token: UserToken? = null
    private val tokenObserver = Observer<IoResult<UserToken>> { token = it.data }

    fun forTarget(target: SocialTarget) {
        socialTarget = target
        getTokenForCurrentSignedInAccount()
        activeUserTokenLivedata?.observeForever(tokenObserver)
        fetchSocialContent()
    }

    private fun fetchSocialContent() {
        flow = socialRepository.getSocialContent(socialTarget, token?.snommocToken)
    }

    fun refresh() {
        if (::socialTarget.isInitialized) {
            fetchSocialContent()
        }
    }

    override fun onCleared() {
        activeUserTokenLivedata?.removeObserver(tokenObserver)
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

    @SignInRequired
    suspend fun updateVote(voteType: SocialVoteType): IoResult<*> = postVote(
        if (shouldRemoveVote(voteType)) {
            SocialVoteType.NULL
        }
        else {
            voteType
        }
    )

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
    //     * Called when a user submits a vote.
    //     * Returns true if the voteType is the same as that already registered.
    //     */
//    internal fun shouldRemoveVote(voteType: SocialVoteType) =
//        livedata.value?.data?.userVote == voteType

    /**
     * Called when a user submits a vote.
     * Returns true if the voteType is the same as that already registered.
     */
    private fun shouldRemoveVote(voteType: SocialVoteType): Boolean {
        println("$voteType vs ${cachedSocialContent.userVote}")
        return voteType == cachedSocialContent.userVote
    }
}

@Deprecated("Not needed in Compose ui implementation")
internal enum class CommentValidation {
    VALID,
    INVALID_TOO_SHORT,
    INVALID_TOO_LONG,
}
