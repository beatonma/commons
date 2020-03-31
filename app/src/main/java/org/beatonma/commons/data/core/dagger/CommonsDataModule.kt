package org.beatonma.commons.data.core.dagger

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import org.beatonma.commons.data.CommonsRemoteDataSource
import org.beatonma.commons.data.core.CommonsRepository
import org.beatonma.commons.data.core.room.COMMONS_DB_FILENAME
import org.beatonma.commons.data.core.room.CommonsDatabase
import org.beatonma.commons.data.core.room.dao.MemberDao
import org.beatonma.commons.network.HttpClientModule
import org.beatonma.commons.network.dagger.MoshiModule
import org.beatonma.commons.network.dagger.NetworkServiceModule
import org.beatonma.commons.network.dagger.RetrofitModule
import org.beatonma.commons.network.retrofit.CommonsService
import javax.inject.Singleton

@Module(
    includes = [
        HttpClientModule::class,
        NetworkServiceModule::class,
        RetrofitModule::class,
        MoshiModule::class
    ]
)
class CommonsDataModule(val context: Context) {
    @Provides
    fun providesContext(): Context = context

    @Singleton
    @Provides
    fun provideCommonsDatabase(context: Context): CommonsDatabase = Room.databaseBuilder(
        context.applicationContext,
        CommonsDatabase::class.java,
        COMMONS_DB_FILENAME
    ).build()

    @Singleton
    @Provides
    fun providesCommonsRepository(
        commonsDatabase: CommonsDatabase,
        commonsRemoteDataSource: CommonsRemoteDataSource
    ): CommonsRepository = CommonsRepository(context, commonsRemoteDataSource, commonsDatabase)

    @Singleton
    @Provides
    fun providesCommonsRemoteDataSource(
        commonsService: CommonsService
    ): CommonsRemoteDataSource = CommonsRemoteDataSource(commonsService)

    @Singleton
    @Provides
    fun providesPeopleDao(commonsDatabase: CommonsDatabase): MemberDao = commonsDatabase.memberDao()
}