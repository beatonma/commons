package org.beatonma.commons.app.featured

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.runtime.ambientOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.beatonma.commons.app.ui.colors.theme
import org.beatonma.commons.app.ui.compose.components.AmbientPartyTheme
import org.beatonma.commons.app.ui.compose.components.Avatar
import org.beatonma.commons.app.ui.compose.components.DEV_AVATAR
import org.beatonma.commons.app.ui.compose.components.PartyBackground
import org.beatonma.commons.app.ui.compose.components.PartyWithTheme
import org.beatonma.commons.compose.ambient.invertedColors
import org.beatonma.commons.compose.ambient.shapes
import org.beatonma.commons.compose.ambient.typography
import org.beatonma.commons.compose.util.dotted
import org.beatonma.commons.data.coerceCurrentPost
import org.beatonma.commons.data.core.MinimalMember
import org.beatonma.commons.data.core.room.entities.member.MemberProfile
import org.beatonma.commons.snommoc.models.ZeitgeistReason
import org.beatonma.commons.theme.compose.theme.withSquareTop

private val PortraitWidth = 260.dp
private val TextPadding = 16.dp
private val MemberPadding = 4.dp // Space between members

private val MemberTextHeightAmbient = ambientOf<Dp> { error("No size set for member text height") }

@Composable
fun Member(
    member: MinimalMember,
    onClick: (MemberProfile) -> Unit,
    reason: ZeitgeistReason?,
    modifier: Modifier = Modifier,
) {
    /**
     * TODO Replace with value calculated from current typography
     *
     * Currently determined by experiment as typography fontSize does not include ascents/descents/
     * overall line height, only the base font size. Unsure how to calculate this at runtime...
     */
    val memberTextHeight = 103.dp

    val profile = member.profile
    val party = member.party
    val partyWithTheme = PartyWithTheme(party, party.theme())

    Providers(
        AmbientPartyTheme provides partyWithTheme
    ) {
        Surface(
            modifier.width(PortraitWidth)
                .padding(MemberPadding)
                .drawShadow(4.dp, shapes.small),
            color = invertedColors.surface,
        ) {
            Providers(MemberTextHeightAmbient provides memberTextHeight) {
                if (profile.portraitUrl == null) {
                    MemberWithoutPortrait(profile, onClick)
                }
                else {
                    MemberWithPortrait(profile, onClick, reason)
                }
            }
        }
    }
}

@Composable
private fun MemberWithoutPortrait(
    profile: MemberProfile,
    onClick: (MemberProfile) -> Unit,
    height: Dp = MemberTextHeightAmbient.current,
    modifier: Modifier = Modifier,
) {
    PartyBackground(
        modifier.clickable(onClick = { onClick(profile) })
            .height(height)
    ) {
        MemberText(profile)
    }
}

@Composable
private fun MemberWithPortrait(
    profile: MemberProfile,
    onClick: (MemberProfile) -> Unit,
    reason: ZeitgeistReason?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier.clickable { onClick(profile) }
    ) {
        Avatar(
            /* profile.portraitUrl,*/
            DEV_AVATAR,
            modifier = Modifier
                .height((MemberTextHeightAmbient.current * 2) + (MemberPadding * 4))
                .fillMaxWidth(),
        )

        PartyBackground(
            Modifier
                .height(MemberTextHeightAmbient.current)
                .clip(shapes.small.withSquareTop())
        ) {
            MemberText(profile)
        }
    }
}

@Composable
private fun MemberText(
    profile: MemberProfile,
    modifier: Modifier = Modifier,
) = MemberText(profile.name,
    dotted(AmbientPartyTheme.current.party.name, profile.parliamentdotuk.toString()),
    profile.coerceCurrentPost(),
    AmbientPartyTheme.current.theme.onPrimary,
    modifier
)

@Composable
private fun MemberText(
    name: String,
    overline: String,
    currentPost: String,
    textColor: Color,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier
            .height(MemberTextHeightAmbient.current)
            .fillMaxWidth()
            .padding(TextPadding)
    ) {
        Text(
            overline,
            style = typography.overline,
            color = textColor)

        Text(
            name,
            style = typography.h6,
            color = textColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            currentPost,
            style = typography.caption,
            color = textColor,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}
