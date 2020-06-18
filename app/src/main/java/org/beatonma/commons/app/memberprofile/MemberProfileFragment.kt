package org.beatonma.commons.app.memberprofile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.beatonma.commons.R
import org.beatonma.commons.app.social.SocialViewController
import org.beatonma.commons.app.social.SocialViewHost
import org.beatonma.commons.app.social.SocialViewModel
import org.beatonma.commons.app.social.SocialViewTheme
import org.beatonma.commons.app.ui.BaseViewmodelFragment
import org.beatonma.commons.app.ui.Snippet
import org.beatonma.commons.app.ui.colors.PartyColors
import org.beatonma.commons.app.ui.colors.Themed
import org.beatonma.commons.app.ui.colors.getPartyTheme
import org.beatonma.commons.app.ui.navigation.OnBackPressed
import org.beatonma.commons.app.ui.recyclerview.RvSpacing
import org.beatonma.commons.app.ui.recyclerview.setup
import org.beatonma.commons.data.IoResult
import org.beatonma.commons.data.NetworkError
import org.beatonma.commons.data.PARLIAMENTDOTUK
import org.beatonma.commons.data.ParliamentID
import org.beatonma.commons.data.core.CompleteMember
import org.beatonma.commons.data.core.room.entities.member.MemberProfile
import org.beatonma.commons.data.core.social.SocialContent
import org.beatonma.commons.databinding.FragmentMemberProfileBinding
import org.beatonma.commons.databinding.FragmentMemberProfileSnippetBinding
import org.beatonma.commons.kotlin.extensions.*

private const val TAG = "MemberProfileFrag"

class MemberProfileFragment : BaseViewmodelFragment(),
    Themed,
    OnBackPressed,
    SocialViewHost
{

    private lateinit var binding: FragmentMemberProfileBinding

    private val viewmodel: MemberProfileViewModel by viewModels { viewmodelFactory }
    override val socialViewModel: SocialViewModel by activityViewModels{ viewmodelFactory }

    override lateinit var socialViewController: SocialViewController

    private val socialObserver = Observer<IoResult<SocialContent>> { result ->
        socialViewController.updateUi(result.data)
    }

    override fun onVoteSubmissionSuccessful() {
        observeSocialViewModel()
    }

    private val adapter = ProfileDataAdapter()

    override var theme: PartyColors? = null
    private val defaultTransition = R.id.transition_scroll

    private fun getMemberIdFromBundle(): ParliamentID = arguments?.getInt(PARLIAMENTDOTUK) ?: 0

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewmodel.forMember(getMemberIdFromBundle())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentMemberProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        socialViewController = setupViewController(binding.root, defaultTransition)

        binding.recyclerview.setup(
            adapter,
            space = RvSpacing(
                topSpace = context.dp(16),
                bottomSpace = dimenCompat(R.dimen.list_overscroll_padding),
                verticalItemSpace = dimenCompat(R.dimen.flow_gap_vertical)
            )
        )

        viewmodel.liveData.observe(viewLifecycleOwner) { result ->
            if (result is NetworkError) networkErrorSnackbar()

            result.data?.let { member ->
                updateUI(member)

                val profile = member.profile
                if (profile != null) {
                    observeSocialContent(profile)
                }
            }
        }
    }

    private fun observeSocialContent(profile: MemberProfile) {
        val target = profile.asSocialTarget()

        lifecycleScope.launch(Dispatchers.Main) {
            socialViewModel.run {
                forTarget(target)
                observeSocialViewModel()
            }
        }
    }

    private fun updateUI(member: CompleteMember) {
        applyUiTheme(context?.getPartyTheme(member.party?.parliamentdotuk))
//        applyUiTheme(member.party?.getTheme(context))

        binding.portrait.load(member.profile?.portraitUrl)

        lifecycleScope.launch(Dispatchers.Main) {
            @Suppress("UNCHECKED_CAST")
            adapter.items = viewmodel.toProfileData(member) as List<ProfileData<Any>>
        }

        updateUiToolbarText(member)
    }

    private fun observeSocialViewModel() {
        socialViewModel.livedata.removeObserver(socialObserver)
        lifecycleScope.launch(Dispatchers.IO) {
            socialViewModel.refresh()
            withContext(Dispatchers.Main) {
                socialViewModel.livedata.observe(viewLifecycleOwner, socialObserver)
            }
        }
    }

    private fun updateUiToolbarText(member: CompleteMember) {
        bindText(
            binding.name to member.profile?.name,
        )
    }

    private fun applyUiTheme(theme: PartyColors?) {
        theme ?: return
        this.theme = theme
        adapter.theme = theme
        binding.apply {
            setBackgroundColor(theme.primary, titlebarBackground)
            setBackgroundColor(theme.accent, accent, portrait)
            setTextColor(
                theme.textPrimaryOnPrimary,
                name,
            )
        }

        socialViewController.collapsedTheme = SocialViewTheme(theme.textSecondaryOnPrimary, theme.textPrimaryOnPrimary)
    }

//    private fun setSnippet(snippet: Snippet) = bindSnippet(binding.snippet, snippet)

//    private fun observeMemberVotes() {
//        // TODO load votes when user requests them and display soemwhere
//        viewmodel.memberVoteLiveData.observe(viewLifecycleOwner) {
//            it.data.dump()
//        }
//    }
}



private fun bindSnippet(binding: FragmentMemberProfileSnippetBinding, snippet: Snippet) {
    binding.apply {
        bindText(
            title to snippet.title,
            content to snippet.content,
            subtitle to snippet.subtitle,
            subcontent to snippet.subcontent,
        )

        binding.root.setOnClickListener { view -> snippet.onclick?.invoke(view.context) }
    }
}
