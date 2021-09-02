package org.beatonma.commons.repo.result

val <R : BaseResult<*, *>> R.isSuccess: Boolean
    get() = this is Success<*> || this is SuccessCode

val <R : BaseResult<*, *>> R.isLoading: Boolean
    get() = this is IoLoading

val <R : BaseResult<*, *>> R.isError: Boolean
    get() = this is Failure<*> || this is ErrorCode

/**
 * Returns true when this is not a loading state
 */
val <R : BaseResult<*, *>> R.isComplete: Boolean
    get() = this !is IoLoading

inline fun <T, E, R> BaseResult<T, E>.map(transform: (T) -> R): BaseResult<R, E> {
    return when (this) {
        is Success -> Success(transform(data))
        is HttpCodeResult -> this
        is Failure -> this
        is IoLoading -> this
    }
}

inline fun <T, E, F> BaseResult<T, E>.mapError(transform: (E) -> F): BaseResult<T, F> {
    return when (this) {
        is Success -> this
        is HttpCodeResult -> this
        is Failure -> Failure(transform(error))
        is IoLoading -> this
    }
}

inline fun <T, E> BaseResult<T, E>.onSuccess(block: (T) -> Unit): BaseResult<T, E> {
    if (this is Success) {
        block(data)
    }
    return this
}

inline fun <T, E> BaseResult<T, E>.onError(block: (E) -> Unit): BaseResult<T, E> {
    if (this is Failure) {
        block(error)
    }
    return this
}

inline fun <T, E> BaseResult<T, E>.onResponseCode(block: (ResponseCode) -> Unit): BaseResult<T, E> {
    if (this is HttpCodeResult) {
        block(responseCode)
    }
    return this
}

inline fun <T, E> BaseResult<T, E>.onSuccessCode(block: (ResponseCode) -> Unit): BaseResult<T, E> {
    if (this is SuccessCode) {
        block(responseCode)
    }
    return this
}

inline fun <T, E> BaseResult<T, E>.onErrorCode(block: (ResponseCode) -> Unit): BaseResult<T, E> {
    if (this is ErrorCode) {
        block(responseCode)
    }
    return this
}

inline fun <T, E> BaseResult<T, E>.onLoading(block: () -> Unit): BaseResult<T, E> {
    if (this is IoLoading) {
        block()
    }
    return this
}
