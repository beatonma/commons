package org.beatonma.commons.app.ui.screens.social

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.beatonma.commons.app.data.SavedStateKey
import org.beatonma.commons.app.data.set
import org.beatonma.commons.core.extensions.autotag
import org.beatonma.commons.data.core.interfaces.Sociable
import org.beatonma.commons.data.core.room.entities.user.UserToken
import org.beatonma.commons.repo.asSocialTarget
import org.beatonma.commons.repo.models.CreatedComment
import org.beatonma.commons.repo.models.CreatedVote
import org.beatonma.commons.repo.repository.SocialRepository
import org.beatonma.commons.repo.result.IoResult
import org.beatonma.commons.repo.result.isSuccess
import org.beatonma.commons.repo.result.onError
import org.beatonma.commons.repo.result.onErrorCode
import org.beatonma.commons.repo.result.onSuccess
import org.beatonma.commons.snommoc.annotations.SignInRequired
import org.beatonma.commons.snommoc.models.social.EmptySocialContent
import org.beatonma.commons.snommoc.models.social.SocialContent
import org.beatonma.commons.snommoc.models.social.SocialTarget
import org.beatonma.commons.snommoc.models.social.SocialVoteType
import javax.inject.Inject

private const val TAG = "SocialViewModel"

private val SavedSocialContent = SavedStateKey("social_content")
private val SavedUiState = SavedStateKey("ui_state") // TODO

@HiltViewModel
class SocialViewModel @Inject constructor(
    private val socialRepository: SocialRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    val livedata: LiveData<SocialContent> =
        savedStateHandle.getLiveData(SavedSocialContent.name, EmptySocialContent)

    val uiState = mutableStateOf(SocialUiState.Collapsed)

    lateinit var socialTarget: SocialTarget

    fun forTarget(target: SocialTarget, userToken: UserToken?) {
        if (!this::socialTarget.isInitialized) {
            socialTarget = target
            refresh(userToken)
        }
    }

    fun forTarget(target: Sociable, userToken: UserToken?) {
        if (!this::socialTarget.isInitialized) {
            socialTarget = target.asSocialTarget()
            refresh(userToken)
        }
    }

    fun refresh(userToken: UserToken?) {
        viewModelScope.launch(Dispatchers.IO) {
            fetchSocialContent(userToken)
        }
    }

    @SignInRequired
    fun postComment(text: String, userToken: UserToken) {
        submitSocialContent(userToken) {
            val comment = CreatedComment(
                userToken = userToken,
                target = socialTarget,
                text = text,
            )

            socialRepository.postComment(comment)
        }
    }

    @SignInRequired
    private fun postVote(voteType: SocialVoteType, userToken: UserToken) {
        submitSocialContent(userToken) {
            val vote = CreatedVote(
                userToken = userToken,
                target = socialTarget,
                voteType = voteType
            )

            socialRepository.postVote(vote)
        }
    }

    @SignInRequired
    fun updateVote(voteType: SocialVoteType, userToken: UserToken) {
        postVote(
            voteType = if (shouldRemoveVote(voteType)) SocialVoteType.NULL else voteType,
            userToken = userToken
        )
    }

    private fun <T> submitSocialContent(userToken: UserToken, block: suspend () -> IoResult<T>) {
        viewModelScope.launch(Dispatchers.IO) {
            block()
                .onSuccess { refresh(userToken) }
                .onError { e -> Log.w(autotag, "Social content submission failed $e") }
                .onErrorCode { e -> Log.w(autotag, "Social content submission failed $e") }
        }
    }

    /**
     * Called when a user submits a vote.
     * Returns true if the voteType is the same as that already registered.
     */
    private fun shouldRemoveVote(voteType: SocialVoteType): Boolean =
        voteType == livedata.value?.userVote

    private suspend fun fetchSocialContent(userToken: UserToken?) {
        socialRepository.getSocialContent(
            socialTarget,
            userToken?.snommocToken
        ).first { it.isSuccess }.onSuccess { data ->
            withContext(Dispatchers.Main) {
                savedStateHandle[SavedSocialContent] = data
            }
        }
    }
}
