package org.beatonma.commons.sampledata

import org.beatonma.commons.snommoc.models.ApiConstituencyCandidate
import org.beatonma.commons.snommoc.models.ApiConstituencyElectionDetails
import org.beatonma.commons.snommoc.models.ApiConstituencyMinimal
import org.beatonma.commons.snommoc.models.ApiElection
import java.time.LocalDate


val SampleApiConstituencyElectionDetails
    get() = ApiConstituencyElectionDetails(
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
            date = LocalDate.parse("2010-05-06"),
            electionType = "General Election"
        ),
        candidates = listOf(
            ApiConstituencyCandidate(
                name = "Mcallister, Francis",
                partyName = "NF",
                order = 8,
                votes = 271,
                party = SampleApiParty,
                profile = SampleApiProfile,
            ),
            ApiConstituencyCandidate(
                name = "Cooper, Roger",
                partyName = "Eng Dem",
                order = 7,
                votes = 403,
                party = SampleApiParty,
                profile = SampleApiProfile,
            ),
            ApiConstituencyCandidate(
                name = "Harling, Mike",
                partyName = "Green",
                order = 6,
                votes = 477,
                party = SampleApiParty,
                profile = SampleApiProfile,
            ),
            ApiConstituencyCandidate(
                name = "Wadsworth, Mark",
                partyName = "UKIP",
                order = 5,
                votes = 1234,
                party = SampleApiParty,
                profile = SampleApiProfile,
            ),
            ApiConstituencyCandidate(
                name = "Neal, Dianne",
                partyName = "BNP",
                order = 4,
                votes = 1396,
                party = SampleApiParty,
                profile = SampleApiProfile,
            ),
            ApiConstituencyCandidate(
                name = "Cox, Michael",
                partyName = "LD",
                order = 3,
                votes = 8995,
                party = SampleApiParty,
                profile = SampleApiProfile,
            ),
            ApiConstituencyCandidate(
                name = "Garg, Sidharath",
                partyName = "Lab",
                order = 2,
                votes = 10542,
                party = SampleApiParty,
                profile = SampleApiProfile,
            ),
            ApiConstituencyCandidate(
                name = "Randall, John",
                partyName = "Con",
                order = 1,
                votes = 21758,
                party = SampleApiParty,
                profile = SampleApiProfile,
            )
        )
    )
