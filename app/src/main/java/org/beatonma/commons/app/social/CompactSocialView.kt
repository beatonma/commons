package org.beatonma.commons.app.social

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.FragmentNavigatorExtras
import org.beatonma.commons.R
import org.beatonma.commons.data.core.repository.SocialTarget
import org.beatonma.commons.data.core.social.SocialContent
import org.beatonma.commons.databinding.FragmentSocialCompactBinding
import org.beatonma.commons.kotlin.extensions.bindText
import org.beatonma.commons.kotlin.extensions.inflate
import org.beatonma.commons.kotlin.extensions.navigateTo
import org.beatonma.commons.kotlin.extensions.stringCompat

@Deprecated("Use SocialViewController with SocialViewHost instead")
class CompactSocialView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
): ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {
    private val binding = FragmentSocialCompactBinding.bind(
        inflate(R.layout.fragment_social_compact, attachToRoot = true)
    )

    fun updateUi(content: SocialContent?) {
        binding.apply {
            bindText(
                commentCount to stringCompat(R.string.integer, content?.commentCount() ?: 0),
                voteUpCount to stringCompat(R.string.integer, content?.ayeVotes() ?: 0),
                voteDownCount to stringCompat(R.string.integer, content?.noVotes() ?: 0)
            )
        }
    }

    fun openComments(target: SocialTarget, @IdRes navActionResId: Int) {
        // TODO animate to [ExpandedSocialFragment]
        navigateTo(
            navActionResId,
            target.toBundle(),
            extras = FragmentNavigatorExtras(
                binding.commentCountIcon to stringCompat(R.string.transition_comment),
//                binding.voteUp to "vote_up",
//                binding.voteDown to "vote_down",
            )
        )
    }
}
