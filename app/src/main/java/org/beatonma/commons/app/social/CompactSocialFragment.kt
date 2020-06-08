package org.beatonma.commons.app.social

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.beatonma.commons.R
import org.beatonma.commons.data.core.repository.SocialTarget
import org.beatonma.commons.data.core.social.SocialContent
import org.beatonma.commons.data.core.social.SocialVoteType
import org.beatonma.commons.databinding.FragmentSocialCompactBinding
import org.beatonma.commons.kotlin.extensions.bindText
import org.beatonma.commons.kotlin.extensions.navigateTo
import org.beatonma.commons.kotlin.extensions.stringCompat

private const val TAG = "SocialFragment"

/**
 * A small view that displays a summary of [SocialContent] - e.g. number of comments and/or votes.
 * Users can submit a vote directly from this view by tapping the relevant vote icon.
 * Clicking the comment count will open a larger view ([ExpandedSocialFragment]) to display the
 * comments and enable comment submission.
 */
class CompactSocialFragment: BaseSocialFragment() {
    private lateinit var binding: FragmentSocialCompactBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentSocialCompactBinding.inflate(inflater)
        return binding.root
    }

    override fun forTarget(target: SocialTarget) {
        super.forTarget(target)
        viewmodel.livedata.observe(viewLifecycleOwner, observer)
    }

    override fun updateUi(content: SocialContent?) {
        binding.apply {
            bindText(
                commentCount to stringCompat(R.string.integer, content?.commentCount() ?: 0),
                voteUpCount to stringCompat(R.string.integer, content?.ayeVotes() ?: 0),
                voteDownCount to stringCompat(R.string.integer, content?.noVotes() ?: 0)
            )
        }
    }

    override fun updateVoteUi(voteType: SocialVoteType) {
        when (voteType) {
            SocialVoteType.AYE -> {

            }
            SocialVoteType.NO -> {

            }
        }
    }

    override fun setupClickListeners() {
        binding.apply {
            comments.setOnClickListener { view ->
                openComments(view)
            }
            upvotes.setOnClickListener {
                submitVote(SocialVoteType.AYE)
            }
            downvotes.setOnClickListener {
                submitVote(SocialVoteType.NO)
            }
        }
    }

    private fun openComments(view: View) {
        // TODO animate to [ExpandedSocialFragment]
        view.navigateTo(
            R.id.action_memberProfileFragment_to_expandedSocialFragment,
            viewmodel.socialTarget.toBundle()
        )
    }
}

