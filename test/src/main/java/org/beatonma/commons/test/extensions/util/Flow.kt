package org.beatonma.commons.test.extensions.util

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException


/**
 * Hide flow emissions until [latchCount] emissions have been collected, then emit the final result.
 */
fun <T> Flow<T>.awaitValue(
    latchCount: Int = 1,
    timeout: Long = 500,
    timeUnit: TimeUnit = TimeUnit.MILLISECONDS,
    timeoutThrows: Boolean = true,
): Flow<T> = channelFlow {
    val latch = CountDownLatch(latchCount)
    var latest: T? = null

    val timeoutJob = launch {
        val timeout = timeUnit.toMillis(timeout)
        delay(timeUnit.toMillis(timeout))
        if (!this@channelFlow.isClosedForSend) {
            latest?.let { send(it) }
            if (timeoutThrows) {
                throw TimeoutException("awaitValue did not complete (timeout=${timeout}ms)")
            }
            else {
                close()
            }
        }
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

/**
 * Hide flow emissions until [latchCount] emissions have been collected, then emit list of all collected results.
 */
fun <T> Flow<T>.awaitValues(
    latchCount: Int,
    timeout: Long = 500,
    timeUnit: TimeUnit = TimeUnit.MILLISECONDS,
    timeoutThrows: Boolean = true,
): Flow<List<T>> = channelFlow {
    val latch = CountDownLatch(latchCount)
    val values = mutableListOf<T>()

    val timeoutJob = launch {
        val timeout = timeUnit.toMillis(timeout)
        delay(timeUnit.toMillis(timeout))
        if (!this@channelFlow.isClosedForSend) {
            send(values)
            if (timeoutThrows) {
                throw TimeoutException("awaitValue did not complete (timeout=${timeout}ms)")
            }
            else {
                close()
            }
        }
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
