package org.beatonma.commons.app.social

import org.beatonma.commons.data.core.repository.SocialTarget
import org.beatonma.commons.data.core.social.SocialContent
import org.beatonma.commons.data.core.social.SocialVoteType

@Deprecated("Use SocialViewController with SocialViewHost instead")
internal interface SocialUiComponent {
    fun forTarget(target: SocialTarget)

    fun updateUi(content: SocialContent?)
    fun updateVoteUi(voteType: SocialVoteType)
    fun setupClickListeners()

    fun onVoteClicked(voteType: SocialVoteType)
    fun submitVote(voteType: SocialVoteType)

    suspend fun onVoteSubmissionSuccessful()
}
