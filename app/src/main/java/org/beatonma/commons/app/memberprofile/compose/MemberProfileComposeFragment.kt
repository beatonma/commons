package org.beatonma.commons.app.memberprofile.compose

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.beatonma.commons.app.composeView
import org.beatonma.commons.app.social.SocialViewModel
import org.beatonma.commons.app.ui.colors.PartyColors
import org.beatonma.commons.app.ui.colors.Themed
import org.beatonma.commons.app.withSystemUi
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.kotlin.extensions.*
import org.beatonma.commons.repo.result.LoadingResult

private const val TAG = "MemberProfileFrag"

@AndroidEntryPoint
class MemberProfileComposeFragment : Fragment(),
    Themed
//    BackPressConsumer,
{
    private val viewmodel: ComposeMemberProfileViewModel by viewModels()
    private val socialViewModel: SocialViewModel by viewModels()

//    override lateinit var socialViewController: SocialViewController
//    override val socialObserver: IoResultObserver<SocialContent> = createSocialObserver()


    override var theme: PartyColors? = null

    private fun getMemberIdFromBundle(): ParliamentID = arguments.getParliamentID()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewmodel.forMember(getMemberIdFromBundle())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val flow = viewmodel.forMember(getMemberIdFromBundle())
        return composeView {
            withSystemUi(window = requireActivity().window) {
                println("composeView")

                val resultState by flow.collectAsState(initial = LoadingResult())

                val data = resultState.data ?: return@withSystemUi

                MemberProfileLayout(data)
//                MemberProfileLayout(viewmodel, getMemberIdFromBundle())
//            val result by viewmodel.zeitgeist.collectAsState(LoadingResult())
//            Column {
//                Spacer(modifier = Modifier.statusBarsHeight())
//                ZeitgeistResult(result)
//            }
            }
//        return composeView {
//            println("composeView")
//            MemberProfileLayout(viewmodel, getMemberIdFromBundle())
//        }

//        return super.onCreateView(inflater, container, savedInstanceState)
        }
    }

    //    override fun inflateBinding(inflater: LayoutInflater) = FragmentMemberProfileBinding.inflate(inflater)

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
