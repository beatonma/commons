package org.beatonma.commons.data.core.dagger

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import org.beatonma.commons.data.CommonsRemoteDataSource
import org.beatonma.commons.data.CommonsRemoteDataSourceImpl
import org.beatonma.commons.data.core.repository.*
import org.beatonma.commons.data.core.room.COMMONS_DB_FILENAME
import org.beatonma.commons.data.core.room.CommonsDatabase
import org.beatonma.commons.data.core.room.dao.*
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
) @InstallIn(ApplicationComponent::class)
class CommonsDataModule {
    @Singleton @Provides
    fun provideCommonsDatabase(@ApplicationContext context: Context): CommonsDatabase = Room.databaseBuilder(
        context.applicationContext,
        CommonsDatabase::class.java,
        COMMONS_DB_FILENAME
    ).fallbackToDestructiveMigration()
        .build()

    @Suppress("UNUSED_PARAMETER")  // context is used to retrieve DAO instances.
    @Singleton @Provides
    fun providesMemberRepository(
        @ApplicationContext context: Context,
        remoteSource: CommonsRemoteDataSource,
        memberDao: MemberDao,
        divisionDao: DivisionDao
    ): MemberRepository = MemberRepository(remoteSource, memberDao, divisionDao)

    @Suppress("UNUSED_PARAMETER")  // context is used to retrieve DAO instances.
    @Singleton @Provides
    fun providesConstituencyRepository(
        @ApplicationContext context: Context,
        remoteSource: CommonsRemoteDataSource,
        constituencyDao: ConstituencyDao,
        memberDao: MemberDao
    ): ConstituencyRepository = ConstituencyRepository(remoteSource, constituencyDao, memberDao)

    @Suppress("UNUSED_PARAMETER")  // context is used to retrieve DAO instances.
    @Singleton @Provides
    fun providesDivisionRepository(
        @ApplicationContext context: Context,
        remoteSource: CommonsRemoteDataSource,
        divisionDao: DivisionDao,
    ): DivisionRepository = DivisionRepository(remoteSource, divisionDao)

    @Suppress("UNUSED_PARAMETER")  // context is used to retrieve DAO instances.
    @Singleton @Provides
    fun providesBillRepository(
        @ApplicationContext context: Context,
        remoteSource: CommonsRemoteDataSource,
        billDao: BillDao,
    ): BillRepository = BillRepository(remoteSource, billDao)

    @Suppress("UNUSED_PARAMETER")  // context is used to retrieve DAO instances.
    @Singleton @Provides
    fun providesUserRepository(
        @ApplicationContext context: Context,
        remoteSource: CommonsRemoteDataSource,
        userDao: UserDao,
    ): UserRepository = UserRepository(remoteSource, userDao)

    @Suppress("UNUSED_PARAMETER")  // context is used to retrieve DAO instances.
    @Singleton @Provides
    fun providesSocialRepository(
        @ApplicationContext context: Context,
        remoteSource: CommonsRemoteDataSource,
    ): SocialRepository = SocialRepository(remoteSource)

    @Singleton @Provides
    fun providesCommonsRemoteDataSource(
        commonsService: CommonsService,
    ): CommonsRemoteDataSource = CommonsRemoteDataSourceImpl(commonsService)

    @Singleton @Provides
    fun providesPeopleDao(commonsDatabase: CommonsDatabase): MemberDao = commonsDatabase.memberDao()

    @Singleton @Provides
    fun providesBillDao(commonsDatabase: CommonsDatabase): BillDao = commonsDatabase.billDao()

    @Singleton @Provides
    fun provideDivisionDao(commonsDatabase: CommonsDatabase): DivisionDao =
        commonsDatabase.divisionDao()

    @Singleton @Provides
    fun provideConstituencyDao(commonsDatabase: CommonsDatabase): ConstituencyDao =
        commonsDatabase.constituencyDao()

    @Singleton @Provides
    fun providesUserDao(commonsDatabase: CommonsDatabase): UserDao = commonsDatabase.userDao()


    // Database cleanup DAOs
    @Singleton @Provides
    fun providesMemberCleanupDao(commonsDatabase: CommonsDatabase): MemberCleanupDao = commonsDatabase.memberCleanupDao()
}
