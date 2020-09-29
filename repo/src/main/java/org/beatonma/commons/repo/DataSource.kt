package org.beatonma.commons.repo

import org.beatonma.commons.core.House
import org.beatonma.commons.core.ParliamentID
import org.beatonma.commons.core.SnommocToken
import org.beatonma.commons.data.core.room.entities.user.UserToken
import org.beatonma.commons.network.core.NetworkException
import org.beatonma.commons.repo.models.CreatedComment
import org.beatonma.commons.repo.models.CreatedVote
import org.beatonma.commons.repo.result.*
import org.beatonma.commons.snommoc.CommonsService
import org.beatonma.commons.snommoc.annotations.SignInRequired
import org.beatonma.commons.snommoc.models.*
import org.beatonma.commons.snommoc.models.search.MemberSearchResult
import org.beatonma.commons.snommoc.models.social.*
import retrofit2.Response
import javax.inject.Inject

interface BaseDataSource {
    suspend fun <T> getResult(call: suspend () -> Response<T>): IoResult<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                return when(body) {
                    null -> SuccessCodeResult(response.code(), message = "Network OK")
                    else -> SuccessResult(body, message = "Network OK")
                }
            }
            return NetworkError(response.message(), NetworkException(response))
        }
        catch (e: Exception) {
            return GenericError("getResult error: ${e.message ?: e}", e)
        }
    }
}


interface CommonsApi {
    // READ
    @Deprecated("Use Zeitgeist")
    suspend fun getFeaturedPeople(): IoResultList<ApiMemberProfile>
    suspend fun getMember(parliamentdotuk: ParliamentID): IoResult<ApiCompleteMember>

    @Deprecated("Use Zeitgeist")
    suspend fun getFeaturedBills(): IoResultList<ApiBill>
    suspend fun getBill(parliamentdotuk: ParliamentID): IoResult<ApiBill>

    suspend fun getVotesForMember(house: House, parliamentdotuk: ParliamentID): IoResultList<ApiMemberVote>

    @Deprecated("Use Zeitgeist")
    suspend fun getFeaturedDivisions(): IoResultList<ApiDivision>
    suspend fun getDivision(house: House, parliamentdotuk: ParliamentID): IoResult<ApiDivision>

    suspend fun getConstituency(parliamentdotuk: ParliamentID): IoResult<ApiConstituency>
    suspend fun getConstituencyDetailsForElection(constituencyId: Int, electionId: Int): IoResult<ApiConstituencyElectionDetails>

    suspend fun getSearchResults(query: String): IoResultList<MemberSearchResult>

    suspend fun getSocialForTarget(
        targetType: SocialTargetType,
        parliamentdotuk: ParliamentID,
        snommocToken: SnommocToken?,
    ): IoResult<SocialContent>

    suspend fun getMessageOfTheDay(): IoResultList<MessageOfTheDay>

    suspend fun getZeitgeist(): IoResult<ApiZeitgeist>

    // WRITE
    suspend fun registerUser(googleToken: String): IoResult<ApiUserToken>

    suspend fun requestRenameAccount(userToken: UserToken, newName: String): IoResult<Void>

    suspend fun deleteUserAccount(token: UserToken): IoResult<Void>

    // Social
    @SignInRequired
    suspend fun postComment(comment: CreatedComment): IoResult<Void>

    @SignInRequired
    suspend fun postVote(vote: CreatedVote): IoResult<Void>
}


class CommonsRemoteDataSource @Inject constructor(
    private val service: CommonsService
) : BaseDataSource, CommonsApi {

    override suspend fun getZeitgeist() = getResult(service::getZeitgeist)

    override suspend fun getFeaturedPeople() = getResult(service::getFeaturedPeople)

    override suspend fun getMember(parliamentdotuk: ParliamentID) = getResult {
        service.getMember(parliamentdotuk)
    }

    override suspend fun getFeaturedBills() = getResult(service::getFeaturedBills)

    override suspend fun getBill(parliamentdotuk: ParliamentID) = getResult {
        service.getBill(parliamentdotuk)
    }

    override suspend fun getFeaturedDivisions() = getResult(service::getFeaturedDivisions)

    override suspend fun getVotesForMember(house: House, parliamentdotuk: ParliamentID) = getResult {
        service.getVotesForMember(house, parliamentdotuk)
    }

    override suspend fun getDivision(house: House, parliamentdotuk: ParliamentID) = getResult {
        service.getDivision(house, parliamentdotuk)
    }

    override suspend fun getConstituency(parliamentdotuk: ParliamentID) = getResult {
        service.getConstituency(parliamentdotuk)
    }

    override suspend fun getConstituencyDetailsForElection(constituencyId: Int, electionId: Int) = getResult {
        service.getConstituencyElectionResults(constituencyId, electionId)
    }

    override suspend fun getSearchResults(query: String) = getResult {
        service.getSearchResults(query)
    }

    override suspend fun getMessageOfTheDay() = getResult(service::getMessageOfTheDay)

    override suspend fun getSocialForTarget(
        targetType: SocialTargetType,
        parliamentdotuk: ParliamentID,
        snommocToken: SnommocToken?,
    ) = getResult {
        service.getSocialContentForTarget(targetType.name, parliamentdotuk, snommocToken)
    }

    // Write

    override suspend fun registerUser(googleToken: String) = getResult {
        service.registerGoogleSignIn(googleToken)
    }

    override suspend fun requestRenameAccount(
        userToken: UserToken,
        newName: String
    ): IoResult<Void> = getResult {
        service.requestRenameAccount(RenameAccountRequest(
            currentUsername = userToken.username,
            token = userToken.snommocToken,
            newUsername = newName
        ))
    }

    override suspend fun deleteUserAccount(token: UserToken): IoResult<Void> = getResult {
        service.deleteUserAccount(DeleteUserRequest(
            gtoken = token.googleId,
            token = token.snommocToken
        ))
    }


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
