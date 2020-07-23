package org.beatonma.commons.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


/**
 * Emits cached results of [databaseQuery] then attempts to update data with [networkCall].
 *
 * - If networkCall succeeds it will update the database cache via [saveCallResult] and the
 * [databaseQuery] will emit the new data.
 *
 * - Otherwise
 */
fun <T, N> cachedResultFlow(
    databaseQuery: () -> Flow<T>,
    networkCall: suspend () -> IoResult<N>,
    saveCallResult: suspend (N) -> Unit
): FlowIoResult<T> = channelFlow {
    send(LoadingResult())

    // Run databaseQuery in separate coroutine and surface results to channelFlow
    launch {
        databaseQuery.invoke()
            .distinctUntilChanged()
            .mapLatest { SuccessResult(it,"DB read") }
            .collectLatest { send(it) }
    }

    submitAndSaveNetworkResult(networkCall, saveCallResult)
}.flowOn(Dispatchers.IO)

/**
 * [networkCall] will only execute if [databaseQuery] does not emit a useful value.
 */
fun <T, N> resultFlowLocalPreferred(
    databaseQuery: suspend () -> Flow<T>,
    networkCall: suspend () -> IoResult<N>,
    saveCallResult: suspend (N) -> Unit,
): FlowIoResult<T> = channelFlow<IoResult<T>> {
    databaseQuery.invoke()
        .collect { queryResult ->
            when {
                queryResult == null -> submitAndSaveNetworkResult(networkCall, saveCallResult)
                queryResult is Collection<*> && queryResult.isEmpty() -> {
                    submitAndSaveNetworkResult(networkCall, saveCallResult)
                }
                else -> sendAndClose(
                    SuccessResult(queryResult, "DB read")
                )
            }
        }
}.flowOn(Dispatchers.IO)

/**
 * Run the network call with no local caching.
 * The resulting flow will emit a LoadingResult while waiting for network response.
 */
fun <T> resultFlowNoCache(
    networkCall: suspend () -> IoResult<T>,
): FlowIoResult<T> = flow {
    emit(LoadingResult<T>())

    emit(networkCall.invoke())
}



private suspend fun <E> ProducerScope<E>.sendAndClose(element: E, cause: Throwable? = null) {
    send(element)
    close(cause)
}


/**
 * Shared network handling block for updating local cache.
 */
private suspend inline fun <E, N> ProducerScope<IoResult<E>>.submitAndSaveNetworkResult(
    networkCall: suspend () -> IoResult<N>,
    saveCallResult: suspend (N) -> Unit
) {
    val response = networkCall.invoke()
    when(response) {
        is SuccessResult -> {
            if (response.data != null) {
                try {
                    saveCallResult(response.data)
                }
                catch (e: Exception) {
                    sendAndClose(LocalError("Unable to save network result: $e", e))
                }
            }
            else {
                sendAndClose(UnexpectedValueError("Null data: ${response.message}", null))
            }
        }

        is IoError -> {
            sendAndClose(NetworkError(response.message, response.error))
        }
    }
}
