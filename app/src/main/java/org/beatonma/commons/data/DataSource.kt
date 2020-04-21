package org.beatonma.commons.data

import org.beatonma.commons.data.core.ApiCompleteMember
import org.beatonma.commons.data.core.room.entities.bill.ApiBill
import org.beatonma.commons.data.core.room.entities.bill.Bill
import org.beatonma.commons.data.core.room.entities.division.ApiDivision
import org.beatonma.commons.data.core.room.entities.division.ApiMemberVote
import org.beatonma.commons.data.core.room.entities.member.House
import org.beatonma.commons.data.core.room.entities.member.MemberProfile
import org.beatonma.commons.data.core.room.entities.user.ApiUserToken
import org.beatonma.commons.data.core.search.MemberSearchResult
import org.beatonma.commons.network.retrofit.CommonsService
import retrofit2.Response
import javax.inject.Inject

abstract class BaseDataSource {
    protected suspend fun <T> getResult(call: suspend () -> Response<T>): IoResult<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    return SuccessResult(body, message = "Network OK ")
                }
            }
            return NetworkError("[${response.code()}] ${response.message()}")
        }
        catch (e: Exception) {
            return GenericError("getResult error: ${e.message ?: e}")
        }
    }
}


interface CommonsRemoteDataSource {
    suspend fun getFeaturedPeople(): IoResult<List<MemberProfile>>
    suspend fun getMember(parliamentdotuk: Int): IoResult<ApiCompleteMember>

    suspend fun getFeaturedBills(): IoResult<List<Bill>>
    suspend fun getBill(parliamentdotuk: Int): IoResult<ApiBill>

    suspend fun getCommonsVotesForMember(parliamentdotuk: Int): IoResult<List<ApiMemberVote>>
    suspend fun getLordsVotesForMember(parliamentdotuk: Int): IoResult<List<ApiMemberVote>>

    suspend fun getFeaturedDivisions(): IoResult<List<ApiDivision>>
    suspend fun getDivision(house: House, parliamentdotuk: Int): IoResult<ApiDivision>
    suspend fun getCommonsDivision(parliamentdotuk: Int): IoResult<ApiDivision>
    suspend fun getLordsDivision(parliamentdotuk: Int): IoResult<ApiDivision>

    suspend fun getSearchResults(query: String): IoResult<List<MemberSearchResult>>

    suspend fun registerUser(googleToken: String): IoResult<ApiUserToken>
}


class CommonsRemoteDataSourceImpl @Inject constructor(
    private val service: CommonsService
) : BaseDataSource(), CommonsRemoteDataSource {

    override suspend fun getFeaturedPeople() = getResult {
        service.getFeaturedPeople()
    }

    override suspend fun getMember(parliamentdotuk: Int) = getResult {
        service.getMember(parliamentdotuk)
    }

    override suspend fun getFeaturedBills() = getResult {
        service.getFeaturedBills()
    }

    override suspend fun getBill(parliamentdotuk: Int) = getResult {
        service.getBill(parliamentdotuk)
    }

    override suspend fun getFeaturedDivisions() = getResult {
        service.getFeaturedDivisions()
    }

    override suspend fun getCommonsVotesForMember(parliamentdotuk: Int) = getResult {
        service.getCommonsVotesForMember(parliamentdotuk)
    }

    override suspend fun getLordsVotesForMember(parliamentdotuk: Int) = getResult {
        service.getLordsVotesForMember(parliamentdotuk)
    }

    override suspend fun getDivision(house: House, parliamentdotuk: Int) =
        when (house) {
            House.Lords -> getLordsDivision(parliamentdotuk)
            else -> getCommonsDivision(parliamentdotuk)
        }

    override suspend fun getCommonsDivision(parliamentdotuk: Int) = getResult {
        service.getCommonsDivision(parliamentdotuk)
    }

    override suspend fun getLordsDivision(parliamentdotuk: Int) = getResult {
        service.getLordsDivision(parliamentdotuk)
    }

    override suspend fun getSearchResults(query: String) = getResult {
        service.getSearchResults(query)
    }

    override suspend fun registerUser(googleToken: String) = getResult {
        service.registerGoogleSignIn(googleToken)
    }
}
