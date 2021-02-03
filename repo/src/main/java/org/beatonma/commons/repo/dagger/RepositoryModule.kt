package org.beatonma.commons.repo.dagger

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import org.beatonma.commons.data.core.room.dao.BillDao
import org.beatonma.commons.data.core.room.dao.ConstituencyDao
import org.beatonma.commons.data.core.room.dao.DivisionDao
import org.beatonma.commons.data.core.room.dao.MemberDao
import org.beatonma.commons.data.core.room.dao.UserDao
import org.beatonma.commons.repo.remotesource.api.CommonsApi
import org.beatonma.commons.repo.remotesource.api.UkParliamentApi
import org.beatonma.commons.repo.repository.BillRepository
import org.beatonma.commons.repo.repository.ConstituencyRepository
import org.beatonma.commons.repo.repository.DivisionRepository
import org.beatonma.commons.repo.repository.MemberRepository
import org.beatonma.commons.repo.repository.MemberRepositoryImpl
import org.beatonma.commons.repo.repository.SocialRepository
import org.beatonma.commons.repo.repository.UserRepository
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
        commonsApi: CommonsApi,
        ukParliamentApi: UkParliamentApi,
        billDao: BillDao,
    ): BillRepository = BillRepository(commonsApi, ukParliamentApi, billDao)

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
