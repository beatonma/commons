package org.beatonma.commons.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.liveData
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.MarginPageTransformer
import dagger.hilt.android.AndroidEntryPoint
import org.beatonma.commons.R
import org.beatonma.commons.app.featured.FeaturedContentViewModel
import org.beatonma.commons.app.search.SearchEnabled
import org.beatonma.commons.app.signin.SignInEnabled
import org.beatonma.commons.app.ui.CommonsFragment
import org.beatonma.commons.app.ui.colors.getTheme
import org.beatonma.commons.app.ui.recyclerview.adapter.LoadingAdapter
import org.beatonma.commons.app.ui.recyclerview.primaryContentWithToolbarSpacing
import org.beatonma.commons.app.ui.recyclerview.setup
import org.beatonma.commons.data.core.room.entities.bill.FeaturedBillWithBill
import org.beatonma.commons.data.core.room.entities.division.FeaturedDivisionWithDivision
import org.beatonma.commons.data.core.room.entities.member.FeaturedMemberProfile
import org.beatonma.commons.databinding.ItemWideImageTitleSubtitleDescriptionBinding
import org.beatonma.commons.databinding.RecyclerviewBinding
import org.beatonma.commons.databinding.ViewpagerBinding
import org.beatonma.commons.kotlin.extensions.*
import org.beatonma.commons.repo.LiveDataIoResultList

@AndroidEntryPoint
class ExperimentalFrontPageFragment : CommonsFragment<ViewpagerBinding>(), SearchEnabled, SignInEnabled {
    private val viewmodel: FeaturedContentViewModel by viewModels()
    private lateinit var adapter: PageAdapter

    override fun inflateBinding(inflater: LayoutInflater) = ViewpagerBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = PageAdapter(this)
        binding.viewpager.let {
            it.adapter = adapter
            it.setPageTransformer(MarginPageTransformer(it.context.dp(16)))
        }

        viewmodel.motd.observe(viewLifecycleOwner) { result ->
            result.report()

            val motd = result.data?.firstOrNull() ?: return@observe
            val action = motd.url?.let { url ->
                SnackbarAction(R.string.more) { view: View ->
                    view.context.openUrl(url)
                }
            }
            snackbar("${motd.title} ${motd.description}", action = action)
        }
    }

    private inner class PageAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = 3

        override fun createFragment(position: Int): Fragment = when (position) {
            0 -> PeopleFrontPageFragment(viewmodel.featuredPeople)
            1 -> BillsFrontPageFragment(viewmodel.featuredBills)
            else -> DivisionsFrontPageFragment(viewmodel.featuredDivisions)
        }
    }
}

abstract class AbstractFeatureFragment<T>(val livedata: LiveDataIoResultList<T>): CommonsFragment<RecyclerviewBinding>() {

    abstract val adapter: AbstractFeatureAdapter<T>

    override fun inflateBinding(inflater: LayoutInflater) = RecyclerviewBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerview.setup(adapter, space = primaryContentWithToolbarSpacing(view.context))

        livedata.observe(viewLifecycleOwner) { result ->
            result.report()
            adapter.diffItems(result.data)
        }
    }
}

abstract class AbstractFeatureAdapter<T>(
    val bindFunc: (T, ItemWideImageTitleSubtitleDescriptionBinding) -> Unit
): LoadingAdapter<T>() {

    override fun onCreateDefaultViewHolder(parent: ViewGroup) =
        object: TypedViewHolder(parent.inflate(R.layout.item_wide_image_title_subtitle_description)) {
            private val vh = ItemWideImageTitleSubtitleDescriptionBinding.bind(itemView)
            override fun bind(item: T) {
                bindFunc(item, vh)
            }
        }
}


class PeopleFrontPageFragment(livedata: LiveDataIoResultList<FeaturedMemberProfile> = liveData {  }): AbstractFeatureFragment<FeaturedMemberProfile>(livedata) {
    override val adapter: AbstractFeatureAdapter<FeaturedMemberProfile> = object : AbstractFeatureAdapter<FeaturedMemberProfile>(
            { item, vh ->
                val featured = item.profile
                val profile = featured.profile
                val theme = featured.party.getTheme(vh.root.context)
                vh.apply {
                    bindText(
                        title to profile.name,
                        subtitle to stringCompat(R.string.member_party_constituency,
                            featured.party.name,
                            featured.constituency?.name ?: ""),
                        description to profile.currentPost,
                        linkColor = theme.accent,
                    )
                    portrait.setBackgroundColor(theme.primary)
                    portrait.load(featured.profile.portraitUrl)
                }
                vh.root.setOnClickListener { view ->
                    view.navigateTo(
                        R.id.action_frontPageFragment_to_memberProfileFragment,
                        featured.bundle())
                }
            }
        ) {}
}

class BillsFrontPageFragment(livedata: LiveDataIoResultList<FeaturedBillWithBill> = liveData {  }): AbstractFeatureFragment<FeaturedBillWithBill>(livedata) {
    override val adapter: AbstractFeatureAdapter<FeaturedBillWithBill> = object :
        AbstractFeatureAdapter<FeaturedBillWithBill>(
            { item, vh ->
                val bill = item.bill

                vh.apply {
                    bindText(
                        title to bill.title,
                        subtitle to bill.date?.formatted(),
                        description to bill.description
                    )
                }

                vh.root.setOnClickListener { view ->
                    view.navigateTo(R.id.action_frontPageFragment_to_billFragment,
                        bill.bundle())
                }
            }
        ) {}
}

class DivisionsFrontPageFragment(livedata: LiveDataIoResultList<FeaturedDivisionWithDivision> = liveData {  }): AbstractFeatureFragment<FeaturedDivisionWithDivision>(livedata) {
    override val adapter: AbstractFeatureAdapter<FeaturedDivisionWithDivision> = object :
        AbstractFeatureAdapter<FeaturedDivisionWithDivision>(
            { item, vh ->
                val division = item.division
                vh.apply {
                    bindText(
                        title to division.title,
                        subtitle to division.date.formatted(),
                        description to "Passed: ${division.passed}"
                    )
                }

                vh.root.setOnClickListener { view ->
                    view.navigateTo(
                        R.id.action_frontPageFragment_to_divisionProfileFragment,
                        item.bundle())
                }
            }
        ) {}
}
