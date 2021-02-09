package org.beatonma.commons.sampledata

import org.beatonma.commons.snommoc.models.search.ConstituencySearchResult
import org.beatonma.commons.snommoc.models.search.MemberSearchResult
import org.beatonma.commons.snommoc.models.search.PartySearchResult

val SampleSearchResults = listOf(
    MemberSearchResult(
        name = "Michael Beaton",
        constituency = ConstituencySearchResult(
            10, "Highland"
        ),
        party = PartySearchResult(100001, "Independent"),
        parliamentdotuk = 65,
        currentPost = "Developer",
        portraitUrl = DEV_AVATAR,
    ),
)
