package org.beatonma.commons.data.core.repository

import fakeIt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.beatonma.commons.androidTest.asDate
import org.beatonma.commons.androidTest.getOrAwaitValue
import org.beatonma.commons.data.BaseRoomTest
import org.beatonma.commons.data.CommonsRemoteDataSource
import org.beatonma.commons.data.core.room.dao.MemberDao
import org.beatonma.commons.data.core.room.entities.member.House
import org.beatonma.commons.data.core.room.entities.member.Post
import org.beatonma.commons.data.testdata.API_MEMBER
import org.beatonma.commons.data.testdata.MEMBER_PUK
import org.beatonma.lib.testing.kotlin.extensions.assertions.shouldbe
import org.junit.Before
import org.junit.Test

class MemberRepositoryTest: BaseRoomTest() {
    lateinit var repository: MemberRepository
    private val dao: MemberDao
        get() = db.memberDao()

    @Before
    override fun setUp() {
        super.setUp()
        repository = MemberRepository(
            fakeIt(CommonsRemoteDataSource::class, object {

            }),
            db.memberDao(),
            db.divisionDao(),
        )
        runBlocking(Dispatchers.Main) {
            dao.insertCompleteMember(MEMBER_PUK, API_MEMBER)
        }
    }

    @Test
    fun ensure_observeCompleteMember_mediatorLiveData_is_constructed_correctly() {
        runBlocking(Dispatchers.Main) {
            val data = repository.observeCompleteMember(MEMBER_PUK)

            // One latch per addSource on MediatorLiveData
            data.getOrAwaitValue {
                profile!!.run {
                    parliamentdotuk shouldbe MEMBER_PUK
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
                constituency!!.run {
                    name shouldbe "Uxbridge and South Ruislip"
                    parliamentdotuk shouldbe 147277
                }
                party!!.run {
                    name shouldbe "Conservative"
                    parliamentdotuk shouldbe 4
                }
                weblinks!!.first().run {
                    url shouldbe "https://twitter.com/borisjohnson"
                    description shouldbe "Twitter"
                }
                addresses!!.first().run {
                    description shouldbe "Parliamentary"
                    address shouldbe "House of Commons, London"
                    postcode shouldbe "SW1A 0AA"
                    phone shouldbe "+442072194682"
                    fax shouldbe "+442072194683"
                    email shouldbe "boris.johnson.mp@parliament.uk"
                }
                committees!!.first().run {
                    membership.run {
                        parliamentdotuk shouldbe 2
                        name shouldbe "Administration Committee"
                        start shouldbe "2015-07-21".asDate()
                        end shouldbe "2017-05-03".asDate()
                    }

                    chairs.first().run {
                        committeeId shouldbe 2
                        start shouldbe "2015-07-21".asDate()
                        end shouldbe "2017-05-03".asDate()
                    }
                }
                historicConstituencies!!.first { it.historicalConstituency.start == "2017-06-08".asDate() }
                    .run {
                        constituency.run {
                            name shouldbe "Uxbridge and South Ruislip"
                            parliamentdotuk shouldbe 147277
                        }
                        historicalConstituency.run {
                            end shouldbe "2019-11-06".asDate()
                        }
                        election.run {
                            name shouldbe "2017 General Election"
                            date shouldbe "2017-06-08".asDate()
                            electionType shouldbe "General Election"
                        }
                    }
                experiences!!.first().run {
                    category shouldbe "Political"
                    organisation shouldbe "Liberal Democrats"
                    title shouldbe "National Treasurer"
                    start shouldbe "2012-12-25".asDate()
                    end shouldbe "2015-12-25".asDate()
                }
                houses!!.first { it.start == "2001-06-07".asDate() }.run {
                    house shouldbe House.commons
                    end shouldbe "2008-06-04".asDate()
                }
                financialInterests!!.first().run {
                    parliamentdotuk shouldbe 12485
                    category shouldbe "Category 1: Directorships"
                    description shouldbe "Director, Durham Group Estates Ltd"
                    dateCreated shouldbe "2013-12-02".asDate()
                    dateAmended shouldbe null
                    dateDeleted shouldbe null
                    registeredLate shouldbe false
                }
                parties!!.first { it.partyAssocation.start == "2017-06-08".asDate() }.run {
                    partyAssocation.run {
                        end shouldbe "2019-11-06".asDate()
                    }
                    party.run {
                        name shouldbe "Conservative"
                        parliamentdotuk shouldbe 4
                    }
                }
                posts!!.run {
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
                topicsOfInterest!!.first().run {
                    category shouldbe "Countries of Interest"
                    topic shouldbe "Australia, Fiji, New Zealand, Samoa"
                }
            }
        }
    }
}
