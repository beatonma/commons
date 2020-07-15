package org.beatonma.commons.data.testdata

import org.beatonma.commons.androidTest.asDate
import org.beatonma.commons.data.core.ApiCompleteMember
import org.beatonma.commons.data.core.room.entities.constituency.Constituency
import org.beatonma.commons.data.core.room.entities.election.ApiElection
import org.beatonma.commons.data.core.room.entities.member.*

const val MEMBER_PUK = 1423

/**
 * Edited API response - items have been added/removed for usefulness, compiled from several
 * actual responses.
 */
val API_MEMBER = ApiCompleteMember(
    profile = MemberProfile(
        parliamentdotuk = MEMBER_PUK,
        name = "Boris Johnson",
        party = Party(name = "Conservative",
            parliamentdotuk = 4),
        constituency = Constituency(
            name = "Uxbridge and South Ruislip",
            parliamentdotuk = 147277),
        isMp = true,
        isLord = false,
        dateOfBirth = "1964-06-19".asDate(), dateOfDeath = null,
        age = 55,
        gender = "M",
        placeOfBirth = Town(
            town = "New York",
            country = "USA"),
        portraitUrl = "https://members-api.parliament.uk/api/members/1423/Portrait?cropType=OneOne",
        currentPost = "Prime Minister",
    ),
    addresses = ApiAddresses(
        physical = listOf(
            ApiPhysicalAddress(
                description = "Parliamentary",
                address = "House of Commons, London",
                postcode = "SW1A 0AA",
                phone = "+442072194682",
                fax = "+442072194683",
                email = "boris.johnson.mp@parliament.uk",
            ),
        ),
        web = listOf(
            ApiWebAddress("https://twitter.com/borisjohnson",
                "Twitter",
            ),
        )
    ),
    committees = listOf(
        ApiCommittee(
            parliamentdotuk = 2,
            name = "Administration Committee",
            start = "2015-07-21".asDate(),
            end = "2017-05-03".asDate(),
            chairs = listOf(
                ApiCommitteeChairship(
                    start = "2015-07-21".asDate(),
                    end = "2017-05-03".asDate(),
                )
            )
        ),
    ),
    constituencies = listOf(
        ApiHistoricalConstituency(
            constituency = Constituency(
                name = "Uxbridge and South Ruislip",
                parliamentdotuk = 147277),
            start = "2019-12-12".asDate(),
            end = null,
            election = ApiElection(
                parliamentdotuk = 397,
                name = "2019 General Election",
                date = "2019-12-12".asDate(),
                electionType = "General Election",
            ),
        ),
        ApiHistoricalConstituency(
            constituency = Constituency(
                name = "Uxbridge and South Ruislip",
                parliamentdotuk = 147277),
            start = "2017-06-08".asDate(),
            end = "2019-11-06".asDate(),
            election = ApiElection(
                parliamentdotuk = 377,
                name = "2017 General Election",
                date = "2017-06-08".asDate(),
                electionType = "General Election",
            ),
        ),
    ),
    experiences = listOf(
        ApiExperience(
            category = "Political",
            organisation = "Liberal Democrats",
            title = "National Treasurer",
            start = "2012-12-25".asDate(),
            end = "2015-12-25".asDate(),
        ),
    ),
    houses = listOf(
        ApiHouseMembership(
            house = House.commons,
            start = "2001-06-07".asDate(),
            end = "2008-06-04".asDate(),
        ),
        ApiHouseMembership(
            house = House.commons,
            start = "2015-05-07".asDate(),
            end = null,
        )
    ),
    financialInterests = listOf(
        ApiFinancialInterest(
            parliamentdotuk = 12485,
            category = "Category 1: Directorships",
            description = "Director, Durham Group Estates Ltd",
            dateCreated = "2013-12-02".asDate(),
            dateAmended = null,
            dateDeleted = null,
            registeredLate = false,
        )
    ),
    parties = listOf(
        ApiPartyAssociation(
            party = Party(
                name = "Conservative",
                parliamentdotuk = 4),
            start = "2019-12-12".asDate(),
            end = null,
        ),
        ApiPartyAssociation(
            party = Party(
                name = "Conservative",
                parliamentdotuk = 4),
            start = "2017-06-08".asDate(),
            end = "2019-11-06".asDate(),
        ),
    ),
    posts = ApiPosts(
        governmental = listOf(
            ApiPost(
                parliamentdotuk = 661,
                name = "Prime Minister, First Lord of the Treasury and Minister for the Civil Service",
                start = "2019-07-24".asDate(),
                end = "2020-01-01".asDate(),
            ),
        ),
        parliamentary = listOf(
            ApiPost(
                parliamentdotuk = 787,
                name = "Leader of the Conservative Party",
                start = "2019-07-23".asDate(),
                end = "2020-01-02".asDate(),
            ),
        ),
        opposition = listOf(
            ApiPost(
                parliamentdotuk = 15,
                name = "Shadow Minister (Business, Innovation and Skills)",
                start = "2005-12-09".asDate(),
                end = "2007-07-16".asDate(),
            ),
        ),
    ),
    topicsOfInterest = listOf(
        ApiTopicOfInterest(
            category = "Countries of Interest",
            topic = "Australia, Fiji, New Zealand, Samoa",
        ),
    ),
)
