package org.beatonma.commons.data.testdata

import org.beatonma.commons.androidTest.asDate
import org.beatonma.commons.data.core.room.entities.division.ApiDivision
import org.beatonma.commons.data.core.room.entities.division.ApiVote
import org.beatonma.commons.data.core.room.entities.division.VoteType
import org.beatonma.commons.data.core.room.entities.member.House

const val DIVISION_PUK = 229684

const val API_DIVISION_JSON = """{
    "parliamentdotuk": 229684,
    "title": "Statutory Instruments: Motion for Approval. That the draft Infrastructure Planning (Radioactive Waste Geological Disposal Facilities) Order 2015, which was laid before this House on 12 January, be approved. Q acc agreed to.",
    "date": "2015-03-25",
    "house": "Commons",
    "passed": true,
    "ayes": 275,
    "noes": 33,
    "did_not_vote": 339,
    "abstentions": 1,
    "deferred_vote": true,
    "errors": 0,
    "non_eligible": 0,
    "suspended_or_expelled": 0,
    "votes": [
        {
            "parliamentdotuk": 1609,
            "name": "Chloe Smith",
            "vote": "Abstains",
            "party": {
                "parliamentdotuk": 15,
                "name": "Labour"
            }
        },
        {
            "parliamentdotuk": 3989,
            "name": "Jack Lopresti",
            "vote": "AyeVote",
            "party": {
                "parliamentdotuk": 15,
                "name": "Labour"
            }
        },
        {
            "parliamentdotuk": 1587,
            "name": "Pat McFadden",
            "vote": "DidNotVote",
            "party": {
                "parliamentdotuk": 15,
                "name": "Labour"
            }
        },
        {
            "parliamentdotuk": 1503,
            "name": "Mr Jamie Reed",
            "vote": "NoVote",
            "party": {
                "parliamentdotuk": 15,
                "name": "Labour"
            }
        },
        {
            "parliamentdotuk": 31415926,
            "name": "Fake Member",
            "vote": "SuspendedOrExpelledVote",
            "party": {
                "parliamentdotuk": 15,
                "name": "Labour"
            }
        }
    ]
}"""

val API_DIVISION = ApiDivision(
    parliamentdotuk = 229684,
    title = "Statutory Instruments: Motion for Approval. That the draft Infrastructure Planning (Radioactive Waste Geological Disposal Facilities) Order 2015, which was laid before this House on 12 January, be approved. Q acc agreed to.",
    date = "2015-03-25".asDate(),
    passed = true,
    ayes = 275,
    noes = 33,
    house = House.commons,
    didNotVote = 339,
    abstentions = 1,
    deferredVote = true,
    errors = 0,
    nonEligible = 0,
    suspendedOrExpelled = 0,
    votes = listOf(
        ApiVote(
            memberId = 1609,
            memberName = "Chloe Smith",
            voteType = VoteType.Abstains,
            party = LABOUR,
        ),
        ApiVote(
            memberId = 3989,
            memberName = "Jack Lopresti",
            voteType = VoteType.AyeVote,
            party = LABOUR,
        ),
        ApiVote(
            memberId = 1587,
            memberName = "Pat McFadden",
            voteType = VoteType.DidNotVote,
            party = LABOUR,
        ),
        ApiVote(
            memberId = 1503,
            memberName = "Mr Jamie Reed",
            voteType = VoteType.NoVote,
            party = LABOUR,
        ),
        ApiVote(
            memberId = 31415926,
            memberName = "Fake Member",
            voteType = VoteType.SuspendedOrExpelledVote,
            party = LABOUR,
        )
    ),
    description = null,
    whippedVote = null,
)
