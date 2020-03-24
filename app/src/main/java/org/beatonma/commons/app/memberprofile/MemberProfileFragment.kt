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
import com.bumptech.glide.Glide
import dagger.android.support.AndroidSupportInjection
import org.beatonma.commons.R
import org.beatonma.commons.app.dagger.Injectable
import org.beatonma.commons.data.PARLIAMENTDOTUK
import org.beatonma.commons.data.core.room.entities.MemberProfile
import org.beatonma.commons.databinding.FragmentMemberProfileBinding
import org.beatonma.commons.ui.colors.PartyColors
import org.beatonma.commons.ui.colors.getPartyTheme
import javax.inject.Inject

class MemberProfileFragment : Fragment(), Injectable {
    private lateinit var binding: FragmentMemberProfileBinding

    @Inject
    lateinit var viewmodelFactory: ViewModelProvider.Factory
    private val viewmodel: MemberProfileViewModel by viewModels { viewmodelFactory }

    fun getMemberIdFromBundle(): Int = arguments?.getInt(PARLIAMENTDOTUK) ?: 0

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
                updateUI(profile)
            }
        })
    }

    private fun updateUI(profile: MemberProfile) {
        val theme = getPartyTheme(profile.party.parliamentdotuk)
        binding.name.text = profile.name
        Glide.with(binding.root)
            .load(profile.portraitUrl)
            .fallback(R.mipmap.ic_launcher)
            .into(binding.portrait)
    }

    private fun applyTheme(theme: PartyColors) {

    }
}
