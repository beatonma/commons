package org.beatonma.commons.data

import org.beatonma.commons.annotations.SignInRequired
import org.beatonma.commons.data.core.ApiCompleteMember
import org.beatonma.commons.data.core.MessageOfTheDay
import org.beatonma.commons.data.core.room.entities.bill.ApiBill
import org.beatonma.commons.data.core.room.entities.bill.Bill
import org.beatonma.commons.data.core.room.entities.constituency.ApiConstituency
import org.beatonma.commons.data.core.room.entities.constituency.ApiConstituencyElectionDetails
import org.beatonma.commons.data.core.room.entities.division.ApiDivision
import org.beatonma.commons.data.core.room.entities.division.ApiMemberVote
import org.beatonma.commons.data.core.room.entities.member.BasicProfile
import org.beatonma.commons.data.core.room.entities.member.House
import org.beatonma.commons.data.core.room.entities.member.MemberProfile
import org.beatonma.commons.data.core.room.entities.user.ApiUserToken
import org.beatonma.commons.data.core.search.MemberSearchResult
import org.beatonma.commons.data.core.social.*
import org.beatonma.commons.network.retrofit.CommonsService
import retrofit2.Response
import javax.inject.Inject

abstract class BaseDataSource {
    protected suspend fun <T> getResult(call: suspend () -> Response<T>): IoResult<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                return when(body) {
                    null -> NoBodySuccessResult(response.code(), message = "Network OK")
                    else -> SuccessResult(body, message = "Network OK ")
                }
            }
            return NetworkError("[${response.code()}] ${response.message()}", null)
        }
        catch (e: Exception) {
            return GenericError("getResult error: ${e.message ?: e}", e)
        }
    }
}


interface CommonsRemoteDataSource {
    // READ
    suspend fun getFeaturedPeople(): IoResultList<MemberProfile>
    suspend fun getMember(parliamentdotuk: ParliamentID): IoResult<ApiCompleteMember>

    suspend fun getFeaturedBills(): IoResultList<Bill>
    suspend fun getBill(parliamentdotuk: ParliamentID): IoResult<ApiBill>

    suspend fun getCommonsVotesForMember(parliamentdotuk: ParliamentID): IoResultList<ApiMemberVote>
    suspend fun getLordsVotesForMember(parliamentdotuk: ParliamentID): IoResultList<ApiMemberVote>

    suspend fun getFeaturedDivisions(): IoResultList<ApiDivision>
    suspend fun getDivision(house: House, parliamentdotuk: ParliamentID): IoResult<ApiDivision>
    suspend fun getCommonsDivision(parliamentdotuk: ParliamentID): IoResult<ApiDivision>
    suspend fun getLordsDivision(parliamentdotuk: ParliamentID): IoResult<ApiDivision>

    suspend fun getConstituency(parliamentdotuk: ParliamentID): IoResult<ApiConstituency>
    suspend fun getMemberForConstituency(parliamentdotuk: ParliamentID): IoResult<BasicProfile>
    suspend fun getConstituencyDetailsForElection(constituencyId: Int, electionId: Int): IoResult<ApiConstituencyElectionDetails>

    suspend fun getSearchResults(query: String): IoResultList<MemberSearchResult>

    suspend fun getSocialForTarget(
        targetType: SocialTargetType,
        parliamentdotuk: ParliamentID,
        snommocToken: SnommocToken?,
    ): IoResult<SocialContent>

    suspend fun getMessageOfTheDay(): IoResultList<MessageOfTheDay>

    // WRITE
    suspend fun registerUser(googleToken: String): IoResult<ApiUserToken>

    // Social
    @SignInRequired
    suspend fun postComment(comment: CreatedComment): IoResult<Void>

    @SignInRequired
    suspend fun postVote(vote: CreatedVote): IoResult<Void>
}


class CommonsRemoteDataSourceImpl @Inject constructor(
    private val service: CommonsService
) : BaseDataSource(), CommonsRemoteDataSource {

    override suspend fun getFeaturedPeople() = getResult {
        service.getFeaturedPeople()
    }

    override suspend fun getMember(parliamentdotuk: ParliamentID) = getResult {
        service.getMember(parliamentdotuk)
    }

    override suspend fun getFeaturedBills() = getResult {
        service.getFeaturedBills()
    }

    override suspend fun getBill(parliamentdotuk: ParliamentID) = getResult {
        service.getBill(parliamentdotuk)
    }

    override suspend fun getFeaturedDivisions() = getResult {
        service.getFeaturedDivisions()
    }

    override suspend fun getCommonsVotesForMember(parliamentdotuk: ParliamentID) = getResult {
        service.getCommonsVotesForMember(parliamentdotuk)
    }

    override suspend fun getLordsVotesForMember(parliamentdotuk: ParliamentID) = getResult {
        service.getLordsVotesForMember(parliamentdotuk)
    }

    override suspend fun getDivision(house: House, parliamentdotuk: ParliamentID) =
        when (house) {
            House.Lords -> getLordsDivision(parliamentdotuk)
            else -> getCommonsDivision(parliamentdotuk)
        }

    override suspend fun getCommonsDivision(parliamentdotuk: ParliamentID) = getResult {
        service.getCommonsDivision(parliamentdotuk)
    }

    override suspend fun getLordsDivision(parliamentdotuk: ParliamentID) = getResult {
        service.getLordsDivision(parliamentdotuk)
    }

    override suspend fun getConstituency(parliamentdotuk: ParliamentID) = getResult {
        service.getConstituency(parliamentdotuk)
    }

    override suspend fun getMemberForConstituency(parliamentdotuk: ParliamentID) = getResult {
        service.getMemberForConstituency(parliamentdotuk)
    }


    override suspend fun getConstituencyDetailsForElection(constituencyId: Int, electionId: Int) = getResult {
        service.getConstituencyElectionResults(constituencyId, electionId)
    }

    override suspend fun getSearchResults(query: String) = getResult {
        service.getSearchResults(query)
    }

    override suspend fun getMessageOfTheDay() = getResult {
        service.getMessageOfTheDay()
    }

    override suspend fun getSocialForTarget(
        targetType: SocialTargetType,
        parliamentdotuk: ParliamentID,
        snommocToken: SnommocToken?,
    ) = getResult {
        service.getSocialContentForTarget(targetType.name, parliamentdotuk, snommocToken)
    }

    override suspend fun registerUser(googleToken: String) = getResult {
        service.registerGoogleSignIn(googleToken)
    }

    // Write

    @SignInRequired
    override suspend fun postComment(comment: CreatedComment) = getResult {
        service.postComment(
            comment.target.targetType.name,
            comment.target.parliamentdotuk,
            comment.userToken.snommocToken,
            comment.text
        )
    }

    @SignInRequired
    override suspend fun postVote(vote: CreatedVote) = getResult {
        if (vote.voteType == SocialVoteType.NULL) {
            service.deleteVote(
                vote.target.targetType.name,
                vote.target.parliamentdotuk,
                DeletedVote(vote.userToken.snommocToken)
            )
        }
        else {
            service.postVote(
                vote.target.targetType.name,
                vote.target.parliamentdotuk,
                vote.userToken.snommocToken,
                vote.voteType.apiName,
            )
        }
    }
}
