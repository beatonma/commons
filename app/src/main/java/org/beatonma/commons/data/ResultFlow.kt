package org.beatonma.commons.data

import kotlinx.coroutines.flow.*
import org.beatonma.commons.kotlin.extensions.dump

fun <T, N> resultFlow(
    databaseQuery: suspend () -> Flow<T>,
    networkCall: suspend () -> IoResult<N>,
    saveCallResult: suspend (N) -> Unit
): FlowIoResult<T> = flow<IoResult<T>> {
    val local = databaseQuery.invoke()

    val response = networkCall.invoke().dump("NETWORK")
    when(response) {
        is SuccessResult -> {
            if (response.data != null) {
                try {
                    saveCallResult(response.data)
                }
                catch (e: Exception) {
                    emit(LocalError("Unable to save network result: $e", e))
                }
            }
            else {
                emit(UnexpectedValueError("Null data: ${response.message}", null))
            }
        }

        is IoError -> {
            emit(NetworkError(response.message, response.error))
        }
    }

    local
        .map { SuccessResult(it, "OK") }
        .collect { emit(it) }
}


fun <T, N> resultFlowLocalPreferred(
    databaseQuery: suspend () -> Flow<T>,
    networkCall: suspend () -> IoResult<N>,
    saveCallResult: suspend (N) -> Unit
): FlowIoResult<T> = flow {
    databaseQuery.invoke().dump("DB.INVOKE")
        .onStart { emit(LoadingResult<T>().dump("ON_START")) }
        .map {
            it.dump("FROM DB")
            SuccessResult(it, "DB read OK")
        }
        .onEmpty {
            val response = networkCall.invoke().dump("NETWORK RESPONSE")
            when (response) {
                is SuccessResult -> {
                    if (response.data != null) {
                        try {
                            saveCallResult(response.data)
                        } catch (e: Exception) {
                            emit(LocalError("Unable to save network result: $e", e))
                        }
                    }
                    else {
                        emit(UnexpectedValueError("Null data: ${response.message}", null))
                    }
                }

                is IoError -> {
                    emit(NetworkError(response.message, response.error))
                }
            }
        }
        .collect {
            emit(it)
        }
}



fun <T> resultFlowNoCache(
    networkCall: suspend () -> IoResult<T>,
): FlowIoResult<T> = flow {
    emit(LoadingResult<T>())

    val response = networkCall.invoke()
    when (response) {
        is SuccessResult -> {
            if (response.data != null) {
                emit(SuccessResult(response.data, "Network OK"))
            }
            else {
                emit(UnexpectedValueError("Null data: ${response.message}", null))
            }
        }

        is IoError -> {
            emit(NetworkError(response.message, response.error))
        }
    }
}
