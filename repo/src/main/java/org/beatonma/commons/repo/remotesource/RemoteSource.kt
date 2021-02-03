package org.beatonma.commons.repo.remotesource

import org.beatonma.commons.repo.result.ErrorCode
import org.beatonma.commons.repo.result.IoResult
import org.beatonma.commons.repo.result.Success
import org.beatonma.commons.repo.result.SuccessCode
import retrofit2.Response

interface RemoteSource {
    suspend fun <T> getResult(call: suspend () -> Response<T>): IoResult<T> {
        val response = call()
        if (response.isSuccessful) {
            val body = response.body()
            return when (body) {
                null -> SuccessCode(response)
                else -> Success(body)
            }
        }
        return ErrorCode(response)
    }
}
