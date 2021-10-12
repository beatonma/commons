package org.beatonma.commons.app.ui.screens.frontpage

import android.text.TextPaint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme.shapes
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.beatonma.commons.app.ui.accessibility.contentDescription
import org.beatonma.commons.app.ui.components.image.Avatar
import org.beatonma.commons.app.ui.components.party.LocalPartyTheme
import org.beatonma.commons.app.ui.components.party.PartyBackground
import org.beatonma.commons.app.ui.components.party.PartyWithTheme
import org.beatonma.commons.app.ui.components.party.partyWithTheme
import org.beatonma.commons.app.ui.currentPostUiDescription
import org.beatonma.commons.compose.shape.withSquareTop
import org.beatonma.commons.data.core.MinimalMember
import org.beatonma.commons.data.core.room.entities.member.MemberProfile
import org.beatonma.commons.theme.invertedColors
import org.beatonma.commons.themed.themedElevation

private val CardWidth = 260.dp
private val TextPadding = 16.dp
private val MemberPadding = 4.dp // Space between members
private const val MemberDescriptionMaxLines = 2


@Composable
fun MemberLayout(
    member: MinimalMember,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    decoration: @Composable () -> Unit = {},
) {
    val profile = member.profile
    val partyWithTheme = partyWithTheme(member.party)

    val contentDescription = profile.contentDescription

    CompositionLocalProvider(
        LocalPartyTheme provides partyWithTheme
    ) {
        Surface(
            modifier
                .width(CardWidth)
                .padding(MemberPadding)
                .semantics(mergeDescendants = true) {
                    this.contentDescription = contentDescription
                },
            color = invertedColors.surface,
            elevation = themedElevation.Card,
            shape = shapes.small,
        ) {
            if (profile.portraitUrl == null) {
                MemberWithoutPortrait(profile, onClick, decoration = decoration)
            } else {
                MemberWithPortrait(profile, onClick, decoration = decoration)
            }
        }
    }
}

@Composable
private fun MemberWithoutPortrait(
    profile: MemberProfile,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    decoration: @Composable () -> Unit,
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
    decoration: @Composable () -> Unit,
) {
    Column(
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
    decoration: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    partyTheme: PartyWithTheme = LocalPartyTheme.current,
) {
    MemberText(
        name = profile.name,
        overline = partyTheme.party.name,
        currentPost = profile.currentPostUiDescription(),
        textColor = partyTheme.theme.onPrimary,
        decoration = decoration,
        modifier = modifier,
    )
}

@Composable
private fun MemberText(
    name: String,
    overline: String,
    currentPost: String,
    textColor: Color,
    decoration: @Composable () -> Unit,
    modifier: Modifier,
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
                overline,
                style = typography.overline,
                maxLines = 1,
                color = textColor,
            )

            decoration()
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

/**
 * Description may be anything up to [MemberDescriptionMaxLines] lines long but [MemberText] needs
 * to be a fixed height so we force the maximum possible height based on the font.
 *
 * No straightforward way to get actual font height (including ascenders/descenders) from
 * [androidx.compose.ui.text.TextStyle] so we resort to using TextPaint.FontMetrics directly.
 * Feels a bit hacky...
 */
@Composable
private fun MemberDescription(
    text: String,
    textColor: Color,
) {
    val fontStyle: TextStyle = typography.caption
    val fontSize = fontStyle.fontSize
    val captionTextHeight = remember(fontSize) {
        val paint: TextPaint = TextPaint().apply {
            textSize = fontSize.value
        }
        with(paint.fontMetrics) {
            ((bottom - top) * MemberDescriptionMaxLines).dp
        }
    }

    Text(
        text,
        style = fontStyle,
        color = textColor,
        maxLines = MemberDescriptionMaxLines,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.requiredHeight(captionTextHeight),
    )
}