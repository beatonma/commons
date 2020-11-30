package org.beatonma.commons.repo.result


import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.delay
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
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import org.beatonma.commons.repo.FlowIoResult
import java.util.concurrent.TimeUnit


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
): FlowIoResult<T> = channelFlow<IoResult<T>> {
    send(LoadingResult())
    var networkCallFinished = false

    // Run databaseQuery in separate coroutine and surface results to channelFlow
    launch {
        databaseQuery.invoke().apply {
            if (distinctUntilChanged) {
                distinctUntilChanged()
            }
        }
            .filter {
                /**
                 * Only emit null/empty [databaseQuery] results if the network call has completed.
                 */
                when {
                    networkCallFinished -> true
                    it is Collection<*> -> it.isNotEmpty()
                    else -> it != null
                }
            }
            .mapLatest { SuccessResult(it, "DB read") }
            .collectLatest(::send)
    }

    networkCallFinished =
        submitAndSaveNetworkResult(networkCall, saveCallResult, closeOnError = false)
}.catch {
    println(it)
    emit(GenericError("cachedResultFlow error", it))
}.flowOn(Dispatchers.IO)

/**
 * [networkCall] will only execute if [databaseQuery] does not emit a useful value.
 */
@OptIn(ExperimentalCoroutinesApi::class)
fun <T, N> resultFlowLocalPreferred(
    databaseQuery: () -> Flow<T>,
    networkCall: suspend () -> IoResult<N>,
    saveCallResult: suspend (N) -> Unit,
): FlowIoResult<T> = channelFlow<IoResult<T>> {
    send(LoadingResult())

    databaseQuery.invoke()
        .collect { queryResult ->
            when {
                queryResult == null -> submitAndSaveNetworkResult(networkCall, saveCallResult)

                queryResult is Collection<*> && queryResult.isEmpty() -> {
                    submitAndSaveNetworkResult(networkCall, saveCallResult)
                }

                else -> send(
                    SuccessResult(queryResult, "DB read")
                )
            }
        }
}.catch {
    emit(GenericError("resultFlowLocalPreferred error $it", it))
}.flowOn(Dispatchers.IO)

/**
 * Run the network call with no local caching.
 * The resulting flow will emit a LoadingResult while waiting for network response.
 */
@OptIn(ExperimentalCoroutinesApi::class)
fun <T> resultFlowNoCache(
    networkCall: suspend () -> IoResult<T>,
): FlowIoResult<T> = flow {
    emit(LoadingResult<T>())

    emit(networkCall.invoke())
}.catch {
    emit(NetworkError("resultFlowNoCache error", it))
}.flowOn(Dispatchers.IO)

@OptIn(ExperimentalCoroutinesApi::class)
suspend inline fun <T> Flow<IoResult<T>>.await(
    timeout: Long = 1000,
    timeUnit: TimeUnit = TimeUnit.MILLISECONDS,
    crossinline predicate: (IoResult<T>) -> Boolean,
) = channelFlow {
    val timeoutJob = launch {
        delay(timeUnit.toMillis(timeout))
        close()
    }

    launch {
        this@await.collect {
            if (predicate(it)) {
                timeoutJob.cancel()
                this@channelFlow.send(it)
                this@channelFlow.close()
            }
        }
    }
}.single()

@OptIn(ExperimentalCoroutinesApi::class)
private suspend fun <E> ProducerScope<E>.sendError(
    element: E,
    cause: Throwable? = null,
    closeFlow: Boolean = true,
) {
    send(element)
    if (closeFlow) {
        close(cause)
    }
}


/**
 * Shared network handling block for updating local cache.
 * Returns true if saveCallResult completes successfully.
 */
@OptIn(ExperimentalCoroutinesApi::class)
private suspend inline fun <E, N> ProducerScope<IoResult<E>>.submitAndSaveNetworkResult(
    networkCall: suspend () -> IoResult<N>,
    saveCallResult: suspend (N) -> Unit,
    closeOnError: Boolean = true,
): Boolean {
    val response = networkCall.invoke()
    when (response) {
        is SuccessResult<*>,
        is SuccessCodeResult,
        -> {
            if (response.data != null) {
                try {
                    saveCallResult(response.data)
                }
                catch (e: Exception) {
                    sendError(LocalError("Unable to save network result: $e", e),
                        e,
                        closeFlow = closeOnError)
                }
            }
            else {
                sendError(UnexpectedValueError("Null data: ${response.message}", null),
                    closeFlow = closeOnError)
            }
        }

        is IoError<*, *> -> {
            sendError(NetworkError(response.message, response.error),
                response.error,
                closeFlow = closeOnError)
        }
    }
    return true
}
