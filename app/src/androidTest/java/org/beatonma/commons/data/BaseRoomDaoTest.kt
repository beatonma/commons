package org.beatonma.commons.data

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.beatonma.commons.androidTest.getOrAwaitValue

private const val TAG = "BaseRoomTest"

abstract class BaseRoomDaoTest<D>: BaseRoomTest() {
    abstract val testPukId: Int
    abstract val dao: D

    /**
     * Run the given function on the dao with the standard PUK as defined in test data
     */
    @Deprecated("Dao methods should return a [Flow], not [LiveData]")
    protected fun <T> daoLiveDataTest(func: D.(Int) -> LiveData<T>, testBlock: T.() -> Unit) =
        dao.func(testPukId).getOrAwaitValue { testBlock(this) }

    protected fun <T> daoTest(func: D.(Int) -> Flow<T>, testBlock: T.() -> Unit) = runBlocking {
        dao.func(testPukId).first().run { testBlock(this) }
    }
}
