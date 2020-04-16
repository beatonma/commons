package org.beatonma.commons.data

import android.content.Context
import androidx.annotation.CallSuper
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import org.beatonma.commons.data.core.room.CommonsDatabase
import org.junit.Before
import org.junit.Rule
import java.util.concurrent.Executors

abstract class BaseRoomTest {
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
}
