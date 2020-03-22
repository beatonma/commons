package org.beatonma.commons.app

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.AndroidSupportInjection
import org.beatonma.commons.R
import org.beatonma.commons.app.dagger.Injectable
import org.beatonma.commons.app.featured.FeaturedContentViewModel
import org.beatonma.commons.data.core.room.entities.FeaturedMemberProfile
import org.beatonma.commons.databinding.FragmentFrontpageBinding
import org.beatonma.commons.databinding.VhFeaturedPersonBinding
import org.beatonma.lib.ui.recyclerview.BaseRecyclerViewAdapter
import org.beatonma.lib.ui.recyclerview.BaseViewHolder
import org.beatonma.lib.ui.recyclerview.kotlin.extensions.setup
import javax.inject.Inject

class ExperimentalFrontPageFragment : Fragment(), Injectable {
    private lateinit var binding: FragmentFrontpageBinding

    @Inject
    lateinit var viewmodelFactory: ViewModelProvider.Factory

    val viewmodel: FeaturedContentViewModel by viewModels { viewmodelFactory }

    private val adapter = FeaturedPeopleAdapter()

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFrontpageBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerview.setup(adapter)

        viewmodel.featuredPeople.observe(viewLifecycleOwner, Observer {
            adapter.notifyDataSetChanged()
        })
    }

    private inner class FeaturedPeopleAdapter : BaseRecyclerViewAdapter() {
        val data: List<FeaturedMemberProfile>?
            get() = viewmodel.featuredPeople.value?.data

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
            return PersonViewHolder(inflate(parent, R.layout.vh_featured_person, false))
        }

        override fun getItemCount() = data?.size ?: 0

        private inner class PersonViewHolder(v: View): BaseViewHolder(v) {
            private val vh = VhFeaturedPersonBinding.bind(v)

            override fun bind(position: Int) {
                val featured = data?.get(position)?.profile ?: return
                vh.name.text = featured.profile.name
                vh.party.text = featured.party.name
                vh.constituency.text = "${featured.constituency.name}"
//                Glide.with(itemView)
//                    .load(profile.portraitUrl)
//                    .fallback(R.mipmap.ic_launcher)
//                    .into(vh.portrait)
            }
        }
    }
}

