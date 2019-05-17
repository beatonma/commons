package org.beatonma.commons.data.core.dagger

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import org.beatonma.commons.data.COMMONS_DB_FILENAME
import org.beatonma.commons.data.CommonsDatabase
import javax.inject.Singleton

@Module
class CommonsCoreModule {
    @Provides
    @Singleton
    fun commonsDataSource(context: Context) = Room.databaseBuilder(
        context.applicationContext,
        CommonsDatabase::class.java,
        COMMONS_DB_FILENAME
    ).build()
}
