package org.beatonma.commons.repo.converters

import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.core.ZeitgeistReason
import org.beatonma.commons.data.core.room.entities.bill.BillData
import org.beatonma.commons.data.core.room.entities.bill.BillPublicationData
import org.beatonma.commons.data.core.room.entities.bill.BillPublicationLink
import org.beatonma.commons.data.core.room.entities.bill.BillSponsorData
import org.beatonma.commons.data.core.room.entities.bill.BillStage
import org.beatonma.commons.data.core.room.entities.bill.BillType
import org.beatonma.commons.data.core.room.entities.bill.Organisation
import org.beatonma.commons.data.core.room.entities.bill.ParliamentarySession
import org.beatonma.commons.data.core.room.entities.bill.ZeitgeistBill
import org.beatonma.commons.data.core.room.entities.constituency.Constituency
import org.beatonma.commons.data.core.room.entities.constituency.ConstituencyBoundary
import org.beatonma.commons.data.core.room.entities.constituency.ConstituencyCandidate
import org.beatonma.commons.data.core.room.entities.constituency.ConstituencyElectionDetails
import org.beatonma.commons.data.core.room.entities.division.CommonsDivisionData
import org.beatonma.commons.data.core.room.entities.division.CommonsDivisionVoteData
import org.beatonma.commons.data.core.room.entities.division.LordsDivisionData
import org.beatonma.commons.data.core.room.entities.division.LordsDivisionVoteData
import org.beatonma.commons.data.core.room.entities.division.ZeitgeistDivision
import org.beatonma.commons.data.core.room.entities.election.ConstituencyResult
import org.beatonma.commons.data.core.room.entities.election.Election
import org.beatonma.commons.data.core.room.entities.member.CommitteeChairship
import org.beatonma.commons.data.core.room.entities.member.CommitteeMembership
import org.beatonma.commons.data.core.room.entities.member.Experience
import org.beatonma.commons.data.core.room.entities.member.FinancialInterest
import org.beatonma.commons.data.core.room.entities.member.HistoricalConstituency
import org.beatonma.commons.data.core.room.entities.member.HouseMembership
import org.beatonma.commons.data.core.room.entities.member.MemberProfile
import org.beatonma.commons.data.core.room.entities.member.Party
import org.beatonma.commons.data.core.room.entities.member.PartyAssociation
import org.beatonma.commons.data.core.room.entities.member.PhysicalAddress
import org.beatonma.commons.data.core.room.entities.member.Post
import org.beatonma.commons.data.core.room.entities.member.TopicOfInterest
import org.beatonma.commons.data.core.room.entities.member.Town
import org.beatonma.commons.data.core.room.entities.member.WebAddress
import org.beatonma.commons.data.core.room.entities.member.ZeitgeistMemberData
import org.beatonma.commons.data.core.room.entities.user.UserToken
import org.beatonma.commons.repo.repository.GoogleAccount
import org.beatonma.commons.snommoc.models.ApiBill
import org.beatonma.commons.snommoc.models.ApiCommittee
import org.beatonma.commons.snommoc.models.ApiCommitteeChairship
import org.beatonma.commons.snommoc.models.ApiCommonsDivision
import org.beatonma.commons.snommoc.models.ApiConstituency
import org.beatonma.commons.snommoc.models.ApiConstituencyBoundary
import org.beatonma.commons.snommoc.models.ApiConstituencyCandidate
import org.beatonma.commons.snommoc.models.ApiConstituencyElectionDetails
import org.beatonma.commons.snommoc.models.ApiConstituencyMinimal
import org.beatonma.commons.snommoc.models.ApiConstituencyResult
import org.beatonma.commons.snommoc.models.ApiElection
import org.beatonma.commons.snommoc.models.ApiExperience
import org.beatonma.commons.snommoc.models.ApiFinancialInterest
import org.beatonma.commons.snommoc.models.ApiHistoricalConstituency
import org.beatonma.commons.snommoc.models.ApiHouseMembership
import org.beatonma.commons.snommoc.models.ApiLordsDivision
import org.beatonma.commons.snommoc.models.ApiMemberProfile
import org.beatonma.commons.snommoc.models.ApiMemberVote
import org.beatonma.commons.snommoc.models.ApiOrganisation
import org.beatonma.commons.snommoc.models.ApiParty
import org.beatonma.commons.snommoc.models.ApiPartyAssociation
import org.beatonma.commons.snommoc.models.ApiPhysicalAddress
import org.beatonma.commons.snommoc.models.ApiPost
import org.beatonma.commons.snommoc.models.ApiSession
import org.beatonma.commons.snommoc.models.ApiTopicOfInterest
import org.beatonma.commons.snommoc.models.ApiTown
import org.beatonma.commons.snommoc.models.ApiVote
import org.beatonma.commons.snommoc.models.ApiWebAddress
import org.beatonma.commons.snommoc.models.ApiZeitgeist
import org.beatonma.commons.snommoc.models.social.ApiUserToken


