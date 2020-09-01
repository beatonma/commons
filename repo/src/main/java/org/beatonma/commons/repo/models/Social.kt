package org.beatonma.commons.repo.models

import org.beatonma.commons.data.core.room.entities.user.UserToken
import org.beatonma.commons.snommoc.models.social.SocialTarget
import org.beatonma.commons.snommoc.models.social.SocialVoteType

data class CreatedVote(
    val userToken: UserToken,
    val target: SocialTarget,
    val voteType: SocialVoteType,
)

data class CreatedComment(
    val userToken: UserToken,
    val target: SocialTarget,
    val text: String,
)
