package org.beatonma.commons.androidTest

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.time.LocalDate
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException


/**
 * Adapted from https://github.com/android/architecture-components-samples/blob/master/LiveDataSample/app/src/test/java/com/android/example/livedatabuilder/util/LiveDataTestUtil.kt
 * The afterObserve block has been replaced with a block that runs using the retrieved data as the receiver.
 *
 * Gets the value of a [LiveData] or waits for it to have one, with a timeout.
 *
 * Use this extension from host-side (JVM) tests. It's recommended to use it alongside
 * `InstantTaskExecutorRule` or a similar mechanism to execute tasks synchronously.
 */
fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS,
    latchCount: Int = 1,
    block: T.() -> Unit? = {},
): T {
    var data: T? = null
    val latch = CountDownLatch(latchCount)
    val observer = object : Observer<T> {
        override fun onChanged(o: T?) {
            val self = this
            o.dump("onChanged: ")
            data = o
            latch.countDown()
            if (latch.count == 0L) {
                runBlocking(Dispatchers.Main) { this@getOrAwaitValue.removeObserver(self) }
            }
        }
    }

    this@getOrAwaitValue.observeForever(observer)

    // Don't wait indefinitely if the LiveData is not set.
    if (!latch.await(time, timeUnit)) {
        this@getOrAwaitValue.removeObserver(observer)
        throw TimeoutException("LiveData value was never set.")
    }

    data!!.block()

    @Suppress("UNCHECKED_CAST")
    return data as T
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