fun ApiUserToken.composeToUserToken(account: GoogleAccount) = UserToken(
    name = account.name,
    photoUrl = account.photoUrl,
    email = account.email,
    snommocToken = this.snommocToken,
    googleId = account.googleId,
    username = this.username,
)


fun ApiHouseMembership.toHouseMembership(memberId: ParliamentID) = HouseMembership(
    house = house,
    memberId = memberId,
    start = start,
    end = end
)

fun ApiTown.toTown() = Town(town = town, country = country)
fun ApiParty.toParty() = Party(parliamentdotuk = parliamentdotuk, name = name)


fun ApiConstituency.toConstituency(): Constituency {
    return Constituency(
        parliamentdotuk = parliamentdotuk,
        name = name,
        start = start,
        end = end,
    )
}

fun ApiConstituencyMinimal.toConstituency(): Constituency = Constituency(
    parliamentdotuk = parliamentdotuk,
    name = name
)

fun ApiConstituencyBoundary.toConstituencyBoundary(constituencyId: ParliamentID) =
    ConstituencyBoundary(
        parliamentdotuk = constituencyId,
        kml = kml,
        area = area,
        boundaryLength = boundaryLength,
        centerLat = centerLat,
        centerLong = centerLong
    )

fun ApiConstituencyResult.toConstituencyResult(constituencyId: Int) = ConstituencyResult(
    memberId = member.parliamentdotuk,
    electionId = election.parliamentdotuk,
    constituencyId = constituencyId,
)

fun ApiElection.toElection() = Election(
    parliamentdotuk = parliamentdotuk,
    name = name,
    date = date,
    electionType = electionType
)

fun ApiConstituencyElectionDetails.toConstituencyElectionDetails() = ConstituencyElectionDetails(
    parliamentdotuk = parliamentdotuk,
    electorate = electorate,
    turnout = turnout,
    turnoutFraction = turnoutFraction,
    result = result,
    majority = majority,
    constituencyId = constituency.parliamentdotuk,
    electionId = election.parliamentdotuk,
)

fun ApiConstituencyCandidate.toConstituencyCandidate(resultsId: Int) = ConstituencyCandidate(
    resultsId = resultsId,
    name = name,
    profile = profile?.toMemberProfile(),
    partyName = partyName,
    party = party?.toParty(),
    order = order,
    votes = votes
)

fun ApiCommonsDivision.toDivision() = CommonsDivisionData(
    parliamentdotuk = parliamentdotuk,
    title = title,
    date = date,
    passed = passed,
    ayes = ayes,
    noes = noes,
    house = house,
    abstentions = abstentions,
    didNotVote = didNotVote,
    nonEligible = nonEligible,
    suspendedOrExpelled = suspendedOrExpelled,
    deferredVote = deferredVote,
    errors = errors,
)

