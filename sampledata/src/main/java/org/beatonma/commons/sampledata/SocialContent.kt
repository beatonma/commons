package org.beatonma.commons.sampledata

import org.beatonma.commons.snommoc.models.social.SocialComment
import org.beatonma.commons.snommoc.models.social.SocialContent
import org.beatonma.commons.snommoc.models.social.SocialVoteType
import org.beatonma.commons.snommoc.models.social.SocialVotes
import java.time.LocalDateTime

val SampleSocialContent = SocialContent(
    title = "Example title",
    votes = SocialVotes(
        aye = 4,
        no = 7,
    ),
    comments = listOf(
        SocialComment(
            "username2",
            text = "and furthermore",
            modified = LocalDateTime.of(2020, 1, 3, 15, 30, 42),
            created = LocalDateTime.of(2020, 1, 3, 15, 30, 42)
        ),
        SocialComment(
            "username2",
            text = "such insight",
            modified = LocalDateTime.of(2020, 1, 3, 15, 2, 58),
            created = LocalDateTime.of(2020, 1, 3, 15, 2, 58)
        ),
        SocialComment(
            "username1",
            text = "witty comment",
            modified = LocalDateTime.of(2019, 10, 17, 11, 23, 12),
            created = LocalDateTime.of(2019, 10, 17, 11, 23, 12),
        ),
    ),
    userVote = SocialVoteType.aye,
)
