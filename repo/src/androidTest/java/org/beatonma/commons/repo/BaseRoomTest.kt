package org.beatonma.commons.repo

import android.content.Context
import androidx.annotation.CallSuper
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.runBlocking
import org.beatonma.commons.data.core.room.CommonsDatabase
import org.beatonma.commons.repo.result.IoResult
import org.beatonma.commons.repo.result.Success
import org.beatonma.commons.repo.result.onSuccess
import org.beatonma.commons.test.extensions.util.awaitValue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import java.util.concurrent.Executors

abstract class BaseRoomTest {
    lateinit var db: CommonsDatabase
    private val context = ApplicationProvider.getApplicationContext<Context>()

    @get:Rule(order = 1)
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

    fun <T> runQuery(call: suspend () -> Flow<IoResult<T>>, block: (T) -> Unit) {
        runBlocking {
            call()
                .awaitValue { it is Success }
                .single()
                .onSuccess(block)
        }
    }
}
