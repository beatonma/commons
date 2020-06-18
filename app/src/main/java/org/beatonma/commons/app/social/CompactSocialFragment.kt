package org.beatonma.commons.app.social

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.beatonma.commons.R
import org.beatonma.commons.data.core.repository.SocialTarget
import org.beatonma.commons.data.core.social.SocialContent
import org.beatonma.commons.data.core.social.SocialVoteType
import org.beatonma.commons.databinding.FragmentSocialCompactBinding
import org.beatonma.commons.kotlin.extensions.bindText
import org.beatonma.commons.kotlin.extensions.navigateTo
import org.beatonma.commons.kotlin.extensions.stringCompat

/**
 * A small view that displays a summary of [SocialContent] - e.g. number of comments and/or votes.
 * Users can submit a vote directly from this view by tapping the relevant vote icon.
 * Clicking the comment count will open a larger view ([ExpandedSocialFragment]) to display the
 * comments and enable comment submission.
 */
@Deprecated("Use SocialViewController with SocialViewHost instead")
class CompactSocialFragment: BaseSocialFragment() {
    private lateinit var binding: FragmentSocialCompactBinding
    @IdRes var navigateToCommentsActionId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentSocialCompactBinding.inflate(inflater)
        return binding.root
    }

    override fun forTarget(target: SocialTarget) {
        lifecycleScope.launch(Dispatchers.Main) {
            viewmodel.forTarget(target)
            viewmodel.livedata.observe(viewLifecycleOwner, observer)
        }
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
            SocialVoteType.aye -> {

            }
            SocialVoteType.no -> {

            }
            SocialVoteType.NULL -> {
                // Reset
            }
        }
    }

    override fun setupClickListeners() {
        binding.apply {
            comments.setOnClickListener { view ->
                openComments(view)
            }
            upvotes.setOnClickListener {
                onVoteClicked(SocialVoteType.aye)
            }
            downvotes.setOnClickListener {
                onVoteClicked(SocialVoteType.no)
            }
        }
    }

    private fun openComments(view: View) {
        // TODO animate to [ExpandedSocialFragment]
        view.navigateTo(
            navigateToCommentsActionId,
            viewmodel.socialTarget.toBundle(),
            extras = FragmentNavigatorExtras(
                binding.flow to stringCompat(R.string.transition_social),
//                binding.commentCountIcon to stringCompat(R.string.transition_comment),
//                binding.voteUp to stringCompat(R.string.transition_upvote),
//                binding.voteDown to stringCompat(R.string.transition_downvote),
            )
        )
    }
}

