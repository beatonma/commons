package org.beatonma.commons.repo.result

import org.beatonma.commons.network.core.Http
import retrofit2.Response

sealed class BaseResult<out T, out E>
typealias IoResult<T> = BaseResult<T, Throwable>

object IoLoading : BaseResult<Nothing, Nothing>()

data class Success<out T>(
    val data: T,
    val message: String? = null,
) : BaseResult<T, Nothing>()

data class Failure<out E>(
    val error: E,
    val message: String? = null,
) : BaseResult<Nothing, E>()

sealed class HttpCodeResult(
    val responseCode: ResponseCode,
    val message: String? = null,
) : BaseResult<Nothing, Nothing>()

class SuccessCode(responseCode: ResponseCode, message: String?) :
    HttpCodeResult(responseCode, message) {
    constructor(response: Response<*>) : this(ResponseCode(response.code()), response.message())
}

class ErrorCode(responseCode: ResponseCode, message: String?) :
    HttpCodeResult(responseCode, message) {
    constructor(response: Response<*>) : this(ResponseCode(response.code()), response.message())
}

inline class ResponseCode(val code: Int) {
    val isSuccess: Boolean get() = Http.Status.isSuccess(code)
    val isError: Boolean get() = Http.Status.isClientError(code)
    val isClientError: Boolean get() = Http.Status.isClientError(code)
    val isServerError: Boolean get() = Http.Status.isServerError(code)
}
