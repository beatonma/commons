package org.beatonma.commons.repo.testdata

import org.beatonma.commons.core.House
import org.beatonma.commons.data.core.CompleteMember
import org.beatonma.commons.data.core.room.entities.member.CommitteeMemberWithChairs
import org.beatonma.commons.data.core.room.entities.member.HistoricalConstituencyWithElection
import org.beatonma.commons.data.core.room.entities.member.PartyAssociationWithParty
import org.beatonma.commons.data.core.room.entities.member.Post
import org.beatonma.commons.repo.converters.*
import org.beatonma.commons.snommoc.models.*
import org.beatonma.commons.test.extensions.util.asDate

const val MEMBER_PUK_BORIS_JOHNSON = 1423
const val MEMBER_PUK_KEIR_STARMER = 4514

val EXAMPLE_MEMBER_PROFILE_BORIS_JOHNSON = ApiMemberProfile(
    parliamentdotuk = MEMBER_PUK_BORIS_JOHNSON,
    name = "Boris Johnson",
    party = ApiParty(
        name = "Conservative",
        parliamentdotuk = 4,
    ),
    constituency = ApiConstituencyMinimal(
        name = "Uxbridge and South Ruislip",
        parliamentdotuk = 147277,
    ),
    isMp = true,
    isLord = false,
    dateOfBirth = "1964-06-19".asDate(),
    dateOfDeath = null,
    age = 55,
    gender = "M",
    placeOfBirth = ApiTown(
        town = "New York",
        country = "USA"
    ),
    portraitUrl = "https://members-api.parliament.uk/api/members/1423/Portrait?cropType=OneOne",
    currentPost = "Prime Minister",
)

val EXAMPLE_MEMBER_PROFILE_KEIR_STARMER = ApiMemberProfile(
    parliamentdotuk = MEMBER_PUK_KEIR_STARMER,
    name = "Keir Starmer",
    party = ApiParty(
        name = "Labour",
        parliamentdotuk = 15,
    ),
    constituency = ApiConstituencyMinimal(
        name = "Holburn and St Pancras",
        parliamentdotuk = 146996,
    ),
    isMp = true,
    isLord = false,
    dateOfBirth = "1962-09-20".asDate(),
    dateOfDeath = null,
    age = 57,
    gender = "M",
    placeOfBirth = ApiTown(
        town = "Southwark",
        country = "England"
    ),
    portraitUrl = "https://members-api.parliament.uk/api/members/4514/Portrait?cropType=ThreeTwo",
    currentPost = "Leader of the Labour Party",
)

/**
 * Edited API response - items have been added/removed for usefulness, compiled from several
 * actual responses.
 */
val API_MEMBER_BORIS_JOHNSON = ApiCompleteMember(
    profile = EXAMPLE_MEMBER_PROFILE_BORIS_JOHNSON,
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
            ApiWebAddress(
                url = "https://twitter.com/borisjohnson",
                description = "Twitter",
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
            constituency = ApiConstituencyMinimal(
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
            constituency = ApiConstituencyMinimal(
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
            party = ApiParty(
                name = "Conservative",
                parliamentdotuk = 4),
            start = "2019-12-12".asDate(),
            end = null,
        ),
        ApiPartyAssociation(
            party = ApiParty(
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


fun ApiCompleteMember.toCompleteMember() = CompleteMember(
    profile = profile.toMemberProfile(),
    party = profile.party.toParty(),
    constituency = profile.constituency?.toConstituency(),
    addresses = addresses.physical.map { it.toPhysicalAddress(MEMBER_PUK_BORIS_JOHNSON) },
    weblinks = addresses.web.map { it.toWebAddress(MEMBER_PUK_BORIS_JOHNSON) },
    posts = listOf(
        posts.governmental.map { it.toPost(MEMBER_PUK_BORIS_JOHNSON, Post.PostType.GOVERNMENTAL) },
        posts.parliamentary.map { it.toPost(MEMBER_PUK_BORIS_JOHNSON, Post.PostType.PARLIAMENTARY) },
        posts.opposition.map { it.toPost(MEMBER_PUK_BORIS_JOHNSON, Post.PostType.OPPOSITION) },
    ).flatten(),
    committees = committees.map { committee ->
        CommitteeMemberWithChairs(
            membership = committee.toCommitteeMembership(profile.parliamentdotuk),
            chairs = committee.chairs.map { chairship ->
                chairship.toCommitteeChairship(
                    committeeId = committee.parliamentdotuk,
                    memberId = profile.parliamentdotuk,
                )
            },
        )
    },
    experiences = experiences.map { it.toExperience(profile.parliamentdotuk) },
    financialInterests = financialInterests.map { it.toFinancialInterest(profile.parliamentdotuk) },
    topicsOfInterest = topicsOfInterest.map { it.toTopicOfInterest(profile.parliamentdotuk) },
    houses = houses.map { it.toHouseMembership(profile.parliamentdotuk) },
    historicConstituencies = constituencies.map {
        HistoricalConstituencyWithElection(
            constituency = it.constituency.toConstituency(),
            historicalConstituency = it.toHistoricalConstituency(profile.parliamentdotuk),
            election = it.election.toElection(),
        )
    },
    parties = parties.map {
        PartyAssociationWithParty(
            partyAssocation = it.toPartyAssociation(profile.parliamentdotuk),
            party = profile.party.toParty(),
        )
    },
)