fun ApiMemberVote.toVote(memberId: ParliamentID) = CommonsDivisionVoteData(
    memberId = memberId,
    divisionId = division.parliamentdotuk,
    vote = voteType,
    memberName = "",
    partyId = 0
)


fun ApiVote.toVote(divisionId: ParliamentID) = CommonsDivisionVoteData(
    divisionId = divisionId,
    memberId = memberId,
    memberName = memberName,
    vote = voteType,
    partyId = party.parliamentdotuk
)

fun ApiBill.getBillData() = BillData(
    id = parliamentdotuk,
    title = title,
    description = description,
    billTypeId = type.parliamentdotuk,
    currentStageId = currentStage.parliamentdotuk,
    isAct = isAct,
    isDefeated = isDefeated,
    lastUpdate = lastUpdate,
    sessionIntroducedId = sessionIntroduced.parliamentdotuk,
    withdrawnAt = withdrawnAt,
)

fun ApiBill.getPublicationData(): List<BillPublicationData> = publications.map { pub ->
    BillPublicationData(
        parliamentdotuk = pub.parliamentdotuk,
        title = pub.title,
        date = pub.date,
        type = pub.type
    )
}

fun ApiBill.getPublicationLinks(): List<BillPublicationLink> = publications.map { pub ->
    pub.links.map { link ->
        BillPublicationLink(
            publicationId = pub.parliamentdotuk,
            title = link.title,
            url = link.url,
            contentType = link.contentType,
        )
    }
}.flatten()

fun ApiBill.getSessions(): List<ParliamentarySession> =
    (listOf(sessionIntroduced) + sessions).map(ApiSession::toParliamentarySession)

fun ApiBill.getBillType(): BillType = BillType(
    id = type.parliamentdotuk,
    name = type.name,
    description = type.description,
    category = type.category,
)

fun ApiBill.getStages(): List<BillStage> = (listOf(currentStage) + stages).map { stage ->
    BillStage(
        parliamentdotuk = stage.parliamentdotuk,
        description = stage.description,
        house = stage.house,
        sessionId = stage.session.parliamentdotuk,
        sittings = stage.sittings,
        latestSitting = stage.latestSitting,
    )
}

fun ApiBill.getSponsorMembers(): List<MemberProfile> =
    sponsors.mapNotNull { it.member?.toMemberProfile() }

fun ApiBill.getSponsorData(): List<BillSponsorData> = sponsors.map {
    BillSponsorData(
        id = it.id,
        billId = this.parliamentdotuk,
        memberId = it.member?.parliamentdotuk,
        organisation = it.organisation?.toOrganisation(),
    )
}


fun ApiLordsDivision.getLordsDivisionData() = LordsDivisionData(
    parliamentdotuk = parliamentdotuk,
    title = title,
    date = date,
    description = description,
    house = house,
    sponsorId = sponsor?.parliamentdotuk,
    passed = passed,
    whippedVote = whippedVote,
    ayes = ayes,
    noes = noes,
)

fun ApiLordsDivision.getSponsor(): MemberProfile? = sponsor?.toMemberProfile()
fun ApiLordsDivision.getParties(): List<Party> =
    votes.map { it.party.toParty() }.distinct()

fun ApiLordsDivision.getVotes(): List<LordsDivisionVoteData> = votes.map { vote ->
    LordsDivisionVoteData(
        divisionId = this.parliamentdotuk,
        memberId = vote.memberId,
        memberName = vote.memberName,
        partyId = vote.party.parliamentdotuk,
        vote = vote.voteType,
    )
}

fun ApiOrganisation.toOrganisation() = Organisation(
    name = name,
    url = url,
)

private fun ApiSession.toParliamentarySession() = ParliamentarySession(
    id = parliamentdotuk,
    name = name,
)

fun ApiPhysicalAddress.toPhysicalAddress(memberId: ParliamentID) = PhysicalAddress(
    memberId = memberId,
    address = address,
    description = description,
    postcode = postcode,
    phone = phone,
    fax = fax,
    email = email
)

