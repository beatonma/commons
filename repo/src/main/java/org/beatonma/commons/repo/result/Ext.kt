package org.beatonma.commons.repo.result

val <R : Result<*, *>> R.isSuccess: Boolean
    get() =
        this is Success<*> || (this is ResponseCode && this.isSuccess)

val <R : Result<*, *>> R.isLoading: Boolean get() = this is Loading

val <R : Result<*, *>> R.isError: Boolean
    get() =
        this is Failure<*> || (this is ResponseCode && this.isError)

inline fun <T, E, R> Result<T, E>.map(transform: (T) -> R): Result<R, E> {
    return when (this) {
        is Success -> Success(transform(data))
        is ResponseCode -> this
        is Failure -> this
        is Loading -> this
    }
}

inline fun <T, E, F> Result<T, E>.mapError(transform: (E) -> F): Result<T, F> {
    return when (this) {
        is Success -> this
        is ResponseCode -> this
        is Failure -> Failure(transform(error))
        is Loading -> this
    }
}

inline fun <T, E> Result<T, E>.onSuccess(block: (T) -> Unit): Result<T, E> {
    if (this is Success) {
        block(data)
    }
    return this
}

inline fun <T, E> Result<T, E>.onError(block: (E) -> Unit): Result<T, E> {
    if (this is Failure) {
        block(error)
    }
    return this
}

inline fun <T, E> Result<T, E>.onResponseCode(block: (Int) -> Unit): Result<T, E> {
    if (this is ResponseCode) {
        block(responseCode)
    }
    return this
}

inline fun <T, E> Result<T, E>.onLoading(block: () -> Unit): Result<T, E> {
    if (this is Loading) {
        block()
    }
    return this
}
