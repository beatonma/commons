package org.beatonma.commons.repo.converters

import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.data.core.room.entities.bill.Bill
import org.beatonma.commons.data.core.room.entities.bill.BillPublicationBasic
import org.beatonma.commons.data.core.room.entities.bill.BillPublicationDetail
import org.beatonma.commons.data.core.room.entities.bill.BillSponsor
import org.beatonma.commons.data.core.room.entities.bill.BillStage
import org.beatonma.commons.data.core.room.entities.bill.BillStageSitting
import org.beatonma.commons.data.core.room.entities.bill.BillType
import org.beatonma.commons.data.core.room.entities.bill.ParliamentarySession
import org.beatonma.commons.data.core.room.entities.constituency.Constituency
import org.beatonma.commons.data.core.room.entities.constituency.ConstituencyBoundary
import org.beatonma.commons.data.core.room.entities.constituency.ConstituencyCandidate
import org.beatonma.commons.data.core.room.entities.constituency.ConstituencyElectionDetails
import org.beatonma.commons.data.core.room.entities.division.Division
import org.beatonma.commons.data.core.room.entities.division.Vote
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
import org.beatonma.commons.data.core.room.entities.user.UserToken
import org.beatonma.commons.repo.repository.GoogleAccount
import org.beatonma.commons.snommoc.models.ApiBill
import org.beatonma.commons.snommoc.models.ApiBillPublication
import org.beatonma.commons.snommoc.models.ApiBillSponsor
import org.beatonma.commons.snommoc.models.ApiBillStage
import org.beatonma.commons.snommoc.models.ApiBillStageSitting
import org.beatonma.commons.snommoc.models.ApiBillType
import org.beatonma.commons.snommoc.models.ApiCommittee
import org.beatonma.commons.snommoc.models.ApiCommitteeChairship
import org.beatonma.commons.snommoc.models.ApiConstituency
import org.beatonma.commons.snommoc.models.ApiConstituencyBoundary
import org.beatonma.commons.snommoc.models.ApiConstituencyCandidate
import org.beatonma.commons.snommoc.models.ApiConstituencyElectionDetails
import org.beatonma.commons.snommoc.models.ApiConstituencyMinimal
import org.beatonma.commons.snommoc.models.ApiConstituencyResult
import org.beatonma.commons.snommoc.models.ApiDivision
import org.beatonma.commons.snommoc.models.ApiElection
import org.beatonma.commons.snommoc.models.ApiExperience
import org.beatonma.commons.snommoc.models.ApiFinancialInterest
import org.beatonma.commons.snommoc.models.ApiHistoricalConstituency
import org.beatonma.commons.snommoc.models.ApiHouseMembership
import org.beatonma.commons.snommoc.models.ApiMemberProfile
import org.beatonma.commons.snommoc.models.ApiMemberVote
import org.beatonma.commons.snommoc.models.ApiParliamentarySession
import org.beatonma.commons.snommoc.models.ApiParty
import org.beatonma.commons.snommoc.models.ApiPartyAssociation
import org.beatonma.commons.snommoc.models.ApiPhysicalAddress
import org.beatonma.commons.snommoc.models.ApiPost
import org.beatonma.commons.snommoc.models.ApiTopicOfInterest
import org.beatonma.commons.snommoc.models.ApiTown
import org.beatonma.commons.snommoc.models.ApiVote
import org.beatonma.commons.snommoc.models.ApiWebAddress
import org.beatonma.commons.snommoc.models.social.ApiUserToken
import org.beatonma.commons.ukparliament.models.UkApiBillPublication

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

fun ApiConstituencyMinimal.toConstituency():Constituency = Constituency(
    parliamentdotuk = parliamentdotuk,
    name = name
)

fun ApiConstituencyBoundary.toConstituencyBoundary(constituencyId: ParliamentID) = ConstituencyBoundary(
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

fun ApiBill.toBill() = Bill(
    parliamentdotuk = parliamentdotuk,
    title = title,
    description = description,
    actName = actName,
    label = label,
    homepage = homepage,
    date = date,
    ballotNumber = ballotNumber,
    billChapter = billChapter,
    isPrivate = isPrivate,
    isMoneyBill = isMoneyBill,
    publicInvolvementAllowed = publicInvolvementAllowed,
    sessionId = session?.parliamentdotuk,
    typeId = type?.name
)

fun ApiDivision.toDivision() = Division(
    parliamentdotuk = parliamentdotuk,
    title = title,
    description = description,
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
    whippedVote = whippedVote,
)

fun ApiMemberVote.toVote(memberId: ParliamentID) = Vote(
    memberId = memberId,
    divisionId = division.parliamentdotuk,
    voteType = voteType,
    memberName = "",
    partyId = 0
)


fun ApiVote.toVote(divisionId: ParliamentID) = Vote(
    divisionId = divisionId,
    memberId = memberId,
    memberName = memberName,
    voteType = voteType,
    partyId = party?.parliamentdotuk
)

fun ApiBillStage.toBillStage(billId: ParliamentID) = BillStage(
    parliamentdotuk = parliamentdotuk,
    billId = billId,
    type = type,
)

fun ApiBillSponsor.toBillSponsor(billId: ParliamentID) = BillSponsor(
    name = name,
    billId = billId,
    profile = profile?.toMemberProfile(),
)

fun ApiBillPublication.toBillPublication(billId: ParliamentID) = BillPublicationBasic(
    parliamentdotuk = parliamentdotuk,
    title = title,
    billId = billId
)

fun UkApiBillPublication.toBillPublicationDetail(billId: ParliamentID) = BillPublicationDetail(
    parliamentdotuk = parliamentdotuk,
    billId = billId,
    url = url,
    contentType = contentType,
    contentLength = contentLength,
)

fun ApiBillStageSitting.toBillStageSitting(billStageId: ParliamentID) = BillStageSitting(
    parliamentdotuk = parliamentdotuk,
    billStageId = billStageId,
    date = date,
    isFormal = isFormal,
    isProvisional = isProvisional
)

fun ApiBillType.toBillType() = BillType(
    name = name,
    description = description,
)

fun ApiParliamentarySession.toParliamentarySession() = ParliamentarySession(
    parliamentdotuk = parliamentdotuk,
    name = name
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

fun ApiCommitteeChairship.toCommitteeChairship(committeeId: ParliamentID, memberId: ParliamentID) = CommitteeChairship(
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

fun ApiTopicOfInterest.toTopicOfInterest(memberId: ParliamentID) = TopicOfInterest(
    memberId = memberId,
    category = category,
    topic = topic
)

fun ApiHistoricalConstituency.toHistoricalConstituency(member: Int) = HistoricalConstituency(
    memberId = member,
    constituencyId = constituency.parliamentdotuk,
    start = start,
    end = end,
    electionId = election.parliamentdotuk
)

fun ApiMemberProfile.toMemberProfile() = MemberProfile(
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
