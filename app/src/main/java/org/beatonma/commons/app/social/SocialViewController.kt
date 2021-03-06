package org.beatonma.commons.app.social

import android.animation.ObjectAnimator
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.IdRes
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.scopes.FragmentScoped
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.navigation.BackPressConsumer
import org.beatonma.commons.app.ui.recyclerview.adapter.AsyncDiffHost
import org.beatonma.commons.app.ui.recyclerview.adapter.LoadingAdapter
import org.beatonma.commons.app.ui.recyclerview.setup
import org.beatonma.commons.databinding.ItemSocialCommentBinding
import org.beatonma.commons.kotlin.data.asStateList
import org.beatonma.commons.kotlin.extensions.*
import org.beatonma.commons.snommoc.models.social.SocialComment
import org.beatonma.commons.snommoc.models.social.SocialContent
import org.beatonma.commons.snommoc.models.social.SocialVoteType
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
@FragmentScoped
class SocialViewController(
    private val host: SocialViewHost,
    private val layout: MotionLayout,
    _collapsedTheme: SocialViewTheme,
    _expandedTheme: SocialViewTheme = _collapsedTheme,
    val defaultTransition: Int? = null,

    @IdRes private val collapsedConstraintsId: Int,
    @IdRes private val expandedConstraintsId: Int,
    @IdRes private val composeCommentConstraintsId: Int,
): BackPressConsumer {
    var collapsedTheme: SocialViewTheme = _collapsedTheme
        set(value) {
            field = value
            updateVoteUi(currentVote)
        }
    var expandedTheme: SocialViewTheme = _expandedTheme
        set(value) {
            field = value
            updateVoteUi(currentVote)
        }

    private val context = layout.context
    private val adapter = CommentAdapter()

    private val title: TextView = layout.findViewById(R.id.social_title)

    private val commentTouchTarget: View = layout.findViewById(R.id.social_comments)
    private val upvoteTouchTarget: View = layout.findViewById(R.id.social_upvotes)
    private val downvoteTouchTarget: View = layout.findViewById(R.id.social_downvotes)

    private val commentIcon: ImageView = layout.findViewById(R.id.social_comment_count_icon)
    private val upvoteIcon: ImageView = layout.findViewById(R.id.social_vote_up)
    private val downvoteIcon: ImageView = layout.findViewById(R.id.social_vote_down)

    private val commentCount: TextView = layout.findViewById(R.id.social_comment_count)
    private val voteUpCount: TextView = layout.findViewById(R.id.social_vote_up_count)
    private val voteDownCount: TextView = layout.findViewById(R.id.social_vote_down_count)

    private val commentAnimationLayer: View = layout.findViewById(R.id.social_create_comment_alpha_layer)

    private var currentVote: SocialVoteType? = null

    private val currentState: State
        get() = when(layout.currentState) {
            expandedConstraintsId -> State.EXPANDED
            composeCommentConstraintsId -> State.COMPOSE_COMMENT
            else -> State.COLLAPSED
        }

    private val theme: SocialViewTheme
        get() = when(currentState) {
            State.COLLAPSED -> collapsedTheme
            else -> expandedTheme
        }

    init {
        layout.findViewById<RecyclerView>(R.id.social_comments_recyclerview).setup(adapter)

        setupTransitionClickListeners(layout, mapOf(
            R.id.social_parent_container to State.EXPANDED,
            R.id.social_create_comment_scrim to State.EXPANDED,
            R.id.social_create_comment_fab to State.COMPOSE_COMMENT,
        ))
        setupSocialClickListeners()
    }

    override fun onBackPressed(): Boolean {
        val targetState = when (layout.currentState) {
            composeCommentConstraintsId -> State.EXPANDED
            expandedConstraintsId -> State.COLLAPSED
            else -> return false
        }

        transitionTo(targetState)

        return true
    }

    fun updateUi(content: SocialContent?, asyncDiffHost: AsyncDiffHost) {
        currentVote = content?.userVote

        asyncDiffHost.diffAdapterItems(adapter, content?.comments ?: listOf())

        val numComments = content?.commentCount() ?: 0
        val numUpvotes = content?.ayeVotes() ?: 0
        val numDownvotes = content?.noVotes() ?: 0

        bindText(
            title to (content?.title ?: "<Title>"),
            commentCount to stringCompat(R.string.integer, numComments),
            voteUpCount to stringCompat(R.string.integer, numUpvotes),
            voteDownCount to stringCompat(R.string.integer, numDownvotes),
        )

        bindContentDescription(
            commentTouchTarget to stringCompat(R.string.content_description_social_comment_count, numComments),
            upvoteTouchTarget to stringCompat(R.string.content_description_social_votes_count_for, numUpvotes),
            downvoteTouchTarget to stringCompat(R.string.content_description_social_votes_count_against, numDownvotes),
        )
    }

    internal fun onVoteSubmissionSuccessful() {

    }

    internal fun onCommentSubmissionSuccessful() {
        transitionTo(State.EXPANDED)
    }

    internal fun updateVoteUi(voteType: SocialVoteType?) {
        val theme = this.theme
        fun setIconColors(@ColorInt upvoteColor: Int, @ColorInt downvoteColor: Int) {
            upvoteIcon.imageTintList = upvoteColor.asStateList()
            downvoteIcon.imageTintList = downvoteColor.asStateList()
        }

        applyColor(theme.colorControlNormal,
            commentIcon,
            commentCount,
            voteUpCount,
            voteDownCount
        )

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

        val shakeAmount = context.dimenCompat(R.dimen.social_invalid_comment_shake_amount).toFloat()

        // Shake the comment UI side to side
        ObjectAnimator.ofFloat(commentAnimationLayer, View.TRANSLATION_X, 0F, shakeAmount, -shakeAmount, 0F)
            .setDuration(context.resources.getInteger(R.integer.animation_duration).toLong())
            .start()
    }

    private inner class CommentAdapter: LoadingAdapter<SocialComment>(
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

    private fun transitionTo(state: State){
        layout.transitionToState(
            when (state) {
                State.COLLAPSED -> collapsedConstraintsId
                State.EXPANDED -> expandedConstraintsId
                State.COMPOSE_COMMENT -> composeCommentConstraintsId
            }
        )

        updateVoteUi(currentVote)
    }

    private fun setupSocialClickListeners() {
        commentTouchTarget.setOnClickListener {
            if (currentState == State.COLLAPSED) transitionTo(State.EXPANDED)
        }
        upvoteTouchTarget.setOnClickListener { onVoteClicked(SocialVoteType.aye) }
        downvoteTouchTarget.setOnClickListener { onVoteClicked(SocialVoteType.no) }
        layout.findViewById<View>(R.id.social_create_comment_submit).setOnClickListener {
            val editText: EditText = layout.findViewById(R.id.social_create_comment_edittext)
            val comment = editText.text.toString()
            host.validateComment(comment)
        }
    }

    private fun onVoteClicked(voteType: SocialVoteType) {
        if (currentState == State.COLLAPSED) {
            transitionTo(State.EXPANDED)
        }
        else {
            host.onVoteClicked(voteType)
        }
    }

    private fun setupTransitionClickListeners(layout: MotionLayout, map: Map<Int, State>) {
        map.forEach { (layoutId, stateId) ->
            layout.findViewById<View>(layoutId).setOnClickListener { transitionTo(stateId) }
        }
    }

    private fun stringCompat(resId: Int, vararg formatArgs: Any?) = layout.context.stringCompat(resId, *formatArgs)
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
