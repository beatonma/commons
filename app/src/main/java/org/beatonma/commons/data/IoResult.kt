package org.beatonma.commons.data

private const val TAG = "IoResult"

sealed class IoResult<out T>(
    val data: T?,
    val message: String?
)

class SuccessResult<T>(data: T, message: String?): IoResult<T>(data, message)
sealed class IoError<T>(data: T?, message: String?): IoResult<T>(data, message)
class NetworkError<T>(message: String?): IoError<T>(null, message)
class GenericError<T>(message: String?): IoError<T>(null, message)
class LocalError<T>(message: String?): IoError<T>(null, message)
class UnexpectedValueError<T>(message: String?): IoError<T>(null, message)
class LoadingResult<T>(data: T? = null, message: String? = null): IoResult<T>(data, message)
