package org.beatonma.commons.repo.result

private const val TAG = "IoResult"

sealed class IoResult<out T>(
    val data: T?,
    val message: String?,
) {
    override fun toString() = "[${this.javaClass.canonicalName}] [$message] $data"
}

class SuccessResult<T>(data: T, message: String? = null) : IoResult<T>(data, message)
class SuccessCodeResult(val responseCode: Int, message: String? = null) :
    IoResult<Nothing>(null, message) {
    override fun toString(): String = "[${this.javaClass.canonicalName}] responseCode=$responseCode"
}

sealed class IoError<T, E : Throwable>(data: T?, message: String?, val error: E) :
    IoResult<T>(data, message) {
    override fun toString(): String =
        "[${this.javaClass.canonicalName}] data=`$data`, message=`$message`, error=`$error`"
}

class NetworkError(error: Throwable, message: String?) :
    IoError<Nothing, Throwable>(null, message, error)

class GenericError(error: Throwable, message: String?) :
    IoError<Nothing, Throwable>(null, message, error)

class LocalError(error: Throwable, message: String?) :
    IoError<Nothing, Throwable>(null, message, error)

class UnexpectedValueError(message: String) :
    IoError<Nothing, Throwable>(null, message, Exception(message))

class NotSignedInError(message: String) :
    IoError<Nothing, Throwable>(null, message, Exception(message))

class LoadingResult<T>(message: String? = null) : IoResult<T>(null, message) {
    override fun toString(): String = "[${this.javaClass.canonicalName}]"
}

val <R : IoResult<*>> R.isSuccess: Boolean get() = this is SuccessResult<*> || this is SuccessCodeResult
val <R : IoResult<*>> R.isLoading: Boolean get() = this is LoadingResult<*>
val <R : IoResult<*>> R.isError: Boolean get() = this is IoError<*, *>
