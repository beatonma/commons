package org.beatonma.commons.core

typealias ParliamentID = Int
typealias SnommocToken = String


/**
 * Names are lowercase so they can be used with Navigation Components deepLink
 * as part of the URL path.
 */
enum class House {
    commons,
    lords,
    ;

    fun otherPlace() = when (this) {
        lords -> commons
        commons -> lords
    }
}

enum class VoteType {
    AyeVote,
    NoVote,
    Abstains,
    DidNotVote,
    SuspendedOrExpelledVote,
}

