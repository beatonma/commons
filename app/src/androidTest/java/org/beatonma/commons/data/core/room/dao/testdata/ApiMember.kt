package org.beatonma.commons.data.core.room.dao.testdata

import org.beatonma.commons.data.core.ApiCompleteMember
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
        dateOfBirth = "1964-06-19", dateOfDeath = null,
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
            PhysicalAddress(
                description = "Parliamentary",
                address = "House of Commons, London",
                postcode = "SW1A 0AA",
                phone = "+442072194682",
                fax = "+442072194683",
                email = "boris.johnson.mp@parliament.uk",
                memberId = MEMBER_PUK
            ),
        ),
        web = listOf(
            WebAddress("https://twitter.com/borisjohnson",
                "Twitter",
                memberId = MEMBER_PUK),
        )
    ),
    committees = listOf(
        ApiCommittee(
            memberId = MEMBER_PUK,
            parliamentdotuk = 2,
            name = "Administration Committee",
            start = "2015-07-21",
            end = "2017-05-03",
            chairs = listOf(
                CommitteeChairship(
                    committeeId = 2,
                    memberId = MEMBER_PUK,
                    start = "2015-07-21",
                    end = "2017-05-03",
                )
            )
        ),
    ),
    constituencies = listOf(
        ApiHistoricalConstituency(
            constituency = Constituency(
                name = "Uxbridge and South Ruislip",
                parliamentdotuk = 147277),
            start = "2019-12-12",
            end = null,
            election = Election(
                parliamentdotuk = 397,
                name = "2019 General Election",
                date = "2019-12-12",
                electionType = "General Election",
            ),
        ),
        ApiHistoricalConstituency(
            constituency = Constituency(
                name = "Uxbridge and South Ruislip",
                parliamentdotuk = 147277),
            start = "2017-06-08",
            end = "2019-11-06",
            election = Election(
                parliamentdotuk = 377,
                name = "2017 General Election",
                date = "2017-06-08",
                electionType = "General Election",
            ),
        ),
    ),
    experiences = listOf(
        Experience(
            memberId = MEMBER_PUK,
            category = "Political",
            organisation = "Liberal Democrats",
            title = "National Treasurer",
            start = "2012-12-25",
            end = "2015-12-25"
        ),
    ),
    houses = listOf(
        HouseMembership(
            house = HouseMembership.House.Commons,
            start = "2001-06-07",
            end = "2008-06-04",
            memberId = MEMBER_PUK,
        ),
        HouseMembership(
            house = HouseMembership.House.Commons,
            start = "2015-05-07",
            end = null,
            memberId = MEMBER_PUK,
        )
    ),
    financialInterests = listOf(
        FinancialInterest(
            memberId = MEMBER_PUK,
            interestId = 12485,
            category = "Category 1: Directorships",
            description = "Director, Durham Group Estates Ltd",
            dateCreated = "2013-12-02",
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
            start = "2019-12-12",
            end = null,
        ),
        ApiPartyAssociation(
            party = Party(
                name = "Conservative",
                parliamentdotuk = 4),
            start = "2017-06-08",
            end = "2019-11-06",
        ),
    ),
    posts = ApiPosts(
        governmental = listOf(
            Post(
                memberId = MEMBER_PUK,
                parliamentdotuk = 661,
                name = "Prime Minister, First Lord of the Treasury and Minister for the Civil Service",
                start = "2019-07-24",
                end = "2020-01-01",
                postType = Post.PostType.GOVERNMENTAL,
            ),
        ),
        parliamentary = listOf(
            Post(
                memberId = MEMBER_PUK,
                parliamentdotuk = 787,
                name = "Leader of the Conservative Party",
                start = "2019-07-23",
                end = "2020-01-02",
                postType = Post.PostType.PARLIAMENTARY,
            ),
        ),
        opposition = listOf(
            Post(
                memberId = MEMBER_PUK,
                parliamentdotuk = 15,
                name = "Shadow Minister (Business, Innovation and Skills)",
                start = "2005-12-09",
                end = "2007-07-16",
                postType = Post.PostType.OPPOSITION,
            ),
        ),
    ),
    topicsOfInterest = listOf(
        TopicOfInterest(
            memberId = MEMBER_PUK,
            category = "Countries of Interest",
            topic = "Australia, Fiji, New Zealand, Samoa",
        ),
    ),
)
