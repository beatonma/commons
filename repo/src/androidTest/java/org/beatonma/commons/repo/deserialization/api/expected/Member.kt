package org.beatonma.commons.repo.deserialization.api.expected

import org.beatonma.commons.core.House
import org.beatonma.commons.snommoc.models.ApiAddresses
import org.beatonma.commons.snommoc.models.ApiCommittee
import org.beatonma.commons.snommoc.models.ApiCompleteMember
import org.beatonma.commons.snommoc.models.ApiConstituencyMinimal
import org.beatonma.commons.snommoc.models.ApiElection
import org.beatonma.commons.snommoc.models.ApiExperience
import org.beatonma.commons.snommoc.models.ApiFinancialInterest
import org.beatonma.commons.snommoc.models.ApiHistoricalConstituency
import org.beatonma.commons.snommoc.models.ApiHouseMembership
import org.beatonma.commons.snommoc.models.ApiMemberProfile
import org.beatonma.commons.snommoc.models.ApiParty
import org.beatonma.commons.snommoc.models.ApiPartyAssociation
import org.beatonma.commons.snommoc.models.ApiPhysicalAddress
import org.beatonma.commons.snommoc.models.ApiPost
import org.beatonma.commons.snommoc.models.ApiPosts
import org.beatonma.commons.snommoc.models.ApiTopicOfInterest
import org.beatonma.commons.test.extensions.util.asDate

