package org.beatonma.commons.app.social

import android.animation.ObjectAnimator
import android.content.Context
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.CallSuper
import androidx.annotation.ColorInt
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.beatonma.commons.R
import org.beatonma.commons.annotations.SignInRequired
import org.beatonma.commons.app.ui.navigation.OnBackPressed
import org.beatonma.commons.app.ui.recyclerview.CommonsLoadingAdapter
import org.beatonma.commons.data.NoBodySuccessResult
import org.beatonma.commons.data.core.social.SocialComment
import org.beatonma.commons.data.core.social.SocialContent
import org.beatonma.commons.data.core.social.SocialVoteType
import org.beatonma.commons.databinding.ItemSocialCommentBinding
import org.beatonma.commons.kotlin.data.asStateList
import org.beatonma.commons.kotlin.extensions.*
import org.beatonma.lib.ui.recyclerview.kotlin.extensions.setup
import java.time.LocalDate

private const val TAG = "SocialViewController"

/**
 * View controller for enabling social content in a parent MotionLayout.
 * Requirements:
 *  - Parent layout must be a MotionLayout.
 *  - Parent layout must include @layout/socialhost_motion
 *  - Parent layout must contain a view with ID=R.id.social_parent_container
 *   - This should use the style @style/Social.Compact.Container
 *  - The parent layout should define layoutDescription=@xml/socialhost_motion_scene, or a derivation
 *    thereof, to enable motion animations.
 *
 *  - The parent activity/fragment should also call SocialHost.onBackPressed() in its onBackPressed
 *    implementation.
 *
 * e.g.
 * <!-- my_layout.xml -->
 *
 *      <androidx.constraintlayout.motion.widget.MotionLayout
 *          ...
 *          app:layoutDescription="@xml/socialhost_motion_scene"
 *          >
 *
 *      ...
 *      <include
 *          layout="@layout/socialhost_motion"/>
 *
 *      <View
 *          android:id="@+id/social_parent_container"
 *          style="@style/Social.Compact.Container"/>
 *
 *      ...
 *      </androidx.constraintlayout.motion.widget.MotionLayout>
 *
 */
class SocialViewController(
    private val host: SocialViewHost,
    private val layout: MotionLayout,
    var collapsedTheme: SocialViewTheme,
    var expandedTheme: SocialViewTheme,
): OnBackPressed {
    private val context = layout.context
    private val adapter = CommentAdapter()

    private val title: TextView = layout.findViewById(R.id.title)

    private val upvoteIcon: ImageView = layout.findViewById(R.id.vote_up)
    private val downvoteIcon: ImageView = layout.findViewById(R.id.vote_down)

    private val commentCount: TextView = layout.findViewById(R.id.comment_count)
    private val voteUpCount: TextView = layout.findViewById(R.id.vote_up_count)
    private val voteDownCount: TextView = layout.findViewById(R.id.vote_down_count)

    private val commentAnimationLayer: View = layout.findViewById(R.id.create_comment_alpha_layer)

    private val currentState: State
        get() = when(layout.currentState) {
            R.id.state_expanded -> State.EXPANDED
            R.id.state_compose_comment -> State.COMPOSE_COMMENT
            else -> State.COLLAPSED
        }

    private val theme: SocialViewTheme
        get() = when(currentState) {
            State.COLLAPSED -> collapsedTheme
            else -> expandedTheme
        }

    init {
        layout.findViewById<RecyclerView>(R.id.comments_recyclerview).setup(adapter)

        setupTransitionClickListeners(layout, mapOf(
            R.id.social_parent_container to R.id.state_expanded,
            R.id.create_comment_scrim to R.id.state_expanded,
            R.id.create_comment_fab to R.id.state_compose_comment,
        ))
        setupSocialClickListeners()
    }

    override fun onBackPressed(): Boolean {
        when (layout.currentState) {
            R.id.state_compose_comment -> transitionTo(State.EXPANDED)
            R.id.state_expanded -> transitionTo(State.COLLAPSED)
            else -> return false
        }

        return true
    }

    fun updateUi(content: SocialContent?, layout: MotionLayout) {
        adapter.items = content?.comments ?: listOf()

        val numComments = content?.commentCount() ?: 0
        val numUpvotes = content?.ayeVotes() ?: 0
        val numDownvotes = content?.noVotes() ?: 0

        bindText(
            title to (content?.title ?: "<Title>"),
            commentCount to layout.context.stringCompat(R.string.integer, numComments),
            voteUpCount to layout.context.stringCompat(R.string.integer, numUpvotes),
            voteDownCount to layout.context.stringCompat(R.string.integer, numDownvotes),
        )
    }

    internal fun onVoteSubmissionSuccessful() {

    }

    internal fun onCommentSubmissionSuccessful() {
        transitionTo(State.EXPANDED)
    }

    internal fun updateVoteUi(voteType: SocialVoteType) {
        val theme = this.theme
        fun setIconColors(@ColorInt upvoteColor: Int, @ColorInt downvoteColor: Int) {
            upvoteIcon.imageTintList = upvoteColor.asStateList()
            downvoteIcon.imageTintList = downvoteColor.asStateList()
        }

        when (voteType) {
            SocialVoteType.aye -> {
                setIconColors(theme.colorControlActive, theme.colorControlNormal)
            }
            SocialVoteType.no -> {
                setIconColors(theme.colorControlNormal, theme.colorControlActive)
            }
            else -> {
                setIconColors(theme.colorControlNormal, theme.colorControlNormal)
            }
        }
    }

    internal fun notifyInvalidComment(result: CommentValidation) {
        // TODO show message with reason for failed submission

        val shakeAmount = context.dp(8F)

        // Shake the comment UI side to side
        ObjectAnimator.ofFloat(commentAnimationLayer, View.TRANSLATION_X, 0F, shakeAmount, -shakeAmount, 0F)
            .setDuration(context.resources.getInteger(R.integer.animation_duration).toLong())
            .start()
    }

    private inner class CommentAdapter: CommonsLoadingAdapter<SocialComment>(
        emptyLayoutID = R.layout.item_social_no_comments
    ) {
        private val today = LocalDate.now()  // Used for time formatting
        override fun onCreateDefaultViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
            object : TypedViewHolder(parent.inflate(R.layout.item_social_comment)) {
                private val vh = ItemSocialCommentBinding.bind(itemView)
                override fun bind(item: SocialComment) {
                    vh.apply {
                        bindText(
                            text to item.text,
                            username to item.username,
                            timestamp to item.created.formatted(today = today)
                        )
                    }
                }
            }
    }

    private fun transitionTo(state: State) = layout.transitionToState(
        when (state) {
            State.COLLAPSED -> R.id.state_collapsed
            State.EXPANDED -> R.id.state_expanded
            State.COMPOSE_COMMENT -> R.id.state_compose_comment
        }
    )

    private fun setupSocialClickListeners() {
        layout.findViewById<View>(R.id.upvotes).setOnClickListener { host.onVoteClicked(SocialVoteType.aye) }
        layout.findViewById<View>(R.id.downvotes).setOnClickListener { host.onVoteClicked(SocialVoteType.no) }
        layout.findViewById<View>(R.id.create_comment_submit).setOnClickListener {
            val editText: EditText = layout.findViewById(R.id.create_comment_edittext)
            val comment = editText.text.toString()
            host.validateComment(comment)
        }
    }
}



