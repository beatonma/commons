package org.beatonma.commons.app.ui.colors

import org.beatonma.commons.R
import org.beatonma.commons.core.VoteType

val VoteType.colorResId
    get() = when (this) {
        VoteType.AyeVote -> R.color.vote_aye
        VoteType.NoVote -> R.color.vote_no
        VoteType.Abstains -> R.color.vote_abstain
        VoteType.DidNotVote -> R.color.vote_didnotvote
        VoteType.SuspendedOrExpelledVote -> R.color.vote_suspended_or_expelled
    }