fun ApiWebAddress.toWebAddress(memberId: ParliamentID) = WebAddress(
    url = url,
    description = description,
    memberId = memberId
)

fun ApiPost.toPost(memberId: ParliamentID, postType: Post.PostType) = Post(
    parliamentdotuk = parliamentdotuk,
    memberId = memberId,
    name = name,
    postType = postType,
    start = start,
    end = end
)

fun ApiCommittee.toCommitteeMembership(member: ParliamentID) = CommitteeMembership(
    parliamentdotuk = parliamentdotuk,
    memberId = member,
    name = name,
    start = start,
    end = end
)

fun ApiCommitteeChairship.toCommitteeChairship(committeeId: ParliamentID, memberId: ParliamentID) =
    CommitteeChairship(
        committeeId = committeeId,
        memberId = memberId,
        start = start,
        end = end
    )

fun ApiPartyAssociation.toPartyAssociation(member: Int) = PartyAssociation(
    memberId = member,
    partyId = party.parliamentdotuk,
    start = start,
    end = end,
)

fun ApiFinancialInterest.toFinancialInterest(memberId: ParliamentID) = FinancialInterest(
    memberId = memberId,
    parliamentdotuk = parliamentdotuk,
    category = category,
    description = description,
    dateCreated = dateCreated,
    dateAmended = dateAmended,
    dateDeleted = dateDeleted,
    registeredLate = registeredLate
)

fun ApiExperience.toExperience(memberId: ParliamentID) = Experience(
    memberId = memberId,
    category = category,
    organisation = organisation,
    title = title,
    start = start,
    end = end
)

fun ApiTopicOfInterest.toTopicOfInterest(memberId: ParliamentID): TopicOfInterest = TopicOfInterest(
    memberId = memberId,
    category = category,
    topic = topic
)

fun ApiHistoricalConstituency.toHistoricalConstituency(member: Int): HistoricalConstituency =
    HistoricalConstituency(
        memberId = member,
        constituencyId = constituency.parliamentdotuk,
        start = start,
        end = end,
        electionId = election.parliamentdotuk
    )

fun ApiMemberProfile.toMemberProfile(): MemberProfile = MemberProfile(
    parliamentdotuk = parliamentdotuk,
    name = name,
    party = party.toParty(),
    constituency = constituency?.toConstituency(),
    active = active,
    isMp = isMp,
    isLord = isLord,
    age = age,
    dateOfBirth = dateOfBirth,
    dateOfDeath = dateOfDeath,
    gender = gender,
    placeOfBirth = placeOfBirth?.toTown(),
    portraitUrl = portraitUrl,
    currentPost = currentPost
)


fun ApiZeitgeist.getZeitgeistBills(): List<ZeitgeistBill> =
    bills.map { item ->
        val bill = item.target
        ZeitgeistBill(
            id = bill.parliamentdotuk,
            reason = item.reason ?: ZeitgeistReason.unspecified,
            priority = item.priority,
            title = bill.title,
            lastUpdate = bill.lastUpdate,
        )
    }

fun ApiZeitgeist.getZeitgeistDivisions(): List<ZeitgeistDivision> =
    divisions.map { item ->
        val division = item.target
        ZeitgeistDivision(
            id = division.parliamentdotuk,
            reason = item.reason ?: ZeitgeistReason.unspecified,
            priority = item.priority,
            title = division.title,
            date = division.date,
            passed = division.passed,
            house = division.house,
        )
    }

fun ApiZeitgeist.getZeitgeistMembers(): List<ZeitgeistMemberData> =
    people.map { item ->
        val member = item.target
        ZeitgeistMemberData(
            id = member.parliamentdotuk,
            reason = item.reason ?: ZeitgeistReason.unspecified,
            priority = item.priority,
            name = member.name,
            partyId = member.party.parliamentdotuk,
            constituencyId = member.constituency?.parliamentdotuk,
            currentPost = member.currentPost,
            portraitUrl = member.portraitUrl,
        )
    }
