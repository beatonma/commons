package org.beatonma.commons.repo.result

import org.beatonma.commons.network.core.Http

sealed class Result<out T, out E>
typealias ThrowableResult<T> = Result<T, Throwable>

object Loading : Result<Nothing, Nothing>()

data class Success<out T>(
    val data: T,
    val message: String? = null,
) : Result<T, Nothing>()

data class Failure<out E>(
    val error: E,
    val message: String? = null,
) : Result<Nothing, E>()

class ResponseCode(
    val responseCode: Int,
) : Result<Nothing, Nothing>() {
    val isSuccess: Boolean get() = Http.Status.isSuccess(responseCode)
    val isError: Boolean get() = Http.Status.isClientError(responseCode)
    val isClientError: Boolean get() = Http.Status.isClientError(responseCode)
    val isServerError: Boolean get() = Http.Status.isServerError(responseCode)
}
