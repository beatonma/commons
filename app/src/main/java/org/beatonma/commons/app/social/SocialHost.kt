package org.beatonma.commons.app.social

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.recyclerview.widget.RecyclerView
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.navigation.OnBackPressed
import org.beatonma.commons.app.ui.recyclerview.CommonsLoadingAdapter
import org.beatonma.commons.data.core.social.SocialComment
import org.beatonma.commons.data.core.social.SocialContent
import org.beatonma.commons.databinding.ItemSocialCommentBinding
import org.beatonma.commons.kotlin.extensions.bindText
import org.beatonma.commons.kotlin.extensions.formatted
import org.beatonma.commons.kotlin.extensions.inflate
import org.beatonma.commons.kotlin.extensions.stringCompat
import org.beatonma.lib.ui.recyclerview.kotlin.extensions.setup
import java.time.LocalDate

private const val TAG = "SocialHost"

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
class SocialHost(val layout: MotionLayout): OnBackPressed {
    private val adapter = CommentAdapter()

    private val title = layout.findViewById<TextView>(R.id.title)
    private val commentCount = layout.findViewById<TextView>(R.id.comment_count)
    private val voteUpCount = layout.findViewById<TextView>(R.id.vote_up_count)
    private val voteDownCount = layout.findViewById<TextView>(R.id.vote_down_count)

    init {
        layout.findViewById<RecyclerView>(R.id.comments_recyclerview).setup(adapter)

        setupTransitionClickListeners(layout, mapOf(
            R.id.social_parent_container to R.id.state_expanded,
            R.id.create_comment_scrim to R.id.state_expanded,
            R.id.create_comment_fab to R.id.state_compose_comment,
        ))
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
}

private fun setupTransitionClickListeners(layout: MotionLayout, map: Map<Int, Int>) {
    map.forEach { (layoutId, stateId) ->
        layout.findViewById<View>(layoutId).setOnClickListener { layout.transitionToState(stateId) }
    }
}

private enum class State {
    COLLAPSED,
    EXPANDED,
    COMPOSE_COMMENT,
}
