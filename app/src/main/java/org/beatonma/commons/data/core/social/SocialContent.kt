package org.beatonma.commons.data.core.social

enum class SocialContentTarget(val urlPath: String) {
    MEMBER("member"),
    BILL("bill"),
    COMMONS_DIVISION("division-commons"),
    LORDS_DIVISION("division-lords"),
    ;
}

data class SocialContent(
    val comments: List<SocialComment>,
    val votes: SocialVotes,
)
