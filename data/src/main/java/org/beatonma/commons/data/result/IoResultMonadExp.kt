package org.beatonma.commons.data.result

private const val TAG = "IoResult"

//sealed class IoResult<out T, E>(
//    val data: T? = null,
//    val error: E? = null,
//    val message: String? = null,
//) {
//    abstract operator fun component1(): T?
//    abstract operator fun component2(): E?
//}
//
//
//sealed class SuccessResult<T>(data: T?, message: String?): IoResult<T, Nothing>(data, message = message) {
//    override fun component1(): T? = data
//    override fun component2(): Nothing? = null
//
//    override fun toString(): String =
//        "[${this.javaClass.canonicalName}] data=`$data`, message=`$message`"
//
//    override fun hashCode(): Int = data.hashCode()
//
//    override fun equals(other: Any?): Boolean {
//        return when {
//            this === other -> true
//            other is SuccessResult<*> -> data == other.data
//            else -> false
//        }
//    }
//}
//class Success<T>(data: T, message: String? = null): SuccessResult<T>(data, message)
//class SuccessCode(val responseCode: Int, message: String? = null): SuccessResult<Nothing>(data = null, message = message) {
//    override fun toString(): String =
//        "[${this.javaClass.canonicalName}] responseCode=`$data`, message=`$message`"
//
//    override fun hashCode(): Int = responseCode.hashCode()
//
//    override fun equals(other: Any?): Boolean {
//        return when {
//            this === other -> true
//            other is SuccessCode -> responseCode == other.responseCode
//            else -> false
//        }
//    }
//}
//
//
//@Suppress("UNCHECKED_CAST")
//sealed class IoError<E>(error: E?, message: String?): IoResult<Nothing, E>(null, error, message) {
//    override fun component1(): Nothing? = null
//    override fun component2(): E? = error
//
//    override fun toString(): String =
//        "[${this.javaClass.canonicalName}] error=`$error`, message=`$message`"
//
//    override fun hashCode(): Int = data.hashCode()
//
//    override fun equals(other: Any?): Boolean {
//        return when {
//            this === other -> true
//            other is IoError<*> -> error == other.error
//            else -> false
//        }
//    }
//}
//
//
//class NetworkError(message: String?, error: Throwable?): IoError<Throwable>(error, message)
//class GenericError<E>(message: String?, error: E?): IoError<E>(error, message)
//class LocalError<E>(message: String?, error: E?): IoError<E>(error, message)
//class UnexpectedValueError<E>(message: String?, error: E?): IoError<E>(error, message)
//class NotSignedInError(message: String?): IoError<Exception>(Exception(message), message)
//
//
//class LoadingResult<T, E>(data: T? = null, message: String? = null): IoResult<T, E>(data, message = message) {
//    override fun component1(): T? = data
//    override fun component2(): Nothing? = null
//
//    override fun hashCode(): Int = data.hashCode()
//
//    override fun equals(other: Any?): Boolean {
//        return when {
//            this === other -> true
//            other is SuccessResult<*> -> data == other.data
//            else -> false
//        }
//    }
//}
//
//
//fun <T> loadingResult(data: T? = null, message: String? = null) =
//    LoadingResult<T, Nothing>(data, message)
