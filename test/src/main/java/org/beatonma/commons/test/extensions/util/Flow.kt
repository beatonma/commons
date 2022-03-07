package org.beatonma.commons.test.extensions.util

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException


@OptIn(ExperimentalCoroutinesApi::class)
private fun <T> ProducerScope<T>.timeout(
    timeout: Long,
    timeUnit: TimeUnit,
    timeoutThrows: Boolean = true,
    onTimeout: suspend () -> Unit,
): Job = launch {
    val timeoutMillis = timeUnit.toMillis(timeout)
    delay(timeoutMillis)
    if (!isClosedForSend) {
        onTimeout()
        if (timeoutThrows) {
            throw TimeoutException("awaitValue did not complete (timeout=${timeoutMillis}ms)")
        } else {
            close()
        }
    }
}

/**
 * Hide flow emissions until [latchCount] emissions have been collected, then emit the final result.
 */
@OptIn(ExperimentalCoroutinesApi::class)
fun <T> Flow<T>.awaitValue(
    latchCount: Int = 1,
    timeout: Long = 500,
    timeUnit: TimeUnit = TimeUnit.MILLISECONDS,
    timeoutThrows: Boolean = true,
): Flow<T> = channelFlow {
    val latch = CountDownLatch(latchCount)
    var latest: T? = null

    val timeoutJob = timeout(timeout, timeUnit, timeoutThrows) {
        latest?.let { send(it) }
    }

    launch {
        this@awaitValue.collect {
            latest = it
            latch.countDown()
            it.dump("Collect ${latch.count}")
            if (latch.count <= 0L) {
                this@channelFlow.send(it)
                this@channelFlow.close()
                timeoutJob.cancel()
            }
        }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
fun <T> Flow<T>.awaitValue(
    timeout: Long = 500,
    timeUnit: TimeUnit = TimeUnit.MILLISECONDS,
    timeoutThrows: Boolean = true,
    condition: (T) -> Boolean,
) = channelFlow<T> {
    var latest: T? = null

    val timeoutJob = timeout(timeout, timeUnit, timeoutThrows) {
        send(latest!!)
    }

    launch {
        collect {
            latest = it
            if (condition.invoke(it)) {
                send(it!!)
                timeoutJob.cancel()
                close()
            } else {
                it.dump("Condition failed")
            }
        }
    }
}

/**
 * Hide flow emissions until [latchCount] emissions have been collected, then emit list of all collected results.
 */
@OptIn(ExperimentalCoroutinesApi::class)
fun <T> Flow<T>.awaitValues(
    latchCount: Int,
    timeout: Long = 500,
    timeUnit: TimeUnit = TimeUnit.MILLISECONDS,
    timeoutThrows: Boolean = true,
): Flow<List<T>> = channelFlow {
    val latch = CountDownLatch(latchCount)
    val values = mutableListOf<T>()

    val timeoutJob = timeout(timeout, timeUnit, timeoutThrows) {
        send(values)
    }

    launch {
        this@awaitValues.collect {
            latch.countDown()
            values.add(it)
            it.dump("Collect ${latch.count}")
            if (latch.count == 0L) {
                this@channelFlow.send(values)
                this@channelFlow.close()
                timeoutJob.cancel()
            }
        }
    }
}