/** https://snommoc.org/api/member/profile/965/ */
internal fun expectedApiMember(): ApiCompleteMember = ApiCompleteMember(
    ApiMemberProfile(
        parliamentdotuk = 965,
        name = "Lord Wrigglesworth",
        active = true,
        party = ApiParty(
            parliamentdotuk = 17,
            name = "Liberal Democrat"
        ),
        constituency = null,
        isMp = false,
        isLord = true,
        dateOfBirth = "1939-12-08".asDate(),
        dateOfDeath = null,
        age = 80,
        gender = "M",
        placeOfBirth = null,
        portraitUrl = null,
        currentPost = null
    ),
    addresses = ApiAddresses(
        physical = listOf(
            ApiPhysicalAddress(
                description = "Parliamentary",
                address = "House of Lords, London",
                postcode = "SW1A 0PW",
                phone = "+442072198743",
                fax = null,
                email = "wrigglesworthi@parliament.uk"
            ),
        ),
        web = listOf()
    ),
    committees = listOf(
        ApiCommittee(
            parliamentdotuk = 354,
            name = "Trade Union Political Funds and Political Party Funding Committee",
            start = "2016-01-28".asDate(),
            end = "2016-02-29".asDate(),
            chairs = listOf()
        ),
        ApiCommittee(
            parliamentdotuk = 230,
            name = "Finance Bill Sub-Committee",
            start = "2014-01-08".asDate(),
            end = "2014-03-11".asDate(),
            chairs = listOf()
        ),
        ApiCommittee(
            parliamentdotuk = 230,
            name = "Finance Bill Sub-Committee",
            start = "2017-01-10".asDate(),
            end = "2017-03-17".asDate(),
            chairs = listOf()
        ),
        ApiCommittee(
            parliamentdotuk = 67,
            name = "European Legislation Sub-Committee II",
            start = "1978-11-01".asDate(),
            end = "1979-04-04".asDate(),
            chairs = listOf()
        ),
        ApiCommittee(
            parliamentdotuk = 64,
            name = "European Legislation",
            start = "1978-11-01".asDate(),
            end = "1983-06-09".asDate(),
            chairs = listOf()
        )
    ),
    constituencies = listOf(
        ApiHistoricalConstituency(
            constituency = ApiConstituencyMinimal(
                parliamentdotuk = 146260,
                name = "Stockton South"
            ),
            start = "1983-06-09".asDate(),
            end = "1987-06-11".asDate(),
            election = ApiElection(
                parliamentdotuk = 12,
                name = "1983 General Election",
                date = "1983-06-09".asDate(),
                electionType = "General Election"
            )
        ),
        ApiHistoricalConstituency(
            constituency = ApiConstituencyMinimal(
                parliamentdotuk = 146380,
                name = "Thornaby"
            ),
            start = "1979-05-03".asDate(),
            end = "1983-06-09".asDate(),
            election = ApiElection(
                parliamentdotuk = 11,
                name = "1979 General Election",
                date = "1979-05-03".asDate(),
                electionType = "General Election"
            )
        ),
        ApiHistoricalConstituency(
            constituency = ApiConstituencyMinimal(
                parliamentdotuk = 146380,
                name = "Thornaby"
            ),
            start = "1974-10-10".asDate(),
            end = "1979-05-03".asDate(),
            election = ApiElection(
                parliamentdotuk = 10,
                name = "1974 (Oct) General Election",
                date = "1974-10-10".asDate(),
                electionType = "General Election"
            )
        ),
        ApiHistoricalConstituency(
            constituency = ApiConstituencyMinimal(
                parliamentdotuk = 146380,
                name = "Thornaby"
            ),
        start = "1974-02-28".asDate(),
        end = "1974-10-10".asDate(),
            election = ApiElection(
                parliamentdotuk = 9,
                name = "1974 (Feb) General Election",
                date = "1974-02-28".asDate(),
                electionType = "General Election"
            )
        )
    ),
    experiences = listOf(
        ApiExperience(
            category = "Political",
            organisation = "Liberal Democrats",
            title = "National Treasurer",
            start = "2012-12-25".asDate(),
            end = "2015-12-25".asDate(),
        ),
        ApiExperience(
            category = "Public life",
            organisation = "Regional Growth Fund Advisory Board",
            title = "Deputy Chairman",
            start = "2012-12-25".asDate(),
            end = "2015-12-25".asDate(),
        ),
        ApiExperience(
            category = "Non political",
            organisation = "Port of Tyne",
            title = "Chairman",
            start = "2005-12-25".asDate(),
            end = "2012-12-25".asDate(),
        ),
        ApiExperience(
            category = "Public life",
            organisation = "Baltic Centre for Contemporary Art",
            title = "Chairman",
            start = "2005-12-25".asDate(),
            end = "2009-12-25".asDate(),
        ),
        ApiExperience(
            category = "Political",
            organisation = null,
            title = "Liberal Democrats Trustees",
            start = "2002-12-25".asDate(),
            end = "2012-12-25".asDate(),
        ),
        ApiExperience(
            category = "Public life",
            organisation = "Tyne Tees Television",
            title = "Director",
            start = "2002-12-25".asDate(),
            end = "2005-12-25".asDate(),
        ),
        ApiExperience(
            category = "Public life",
            organisation = "Newcastle Gateshead Initiative",
            title = "Chairman",
            start = "1999-12-25".asDate(),
            end = "2004-12-25".asDate(),
        ),
        ApiExperience(
            category = "Non political",
            organisation = "Prima Europe and GPC",
            title = "Chairman",
            start = "1996-12-25".asDate(),
            end = "2000-12-25".asDate(),
        ),
        ApiExperience(
            category = "Non political",
            organisation = "UK Land Estates",
            title = "Founding Chairman",
            start = "1995-12-25".asDate(),
            end = "2009-12-25".asDate(),
        ),
        ApiExperience(
            category = "Public life",
            organisation = "Teeside University Board of Governors",
            title = "Member & Deputy Chairman",
            start = "1993-12-25".asDate(),
            end = "2002-12-25".asDate(),
        ),
        ApiExperience(
            category = "Political",
            organisation = "Liberal Democrats",
            title = "President",
            start = "1988-12-25".asDate(),
            end = "1990-12-25".asDate(),
        ),
        ApiExperience(
            category = "Non political",
            organisation = "John Lilvingston and Sons and Fairfield Industries",
            title = "Deputy Chairman and Director",
            start = "1988-12-25".asDate(),
            end = "1995-12-25".asDate(),
        )
    ),
    houses = listOf(
        ApiHouseMembership(
            house = House.commons,
            start = "1974-02-28".asDate(),
            end = "1987-06-11".asDate(),
        ),
        ApiHouseMembership(
            house = House.lords,
            start = "2013-09-05".asDate(),
            end = null
        ),
    ),
    financialInterests = listOf(
        ApiFinancialInterest(
            parliamentdotuk = 12485,
            category = "Category 1: Directorships",
            description = "Director, Durham Group Estates Ltd",
            dateCreated = "2013-12-02".asDate(),
            dateAmended = null,
            dateDeleted = null,
            registeredLate = false
        ),
        ApiFinancialInterest(
            parliamentdotuk = 12486,
            category = "Category 1: Directorships",
            description = "Director, Durham Group Investments Ltd",
            dateCreated = "2013-12-02".asDate(),
            dateAmended = null,
            dateDeleted = null,
            registeredLate = false
        ),
        ApiFinancialInterest(
            parliamentdotuk = 12487,
            category = "Category 1: Directorships",
            description = "Director, Rudchester Estates Ltd (non-trading)",
            dateCreated = "2013-12-02".asDate(),
            dateAmended = null,
            dateDeleted = null,
            registeredLate = false
        ),
        ApiFinancialInterest(
            parliamentdotuk = 12488,
            category = "Category 1: Directorships",
            description = "Director, Northern Corporate Finance Ltd (non-trading)",
            dateCreated = "2013-12-02".asDate(),
            dateAmended = null,
            dateDeleted = null,
            registeredLate = false
        ),
        ApiFinancialInterest(
            parliamentdotuk = 12489,
            category = "Category 4: Shareholdings (a)",
            description = "Durham Group Estates Ltd (commercial property company)",
            dateCreated = "2013-12-02".asDate(),
            dateAmended = null,
            dateDeleted = null,
            registeredLate = false
        ),
        ApiFinancialInterest(
            parliamentdotuk = 12490,
            category = "Category 4: Shareholdings (a)",
            description = "Durham Group Investments Ltd (commercial property company)",
            dateCreated = "2013-12-02".asDate(),
            dateAmended = null,
            dateDeleted = null,
            registeredLate = false
        ),
        ApiFinancialInterest(
            parliamentdotuk = 12491,
            category = "Category 4: Shareholdings (a)",
            description = "Rudchester Estates Ltd (non-trading)",
            dateCreated = "2013-12-02".asDate(),
            dateAmended = null,
            dateDeleted = null,
            registeredLate = false
        ),
        ApiFinancialInterest(
            parliamentdotuk = 12492,
            category = "Category 4: Shareholdings (a)",
            description = "Northern Corporate Finance Ltd (non-trading)",
            dateCreated = "2013-12-02".asDate(),
            dateAmended = null,
            dateDeleted = null,
            registeredLate = false
        ),
        ApiFinancialInterest(
            parliamentdotuk = 24419,
            category = "Category 4: Shareholdings (b)",
            description = "Caspian Learning Ltd (company providing financial analysis using artificial intelligence) (entry formerly read Machine Delta (a division of Caspian Learning Ltd providing intelligent quality assurance using artificial intelligence))",
            dateCreated = "2015-12-31".asDate(),
            dateAmended = "2020-08-19".asDate(),
            dateDeleted = null,
            registeredLate = false
        ),
        ApiFinancialInterest(
            parliamentdotuk = 36291,
            category = "Category 1: Directorships",
            description = "Director, TRNKLD (Holdings) Ltd (property)",
            dateCreated = "2020-08-19".asDate(),
            dateAmended = null,
            dateDeleted = null,
            registeredLate = false
        ),
    ),
    parties = listOf(
        ApiPartyAssociation(
            party = ApiParty(
                parliamentdotuk = 15,
                name = "Labour"
            ),
            start = "1974-02-28".asDate(),
            end = "1981-03-02".asDate(),
        ),
        ApiPartyAssociation(
            party = ApiParty(
                parliamentdotuk = 32,
                name = "Social Democratic Party"
            ),
            start = "1981-03-02".asDate(),
            end = "2013-09-05".asDate(),
        ),
        ApiPartyAssociation(
            party = ApiParty(
                parliamentdotuk = 17,
                name = "Liberal Democrat"
            ),
            start = "1988-03-03".asDate(),
            end = null,
        )
    ),
    posts = ApiPosts(
        governmental = listOf(),
        opposition = listOf(),
        parliamentary = listOf(
            ApiPost(
                parliamentdotuk = 1098,
                name = "Party Chair, Liberal Democrats",
                start = "1989-01-01".asDate(),
                end = "1990-12-31".asDate(),
            )
        )
    ),
    topicsOfInterest = listOf(
        ApiTopicOfInterest(
            category = "Concerns: Policy Interests",
            topic = "Business, industry and consumers; Economy and finance; European Union; Parliament, government and politics; UK Regional Policy"
        ),
        ApiTopicOfInterest(
            category = "Concerns: World Areas",
            topic = "India; Poland; South East Asia; Turkey; USA"
        ),
        ApiTopicOfInterest(
            category = "Concerns: UK Areas",
            topic = "North East England"
        )
    )
)
