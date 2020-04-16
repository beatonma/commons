package org.beatonma.commons.data

import androidx.lifecycle.LiveData
import org.beatonma.commons.androidTest.getOrAwaitValue

private const val TAG = "BaseRoomTest"

abstract class BaseRoomDaoTest<D>: BaseRoomTest() {
    abstract val testPukId: Int
    abstract val dao: D

    /**
     * Run the given function on the dao with the standard PUK as defined in test data
     */
    protected fun <T> daoTest(func: D.(Int) -> LiveData<T>, testBlock: T.() -> Unit) =
        dao.func(testPukId).getOrAwaitValue { testBlock(this) }
}
