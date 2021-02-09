package org.beatonma.commons.sampledata

import org.beatonma.commons.snommoc.models.social.SocialComment
import org.beatonma.commons.snommoc.models.social.SocialContent
import org.beatonma.commons.snommoc.models.social.SocialVoteType
import org.beatonma.commons.snommoc.models.social.SocialVotes
import java.time.LocalDateTime

val SampleSocialContent = SocialContent(
    title = "example",
    votes = SocialVotes(
        aye = 3,
        no = 7,
    ),
    comments = listOf(
        SocialComment(
            "username",
            text = "witty comment",
            modified = LocalDateTime.now(),
            created = LocalDateTime.now()
        ),
    ),
    userVote = SocialVoteType.aye,
)
