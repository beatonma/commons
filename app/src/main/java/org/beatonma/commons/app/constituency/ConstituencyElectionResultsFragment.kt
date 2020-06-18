package org.beatonma.commons.app.constituency

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.BaseViewmodelFragment
import org.beatonma.commons.app.ui.colors.getPartyTheme
import org.beatonma.commons.app.ui.recyclerview.CommonsLoadingAdapter
import org.beatonma.commons.app.ui.recyclerview.setup
import org.beatonma.commons.app.ui.recyclerview.viewholder.StaticViewHolder
import org.beatonma.commons.app.ui.views.BarChartCategory
import org.beatonma.commons.data.NetworkError
import org.beatonma.commons.data.ParliamentID
import org.beatonma.commons.data.core.room.entities.constituency.ConstituencyElectionDetailsWithExtras
import org.beatonma.commons.data.resolution.PartyResolution
import org.beatonma.commons.databinding.FragmentConstituencyElectionResultsBinding
import org.beatonma.commons.databinding.ItemConstituencyCandidateBinding
import org.beatonma.commons.databinding.ItemConstituencyCandidateDepositSeparatorBinding
import org.beatonma.commons.kotlin.extensions.*

private const val TAG = "ConstElectResFragment"
private const val VIEW_TYPE_DEPOSIT_SEPARATOR = 1

const val CONSTITUENCY_ID = "constituency_id"
const val ELECTION_ID = "election_id"

private data class BundleData(val constituencyId: ParliamentID, val electionId: ParliamentID)


class ConstituencyElectionResultsFragment : BaseViewmodelFragment() {

    private lateinit var binding: FragmentConstituencyElectionResultsBinding
    private val viewmodel: ConstituencyElectionResultsViewModel by viewModels { viewmodelFactory }
    private val adapter = CandidatesAdapter()

    private fun getConstituencyAndElectionFromBundle(): BundleData? {
        return BundleData(
            constituencyId = arguments?.getInt(CONSTITUENCY_ID) ?: return null,
            electionId = arguments?.getInt(ELECTION_ID) ?: return null
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Get values from arguments
        val data = getConstituencyAndElectionFromBundle()
        if (data == null) {
            Log.w(TAG, "Failed to get constituency,election IDs from bundle!")
            return
        }
        viewmodel.forConstituencyInElection(
            constituencyId = data.constituencyId,
            electionId = data.electionId
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentConstituencyElectionResultsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.candidates.setup(adapter)

        viewmodel.liveData.observe(viewLifecycleOwner) { result ->
            if (result is NetworkError) networkErrorSnackbar()

            val data = result.data
            if (data != null) {
                updateUi(data)
            }
        }
    }

    private fun updateUi(data: ConstituencyElectionDetailsWithExtras) {
        val details = data.details ?: return
        val election = data.election
        val constituency = data.constituency
        bindText(
            binding.electionName to stringCompat(
                R.string.constituency_election_header,
                election?.name),

            binding.constituencyName to constituency?.name,

            binding.electionDate to election?.date?.formatted(),

            binding.turnout to stringCompat(
                R.string.constituency_election_turnout,
                details.turnout.formatted(),
                details.electorate.formatted(),
                viewmodel.getTurnoutPercentage(data.details)
            )
        )

        adapter.winningVoteCount = data.candidates?.minBy { it.order }?.votes ?: 0
        adapter.totalVotes = details.turnout
        adapter.items = viewmodel.composeCandidateData(data)
    }

    private inner class CandidatesAdapter: CommonsLoadingAdapter<CandidateData>(null) {
        var winningVoteCount: Int = 0  // For normalising bar charts
        var totalVotes: Int = 0  // For calculating percentages

        override fun getItemViewType(position: Int) = when(items?.get(position)) {
            is DepositSeparator -> VIEW_TYPE_DEPOSIT_SEPARATOR
            else -> super.getItemViewType(position)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when(viewType) {
            VIEW_TYPE_DEPOSIT_SEPARATOR -> DepositSeparatorViewHolder(parent.inflate(R.layout.item_constituency_candidate_deposit_separator))
            else -> super.onCreateViewHolder(parent, viewType)
        }

        override fun onCreateDefaultViewHolder(parent: ViewGroup) =
            object: TypedViewHolder(parent.inflate(R.layout.item_constituency_candidate)) {
                private val vh = ItemConstituencyCandidateBinding.bind(itemView)

                override fun bind(item: CandidateData) {
                    val candidate = (item as? Candidate)?.candidate ?: return
                    val theme = context.getPartyTheme(PartyResolution.getPartyId(candidate.partyName))

                    vh.apply {
                        barchart.categories = listOf(
                            BarChartCategory(candidate.votes, theme.primary, "Votes"),
                            BarChartCategory(winningVoteCount - candidate.votes, Color.TRANSPARENT, "Other")  // Normalised to winningVoteCount
//                        BarChartCategory(totalVotes - candidate.votes, Color.TRANSPARENT, "Other")  // Relative to totalVotes
                        )
                        bindText(
                            rank to stringCompat(R.string.integer, candidate.order),
                            name to stringCompat(R.string.hyphenated, viewmodel.getFormattedName(candidate), viewmodel.getFormattedParty(candidate)),
                            description to stringCompat(R.string.constituency_candidate_votes,
                                candidate.votes.formatted(),
                                viewmodel.getFormattedPercentage(candidate.votes, totalVotes)
                            )
                        )
                    }
                }
            }

        private inner class DepositSeparatorViewHolder(view: View): StaticViewHolder(view) {
            private val vh = ItemConstituencyCandidateDepositSeparatorBinding.bind(view)

            override fun bind() {
                itemView.setOnClickListener {
                    vh.about.toggleVisibility()
                }
            }
        }
    }
}
