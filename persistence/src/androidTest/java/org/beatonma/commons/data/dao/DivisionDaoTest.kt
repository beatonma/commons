package org.beatonma.commons.data.dao

import org.beatonma.commons.core.House
import org.beatonma.commons.core.VoteType
import org.beatonma.commons.data.core.room.dao.DivisionDao
import org.beatonma.commons.data.core.room.entities.division.CommonsDivisionData
import org.beatonma.commons.data.core.room.entities.division.CommonsDivisionVoteData
import org.beatonma.commons.test.extensions.util.asDate
import org.junit.Test

private const val DIVISION_ID = 229684

class DivisionDaoTest: BaseRoomDaoTest<DivisionDao>() {

    override val dao: DivisionDao
        get() = db.divisionDao()

    @Test
    fun ensure_DivisionWithVotes_is_retrieved_correctly() {
        runInsert(DivisionDao::insertDivision) {
            CommonsDivisionData(
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
                whippedVote = null,
                description = null,
            )
        }

        runInsert(DivisionDao::insertVotes) {
            listOf(
                CommonsDivisionVoteData(
                    memberId = 1609,
                    divisionId = DIVISION_ID,
                    memberName = "Chloe Smith",
                    voteType = VoteType.Abstains,
                    partyId = 15
                ),
                CommonsDivisionVoteData(
                    memberId = 3989,
                    divisionId = DIVISION_ID,
                    memberName = "Jack Lopresti",
                    voteType = VoteType.AyeVote,
                    partyId = 15
                ),
                CommonsDivisionVoteData(
                    memberId = 1587,
                    divisionId = DIVISION_ID,
                    memberName = "Pat McFadden",
                    voteType = VoteType.DidNotVote,
                    partyId = 15
                ),
                CommonsDivisionVoteData(
                    memberId = 1503,
                    divisionId = DIVISION_ID,
                    memberName = "Mr Jamie Reed",
                    voteType = VoteType.NoVote,
                    partyId = 15
                ),
                CommonsDivisionVoteData(
                    memberId = 31415926,
                    divisionId = DIVISION_ID,
                    memberName = "Fake Member",
                    voteType = VoteType.SuspendedOrExpelledVote,
                    partyId = 15
                )
            )
        }

        runQuery({ dao.getDivisionWithVotes(DIVISION_ID) }) {
            data.run {
                parliamentdotuk shouldbe 229684
                title shouldbe "Statutory Instruments: Motion for Approval. That the draft Infrastructure Planning (Radioactive Waste Geological Disposal Facilities) Order 2015, which was laid before this House on 12 January, be approved. Q acc agreed to."
                date shouldbe "2015-03-25".asDate()
                ayes shouldbe 275
                noes shouldbe 33
                passed shouldbe true
            }
            votes.size shouldbe 5
            votes.first { it.data.memberId == 1609 }.data.run {
                memberName shouldbe "Chloe Smith"
                voteType shouldbe VoteType.Abstains
            }
            votes.first { it.data.memberId == 3989 }.data.run {
                memberName shouldbe "Jack Lopresti"
                voteType shouldbe VoteType.AyeVote
            }
            votes.first { it.data.memberId == 1587 }.data.run {
                memberName shouldbe "Pat McFadden"
                voteType shouldbe VoteType.DidNotVote
            }
            votes.first { it.data.memberId == 1503 }.data.run {
                memberName shouldbe "Mr Jamie Reed"
                voteType shouldbe VoteType.NoVote
            }
            votes.first { it.data.memberId == 31415926 }.data.run {
                memberName shouldbe "Fake Member"
                voteType shouldbe VoteType.SuspendedOrExpelledVote
            }
        }
    }
}
