package org.beatonma.commons.repo.deserialization.api.expected

import org.beatonma.commons.repo.androidTest.asDate
import org.beatonma.commons.snommoc.models.ApiConstituencyCandidate
import org.beatonma.commons.snommoc.models.ApiConstituencyElectionDetails
import org.beatonma.commons.snommoc.models.ApiConstituencyMinimal
import org.beatonma.commons.snommoc.models.ApiElection

/** https://snommoc.org/api/constituency/147277/election/19/ */
fun expectedApiConstituencyElectionResult(): ApiConstituencyElectionDetails = ApiConstituencyElectionDetails(
    parliamentdotuk = 383585,
    electorate = 71160,
    turnout = 45076,
    turnoutFraction = "0.633",
    result = "Con Hold",
    majority = 11216,
    constituency = ApiConstituencyMinimal(
        parliamentdotuk = 147277,
        name = "Uxbridge and South Ruislip"
    ),
    election = ApiElection(
        parliamentdotuk = 19,
        name = "2010 General Election",
        date = "2010-05-06".asDate(),
        electionType = "General Election"
    ),
    candidates = listOf(
        ApiConstituencyCandidate(
            name = "Mcallister, Francis",
            partyName = "NF",
            order = 8,
            votes = 271
        ),
        ApiConstituencyCandidate(
            name = "Cooper, Roger",
            partyName = "Eng Dem",
            order = 7,
            votes = 403
        ),
        ApiConstituencyCandidate(
            name = "Harling, Mike",
            partyName = "Green",
            order = 6,
            votes = 477
        ),
        ApiConstituencyCandidate(
            name = "Wadsworth, Mark",
            partyName = "UKIP",
            order = 5,
            votes = 1234
        ),
        ApiConstituencyCandidate(
            name = "Neal, Dianne",
            partyName = "BNP",
            order = 4,
            votes = 1396
        ),
        ApiConstituencyCandidate(
            name = "Cox, Michael",
            partyName = "LD",
            order = 3,
            votes = 8995
        ),
        ApiConstituencyCandidate(
            name = "Garg, Sidharath",
            partyName = "Lab",
            order = 2,
            votes = 10542
        ),
        ApiConstituencyCandidate(
            name = "Randall, John",
            partyName = "Con",
            order = 1,
            votes = 21758
        )
    )
)
