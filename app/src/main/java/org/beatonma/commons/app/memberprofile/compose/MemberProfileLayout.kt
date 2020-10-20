package org.beatonma.commons.app.memberprofile.compose

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRowFor
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import org.beatonma.commons.R
import org.beatonma.commons.app.social.compose.SocialScaffoldColumn
import org.beatonma.commons.compose.components.Avatar
import org.beatonma.commons.compose.components.OptionalText
import org.beatonma.commons.compose.util.dotted
import org.beatonma.commons.data.core.CompleteMember
import org.beatonma.commons.data.core.room.entities.constituency.Constituency
import org.beatonma.commons.data.core.room.entities.member.MemberProfile
import org.beatonma.commons.data.core.room.entities.member.Party
import org.beatonma.commons.data.core.room.entities.member.WebAddress

private const val AVATAR_ASPECT_RATIO = 3F / 2F


@Composable
fun MemberProfileLayout(completeMember: CompleteMember) {
    val profile = completeMember.profile ?: return
    SocialScaffoldColumn(
        contentBefore = {
            Avatar(
                profile.portraitUrl,
                Modifier.fillMaxWidth()
                    .aspectRatio(AVATAR_ASPECT_RATIO)
            )
        },
        contentAfter = {
            MemberProfile(completeMember)
        }
    )
}

@Composable
private fun MemberProfile(completeMember: CompleteMember) {
    val profile = completeMember.profile ?: return
    Column {
        LazyRowFor(items = completeMember.weblinks ?: listOf()) { weblink ->
            Weblink(
                weblink,
                Modifier.padding(horizontal = 4.dp)
            )
        }

        CurrentPosition(profile, completeMember.party, completeMember.constituency)
    }
}

@Composable
private fun CurrentPosition(profile: MemberProfile, party: Party?, constituency: Constituency?) {
    Surface {
        Column {
            if (profile.active == true) {
                OptionalText(profile.currentPost)
                OptionalText(dotted(party?.name, constituency?.name))
            }
            else {
                Text(stringResource(R.string.member_inactive))
            }
        }
    }
}

@Composable
fun Weblink(weblink: WebAddress, modifier: Modifier = Modifier) {
    Text(weblink.url)
}
