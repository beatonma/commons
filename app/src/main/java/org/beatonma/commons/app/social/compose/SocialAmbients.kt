package org.beatonma.commons.app.social.compose

import androidx.compose.material.MaterialTheme.colors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableAmbient
import androidx.compose.runtime.ambientOf
import androidx.compose.ui.graphics.Color
import org.beatonma.commons.ActionBlock
import org.beatonma.commons.app.ui.colors.ComposePartyColors
import org.beatonma.commons.snommoc.models.social.EmptySocialContent
import org.beatonma.commons.snommoc.models.social.SocialComment

val AmbientSocialContent = ambientOf { EmptySocialContent }
val AmbientSocialTheme: ProvidableAmbient<SocialTheme> =
    ambientOf { error("Social theme has not been provided") }
val AmbientSocialActions = ambientOf { SocialActions() }

class SocialActions(
    val onVoteUpClick: ActionBlock = {},
    val onVoteDownClick: ActionBlock = {},
    val onExpandedCommentIconClick: ActionBlock = {},
    val onCommentClick: (SocialComment) -> Unit = {},
)

class SocialTheme(
    val collapsedBackground: Color = Color.Transparent,
    val collapsedOnBackground: Color = Color.Transparent,
    val expandedBackground: Color = Color.Transparent,
    val expandedOnBackground: Color = Color.Transparent,
)

@Composable
fun ComposePartyColors.asSocialTheme() = SocialTheme(
    primary,
    onPrimary,
    colors.background,
    colors.onBackground
)
