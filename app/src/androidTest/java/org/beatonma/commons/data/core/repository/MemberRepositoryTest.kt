package org.beatonma.commons.data.core.repository

import fakeIt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.runBlocking
import org.beatonma.commons.androidTest.asDate
import org.beatonma.commons.androidTest.awaitValue
import org.beatonma.commons.data.BaseRoomTest
import org.beatonma.commons.data.CommonsRemoteDataSource
import org.beatonma.commons.data.core.room.dao.MemberDao
import org.beatonma.commons.data.core.room.entities.member.House
import org.beatonma.commons.data.core.room.entities.member.Post
import org.beatonma.commons.data.testdata.API_MEMBER_BORIS_JOHNSON
import org.beatonma.commons.data.testdata.MEMBER_PUK_BORIS_JOHNSON
import org.beatonma.commons.kotlin.extensions.dump
import org.beatonma.lib.testing.kotlin.extensions.assertions.shouldbe
import org.junit.Before
import org.junit.Test

class MemberRepositoryTest: BaseRoomTest() {
    lateinit var repository: MemberRepositoryImpl
    private val dao: MemberDao
        get() = db.memberDao()

    @Before
    override fun setUp() {
        super.setUp()

        repository = MemberRepositoryImpl(
            fakeIt(CommonsRemoteDataSource::class, object {

            }),
            dao,
        )

        runBlocking(Dispatchers.Main) {
            dao.insertCompleteMember(MEMBER_PUK_BORIS_JOHNSON, API_MEMBER_BORIS_JOHNSON)
        }
    }

    /**
     * This is currently failing!
     *
     * Values other than [profile, constituency, party, experiences] are not being inserted correctly,
     * as discovered by using a database file and viewing the file after running the test.
     * Missing values are consistent and not related to insertion order.
     * [insertCompleteMember] runs correctly in [MemberDaoInsertCompleteMemberTest], and in the live app,
     * so not sure why it fails to write values when used here?
     */
    @Test
    fun ensure_getCompleteMember_is_constructed_correctly() {
        runBlocking(Dispatchers.Main) {
            repository.getCompleteMember(MEMBER_PUK_BORIS_JOHNSON)
                .awaitValue(latchCount = 13)
                .single()
                .run {
                    dump()
                    profile!!.run {
                        parliamentdotuk shouldbe MEMBER_PUK_BORIS_JOHNSON
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
