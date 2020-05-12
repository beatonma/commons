package org.beatonma.commons.app.division

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.BaseViewmodelFragment
import org.beatonma.commons.app.ui.colors.colorResId
import org.beatonma.commons.data.NetworkError
import org.beatonma.commons.data.PARLIAMENTDOTUK
import org.beatonma.commons.data.ParliamentID
import org.beatonma.commons.data.core.room.entities.division.Division
import org.beatonma.commons.data.core.room.entities.division.Vote
import org.beatonma.commons.data.core.room.entities.member.House
import org.beatonma.commons.databinding.FragmentDivisionProfileBinding
import org.beatonma.commons.databinding.ItemWideTitleBinding
import org.beatonma.commons.kotlin.extensions.formatted
import org.beatonma.commons.kotlin.extensions.navigateTo
import org.beatonma.commons.kotlin.extensions.networkErrorSnackbar
import org.beatonma.commons.kotlin.extensions.stringCompat
import org.beatonma.lib.ui.recyclerview.BaseRecyclerViewAdapter
import org.beatonma.lib.ui.recyclerview.BaseViewHolder
import org.beatonma.lib.ui.recyclerview.kotlin.extensions.setupGrid
import org.beatonma.lib.util.kotlin.extensions.colorCompat
import org.beatonma.lib.util.kotlin.extensions.dp

private const val TAG = "DivisionProfileFragment"

class DivisionDetailFragment : BaseViewmodelFragment() {
    companion object {
        const val HOUSE = "house"
    }

    private lateinit var binding: FragmentDivisionProfileBinding
    private val viewmodel: DivisionDetailViewModel by viewModels { viewmodelFactory }
    private val adapter = VotesAdapter()

    private fun getDivisionFromBundle(): Pair<House, ParliamentID>? {
        val args = arguments ?: return null
        val house = enumValues<House>()[args.getInt(HOUSE)]
        val parliamentdotuk = args.getInt(PARLIAMENTDOTUK)

        return Pair(house, parliamentdotuk)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val division = getDivisionFromBundle() ?: return
        viewmodel.forDivision(division.first, division.second)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentDivisionProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerview.setupGrid(adapter, context.dp(160), columnSpace = context.dp(8))

        viewmodel.liveData.observe(viewLifecycleOwner) { result ->
            if (result is NetworkError) networkErrorSnackbar()

            val data = result.data
            if (data != null) {
                updateUI(data.division, data.votes)
            }
        }

        viewmodel.voteLiveData.observe(viewLifecycleOwner) { votes -> updateVotes(votes) }
    }

    private fun updateUI(division: Division, votes: List<Vote>) {
        binding.apply {
            title.text = division.title
            date.text = division.date.formatted()
            house.text = division.house.name
            ayes.text = stringCompat(R.string.division_ayes, division.ayes)
            noes.text = stringCompat(R.string.division_noes, division.noes)

            chart.categories = viewmodel.getVoteChartData(division)
        }
    }

    private fun updateVotes(votes: List<Vote>) {
        adapter.items = votes
        adapter.notifyDataSetChanged()
    }
}

private class VotesAdapter(var items: List<Vote>? = null) : BaseRecyclerViewAdapter() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder =
        VoteViewHolder(inflate(parent, R.layout.item_wide_title))

    override fun getItemCount(): Int = items?.size ?: 0

    private inner class VoteViewHolder(view: View) : BaseViewHolder(view) {
        private val vh = ItemWideTitleBinding.bind(view)

        override fun bind(position: Int) {
            val vote = items?.get(position) ?: return
            vh.apply {
                title.text = vote.memberName
                accent.setBackgroundColor(root.colorCompat(vote.voteType.colorResId))
            }
            itemView.setOnClickListener { view ->
                view.navigateTo(
                    R.id.action_divisionProfileFragment_to_memberProfileFragment,
                    bundleOf(
                        PARLIAMENTDOTUK to vote.memberId,
                    )
                )
            }
        }
    }
}
