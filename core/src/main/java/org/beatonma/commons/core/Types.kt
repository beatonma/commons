package org.beatonma.commons.core

import androidx.annotation.Keep

typealias ParliamentID = Int
typealias SnommocToken = String

@Keep
@Suppress("EnumEntryName") // Lowercase names to match API values
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

sealed interface DivisionVoteType

@Keep
enum class VoteType : DivisionVoteType {
    AyeVote,
    NoVote,
    Abstains,
    DidNotVote,
    SuspendedOrExpelledVote,
}

@Keep
@Suppress("EnumEntryName") // Lowercase names to match API values
enum class LordsVoteType : DivisionVoteType {
    content,
    not_content,
    ;
}

@Keep
@Suppress("EnumEntryName") // Lowercase names to match API values
enum class ZeitgeistReason {
    feature,
    social,
    unspecified,
    ;
}
