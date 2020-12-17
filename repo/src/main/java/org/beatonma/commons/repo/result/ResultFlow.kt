package org.beatonma.commons.repo.result

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import org.beatonma.commons.core.extensions.autotag
import org.beatonma.commons.repo.ResultFlow

/**
 * Emits cached results of [databaseQuery] then attempts to update data with [networkCall].
 *
 * - If networkCall succeeds it will update the database cache via [saveCallResult] and the
 * [databaseQuery] will emit the new data.
 *
 * - Otherwise
 */
@OptIn(ExperimentalCoroutinesApi::class)
fun <T, N> cachedResultFlow(
    databaseQuery: () -> Flow<T>,
    networkCall: suspend () -> IoResult<N>,
    saveCallResult: suspend (N) -> Unit,
    distinctUntilChanged: Boolean = true,
): ResultFlow<T> =
    channelFlow<IoResult<T>> {
        send(IoLoading)
        var networkCallFinished = false

        // Run databaseQuery in separate coroutine and surface results to channelFlow
        launch {
            databaseQuery.invoke().apply {
                if (distinctUntilChanged) {
                    distinctUntilChanged()
                }
            }
                .filter { result ->
                    /**
                     * Only emit null/empty [databaseQuery] results if the network call has completed.
                     */
                    println("filtering [networkCallFinished=$networkCallFinished]: $result")
                    when {
                        networkCallFinished -> true
                        result is Collection<*> -> result.isNotEmpty()
                        else -> result != null
                    }
                }
                .mapLatest(::Success)
                .collectLatest(::send)
        }

        networkCallFinished =
            submitAndSaveNetworkResult(networkCall, saveCallResult, closeOnError = false)
    }.catch { e ->
        emit(Failure(e, "cachedResultFlow error"))
    }.flowOn(Dispatchers.IO)

/**
 * [networkCall] will only execute if [databaseQuery] does not emit a useful value.
 */
@OptIn(ExperimentalCoroutinesApi::class)
fun <T, N> resultFlowLocalPreferred(
    databaseQuery: () -> Flow<T>,
    networkCall: suspend () -> IoResult<N>,
    saveCallResult: suspend (N) -> Unit,
): ResultFlow<T> =
    channelFlow<IoResult<T>> {
        send(IoLoading)

        databaseQuery.invoke()
            .collect { queryResult ->
                when {
                    queryResult == null -> submitAndSaveNetworkResult(networkCall, saveCallResult)

                    queryResult is Collection<*> && queryResult.isEmpty() -> {
                        submitAndSaveNetworkResult(networkCall, saveCallResult)
                    }

                    else -> send(
                        Success(queryResult)
                    )
                }
            }
    }.catch { e ->
        emit(Failure(e, "resultFlowLocalPreferred error"))
    }.flowOn(Dispatchers.IO)

/**
 * Run the network call with no local caching.
 * The resulting flow will emit a LoadingResult while waiting for network response.
 */
@OptIn(ExperimentalCoroutinesApi::class)
fun <T> resultFlowNoCache(
    networkCall: suspend () -> IoResult<T>,
): ResultFlow<T> =
    flow {
        emit(IoLoading)

        val response = networkCall()
        emit(response)
    }
        .catch { e ->
            emit(Failure(e, "resultFlowNoCache error"))
        }
        .flowOn(Dispatchers.IO)

/**
 * Shared network handling block for updating local cache.
 * Returns true if saveCallResult completes successfully.
 */
@Suppress("REDUNDANT_INLINE_SUSPEND_FUNCTION_TYPE") // networkCall and saveCallResult require suspend keyword
@OptIn(ExperimentalCoroutinesApi::class)
private suspend inline fun <E, N> ProducerScope<IoResult<E>>.submitAndSaveNetworkResult(
    networkCall: suspend () -> IoResult<N>,
    saveCallResult: suspend (N) -> Unit,
    closeOnError: Boolean = true,
): Boolean {
    val response: IoResult<N> = networkCall.invoke()

    when (response) {
        is Success -> saveCallResult(response.data)
        is Failure -> sendError(response, response.error, closeOnError)
        is ErrorCode -> sendError(response)
    }
    return true
}

@OptIn(ExperimentalCoroutinesApi::class)
private suspend fun <E> ProducerScope<E>.sendError(
    element: E,
    cause: Throwable? = null,
    closeFlow: Boolean = true,
) {
    if (cause != null) {
        Log.w(autotag, cause)
    }

    send(element)

    if (closeFlow) {
        close(cause)
    }
}
