package org.beatonma.commons.app.social

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
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
import org.beatonma.commons.app.ui.recyclerview.setup
import org.beatonma.commons.app.ui.transition.onTransitionEnd
import org.beatonma.commons.data.NetworkError
import org.beatonma.commons.data.NoBodySuccessResult
import org.beatonma.commons.data.core.repository.SocialTarget
import org.beatonma.commons.data.core.social.SocialComment
import org.beatonma.commons.data.core.social.SocialContent
import org.beatonma.commons.data.core.social.SocialVoteType
import org.beatonma.commons.databinding.FragmentSocialExpandedBinding
import org.beatonma.commons.databinding.ItemSocialCommentBinding
import org.beatonma.commons.kotlin.data.asStateList
import org.beatonma.commons.kotlin.extensions.*
import java.time.LocalDate

@Deprecated("Use SocialViewController with SocialViewHost instead")
class ExpandedSocialFragment: BaseSocialFragment(), OnBackPressed {
    private lateinit var binding: FragmentSocialExpandedBinding
    private val adapter = CommentAdapter()

    @ColorInt
    var colorControlNormal: Int = 0

    @ColorInt
    var colorControlActive: Int = 0

    override fun onAttach(context: Context) {
        super.onAttach(context)

        resolveColorAttributes(context)

        val args = arguments ?: return
        val target = SocialTarget(args)

        forTarget(target)
    }

    override fun forTarget(target: SocialTarget) {
        lifecycleScope.launch(Dispatchers.Main) {
            viewmodel.forTarget(target)
            viewmodel.livedata.observe(viewLifecycleOwner, observer)
        }
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

        binding.commentsRecyclerview.setup(adapter)
    }

    override fun onBackPressed(): Boolean {
        if (composeCommentUiVisible()) {
            hideComposeCommentUi()
            return true
        }

        return false
    }

    override fun updateUi(content: SocialContent?) {
        if (content?.userVote != null) {
            updateVoteUi(content.userVote)
        }

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
            upvotes.setOnClickListener { onVoteClicked(SocialVoteType.aye) }
            downvotes.setOnClickListener { onVoteClicked(SocialVoteType.no) }

            createCommentFab.setOnClickListener { showComposeCommentUi() }
            createCommentScrim.setOnClickListener { hideComposeCommentUi() }

            createCommentSubmit.setOnClickListener {
                val comment = binding.createCommentEdittext.text.toString()
                val validationResult = viewmodel.validateComment(comment)
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

        // Shake the comment UI side to side
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
        fun setIconColors(@ColorInt upvoteColor: Int, @ColorInt downvoteColor: Int) {
            binding.voteUp.imageTintList = upvoteColor.asStateList()
            binding.voteDown.imageTintList = downvoteColor.asStateList()
        }

        when (voteType) {
            SocialVoteType.aye -> {
                setIconColors(colorControlActive, colorControlNormal)
            }
            SocialVoteType.no -> {
                setIconColors(colorControlNormal, colorControlActive)
            }
            else -> {
                setIconColors(colorControlNormal, colorControlNormal)
            }
        }
    }

    private fun resolveColorAttributes(context: Context) {
        val typedValue = TypedValue()
        val theme = context.theme

        theme.resolveAttribute(R.attr.colorControlNormal, typedValue, true)
        colorControlNormal = typedValue.data

        theme.resolveAttribute(R.attr.colorControlActivated, typedValue, true)
        colorControlActive = typedValue.data
    }

    private inner class CommentAdapter: CommonsLoadingAdapter<SocialComment>(
        emptyLayoutID = R.layout.item_social_no_comments
    ) {
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
