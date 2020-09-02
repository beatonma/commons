package org.beatonma.commons.data.dao

import android.content.Context
import androidx.annotation.CallSuper
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.runBlocking
import org.beatonma.commons.data.core.room.CommonsDatabase
import org.beatonma.commons.test.extensions.util.awaitValue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import java.util.concurrent.Executors

private const val TAG = "BaseRoomTest"

abstract class BaseRoomDaoTest<D> {
    abstract val dao: D

    lateinit var db: CommonsDatabase
    val context = ApplicationProvider.getApplicationContext<Context>()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()


    @CallSuper
    @Before
    open fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            context,
            CommonsDatabase::class.java
        )
            .allowMainThreadQueries()
            .setTransactionExecutor(Executors.newSingleThreadExecutor())
            .build()
    }

    @CallSuper
    @After
    open fun tearDown() {
        db.close()
    }

    fun <T> runInsert(insertFunc: suspend D.(T) -> Unit, block: () -> T) {
        runBlocking {
            insertFunc.invoke(
                dao,
                block.invoke())
        }
    }

    fun <T> runQuery(queryFunc: suspend () -> Flow<T>, block: T.() -> Unit) {
        runBlocking {
            queryFunc.invoke().awaitValue().single().run {
                block()
            }
        }
    }
}
