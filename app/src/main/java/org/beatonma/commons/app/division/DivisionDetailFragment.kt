package org.beatonma.commons.app.division

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.beatonma.commons.R
import org.beatonma.commons.app.social.SocialViewController
import org.beatonma.commons.app.social.SocialViewHost
import org.beatonma.commons.app.social.SocialViewModel
import org.beatonma.commons.app.ui.CommonsFragment
import org.beatonma.commons.app.ui.navigation.BackPressConsumer
import org.beatonma.commons.app.ui.recyclerview.adapter.LoadingAdapter
import org.beatonma.commons.app.ui.recyclerview.defaultPrimaryContentSpacing
import org.beatonma.commons.app.ui.recyclerview.setup
import org.beatonma.commons.data.IoResultObserver
import org.beatonma.commons.data.core.room.entities.division.Division
import org.beatonma.commons.data.core.room.entities.division.VoteType
import org.beatonma.commons.data.core.room.entities.division.VoteWithParty
import org.beatonma.commons.data.core.room.entities.member.House
import org.beatonma.commons.data.core.social.SocialContent
import org.beatonma.commons.data.resolution.describe
import org.beatonma.commons.databinding.FragmentDivisionDetailBinding
import org.beatonma.commons.databinding.ItemDivisionVoteBinding
import org.beatonma.commons.databinding.MergeDivisionResultsChartKeyBinding
import org.beatonma.commons.kotlin.data.asStateList
import org.beatonma.commons.kotlin.extensions.*

private const val TAG = "DivisionProfileFragment"

@AndroidEntryPoint
class DivisionDetailFragment : CommonsFragment(),
    BackPressConsumer,
    SocialViewHost {
    companion object {
        const val HOUSE = "house"
    }

    private lateinit var binding: FragmentDivisionDetailBinding
    private lateinit var voteSummaryBinding: MergeDivisionResultsChartKeyBinding
    private val viewmodel: DivisionDetailViewModel by viewModels()
    private val adapter = VotesAdapter()

    override val socialViewModel: SocialViewModel by viewModels()
    override lateinit var socialViewController: SocialViewController
    override val socialObserver: IoResultObserver<SocialContent> = createSocialObserver()

    private fun getDivisionFromBundle(): BundledDivision = arguments.getDivision()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewmodel.forDivision(getDivisionFromBundle())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentDivisionDetailBinding.inflate(inflater)
        voteSummaryBinding = MergeDivisionResultsChartKeyBinding.bind(binding.root)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        socialViewController = setupViewController(binding.root,
            collapsedConstraintsId = R.id.state_default)

        binding.recyclerview.setup(
            adapter,
            space = defaultPrimaryContentSpacing(view.context),
        )

        viewmodel.liveData.observe(viewLifecycleOwner) { result ->
            result.report()

            withNotNull(result.data) { data ->
                lifecycleScope.launch {
                    val votes = viewmodel.sortedVotes(data.votes)
                    withContext(Dispatchers.Main) {
                        observeSocialContent(data.division)
                        updateUI(data.division, votes)
                    }
                }
            }
        }
    }

    private fun updateUI(division: Division, votes: List<VoteWithParty>) {
        with (binding) {
            when (division.house) {
                House.lords -> bindText(
                    title to division.title,
                    description to division.description,
                )
                House.commons -> bindText(
                    title to stringCompat(R.string.division_title),
                    description to division.title
                )
            }

            bindText(
                houseAndDate to context?.dotted(
                    context?.describe(division.house),
                    division.date.formatted(),
                )
            )

            chart.categories = viewmodel.getVoteChartData(division)
        }

        with (voteSummaryBinding) {
            bindText(
                ayes to stringCompat(R.string.division_ayes, division.ayes),
                noes to stringCompat(R.string.division_noes, division.noes),
            )
            bindText(didNotVotes, division.didNotVote) { stringCompat(R.string.division_did_not_vote, division.didNotVote)}
            bindText(abstentions, division.abstentions) { stringCompat(R.string.division_did_not_vote, division.abstentions)}

            setDependantVisibility(parent = didNotVotes, didNotVotesColor, didNotVotesFlow)
            setDependantVisibility(parent = abstentions, abstentionsColor, abstentionsFlow)
        }

        adapter.items = votes
    }
}

private class VotesAdapter : LoadingAdapter<VoteWithParty>() {
    private data class IconTheme(
        @DrawableRes val drawableRes: Int,
        @ColorRes val colorRes: Int
    ) {
        fun applyTo(imageView: ImageView) {
            imageView.apply {
                setImageResource(drawableRes)
                imageTintList = colorCompat(colorRes).asStateList()
            }
        }
    }

    private val voteThemes = mapOf(
        VoteType.AyeVote to IconTheme(R.drawable.ic_vote_aye, R.color.vote_aye),
        VoteType.NoVote to IconTheme(R.drawable.ic_vote_no, R.color.vote_no),
        VoteType.Abstains to IconTheme(R.drawable.ic_vote_neutral, R.color.vote_abstain),
        VoteType.DidNotVote to IconTheme(R.drawable.ic_vote_neutral, R.color.vote_didnotvote),
        VoteType.SuspendedOrExpelledVote to IconTheme(R.drawable.ic_vote_neutral, R.color.vote_suspended_or_expelled),
    )

    override fun onCreateDefaultViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
        object: TypedViewHolder(parent.inflate(R.layout.item_division_vote)) {
            private val vh = ItemDivisionVoteBinding.bind(itemView)

            override fun bind(item: VoteWithParty) {
                vh.apply {
                    bindText(
                        memberName to item.vote.memberName,
                        memberParty to item.party?.name,
                    )

                    voteThemes[item.vote.voteType]?.applyTo(voteTypeIcon)
                }

                itemView.setOnClickListener { view ->
                    view.navigateTo(
                        R.id.action_divisionProfileFragment_to_memberProfileFragment,
                        item.vote.memberBundle()
                    )
                }
            }
        }
}
