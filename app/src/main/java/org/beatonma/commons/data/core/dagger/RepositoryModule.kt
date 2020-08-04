package org.beatonma.commons.data.core.dagger

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import org.beatonma.commons.data.CommonsRemoteDataSource
import org.beatonma.commons.data.core.repository.*
import org.beatonma.commons.data.core.room.dao.*
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class RepositoryModule {
    @Suppress("UNUSED_PARAMETER")  // context is used to retrieve DAO instances.
    @Singleton
    @Provides
    fun providesMemberRepository(
        @ApplicationContext context: Context,
        remoteSource: CommonsRemoteDataSource,
        memberDao: MemberDao,
    ): MemberRepository = MemberRepositoryImpl(remoteSource, memberDao)

    @Suppress("UNUSED_PARAMETER")  // context is used to retrieve DAO instances.
    @Singleton
    @Provides
    fun providesConstituencyRepository(
        @ApplicationContext context: Context,
        remoteSource: CommonsRemoteDataSource,
        constituencyDao: ConstituencyDao,
        memberDao: MemberDao
    ): ConstituencyRepository = ConstituencyRepository(remoteSource, constituencyDao, memberDao)

    @Suppress("UNUSED_PARAMETER")  // context is used to retrieve DAO instances.
    @Singleton
    @Provides
    fun providesDivisionRepository(
        @ApplicationContext context: Context,
        remoteSource: CommonsRemoteDataSource,
        divisionDao: DivisionDao,
    ): DivisionRepository = DivisionRepository(remoteSource, divisionDao)

    @Suppress("UNUSED_PARAMETER")  // context is used to retrieve DAO instances.
    @Singleton
    @Provides
    fun providesBillRepository(
        @ApplicationContext context: Context,
        remoteSource: CommonsRemoteDataSource,
        billDao: BillDao,
    ): BillRepository = BillRepository(remoteSource, billDao)

    @Suppress("UNUSED_PARAMETER")  // context is used to retrieve DAO instances.
    @Singleton
    @Provides
    fun providesUserRepository(
        @ApplicationContext context: Context,
        remoteSource: CommonsRemoteDataSource,
        userDao: UserDao,
    ): UserRepository = UserRepository(remoteSource, userDao)

    @Suppress("UNUSED_PARAMETER")  // context is used to retrieve DAO instances.
    @Singleton
    @Provides
    fun providesSocialRepository(
        @ApplicationContext context: Context,
        remoteSource: CommonsRemoteDataSource,
    ): SocialRepository = SocialRepository(remoteSource)

}
