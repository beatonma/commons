package org.beatonma.commons.repo.dagger

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import org.beatonma.commons.data.core.room.dao.*
import org.beatonma.commons.repo.CommonsApi
import org.beatonma.commons.repo.repository.*
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class RepositoryModule {
    @Suppress("UNUSED_PARAMETER")  // context is used to retrieve DAO instances.
    @Singleton
    @Provides
    fun providesMemberRepository(
        @ApplicationContext context: Context,
        remoteSource: CommonsApi,
        memberDao: MemberDao,
    ): MemberRepository = MemberRepositoryImpl(remoteSource, memberDao)

    @Suppress("UNUSED_PARAMETER")  // context is used to retrieve DAO instances.
    @Singleton
    @Provides
    fun providesConstituencyRepository(
        @ApplicationContext context: Context,
        remoteSource: CommonsApi,
        constituencyDao: ConstituencyDao,
        memberDao: MemberDao
    ): ConstituencyRepository = ConstituencyRepository(remoteSource, constituencyDao, memberDao)

    @Suppress("UNUSED_PARAMETER")  // context is used to retrieve DAO instances.
    @Singleton
    @Provides
    fun providesDivisionRepository(
        @ApplicationContext context: Context,
        remoteSource: CommonsApi,
        divisionDao: DivisionDao,
    ): DivisionRepository = DivisionRepository(remoteSource, divisionDao)

    @Suppress("UNUSED_PARAMETER")  // context is used to retrieve DAO instances.
    @Singleton
    @Provides
    fun providesBillRepository(
        @ApplicationContext context: Context,
        remoteSource: CommonsApi,
        billDao: BillDao,
    ): BillRepository = BillRepository(remoteSource, billDao)

    @Suppress("UNUSED_PARAMETER")  // context is used to retrieve DAO instances.
    @Singleton
    @Provides
    fun providesUserRepository(
        @ApplicationContext context: Context,
        remoteSource: CommonsApi,
        userDao: UserDao,
    ): UserRepository = UserRepository(remoteSource, userDao)

    @Suppress("UNUSED_PARAMETER")  // context is used to retrieve DAO instances.
    @Singleton
    @Provides
    fun providesSocialRepository(
        @ApplicationContext context: Context,
        remoteSource: CommonsApi,
    ): SocialRepository = SocialRepository(remoteSource)
}
