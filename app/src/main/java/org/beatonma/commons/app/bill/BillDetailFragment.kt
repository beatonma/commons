package org.beatonma.commons.app.bill

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.CommonsFragment
import org.beatonma.commons.app.ui.recyclerview.adapter.LoadingAdapter
import org.beatonma.commons.app.ui.recyclerview.adapter.StickyHeaderAdapter
import org.beatonma.commons.app.ui.recyclerview.adapter.TypedAdapter
import org.beatonma.commons.app.ui.recyclerview.defaultHorizontalOverscroll
import org.beatonma.commons.app.ui.recyclerview.itemdecorator.sticky.HorizontalStickyHeaderDecoration
import org.beatonma.commons.app.ui.recyclerview.itemdecorator.sticky.StickyHeader
import org.beatonma.commons.app.ui.recyclerview.itemdecorator.sticky.StickyHeaderDecoration
import org.beatonma.commons.app.ui.recyclerview.setup
import org.beatonma.commons.data.ParliamentID
import org.beatonma.commons.data.core.room.entities.bill.BillSponsorWithParty
import org.beatonma.commons.data.core.room.entities.bill.CompleteBill
import org.beatonma.commons.data.core.room.entities.member.House
import org.beatonma.commons.data.resolution.describe
import org.beatonma.commons.databinding.FragmentBillProfileBinding
import org.beatonma.commons.databinding.ItemBillStageBinding
import org.beatonma.commons.databinding.ItemMemberCompactBinding
import org.beatonma.commons.kotlin.extensions.*

private const val TAG = "BillDetailFrag"

@AndroidEntryPoint
class BillDetailFragment : CommonsFragment() {

    private lateinit var binding: FragmentBillProfileBinding

    private val viewmodel: BillDetailViewModel by viewModels()

    private val stagesAdapter = BillStagesAdapter()
    private val sponsorsAdapter = BillSponsorsAdapter()

    private fun getBillIdFromBundle(): ParliamentID = arguments.getParliamentID()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewmodel.forBill(getBillIdFromBundle())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBillProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.stagesRecyclerview.setup(
            stagesAdapter,
            orientation = HORIZONTAL,
            space = defaultHorizontalOverscroll(view.context),
            itemDecoration = HorizontalStickyHeaderDecoration(
                view.context,
                space = StickyHeaderDecoration.Space(
                    paddingHorizontal = context.dimenCompat(R.dimen.card_padding_horizontal),
                    paddingBottom = context.dimenCompat(R.dimen.card_padding_bottom),
                    marginBetweenGroups = context.dimenCompat(R.dimen.list_space_between_groups_horizontal)
                )
            ),
        )
        binding.sponsorsRecyclerview.setup(sponsorsAdapter, orientation = HORIZONTAL)

        viewmodel.liveData.observe(viewLifecycleOwner) { result ->
            result.report()

            val data = result.data
            if (data != null) {
                updateUi(data)
            }
        }
    }

    private fun updateUi(bill: CompleteBill) {
        with(binding) {
            bindText(
                title to bill.bill?.title,
                typeAndSession to context?.dotted(
                    stringCompat(R.string.bill_session, bill.session?.name),
                    bill.type?.name
                ),
                publications to stringCompat(R.string.bill_publications_count,
                    bill.publications?.size ?: 0),
                stages to stringCompat(R.string.bill_stages_count, bill.stages?.size ?: 0),
            )
        }

        sponsorsAdapter.items = bill.sponsors

        lifecycleScope.launch {
            val annotated = viewmodel.getAnnotatedStages(bill)
            withContext(Dispatchers.Main) {
                stagesAdapter.items = annotated
            }
        }
    }

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
