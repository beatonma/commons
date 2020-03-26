package org.beatonma.commons.app.memberprofile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_member_profile_snippet.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.beatonma.commons.R
import org.beatonma.commons.app.dagger.Injectable
import org.beatonma.commons.data.PARLIAMENTDOTUK
import org.beatonma.commons.data.core.room.entities.MemberProfile
import org.beatonma.commons.databinding.FragmentMemberProfileBinding
import org.beatonma.commons.ui.colors.PartyColors
import org.beatonma.commons.ui.colors.getPartyTheme
import javax.inject.Inject


@ExperimentalStdlibApi
class MemberProfileFragment : Fragment(), Injectable {
    private lateinit var binding: FragmentMemberProfileBinding

    @Inject
    lateinit var viewmodelFactory: ViewModelProvider.Factory
    private val viewmodel: MemberProfileViewModel by viewModels { viewmodelFactory }
    private var snippetIndex = 0;
    private var snippetJob: Job? = null

    private fun getMemberIdFromBundle(): Int = arguments?.getInt(PARLIAMENTDOTUK) ?: 0

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        viewmodel.forMember(getMemberIdFromBundle())

        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMemberProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.portrait.setImageResource(R.mipmap.ic_launcher)

        viewmodel.member.observe(viewLifecycleOwner, Observer { result ->
            result.data?.let { profile ->
                updateUI(profile.profile)
            }
        })

        viewmodel.snippets.observe(viewLifecycleOwner, Observer { nextSnippet() })
    }

    private fun updateUI(profile: MemberProfile) {
        applyTheme(getPartyTheme(profile.party.parliamentdotuk))
        binding.apply {
            name.text = profile.name

            Glide.with(root)
                .load(R.mipmap.politician_generic)
//                .load(profile.portraitUrl)
                .fallback(R.mipmap.ic_launcher)
                .into(portrait)
        }
    }

    private fun nextSnippet() {
        snippetJob?.cancel()

        val snippets = viewmodel.snippets.value
        if (snippets == null || snippets.isEmpty()) {
            return
        }

        snippetIndex = (snippetIndex + 1) % snippets.size
        setSnippet(snippets[snippetIndex])

        snippetJob = lifecycleScope.launch(Dispatchers.Main) {
            delay(2500)
            nextSnippet()
        }
    }

    private fun setSnippet(snippet: Snippet) {
        binding.snippet.apply {
            title.text = snippet.title
            content.text = snippet.content
            subtitle.text = snippet.subtitle
            subcontent.text = snippet.subcontent
        }

        binding.snippet.action.setOnClickListener { view -> snippet.onclick?.invoke(view.context) }
        binding.snippet.snippet.action.apply {
            if (snippet.onclick == null || snippet.clickActionText == null) {
                visibility = View.GONE
            }
            else {
                text = snippet.clickActionText
                visibility = View.VISIBLE
            }
        }
    }

    private fun applyTheme(theme: PartyColors) {
        binding.apply{
            portrait.setBackgroundColor(theme.primary)
            accentLine.setBackgroundColor(theme.primary)
        }
    }
}
