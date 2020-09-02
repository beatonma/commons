package org.beatonma.commons.data.dao

import kotlinx.coroutines.runBlocking
import org.beatonma.commons.core.House
import org.beatonma.commons.data.core.room.dao.MemberDao
import org.beatonma.commons.data.core.room.entities.constituency.Constituency
import org.beatonma.commons.data.core.room.entities.election.Election
import org.beatonma.commons.data.core.room.entities.member.*
import org.beatonma.commons.test.extensions.assertions.shouldbe
import org.beatonma.commons.test.extensions.util.asDate
import org.junit.Before
import org.junit.Test

private const val MEMBER_ID = 1423

class MemberDaoTest: BaseRoomDaoTest<MemberDao>() {
    override val dao: MemberDao
        get() = db.memberDao()

    private val profile = MemberProfile(
        parliamentdotuk = MEMBER_ID,
        name = "Boris Johnson",
        party = Party(
            name = "Conservative",
            parliamentdotuk = 4,
        ),
        constituency = Constituency(
            name = "Uxbridge and South Ruislip",
            parliamentdotuk = 147277,
        ),
        isMp = true,
        isLord = false,
        dateOfBirth = "1964-06-19".asDate(),
        dateOfDeath = null,
        age = 55,
        gender = "M",
        placeOfBirth = Town(
            town = "New York",
            country = "USA"
        ),
        portraitUrl = "https://members-api.parliament.uk/api/members/1423/Portrait?cropType=OneOne",
        currentPost = "Prime Minister",
    )

    @Before
    override fun setUp() {
        super.setUp()

        runBlocking {
            dao.safeInsertProfile(profile)
        }
    }

