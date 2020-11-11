package org.beatonma.commons.app.memberprofile.compose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Providers
import androidx.compose.runtime.ambientOf
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.beatonma.commons.app.signin.AmbientUserToken
import org.beatonma.commons.app.signin.NullUserToken
import org.beatonma.commons.app.social.SocialUiState
import org.beatonma.commons.app.social.SocialViewModel
import org.beatonma.commons.app.social.compose.AmbientSocialActions
import org.beatonma.commons.app.social.compose.AmbientSocialContent
import org.beatonma.commons.app.social.compose.AmbientSocialUiState
import org.beatonma.commons.app.social.compose.SocialActions
import org.beatonma.commons.app.ui.colors.PartyColors
import org.beatonma.commons.app.ui.colors.Themed
import org.beatonma.commons.app.ui.compose.composeView
import org.beatonma.commons.app.ui.compose.withSystemUi
import org.beatonma.commons.app.ui.navigation.BackPressConsumer
import org.beatonma.commons.compose.util.update
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.core.extensions.withNotNull
import org.beatonma.commons.kotlin.extensions.getParliamentID
import org.beatonma.commons.repo.asSocialTarget
import org.beatonma.commons.repo.result.LoadingResult
import org.beatonma.commons.snommoc.models.social.EmptySocialContent
import org.beatonma.commons.snommoc.models.social.SocialTarget
import org.beatonma.commons.snommoc.models.social.SocialTargetType
import org.beatonma.commons.snommoc.models.social.SocialVoteType

internal val ViewmodelAmbient =
    ambientOf<ComposeMemberProfileViewModel> { error("Viewmodel not set") }

@AndroidEntryPoint
class MemberProfileComposeFragment : Fragment(),
    Themed,
    BackPressConsumer {
    private val viewmodel: ComposeMemberProfileViewModel by viewModels()
    private val socialViewModel: SocialViewModel by viewModels()
    private val socialUiState = mutableStateOf(SocialUiState.Expanded)

    override var theme: PartyColors? = null

    private fun getMemberIdFromBundle(): ParliamentID = arguments.getParliamentID()

    private fun onVoteClicked(voteType: SocialVoteType) {
        println("onVoteClicked $voteType") // TODO handle result and refresh ui state
        lifecycleScope.launch(Dispatchers.IO) {
            socialViewModel.postVote(
                if (socialViewModel.shouldRemoveVote(voteType)) {
                    SocialVoteType.NULL
                }
                else {
                    voteType
                }
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val memberId = getMemberIdFromBundle()
        val flow = viewmodel.forMember(memberId)
        socialViewModel.forTarget(SocialTarget(SocialTargetType.member, parliamentdotuk = memberId))
        val socialFlow = socialViewModel.flow
        val activeUserFlow = viewmodel.getActiveToken()

        val socialCallbacks = SocialActions(
            onVoteUpClick = { onVoteClicked(SocialVoteType.aye) },
            onVoteDownClick = { onVoteClicked(SocialVoteType.no) },
            onExpandedCommentIconClick = { },
            onCommentClick = { comment ->
                println("Clicked comment $comment")
            },
            onCreateCommentClick = { socialUiState.update(SocialUiState.ComposeComment) },
            onCommentSubmitClick = {

            },
        )

        return composeView {
            Providers(
                AmbientSocialActions provides socialCallbacks,
                AmbientSocialUiState provides socialUiState,
            ) {
                withSystemUi {
                    val resultState by flow.collectAsState(initial = LoadingResult())
                    val socialState by socialFlow.collectAsState(initial = LoadingResult())
                    val activeUserState = activeUserFlow?.collectAsState(initial = LoadingResult())

                    val data = resultState.data ?: return@withSystemUi
                    withNotNull(data.profile) {
                        socialViewModel.forTarget(it.asSocialTarget())
                    }

                    Providers(
                        AmbientSocialContent provides (socialState.data ?: EmptySocialContent),
                        AmbientUserToken provides (activeUserState?.value?.data ?: NullUserToken),
                    ) {
                        MemberProfileLayout(data)
                    }
                }
            }
        }
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

        else -> {
            // TODO
            false
        }
    }

    //    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        socialViewController = setupViewController(
//            binding.root,
//            collapsedConstraintsId = R.id.state_default,
//        )
//
//        binding.recyclerview.setup(
//            adapter,
//            space = defaultPrimaryContentSpacing(view.context)
//        )
//
//        viewmodel.liveData.observe(viewLifecycleOwner) { result ->
//            result.handle { member ->
//                updateUI(member)
//
//                withNotNull(member.profile) { profile ->
//                    observeSocialContent(profile)
//                }
//            }
//        }
//    }

//    private fun updateUI(member: CompleteMember) {
//        applyUiTheme(context?.getPartyTheme(member.party?.parliamentdotuk))
//
//        binding.portrait.load(member.profile?.portraitUrl)
////
//        lifecycleScope.launch {
//            @Suppress("UNCHECKED_CAST")
//            adapter.diffItems(viewmodel.toProfileData(member) as List<ProfileData<Any>>)?.invokeOnCompletion {
//                binding.recyclerview.scrollToPosition(0)
//            }
//        }
//
//        updateUiToolbarText(member)
//    }
//
//    private fun updateUiToolbarText(member: CompleteMember) {
//        bindText(
//            binding.name to member.profile?.name,
//        )
//    }
//
//    private fun applyUiTheme(theme: PartyColors?) {
//        theme ?: return
//        this.theme = theme
//        adapter.theme = theme
//        binding.apply {
//            setBackgroundColor(theme.primary, titlebarBackground)
//            setBackgroundColor(theme.accent, accent, portrait)
//            setTextColor(
//                theme.textPrimaryOnPrimary,
//                name,
//            )
//        }
//
//        socialViewController.collapsedTheme = SocialViewTheme(theme.textSecondaryOnPrimary, theme.textPrimaryOnPrimary)
//    }
}
