package org.beatonma.commons.data.testdata

import org.beatonma.commons.data.core.room.entities.division.ApiDivision
import org.beatonma.commons.data.core.room.entities.division.ApiVote
import org.beatonma.commons.data.core.room.entities.division.VoteType
import org.beatonma.commons.data.core.room.entities.member.House

const val DIVISION_PUK = 229684

const val API_DIVISION_JSON = """{
    "parliamentdotuk": 229684,
    "title": "Statutory Instruments: Motion for Approval. That the draft Infrastructure Planning (Radioactive Waste Geological Disposal Facilities) Order 2015, which was laid before this House on 12 January, be approved. Q acc agreed to.",
    "date": "2015-03-25",
    "passed": true,
    "ayes": 275,
    "noes": 33,
    "house": "Commons",
    "votes": [
        {
            "parliamentdotuk": 1609,
            "name": "Chloe Smith",
            "vote": "Abstains"
        },
        {
            "parliamentdotuk": 3989,
            "name": "Jack Lopresti",
            "vote": "AyeVote"
        },
        {
            "parliamentdotuk": 1587,
            "name": "Pat McFadden",
            "vote": "DidNotVote"
        },
        {
            "parliamentdotuk": 1503,
            "name": "Mr Jamie Reed",
            "vote": "NoVote"
        },
        {
            "parliamentdotuk": 31415926,
            "name": "Fake Member",
            "vote": "SuspendedOrExpelledVote"
        }
    ]
}"""

val API_DIVISION = ApiDivision(
    parliamentdotuk = 229684,
    title = "Statutory Instruments: Motion for Approval. That the draft Infrastructure Planning (Radioactive Waste Geological Disposal Facilities) Order 2015, which was laid before this House on 12 January, be approved. Q acc agreed to.",
    date = "2015-03-25",
    passed = true,
    ayes = 275,
    noes = 33,
    house = House.Commons,
    votes = listOf(
        ApiVote(
            memberId = 1609,
            memberName = "Chloe Smith",
            voteType = VoteType.Abstains
        ),
        ApiVote(
            memberId = 3989,
            memberName = "Jack Lopresti",
            voteType = VoteType.AyeVote
        ),
        ApiVote(
            memberId = 1587,
            memberName = "Pat McFadden",
            voteType = VoteType.DidNotVote
        ),
        ApiVote(
            memberId = 1503,
            memberName = "Mr Jamie Reed",
            voteType = VoteType.NoVote
        ),
        ApiVote(
            memberId = 31415926,
            memberName = "Fake Member",
            voteType = VoteType.SuspendedOrExpelledVote
        )
    )
)
