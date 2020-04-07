package org.beatonma.commons.androidTest

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.beatonma.lib.util.kotlin.extensions.dump
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
    block: T.() -> Unit? = {},
): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(o: T?) {
            val self = this
            o.dump("onChanged: ")
            data = o
            latch.countDown()
            runBlocking(Dispatchers.Main) { this@getOrAwaitValue.removeObserver(self) }
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
