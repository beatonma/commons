package org.beatonma.commons.data.core.dagger

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import org.beatonma.commons.CommonsApplication
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
)
class CommonsDataModule(val application: CommonsApplication) {

    @Provides
    fun providesContext(): Context = application.applicationContext

    @Provides
    fun providesApplciation(): CommonsApplication = application

    @Singleton @Provides
    fun provideCommonsDatabase(context: Context): CommonsDatabase = Room.databaseBuilder(
            context.applicationContext,
            CommonsDatabase::class.java,
            COMMONS_DB_FILENAME
        ).fallbackToDestructiveMigration()
        .build()

    @Suppress("UNUSED_PARAMETER")  // context is used to retrieve DAO instances.
    @Singleton @Provides
    fun providesMemberRepository(
        context: Context,
        commonsRemoteDataSource: CommonsRemoteDataSource,
        memberDao: MemberDao,
        divisionDao: DivisionDao
    ): MemberRepository = MemberRepository(commonsRemoteDataSource, memberDao, divisionDao)

    @Suppress("UNUSED_PARAMETER")  // context is used to retrieve DAO instances.
    @Singleton @Provides
    fun providesConstituencyRepository(
        context: Context,
        commonsRemoteDataSource: CommonsRemoteDataSource,
        constituencyDao: ConstituencyDao,
        memberDao: MemberDao
    ): ConstituencyRepository = ConstituencyRepository(commonsRemoteDataSource, constituencyDao, memberDao)

    @Suppress("UNUSED_PARAMETER")  // context is used to retrieve DAO instances.
    @Singleton @Provides
    fun providesDivisionRepository(
        context: Context,
        commonsRemoteDataSource: CommonsRemoteDataSource,
        divisionDao: DivisionDao,
    ): DivisionRepository = DivisionRepository(commonsRemoteDataSource, divisionDao)

    @Suppress("UNUSED_PARAMETER")  // context is used to retrieve DAO instances.
    @Singleton @Provides
    fun providesBillRepository(
        context: Context,
        commonsRemoteDataSource: CommonsRemoteDataSource,
        billDao: BillDao,
    ): BillRepository = BillRepository(commonsRemoteDataSource, billDao)

    @Suppress("UNUSED_PARAMETER")  // context is used to retrieve DAO instances.
    @Singleton @Provides
    fun providesUserRepository(
        context: Context,
        commonsRemoteDataSource: CommonsRemoteDataSource,
        userDao: UserDao,
    ): UserRepository = UserRepository(commonsRemoteDataSource, userDao)

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
}
