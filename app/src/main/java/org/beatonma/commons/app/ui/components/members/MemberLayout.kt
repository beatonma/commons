package org.beatonma.commons.app.ui.components.members

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.beatonma.commons.app.ui.accessibility.contentDescription
import org.beatonma.commons.app.ui.components.image.Avatar
import org.beatonma.commons.app.ui.components.party.LocalPartyTheme
import org.beatonma.commons.app.ui.components.party.PartyBackground
import org.beatonma.commons.app.ui.components.party.PartyWithTheme
import org.beatonma.commons.app.ui.components.party.partyWithTheme
import org.beatonma.commons.app.ui.currentPostUiDescription
import org.beatonma.commons.compose.TestTag
import org.beatonma.commons.compose.animation.FadeIn
import org.beatonma.commons.compose.shape.withSquareTop
import org.beatonma.commons.core.extensions.fastForEach
import org.beatonma.commons.data.core.room.entities.member.MemberProfile
import org.beatonma.commons.preview.InAppPreview
import org.beatonma.commons.sampledata.SampleMember
import org.beatonma.commons.theme.invertedColors
import org.beatonma.commons.themed.elevation

@VisibleForTesting
internal val MemberLayoutCardWidth = 260.dp
private val TextPadding = 16.dp
private val MemberPadding = 4.dp // Space around each member
private const val MemberDescriptionMaxLines = 2


@Composable
internal fun MemberLayout(
    profile: MemberProfile,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    showImages: Boolean = true,
    decoration: (@Composable () -> Unit)? = null,
) {
    val partyWithTheme = partyWithTheme(profile.party)
    val contentDescription = profile.contentDescription

    CompositionLocalProvider(
        LocalPartyTheme provides partyWithTheme
    ) {
        FadeIn {
            MemberLayoutCard(
                profile.name,
                contentDescription,
                modifier
            ) {
                when {
                    showImages && profile.portraitUrl != null -> {
                        MemberWithPortrait(profile, onClick, decoration = decoration)
                    }
                    else -> MemberWithoutPortrait(profile, onClick, decoration = decoration)
                }
            }
        }
    }
}

@Composable
private fun MemberLayoutCard(
    name: String,
    contentDescription: String,
    modifier: Modifier,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier
            .width(MemberLayoutCardWidth)
            .semantics(mergeDescendants = true) {
                this.contentDescription = contentDescription
                testTag = TestTag.member(name)
            }
            .padding(MemberPadding),
        color = invertedColors.surface,
        elevation = elevation.Card,
        shape = shapes.small,
        content = content,
    )
}

@Composable
private fun MemberWithoutPortrait(
    profile: MemberProfile,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    decoration: (@Composable () -> Unit)?,
) {
    PartyBackground(
        modifier.clickable(onClick = onClick)
    ) {
        MemberText(profile, decoration)
    }
}

@Composable
private fun MemberWithPortrait(
    profile: MemberProfile,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    decoration: (@Composable () -> Unit)?,
) {
    MemberWithPortraitLayout(
        modifier.clickable(onClick = onClick)
    ) {
        Avatar(
            profile.portraitUrl,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
        )
        PartyBackground(
            Modifier.clip(shapes.small.withSquareTop())
        ) {
            MemberText(profile, decoration)
        }
    }
}

@Composable
private fun MemberText(
    profile: MemberProfile,
    decoration: (@Composable () -> Unit)?,
    modifier: Modifier = Modifier,
    partyTheme: PartyWithTheme = LocalPartyTheme.current,
) {
    MemberText(
        name = profile.name,
        party = partyTheme.party.name,
        currentPost = profile.currentPostUiDescription(),
        textColor = partyTheme.theme.onPrimary,
        modifier = modifier,
        decoration = decoration,
    )
}

@Composable
private fun MemberText(
    name: String,
    party: String,
    currentPost: String,
    textColor: Color,
    modifier: Modifier,
    decoration: (@Composable () -> Unit)?,
) {
    Column(
        modifier
            .fillMaxWidth()
            .padding(TextPadding)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                party,
                style = typography.overline,
                maxLines = 1,
                color = textColor,
            )

            decoration?.invoke()
        }

        Text(
            name,
            style = typography.h6,
            color = textColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        MemberDescription(currentPost, textColor)
    }
}

@Composable
private fun MemberDescription(
    text: String,
    textColor: Color,
) {
    // MemberDescription must always be the same height so we add newline characters to fill
    // up to [MemberDescriptionMaxLines].
    Text(
        "$text${"\n".repeat(MemberDescriptionMaxLines)}",
        style = typography.caption,
        color = textColor,
        maxLines = MemberDescriptionMaxLines,
        overflow = TextOverflow.Ellipsis,
    )
}

/**
 * Layout ensures a MemberWithPortrait is the same height as 3 MemberWithoutPortrait.
 */
@Composable
private fun MemberWithPortraitLayout(
    modifier: Modifier,
    content: @Composable () -> Unit,
) {
    Layout(content, modifier) { measurables, constraints ->
        check(measurables.size == 2) { "Expected Avatar, MemberText" }

        val textPlaceable = measurables[1].measure(constraints)
        val textHeight = textPlaceable.height
        val avatarHeight = (textHeight * 2) + (MemberPadding.roundToPx() * 4)
        val avatarPlaceable = measurables[0].measure(
            constraints.copy(minHeight = avatarHeight, maxHeight = avatarHeight)
        )
        val placeables = listOf(avatarPlaceable, textPlaceable)

        val width: Int = constraints.maxWidth
        val height: Int = placeables.sumOf(Placeable::height)

        layout(width, height) {
            val x = 0
            var y = 0

            placeables.fastForEach {
                it.placeRelative(x, y)
                y += it.height
            }
        }
    }
}

@Preview("MemberWithPortrait")
@Composable
private fun MemberWithPortraitPreview() {
    InAppPreview {
        MemberLayout(
            SampleMember,
            onClick = {},
        )
    }
}

@Preview("MemberNoPortrait")
@Composable
private fun MemberNoPortraitPreview() {
    InAppPreview {
        MemberLayout(
            SampleMember,
            showImages = false,
            onClick = {},
        )
    }
}
