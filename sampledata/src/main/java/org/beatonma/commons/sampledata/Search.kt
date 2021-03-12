package org.beatonma.commons.sampledata

import org.beatonma.commons.data.core.room.entities.constituency.Constituency
import org.beatonma.commons.data.core.room.entities.member.MemberProfile
import org.beatonma.commons.data.core.room.entities.member.Party
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
    SampleMember.asSearchResult(),
)

private fun MemberProfile.asSearchResult() = MemberSearchResult(
    name = name,
    parliamentdotuk = parliamentdotuk,
    constituency = constituency?.asSearchResult(),
    party = party.asSearchResult(),
    portraitUrl = portraitUrl,
    currentPost = currentPost
)

private fun Constituency.asSearchResult() = ConstituencySearchResult(
    name = name,
    parliamentdotuk = parliamentdotuk,
)

private fun Party.asSearchResult() = PartySearchResult(
    name = name,
    parliamentdotuk = parliamentdotuk,
)
