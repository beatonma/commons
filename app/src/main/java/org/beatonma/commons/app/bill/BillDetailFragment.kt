package org.beatonma.commons.app.bill

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import dagger.hilt.android.AndroidEntryPoint
import org.beatonma.commons.R
import org.beatonma.commons.app.social.SocialViewController
import org.beatonma.commons.app.social.SocialViewHost
import org.beatonma.commons.app.social.SocialViewModel
import org.beatonma.commons.app.ui.CommonsFragment
import org.beatonma.commons.app.ui.recyclerview.adapter.LoadingAdapter
import org.beatonma.commons.app.ui.recyclerview.adapter.StickyHeaderAdapter
import org.beatonma.commons.app.ui.recyclerview.adapter.TypedAdapter
import org.beatonma.commons.app.ui.recyclerview.defaultHorizontalOverscroll
import org.beatonma.commons.app.ui.recyclerview.itemdecorator.sticky.HorizontalStickyHeaderDecoration
import org.beatonma.commons.app.ui.recyclerview.itemdecorator.sticky.StickyHeader
import org.beatonma.commons.app.ui.recyclerview.setup
import org.beatonma.commons.core.House
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.data.core.room.entities.bill.BillSponsorWithParty
import org.beatonma.commons.data.core.room.entities.bill.CompleteBill
import org.beatonma.commons.data.resolution.describe
import org.beatonma.commons.databinding.FragmentBillDetailBinding
import org.beatonma.commons.databinding.ItemBillStageBinding
import org.beatonma.commons.databinding.ItemMemberCompactBinding
import org.beatonma.commons.kotlin.extensions.*

private const val TAG = "BillDetailFrag"

@AndroidEntryPoint
class BillDetailFragment : CommonsFragment<FragmentBillDetailBinding>(), SocialViewHost {
    private val viewmodel: BillDetailViewModel by viewModels()

    private val stagesAdapter = BillStagesAdapter()
    private val sponsorsAdapter = BillSponsorsAdapter()

    override val socialViewModel: SocialViewModel by viewModels()
    override lateinit var socialViewController: SocialViewController
    override val socialObserver = createSocialObserver()

    private fun getBillIdFromBundle(): ParliamentID = arguments.getParliamentID()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewmodel.forBill(getBillIdFromBundle())
    }

    override fun inflateBinding(inflater: LayoutInflater) = FragmentBillDetailBinding.inflate(inflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        socialViewController = setupViewController(binding.root)

        binding.stagesRecyclerview.setup(
            stagesAdapter,
            orientation = HORIZONTAL,
            space = defaultHorizontalOverscroll(view.context),
            itemDecoration = HorizontalStickyHeaderDecoration(view.context),
        )
        binding.sponsorsRecyclerview.setup(sponsorsAdapter, orientation = HORIZONTAL)

        viewmodel.liveData.observe(viewLifecycleOwner) { result ->
            result.handle(
                withData = { data ->
                    updateUi(data)
                    org.beatonma.commons.core.extensions.withNotNull(data.bill) { bill ->
                        observeSocialContent(bill)
                    }
                },
                noData = {
                    updateSponsorsUi(listOf())
                    updateStagesUi(listOf())
                }
            )
        }

        viewmodel.stagesLiveData.observe(viewLifecycleOwner) { updateStagesUi(it) }
    }

    private fun updateUi(bill: CompleteBill) {
        with(binding) {
            bindText(
                title to bill.bill?.title,
                description to bill.bill?.description,
                typeAndSession to context?.dotted(
                    bill.session?.name,
                    bill.type?.name
                ),
                publications to stringCompat(R.string.bill_publications_count,
                    bill.publications?.size ?: 0),
            )
        }

        updateSponsorsUi(bill.sponsors ?: listOf())
    }

    private fun updateSponsorsUi(sponsors: List<BillSponsorWithParty>?) = sponsorsAdapter.diffItems(sponsors)
    private fun updateStagesUi(stages: List<AnnotatedBillStage>?) = stagesAdapter.diffItems(stages)

    private inner class BillStagesAdapter : TypedAdapter<AnnotatedBillStage>(), StickyHeaderAdapter {

        override fun onCreateDefaultViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
            object : TypedViewHolder(parent.inflate(R.layout.item_bill_stage)) {
                private val vh = ItemBillStageBinding.bind(itemView)

                override fun bind(item: AnnotatedBillStage) {
                    val stage = item.stage.stage
                    val sittings = item.stage.sittings

                    vh.apply {
                        bindText(
                            stageTitle to stage.type,
                            stageSittingsCount to stringCompat(
                                R.plurals.bill_sittings,
                                sittings.size,
                                quantity = sittings.size
                            ),
                            dates to context.dotted(
                                sittings
                                    .sortedBy { it.date }
                                    .map { it.date.formatted() }
                            ),
                        )
                    }
                }
            }

        override fun getStickyHeaderForPosition(position: Int): StickyHeader {
            val item = items?.get(position)

            return StickyHeader(
                context?.describe(item?.house) ?: "",
                backgroundColor = when {
                    item?.house == House.commons -> colorCompat(R.color.puk_house_of_commons_green)
                    item?.house == House.lords -> colorCompat(R.color.puk_house_of_lords_red)
                    item?.isConsiderationOfAmendments() == true -> colorCompat(R.color.puk_parliament)
                    item?.isRoyalAssent() == true -> colorCompat(R.color.puk_royal)
                    else -> null
                },
                backgroundCornerRadius = dimenCompat(R.dimen.card_corner_radius),
                textColor = colorCompat(R.color.TextPrimaryLight),
                backgroundFillsView = true
            )
        }

        override fun isHeaderSameForPositions(first: Int, second: Int): Boolean {
            val left = items?.get(first)
            val right = items?.get(second)

            if (left == null || right == null) {
                return left == right
            }

            if (left.house != null || right.house != null) {
                return left.house == right.house
            }

            return left.progress == right.progress
        }
    }

    private inner class BillSponsorsAdapter : LoadingAdapter<BillSponsorWithParty>() {

        override fun onCreateDefaultViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
            object : TypedViewHolder(parent.inflate(R.layout.item_member_compact)) {
                private val vh = ItemMemberCompactBinding.bind(itemView)

                override fun bind(item: BillSponsorWithParty) {
                    vh.apply {
                        bindText(
                            name to item.sponsor.name,
                            party to item.party?.name,
                        )
                    }

                    itemView.setOnClickListener { view ->
                        view.navigateTo(
                            R.id.action_billFragment_to_memberProfileFragment,
                            item.sponsor.memberBundle()
                        )
                    }
                }
            }
    }
}
