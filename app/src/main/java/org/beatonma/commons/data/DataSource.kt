package org.beatonma.commons.data

import org.beatonma.commons.network.retrofit.CommonsService
import retrofit2.Response
import javax.inject.Inject

abstract class BaseDataSource {
    protected suspend fun <T> getResult(call: suspend () -> Response<T>): Result<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    return Result.success(body, message = "Network OK ")
                }
            }
            return Result.error("Network error: [${response.code()}] ${response.message()}")
        } catch (e: Exception) {
            return Result.error("getResult error: ${e.message ?: e}")
        }
    }
}


class CommonsRemoteDataSource @Inject constructor(
    val service: CommonsService
) : BaseDataSource() {

    suspend fun getFeaturedPeople() = getResult {
        service.getFeaturedPeople()
    }
}
