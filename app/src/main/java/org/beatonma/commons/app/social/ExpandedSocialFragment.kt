package org.beatonma.commons.app.social

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.beatonma.commons.BuildConfig.SOCIAL_COMMENT_MAX_LENGTH
import org.beatonma.commons.R
import org.beatonma.commons.annotations.SignInRequired
import org.beatonma.commons.app.ui.navigation.OnBackPressed
import org.beatonma.commons.app.ui.recyclerview.CommonsLoadingAdapter
import org.beatonma.commons.app.ui.transition.onTransitionEnd
import org.beatonma.commons.data.NetworkError
import org.beatonma.commons.data.NoBodySuccessResult
import org.beatonma.commons.data.core.repository.SocialTarget
import org.beatonma.commons.data.core.social.SocialComment
import org.beatonma.commons.data.core.social.SocialContent
import org.beatonma.commons.data.core.social.SocialVoteType
import org.beatonma.commons.databinding.FragmentSocialExpandedBinding
import org.beatonma.commons.databinding.ItemSocialCommentBinding
import org.beatonma.commons.kotlin.extensions.*
import org.beatonma.lib.ui.recyclerview.kotlin.extensions.setup
import java.time.LocalDate

private const val TAG = "ExpandedSocialFrag"

class ExpandedSocialFragment: BaseSocialFragment(), OnBackPressed {
    private lateinit var binding: FragmentSocialExpandedBinding
    private val adapter = CommentAdapter()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val args = arguments ?: return
        val target = SocialTarget(args)
        viewmodel.forTarget(target)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentSocialExpandedBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        binding.commentsRecyclerview.setup(adapter)

        viewmodel.livedata.observe(viewLifecycleOwner, observer)
    }

    override fun onBackPressed(): Boolean {
        if (composeCommentUiVisible()) {
            hideComposeCommentUi()
            return true
        }

        return false
    }

    override fun updateUi(content: SocialContent?) {
        adapter.items = content?.comments ?: listOf()
        val numComments = content?.commentCount() ?: 0
        val numUpvotes = content?.ayeVotes() ?: 0
        val numDownvotes = content?.noVotes() ?: 0

        binding.apply {
            bindText(
                title to (content?.title ?: "<Title>"),
                commentCount to stringCompat(R.string.integer, numComments),
                voteUpCount to stringCompat(R.string.integer, numUpvotes),
                voteDownCount to stringCompat(R.string.integer, numDownvotes),
            )

            bindContentDescription(
                commentCountIcon to stringCompat(R.string.content_description_social_comment_count, numComments),
                voteUp to stringCompat(R.string.content_description_social_votes_count_for, numUpvotes),
                voteDown to stringCompat(R.string.content_description_social_votes_count_against, numDownvotes),
            )
        }
    }

    override fun setupClickListeners() {
        binding.apply {
            upvotes.setOnClickListener { submitVote(SocialVoteType.AYE) }
            downvotes.setOnClickListener { submitVote(SocialVoteType.NO) }

            createCommentFab.setOnClickListener { showComposeCommentUi() }
            createCommentScrim.setOnClickListener { hideComposeCommentUi() }

            createCommentSubmit.setOnClickListener {
                val comment = binding.createCommentEdittext.text.toString()
                val validationResult = validateComment(comment)
                when (validationResult) {
                    CommentValidation.VALID -> submitComment(comment)
                    else -> notifyCommentIsInvalid(validationResult)
                }
            }
        }
    }

    private fun showComposeCommentUi() {
        binding.root.onTransitionEnd { _, _ ->
            binding.createCommentEdittext.focusAndShowKeyboard()
        }
        binding.root.transitionToEnd()
    }

    private fun hideComposeCommentUi() {
        binding.root.onTransitionEnd { _, _ ->
            binding.createCommentEdittext.hideKeyboard()
        }
        binding.root.transitionToStart()
    }

    private fun composeCommentUiVisible() =
        binding.root.currentState == R.id.state_compose_comment

            private fun validateComment(text: String): CommentValidation {
        val length = text.length

        return when {
            length < 10 -> CommentValidation.INVALID_TOO_SHORT
            length > SOCIAL_COMMENT_MAX_LENGTH -> CommentValidation.INVALID_TOO_LONG

            // Further validation on server side
            else -> CommentValidation.VALID
        }
    }

    private fun notifyCommentIsInvalid(result: CommentValidation) {
        when (result) {
            CommentValidation.INVALID_TOO_SHORT -> {
                snackbar("Comment too short!", anchor = binding.createCommentSubmit)
            }
            CommentValidation.INVALID_TOO_LONG -> {
                snackbar(
                    getString(R.string.social_comment_invalid_too_long, SOCIAL_COMMENT_MAX_LENGTH),
                    anchor = binding.createCommentSubmit)
            }
        }

        val shakeAmount = context.dp(8F)

        ObjectAnimator.ofFloat(binding.createCommentAlphaLayer, View.TRANSLATION_X, 0F, shakeAmount, -shakeAmount, 0F)
            .setDuration(requireContext().resources.getInteger(R.integer.animation_duration).toLong())
            .start()
    }

    @SignInRequired
    private fun submitComment(text: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val result = viewmodel.postComment(text)
            when (result) {
                is NetworkError -> networkErrorSnackbar(result.error)
                is NoBodySuccessResult -> onCommentSubmissionSuccessful()

                else -> {
                    Log.w(TAG, result.toString())
                    snackbar(result.message ?: result.toString())
                }
            }
        }
    }

    private suspend fun onCommentSubmissionSuccessful() {
        withContext(Dispatchers.Main) {
            snackbar("Comment submitted!")
            refreshAndObserveSocialContent()
            hideComposeCommentUi()
        }
    }

    override fun updateVoteUi(voteType: SocialVoteType) {
        when (voteType) {
            SocialVoteType.AYE -> {

            }
            SocialVoteType.NO -> {

            }
            else -> {
                Log.w(TAG, "Unhandled voteType: $voteType")
            }
        }
    }

    private inner class CommentAdapter: CommonsLoadingAdapter<SocialComment>() {
        private val today = LocalDate.now()  // Used for time formatting

        override fun onCreateDefaultViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
            object: TypedViewHolder(parent.inflate(R.layout.item_social_comment)) {
                private val vh = ItemSocialCommentBinding.bind(itemView)

                override fun bind(item: SocialComment) {
                    vh.apply {
                        bindText(
                            text to item.text,
                            username to item.username,
                            timestamp to item.created.formatted(today=today)
                        )
                    }
                }
            }
    }


}

private enum class CommentValidation {
    VALID,
    INVALID_TOO_SHORT,
    INVALID_TOO_LONG,
}
