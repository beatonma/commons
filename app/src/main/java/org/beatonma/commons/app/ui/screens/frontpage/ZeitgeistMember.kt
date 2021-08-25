package org.beatonma.commons.app.ui.screens.frontpage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.beatonma.commons.app.ui.accessibility.contentDescription
import org.beatonma.commons.app.ui.components.image.Avatar
import org.beatonma.commons.app.ui.components.party.LocalPartyTheme
import org.beatonma.commons.app.ui.components.party.PartyBackground
import org.beatonma.commons.app.ui.components.party.partyWithTheme
import org.beatonma.commons.compose.shape.withSquareTop
import org.beatonma.commons.compose.util.dot
import org.beatonma.commons.data.core.MinimalMember
import org.beatonma.commons.data.core.room.entities.member.MemberProfile
import org.beatonma.commons.data.resolution.currentPostUiDescription
import org.beatonma.commons.snommoc.models.ZeitgeistReason
import org.beatonma.commons.theme.compose.theme.invertedColors

private val PortraitWidth = 260.dp
private val TextPadding = 16.dp
private val MemberPadding = 4.dp // Space between members

private val LocalMemberTextHeight =
    compositionLocalOf<Dp> { error("No size set for member text height") }

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
    val partyWithTheme = partyWithTheme(party)

    val contentDescription = profile.contentDescription

    CompositionLocalProvider(
        LocalPartyTheme provides partyWithTheme
    ) {
        Surface(
            modifier
                .width(PortraitWidth)
                .padding(MemberPadding)
                .shadow(4.dp, shapes.small)
                .semantics(mergeDescendants = true) {
                    this.contentDescription = contentDescription
                },
            color = invertedColors.surface,
        ) {
            CompositionLocalProvider(LocalMemberTextHeight provides memberTextHeight) {
                if (profile.portraitUrl == null) {
                    MemberWithoutPortrait(profile, onClick)
                } else {
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
    modifier: Modifier = Modifier,
    height: Dp = LocalMemberTextHeight.current,
) {
    PartyBackground(
        modifier
            .clickable(onClick = { onClick(profile) })
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
            profile.portraitUrl,
            modifier = Modifier
                .height((LocalMemberTextHeight.current * 2) + (MemberPadding * 4))
                .fillMaxWidth(),
        )

        PartyBackground(
            Modifier
                .height(LocalMemberTextHeight.current)
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
) =
    MemberText(
        profile.name,
        LocalPartyTheme.current.party.name dot profile.parliamentdotuk.toString(),
        profile.currentPostUiDescription(),
        LocalPartyTheme.current.theme.onPrimary,
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
            .height(LocalMemberTextHeight.current)
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