    @Test
    fun ensure_MemberProfile_is_written_and_retrieved_correctly() {
        runQuery({ dao.getMemberProfile(MEMBER_ID) }) {
            parliamentdotuk shouldbe MEMBER_ID
            name shouldbe "Boris Johnson"
            isMp shouldbe true
            isLord shouldbe false
            age shouldbe 55
            dateOfBirth shouldbe "1964-06-19".asDate()
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
        runInsert(MemberDao::insertWebAddresses) {
            listOf(
                WebAddress(
                    memberId = MEMBER_ID,
                    url = "https://twitter.com/borisjohnson",
                    description = "Twitter",
                )
            )
        }

        runQuery({ dao.getWebAddresses(MEMBER_ID)}) {
            first().run {
                url shouldbe "https://twitter.com/borisjohnson"
                description shouldbe "Twitter"
            }
        }
    }

    @Test
    fun ensure_PhysicalAddresses_ars_written_and_retrieved_correctly() {
        runInsert(MemberDao::insertPhysicalAddresses) {
            listOf(
                PhysicalAddress(
                    memberId = MEMBER_ID,
                    description = "Parliamentary",
                    address = "House of Commons, London",
                    postcode = "SW1A 0AA",
                    phone = "+442072194682",
                    fax = "+442072194683",
                    email = "boris.johnson.mp@parliament.uk",
                )
            )
        }

        runQuery({ dao.getPhysicalAddresses(MEMBER_ID) }) {
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
        runInsert(MemberDao::insertCommitteeMemberships) {
            listOf(
                CommitteeMembership(
                    memberId = MEMBER_ID,
                    parliamentdotuk = 2,
                    name = "Administration Committee",
                    start = "2015-07-21".asDate(),
                    end = "2017-05-03".asDate(),
                )
            )
        }

        runInsert(MemberDao::insertCommitteeChairships) {
            listOf(
                CommitteeChairship(
                    memberId = MEMBER_ID,
                    committeeId = 2,
                    start = "2015-07-21".asDate(),
                    end = "2017-05-03".asDate(),
                )
            )
        }

        runQuery({ dao.getCommitteeMembershipWithChairship(MEMBER_ID) }) {
            val first = first()
            first.membership.run {
                parliamentdotuk shouldbe 2
                name shouldbe "Administration Committee"
                start shouldbe "2015-07-21".asDate()
                end shouldbe "2017-05-03".asDate()
            }

            first.chairs.first().run {
                committeeId shouldbe 2
                start shouldbe "2015-07-21".asDate()
                end shouldbe "2017-05-03".asDate()
            }
        }
    }

    @Test
    fun ensure_HistoricalConstituencies_ars_written_and_retrieved_correctly() {
        runInsert(MemberDao::insertConstituencyIfNotExists) {
            Constituency(
                name = "Uxbridge and South Ruislip",
                parliamentdotuk = 147277,
            )
        }
        runInsert(MemberDao::insertElection) {
            Election(
                parliamentdotuk = 377,
                name = "2017 General Election",
                date = "2017-06-08".asDate(),
                electionType = "General Election",
            )
        }
        runInsert(MemberDao::insertMemberForConstituencies) {
            listOf(
                HistoricalConstituency(
                    memberId = MEMBER_ID,
                    constituencyId = 147277,
                    electionId = 377,
                    start = "2017-06-08".asDate(),
                    end = "2019-11-06".asDate(),
                )
            )
        }

        runQuery({ dao.getHistoricalConstituencies(MEMBER_ID) }) {
            val first = first { it.historicalConstituency.start == "2017-06-08".asDate() }
            first.constituency.run {
                name shouldbe "Uxbridge and South Ruislip"
                parliamentdotuk shouldbe 147277
            }
            first.historicalConstituency.run {
                end shouldbe "2019-11-06".asDate()
            }
            first.election.run {
                name shouldbe "2017 General Election"
                date shouldbe "2017-06-08".asDate()
                electionType shouldbe "General Election"
            }
        }
    }

    @Test
    fun ensure_Experiences_ars_written_and_retrieved_correctly() {
        runInsert(MemberDao::insertExperiences) {
            listOf(
                Experience(
                    memberId = MEMBER_ID,
                    category = "Political",
                    organisation = "Liberal Democrats",
                    title = "National Treasurer",
                    start = "2012-12-25".asDate(),
                    end = "2015-12-25".asDate(),
                )
            )
        }

        runQuery({ dao.getExperiences(MEMBER_ID)}) {
            first().run {
                category shouldbe "Political"
                organisation shouldbe "Liberal Democrats"
                title shouldbe "National Treasurer"
                start shouldbe "2012-12-25".asDate()
                end shouldbe "2015-12-25".asDate()
            }
        }
    }

    @Test
    fun ensure_Houses_ars_written_and_retrieved_correctly() {
        runInsert(MemberDao::insertHouseMemberships) {
            listOf(
                HouseMembership(
                    memberId = MEMBER_ID,
                    house = House.commons,
                    start = "2001-06-07".asDate(),
                    end = "2008-06-04".asDate(),
                )
            )
        }

        runQuery({ dao.getHouseMemberships(MEMBER_ID) }) {
            first().run {
                house shouldbe House.commons
                start shouldbe "2001-06-07".asDate()
                end shouldbe "2008-06-04".asDate()
            }
        }
    }

    @Test
    fun ensure_FinancialInterests_ars_written_and_retrieved_correctly() {
        runInsert(MemberDao::insertFinancialInterests) {
            listOf(
                FinancialInterest(
                    memberId = MEMBER_ID,
                    parliamentdotuk = 12485,
                    category = "Category 1: Directorships",
                    description = "Director, Durham Group Estates Ltd",
                    dateCreated = "2013-12-02".asDate(),
                    dateAmended = null,
                    dateDeleted = null,
                    registeredLate = false,
                )
            )
        }

        runQuery({ dao.getFinancialInterests(MEMBER_ID) }) {
            first().run {
                parliamentdotuk shouldbe 12485
                category shouldbe "Category 1: Directorships"
                description shouldbe "Director, Durham Group Estates Ltd"
                dateCreated shouldbe "2013-12-02".asDate()
                dateAmended shouldbe null
                dateDeleted shouldbe null
                registeredLate shouldbe false
            }
        }
    }

    @Test
    fun ensure_PartyAssociations_ars_written_and_retrieved_correctly() {
        runInsert(MemberDao::insertPartyAssociations) {
            listOf(
                PartyAssociation(
                    memberId = MEMBER_ID,
                    partyId = 4,
                    start = "2017-06-08".asDate(),
                    end = "2019-11-06".asDate()
                )
            )
        }

        runQuery({ dao.getPartyAssociations(MEMBER_ID) }) {
            val first = first()
            first.partyAssocation.run {
                end shouldbe "2019-11-06".asDate()
            }
            first.party.run {
                name shouldbe "Conservative"
                parliamentdotuk shouldbe 4
            }
        }
    }

    @Test
    fun ensure_Posts_ars_written_and_retrieved_correctly() {
        runInsert(MemberDao::insertPosts) {
            listOf(
                Post(
                    memberId = MEMBER_ID,
                    postType = Post.PostType.GOVERNMENTAL,
                    parliamentdotuk = 661,
                    name = "Prime Minister, First Lord of the Treasury and Minister for the Civil Service",
                    start = "2019-07-24".asDate(),
                    end = "2020-01-01".asDate(),
                ),
                Post(
                    memberId = MEMBER_ID,
                    postType = Post.PostType.PARLIAMENTARY,
                    parliamentdotuk = 787,
                    name = "Leader of the Conservative Party",
                    start = "2019-07-23".asDate(),
                    end = "2020-01-02".asDate(),
                ),
                Post(
                    memberId = MEMBER_ID,
                    postType = Post.PostType.OPPOSITION,
                    parliamentdotuk = 15,
                    name = "Shadow Minister (Business, Innovation and Skills)",
                    start = "2005-12-09".asDate(),
                    end = "2007-07-16".asDate(),
                )
            )
        }

        runQuery({ dao.getPosts(MEMBER_ID) }) {
            size shouldbe 3
            first { it.postType == Post.PostType.GOVERNMENTAL }.run {
                parliamentdotuk shouldbe 661
                name shouldbe "Prime Minister, First Lord of the Treasury and Minister for the Civil Service"
                start shouldbe "2019-07-24".asDate()
                end shouldbe "2020-01-01".asDate()
            }
            first { it.postType == Post.PostType.PARLIAMENTARY }.run {
                parliamentdotuk shouldbe 787
                name shouldbe "Leader of the Conservative Party"
                start shouldbe "2019-07-23".asDate()
                end shouldbe "2020-01-02".asDate()
            }
            first { it.postType == Post.PostType.OPPOSITION }.run {
                parliamentdotuk shouldbe 15
                name shouldbe "Shadow Minister (Business, Innovation and Skills)"
                start shouldbe "2005-12-09".asDate()
                end shouldbe "2007-07-16".asDate()
            }
        }
    }

    @Test
    fun ensure_TopicOfInterest_ars_written_and_retrieved_correctly() {
        runInsert(MemberDao::insertTopicsOfInterest) {
            listOf(
                TopicOfInterest(
                    memberId = MEMBER_ID,
                    category = "Countries of Interest",
                    topic = "Australia, Fiji, New Zealand, Samoa",
                ),
            )
        }

        runQuery({ dao.getTopicsOfInterest(MEMBER_ID) }) {
            first().run {
                category shouldbe "Countries of Interest"
                topic shouldbe "Australia, Fiji, New Zealand, Samoa"
            }
        }
    }
}
