package org.beatonma.commons.data

import org.beatonma.commons.data.core.room.entities.member.House
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
                    return IoResult.success(body, message = "Network OK ")
                }
            }
            return IoResult.error("Network error: [${response.code()}] ${response.message()}")
        } catch (e: Exception) {
            return IoResult.error("getResult error: ${e.message ?: e}")
        }
    }
}


class CommonsRemoteDataSource @Inject constructor(
    private val service: CommonsService
) : BaseDataSource() {

    suspend fun getFeaturedPeople() = getResult {
        service.getFeaturedPeople()
    }

    suspend fun getMember(parliamentdotuk: Int) = getResult {
        service.getMember(parliamentdotuk)
    }

    suspend fun getFeaturedBills() = getResult {
        service.getFeaturedBills()
    }

    suspend fun getBill(parliamentdotuk: Int) = getResult {
        service.getBill(parliamentdotuk)
    }

    suspend fun getFeaturedDivisions() = getResult {
        service.getFeaturedDivisions()
    }

    suspend fun getDivision(house: String, parliamentdotuk: Int) =
        if (house == House.Lords.name) {
            getLordsDivision(parliamentdotuk)
        }
        else {
            getCommonsDivision(parliamentdotuk)
        }

    suspend fun getCommonsDivision(parliamentdotuk: Int) = getResult {
        service.getCommonsDivision(parliamentdotuk)
    }

    suspend fun getLordsDivision(parliamentdotuk: Int) = getResult {
        service.getLordsDivision(parliamentdotuk)
    }
}
