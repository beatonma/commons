package org.beatonma.commons.core

import androidx.annotation.Keep

typealias ParliamentID = Int
typealias SnommocToken = String

/**
 * Names are lowercase so they can be used with Navigation Components deepLink
 * as part of the URL path.
 */
@Keep
enum class House {
    commons,
    lords,
    unassigned,
    ;

    fun otherPlace() = when (this) {
        lords -> commons
        commons -> lords
        unassigned -> unassigned
    }
}

@Keep
enum class VoteType {
    AyeVote,
    NoVote,
    Abstains,
    DidNotVote,
    SuspendedOrExpelledVote,
}
