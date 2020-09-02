package org.beatonma.commons.app.memberprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import org.beatonma.commons.R
import org.beatonma.commons.app.social.SocialViewController
import org.beatonma.commons.app.social.SocialViewHost
import org.beatonma.commons.app.social.SocialViewModel
import org.beatonma.commons.app.social.SocialViewTheme
import org.beatonma.commons.app.ui.CommonsFragment
import org.beatonma.commons.app.ui.colors.PartyColors
import org.beatonma.commons.app.ui.colors.Themed
import org.beatonma.commons.app.ui.colors.getPartyTheme
import org.beatonma.commons.app.ui.navigation.BackPressConsumer
import org.beatonma.commons.app.ui.recyclerview.defaultPrimaryContentSpacing
import org.beatonma.commons.app.ui.recyclerview.setup
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.core.extensions.withNotNull
import org.beatonma.commons.data.core.CompleteMember
import org.beatonma.commons.databinding.FragmentMemberProfileBinding
import org.beatonma.commons.kotlin.extensions.*
import org.beatonma.commons.repo.IoResultObserver
import org.beatonma.commons.snommoc.models.social.SocialContent

private const val TAG = "MemberProfileFrag"

@AndroidEntryPoint
class MemberProfileFragment : CommonsFragment<FragmentMemberProfileBinding>(),
    Themed,
    BackPressConsumer,
    SocialViewHost
{
    private val viewmodel: MemberProfileViewModel by viewModels()
    override val socialViewModel: SocialViewModel by viewModels()

    override lateinit var socialViewController: SocialViewController
    override val socialObserver: IoResultObserver<SocialContent> = createSocialObserver()

    private val adapter = ProfileDataAdapter(asyncDiffHost = this)

    override var theme: PartyColors? = null

    private fun getMemberIdFromBundle(): ParliamentID = arguments.getParliamentID()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewmodel.forMember(getMemberIdFromBundle())
    }

    override fun inflateBinding(inflater: LayoutInflater) = FragmentMemberProfileBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        socialViewController = setupViewController(
            binding.root,
            collapsedConstraintsId = R.id.state_default,
        )

        binding.recyclerview.setup(
            adapter,
            space = defaultPrimaryContentSpacing(view.context)
        )

        viewmodel.liveData.observe(viewLifecycleOwner) { result ->
            result.handle { member ->
                updateUI(member)

                withNotNull(member.profile) { profile ->
                    observeSocialContent(profile)
                }
            }
        }
    }

    private fun updateUI(member: CompleteMember) {
        applyUiTheme(context?.getPartyTheme(member.party?.parliamentdotuk))

        binding.portrait.load(member.profile?.portraitUrl)
//
        lifecycleScope.launch {
            @Suppress("UNCHECKED_CAST")
            adapter.diffItems(viewmodel.toProfileData(member) as List<ProfileData<Any>>)?.invokeOnCompletion {
                binding.recyclerview.scrollToPosition(0)
            }
        }

        updateUiToolbarText(member)
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
}
