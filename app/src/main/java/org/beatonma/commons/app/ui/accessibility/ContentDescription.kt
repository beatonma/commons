package org.beatonma.commons.app.ui.accessibility

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.beatonma.commons.R
import org.beatonma.commons.data.core.room.entities.member.MemberProfile
import org.beatonma.commons.data.core.room.entities.user.UserToken
import org.beatonma.commons.snommoc.models.social.SocialComment
import org.beatonma.commons.snommoc.models.social.SocialContent
import org.beatonma.commons.theme.formatting.formatted
import org.beatonma.commons.compose.R as ComposeR

object ContentDescription {
    val Loading @Composable get() = stringResource(R.string.content_description_loading)
    val More @Composable get() = stringResource(ComposeR.string.content_description_show_more)
    val Less @Composable get() = stringResource(ComposeR.string.content_description_show_less)
}

val MemberProfile.contentDescription
    @Composable get() =
        stringResource(
            R.string.content_description_open_member_profile_for,
            this.name
        )

val UserToken.contentDescription
    @Composable get() =
        stringResource(
            R.string.content_description_open_account_management,
            this.username,
        )

val SocialComment.contentDescription
    @Composable get() =
        stringResource(
            R.string.content_description_social_comment,
            this.username,
            this.modified.formatted(),
            this.text
        )

val SocialContent.contentDescription
    @Composable get(): String {
        val comments =
            stringResource(R.string.content_description_social_comment_count, this.commentCount)
        val ayes =
            stringResource(R.string.content_description_social_votes_count_for, this.ayeVotes)
        val noes =
            stringResource(R.string.content_description_social_votes_count_against, this.noVotes)

        return "$comments; $ayes; $noes"
    }
