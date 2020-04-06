package org.beatonma.commons.data

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import org.beatonma.commons.data.core.room.CommonsDatabase
import org.junit.Before
import org.junit.Rule
import java.util.concurrent.Executors

private const val TAG = "BaseRoomTest"

abstract class BaseRoomDaoTest<D> {
    lateinit var db: CommonsDatabase
    abstract val dao: D

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    open fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
                context,
                CommonsDatabase::class.java
            )
            .allowMainThreadQueries()
            .setTransactionExecutor(Executors.newSingleThreadExecutor())
            .build()
    }
}
