package org.beatonma.commons.data.core.room.dao

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.beatonma.commons.data.BaseRoomDaoTest
import org.beatonma.commons.data.core.room.entities.member.House
import org.beatonma.commons.data.core.room.entities.member.Post
import org.beatonma.commons.data.testdata.API_MEMBER
import org.beatonma.commons.data.testdata.MEMBER_PUK
import org.beatonma.lib.testing.kotlin.extensions.assertions.shouldbe
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    MemberDaoInsertApiCompleteMemberTest::class,
)
class MemberDaoTestSuite

class MemberDaoInsertApiCompleteMemberTest: BaseRoomDaoTest<MemberDao>() {
    override val dao: MemberDao
        get() = db.memberDao()

    override val testPukId: Int = MEMBER_PUK

    @Before
    override fun setUp() {
        super.setUp()

        runBlocking(Dispatchers.Main) {
            dao.insertCompleteMember(MEMBER_PUK, API_MEMBER)
        }
    }

    @Test
    fun ensure_MemberProfile_is_written_and_retrieved_correctly() {
        daoTest(MemberDao::getMemberProfile) {
            parliamentdotuk shouldbe MEMBER_PUK
            name shouldbe "Boris Johnson"
            isMp shouldbe true
            isLord shouldbe false
            age shouldbe 55
            dateOfBirth shouldbe "1964-06-19"
            dateOfDeath shouldbe null
            gender shouldbe "M"
            placeOfBirth!!.run {
                town shouldbe "New York"
                country shouldbe "USA"
            }
            portraitUrl shouldbe "https://members-api.parliament.uk/api/members/1423/Portrait?cropType=OneOne"
            currentPost shouldbe "Prime Minister"
        }
    }

    @Test
    fun ensure_WebAddresses_ars_written_and_retrieved_correctly() {
        daoTest(MemberDao::getWebAddresses) {
            first().run {
                url shouldbe "https://twitter.com/borisjohnson"
                description shouldbe "Twitter"
            }
        }
    }

    @Test
    fun ensure_PhysicalAddresses_ars_written_and_retrieved_correctly() {
        daoTest(MemberDao::getPhysicalAddresses) {
            first().run {
                description shouldbe "Parliamentary"
                address shouldbe "House of Commons, London"
                postcode shouldbe "SW1A 0AA"
                phone shouldbe "+442072194682"
                fax shouldbe "+442072194683"
                email shouldbe "boris.johnson.mp@parliament.uk"

            }
        }
    }

    @Test
    fun ensure_CommitteeMemberships_ars_written_and_retrieved_correctly() {
        daoTest(MemberDao::getCommitteeMembershipWithChairship) {
            val first = first()
            first.membership.run {
                parliamentdotuk shouldbe 2
                name shouldbe "Administration Committee"
                start shouldbe "2015-07-21"
                end shouldbe "2017-05-03"
            }

            first.chairs.first().run {
                committeeId shouldbe 2
                start shouldbe "2015-07-21"
                end shouldbe "2017-05-03"
            }
        }
    }

    @Test
    fun ensure_HistoricalConstituencies_ars_written_and_retrieved_correctly() {
        daoTest(MemberDao::getHistoricalConstituencies) {
            size shouldbe 2
            val first = first { it.historicalConstituency.start == "2017-06-08" }
            first.constituency.run {
                name shouldbe "Uxbridge and South Ruislip"
                parliamentdotuk shouldbe 147277
            }
            first.historicalConstituency.run {
                end shouldbe "2019-11-06"
            }
            first.election.run {
                name shouldbe "2017 General Election"
                date shouldbe "2017-06-08"
                electionType shouldbe "General Election"
            }
        }
    }

    @Test
    fun ensure_Experiences_ars_written_and_retrieved_correctly() {
        daoTest(MemberDao::getExperiences) {
            first().run {
                category shouldbe "Political"
                organisation shouldbe "Liberal Democrats"
                title shouldbe "National Treasurer"
                start shouldbe "2012-12-25"
                end shouldbe "2015-12-25"
            }
        }
    }

    @Test
    fun ensure_Houses_ars_written_and_retrieved_correctly() {
        daoTest(MemberDao::getHouseMemberships) {
            size shouldbe 2
            first { it.start == "2001-06-07" }.run {
                house shouldbe House.Commons
                end shouldbe "2008-06-04"
            }
        }
    }

    @Test
    fun ensure_FinancialInterests_ars_written_and_retrieved_correctly() {
        daoTest(MemberDao::getFinancialInterests) {
            first().run {
                interestId shouldbe 12485
                category shouldbe "Category 1: Directorships"
                description shouldbe "Director, Durham Group Estates Ltd"
                dateCreated shouldbe "2013-12-02"
                dateAmended shouldbe null
                dateDeleted shouldbe null
                registeredLate shouldbe false
            }
        }
    }

    @Test
    fun ensure_PartyAssociations_ars_written_and_retrieved_correctly() {
        daoTest(MemberDao::getPartyAssociations) {
            size shouldbe 2
            val first = first { it.partyAssocation.start == "2017-06-08" }
            first.partyAssocation.run {
                end shouldbe "2019-11-06"
            }
            first.party.run {
                name shouldbe "Conservative"
                parliamentdotuk shouldbe 4
            }
        }
    }

    @Test
    fun ensure_Posts_ars_written_and_retrieved_correctly() {
        daoTest(MemberDao::getPosts) {
            size shouldbe 3
            first { it.postType == Post.PostType.GOVERNMENTAL }.run {
                parliamentdotuk shouldbe 661
                name shouldbe "Prime Minister, First Lord of the Treasury and Minister for the Civil Service"
                start shouldbe "2019-07-24"
                end shouldbe "2020-01-01"
            }
            first { it.postType == Post.PostType.PARLIAMENTARY }.run {
                parliamentdotuk shouldbe 787
                name shouldbe "Leader of the Conservative Party"
                start shouldbe "2019-07-23"
                end shouldbe "2020-01-02"
            }
            first { it.postType == Post.PostType.OPPOSITION }.run {
                parliamentdotuk shouldbe 15
                name shouldbe "Shadow Minister (Business, Innovation and Skills)"
                start shouldbe "2005-12-09"
                end shouldbe "2007-07-16"
            }
        }
    }

    @Test
    fun ensure_TopicOfInterest_ars_written_and_retrieved_correctly() {
        daoTest(MemberDao::getTopicsOfInterest) {
            first().run {
                category shouldbe "Countries of Interest"
                topic shouldbe "Australia, Fiji, New Zealand, Samoa"
            }
        }
    }
}
