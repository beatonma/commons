package org.beatonma.commons.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

private const val TAG = "BaseRoomTest"

abstract class BaseRoomDaoTest<D>: BaseRoomTest() {
    abstract val testPukId: Int
    abstract val dao: D

    /**
     * Run the given function on the dao with the standard PUK as defined in test data
     */
    protected fun <T> daoTest(func: D.(Int) -> Flow<T>, testBlock: T.() -> Unit) = runBlocking {
        dao.func(testPukId).first().run { testBlock(this) }
    }
}
