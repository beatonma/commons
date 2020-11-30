package org.beatonma.commons.repo.result

private const val TAG = "IoResult"

sealed class IoResult<out T>(
    val data: T?,
    val message: String?,
) {
    override fun toString() = "[${this.javaClass.canonicalName}] [$message] $data"
}

class SuccessResult<T>(data: T, message: String?): IoResult<T>(data, message)
class SuccessCodeResult(val responseCode: Int, message: String?): IoResult<Nothing>(null, message) {
    override fun toString(): String = "[${this.javaClass.canonicalName}] responseCode=$responseCode"
}

sealed class IoError<T, E: Throwable>(data: T?, message: String?, val error: E?): IoResult<T>(data, message) {
    override fun toString(): String =
        "[${this.javaClass.canonicalName}] data=`$data`, message=`$message`, error=`$error`"
}
class NetworkError(message: String?, error: Throwable?): IoError<Nothing, Throwable>(null, message, error)
class GenericError(message: String?, error: Throwable?): IoError<Nothing, Throwable>(null, message, error)
class LocalError(message: String?, error: Throwable?): IoError<Nothing, Throwable>(null, message, error)
class UnexpectedValueError(message: String?, error: Throwable?): IoError<Nothing, Throwable>(null, message, error)
class NotSignedInError(message: String?, error: Throwable? = null): IoError<Nothing, Throwable>(null, message, error)

class LoadingResult<T>(message: String? = null) : IoResult<T>(null, message) {
    override fun toString(): String = "[${this.javaClass.canonicalName}]"
}


val <R: IoResult<*>> R.isSuccess: Boolean get()  = this is SuccessResult<*> || this is SuccessCodeResult
val <R: IoResult<*>> R.isLoading: Boolean get()  = this is LoadingResult<*>
val <R: IoResult<*>> R.isError: Boolean get()  = this is IoError<*, *>
