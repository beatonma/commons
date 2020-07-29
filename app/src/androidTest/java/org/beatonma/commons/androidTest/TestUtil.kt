package org.beatonma.commons.androidTest

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/**
 * Hide flow emissions until [latchCount] emissions have been collected, then emit the final result.
 */
fun <T> Flow<T>.awaitValue(
    latchCount: Int,
    time: Long = 2000,
    timeUnit: TimeUnit = TimeUnit.MILLISECONDS,
): Flow<T> = channelFlow {
    val latch = CountDownLatch(latchCount)

    val timeoutJob = launch {
        val timeout = timeUnit.toMillis(time)
        delay(timeUnit.toMillis(time))
        if (!this@channelFlow.isClosedForSend) {
            throw TimeoutException("awaitValue did not complete (timeout=${timeout}ms)")
        }
    }

    launch {
        this@awaitValue.collect {
            latch.countDown()
            if (latch.count == 0L) {
                this@channelFlow.send(it)
                this@channelFlow.close()
                timeoutJob.cancel()
            }
        }
    }
}


/**
 * Dump value to log (with optional message) and return `this` value
 * Convenience for `.also { println("$it") }`
 * If the resulting string is very long it will be broken into chunks.
 * Should only be used for debugging purposes
 */
fun <T> T.dump(message: String = ""): T {
    if (this is Collection<*>) {
        if (message.isNotEmpty()) println("$message [$size]:")
        return also {
            forEach { item -> item.dump("  ") }
        }
    }
    val str = toString()
    val step = 2048
    val length = str.length
    var pos = 0
    while(pos < str.length) {
        println("${if (message.isNotEmpty()) "$message " else "" } ${str.substring(pos,
            Integer.min(length, pos + step))}")
        pos += step
    }
    return this
}



fun String.asDate(): LocalDate = LocalDate.parse(this)