private fun setupTransitionClickListeners(layout: MotionLayout, map: Map<Int, Int>) {
    map.forEach { (layoutId, stateId) ->
        layout.findViewById<View>(layoutId).setOnClickListener { layout.transitionToState(stateId) }
    }
}


interface SocialViewHost: LifecycleOwner, OnBackPressed {
    val socialViewModel: SocialViewModel
    val socialViewController: SocialViewController

    fun setupViewController(
        layout: MotionLayout,
        collapsedTheme: SocialViewTheme? = null
    ): SocialViewController {
        val expandedTheme = resolveColorAttributes(layout.context)
        return SocialViewController(this, layout, collapsedTheme ?: expandedTheme, expandedTheme)
    }

    fun onVoteSubmissionSuccessful() {
        socialViewController.onVoteSubmissionSuccessful()
    }

    @CallSuper
    fun onCommentSubmissionSuccessful() {
        socialViewController.onCommentSubmissionSuccessful()
    }

    fun onVoteClicked(voteType: SocialVoteType) {
        if (socialViewModel.shouldRemoveVote(voteType)) {
            submitVote(SocialVoteType.NULL)
        }
        else {
            submitVote(voteType)
        }
    }

    @SignInRequired
    fun submitVote(voteType: SocialVoteType) {
        socialViewController.updateVoteUi(voteType)
        lifecycleScope.launch {
            val result = socialViewModel.postVote(voteType)
            when (result) {
                is NoBodySuccessResult -> onVoteSubmissionSuccessful()
                else -> {
                    Log.w(TAG, result.toString())
                }
            }
        }
    }

    @SignInRequired
    private fun submitComment(text: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val result = socialViewModel.postComment(text)
            when (result) {
                is NoBodySuccessResult -> onCommentSubmissionSuccessful()
                else -> {
                    Log.w(TAG, result.toString())
                }
            }
        }
    }

    fun validateComment(comment: String) {
        val result = socialViewModel.validateComment(comment)
        when (result) {
            CommentValidation.VALID -> submitComment(comment)
            else -> socialViewController.notifyInvalidComment(result)
        }
    }

    override fun onBackPressed(): Boolean {
        return socialViewController.onBackPressed()
    }

    fun resolveColorAttributes(context: Context): SocialViewTheme {
        val typedValue = TypedValue()
        val theme = context.theme

        theme.resolveAttribute(R.attr.colorControlNormal, typedValue, true)
        val colorControlNormal = typedValue.data

        theme.resolveAttribute(R.attr.colorControlActivated, typedValue, true)
        val colorControlActive = typedValue.data

        return SocialViewTheme(colorControlNormal, colorControlActive)
    }
}

data class SocialViewTheme(
    val colorControlNormal: Int,
    val colorControlActive: Int
)


internal enum class State {
    COLLAPSED,
    EXPANDED,
    COMPOSE_COMMENT,
}
