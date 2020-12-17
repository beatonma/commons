package org.beatonma.commons.app.memberprofile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Providers
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.beatonma.commons.app.signin.UserAccountViewModel
import org.beatonma.commons.app.signin.compose.AmbientUserToken
import org.beatonma.commons.app.signin.compose.NullUserToken
import org.beatonma.commons.app.social.SocialUiState
import org.beatonma.commons.app.social.SocialViewModel
import org.beatonma.commons.app.social.compose.AmbientSocialActions
import org.beatonma.commons.app.social.compose.AmbientSocialContent
import org.beatonma.commons.app.social.compose.AmbientSocialUiState
import org.beatonma.commons.app.social.compose.SocialActions
import org.beatonma.commons.app.ui.compose.WithResultData
import org.beatonma.commons.app.ui.compose.composeView
import org.beatonma.commons.app.ui.navigation.BackPressConsumer
import org.beatonma.commons.compose.util.update
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.core.extensions.autotag
import org.beatonma.commons.data.core.room.entities.user.UserToken
import org.beatonma.commons.kotlin.extensions.getParliamentID
import org.beatonma.commons.repo.result.IoLoading
import org.beatonma.commons.repo.result.IoResult
import org.beatonma.commons.repo.result.onError
import org.beatonma.commons.repo.result.onSuccess
import org.beatonma.commons.snommoc.models.social.EmptySocialContent
import org.beatonma.commons.snommoc.models.social.SocialVoteType

@AndroidEntryPoint
class MemberProfileComposeFragment : Fragment(),
    BackPressConsumer {
    private val viewmodel: ComposeMemberProfileViewModel by viewModels()

    private val userAccountViewModel: UserAccountViewModel by activityViewModels()
    private val socialViewModel: SocialViewModel by viewModels()
    private val socialUiState = mutableStateOf(SocialUiState.Collapsed)

    private fun getMemberIdFromBundle(): ParliamentID = arguments.getParliamentID()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Member data
        val memberId = getMemberIdFromBundle()
        val flow = viewmodel.forMember(memberId)

        return composeView {
            val result by flow.collectAsState(initial = IoLoading)
            val activeUserState =
                userAccountViewModel.userTokenLiveData.observeAsState(NullUserToken)
            val social = socialViewModel.livedata.observeAsState(EmptySocialContent)

            val socialCallbacks =
                SocialActions(
                    onVoteUpClick = { onVoteClicked(SocialVoteType.aye, activeUserState.value) },
                    onVoteDownClick = { onVoteClicked(SocialVoteType.no, activeUserState.value) },
                    onExpandedCommentIconClick = { },
                    onCommentClick = { comment ->
                        println("Clicked comment $comment")
                    },
                    onCreateCommentClick = { socialUiState.update(SocialUiState.ComposeComment) },
                    onCommentSubmitClick = { commentText ->
                        onPostComment(commentText,
                            activeUserState.value)
                    }
                )

            WithResultData(result) { data ->
                socialViewModel.forTarget(data.profile, activeUserState.value)

                Providers(
                    AmbientSocialActions provides socialCallbacks,
                    AmbientSocialUiState provides socialUiState,
                    AmbientSocialContent provides social.value,
                    AmbientUserToken provides activeUserState.value,
                ) {
                    MemberProfileLayout(data)
                }
            }
        }
    }

    private fun onVoteClicked(voteType: SocialVoteType, userToken: UserToken) {
        onSubmitSocialContent(userToken) { socialViewModel.updateVote(voteType, userToken) }
    }

    private fun onPostComment(text: String, userToken: UserToken) {
        onSubmitSocialContent(userToken) { socialViewModel.postComment(text, userToken) }
        socialUiState.update(SocialUiState.Expanded)
    }

    private fun <T> onSubmitSocialContent(userToken: UserToken, block: suspend () -> IoResult<T>) {
        lifecycleScope.launch(Dispatchers.IO) {
            block()
                .onSuccess { refreshSocialContent(userToken) }
                .onError { e ->
                    Log.w(autotag, "social content submission failed: ${e.message} $e")
                }
        }
    }

    private fun refreshSocialContent(userToken: UserToken?) {
        socialViewModel.refresh(userToken)
    }

    override fun onBackPressed(): Boolean = when (socialUiState.value) {
        SocialUiState.Expanded -> {
            socialUiState.update(SocialUiState.Collapsed)
            true
        }

        SocialUiState.ComposeComment -> {
            socialUiState.update(SocialUiState.Expanded)
            true
        }

        else -> false
    }
}
