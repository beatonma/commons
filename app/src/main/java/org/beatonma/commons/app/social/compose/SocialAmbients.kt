package org.beatonma.commons.app.social.compose

import androidx.compose.runtime.ambientOf
import org.beatonma.commons.ActionBlock
import org.beatonma.commons.app.social.SocialViewModel
import org.beatonma.commons.snommoc.models.social.EmptySocialContent
import org.beatonma.commons.snommoc.models.social.SocialComment

val SocialAmbient = ambientOf { EmptySocialContent }
val SocialViewModelAmbient =
    ambientOf<SocialViewModel> { error("SocialViewModel has not been provided") }

val SocialActionsAmbient = ambientOf { SocialActions() }

class SocialActions(
    val onVoteUpClick: ActionBlock = {},
    val onVoteDownClick: ActionBlock = {},
    val onExpandedCommentIconClick: ActionBlock = {},
    val onCommentClick: (SocialComment) -> Unit = {},
)
