package org.beatonma.commons.data

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
}
