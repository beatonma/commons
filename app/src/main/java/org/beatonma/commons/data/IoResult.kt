package org.beatonma.commons.data

private const val TAG = "IoResult"

sealed class IoResult<out T>(
    val data: T?,
    val message: String?
)

class SuccessResult<T>(data: T, message: String?): IoResult<T>(data, message)

sealed class IoError<T>(data: T?, message: String?, val error: Exception?): IoResult<T>(data, message)
class NetworkError<T>(message: String?, error: Exception?): IoError<T>(null, message, error)
class GenericError<T>(message: String?, error: Exception?): IoError<T>(null, message, error)
class LocalError<T>(message: String?, error: Exception?): IoError<T>(null, message, error)
class UnexpectedValueError<T>(message: String?, error: Exception?): IoError<T>(null, message, error)

class LoadingResult<T>(data: T? = null, message: String? = null): IoResult<T>(data, message)
