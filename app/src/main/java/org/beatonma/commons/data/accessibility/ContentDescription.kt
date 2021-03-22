package org.beatonma.commons.data.accessibility

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import org.beatonma.commons.R
import org.beatonma.commons.data.core.room.entities.member.MemberProfile
import org.beatonma.commons.data.core.room.entities.user.UserToken


val MemberProfile.contentDescription @Composable get() =
    stringResource(
        R.string.content_description_open_member_profile_for,
        this.name
    )

val UserToken.contentDescription @Composable get() =
    stringResource(
        R.string.content_description_open_account_management,
        this.username,
    )
