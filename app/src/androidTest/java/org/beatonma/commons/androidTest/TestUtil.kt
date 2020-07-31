package org.beatonma.commons.androidTest

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.beatonma.commons.kotlin.extensions.clipToLength
import org.beatonma.lib.testing.kotlin.extensions.assertions.assertEquals
import java.time.LocalDate
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/**
 * Hide flow emissions until [latchCount] emissions have been collected, then emit the final result.
 */
fun <T> Flow<T>.awaitValue(
    latchCount: Int = 1,
    timeout: Long = 1000,
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
    timeout: Long = 1000,
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

/**
 * Run each assertion block on the value in the corresponding position of this list.
 * i.e. The first block will run on the first item, etc.
 * An assertion function must be provided for each list item.
 */
fun <T> List<T>.assertEach(vararg assertionBlocks: (T) -> Unit) {
    assertionBlocks.size.assertEquals(this.size, message = "An assertion function must be provided for each list item.")

    forEachIndexed { index, obj ->
        assertionBlocks[index].invoke(obj)
    }
}


/**
 * Dump value to log (with optional message) and return `this` value
 * Convenience for `.also { println("$it") }`
 * If the resulting string is very long it will be broken into chunks.
 * Should only be used for debugging purposes
 */
fun <T: Any?> T.dump(message: String = "", maxLength: Int = -1): T {
    when (this) {
        is Collection<*> -> {
            if (message.isNotEmpty()) println("$message [$size]:")
            forEach { item -> item.dump("  ", maxLength) }
        }

        is Array<*> -> {
            if (message.isNotEmpty()) println("$message [$size]:")
            forEach { item -> item.dump("  ", maxLength) }
        }

        is IntArray -> {
            println("${if (message.isNotEmpty()) "$message " else ""}[$size]: ${ joinToString(separator = ", ") { "$it" } }".clipToLength(maxLength))
        }

        else -> {
            val str = toString().clipToLength(maxLength)
            val step = 2048
            val length = str.length
            var pos = 0
            while(pos < str.length) {
                println("${if (message.isNotEmpty()) "$message " else "" } ${str.substring(pos,
                    Integer.min(length, pos + step)
                )}")
                pos += step
            }
        }
    }

    return this
}



fun String.asDate(): LocalDate = LocalDate.parse(this)
