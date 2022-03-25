package org.beatonma.commons.repo.dagger

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.beatonma.commons.data.core.room.dao.BillDao
import org.beatonma.commons.data.core.room.dao.ConstituencyDao
import org.beatonma.commons.data.core.room.dao.DivisionDao
import org.beatonma.commons.data.core.room.dao.MemberDao
import org.beatonma.commons.data.core.room.dao.UserDao
import org.beatonma.commons.repo.remotesource.api.CommonsApi
import org.beatonma.commons.repo.repository.BillRepository
import org.beatonma.commons.repo.repository.ConstituencyRepository
import org.beatonma.commons.repo.repository.DivisionRepository
import org.beatonma.commons.repo.repository.MemberRepository
import org.beatonma.commons.repo.repository.MemberRepositoryImpl
import org.beatonma.commons.repo.repository.SearchRepository
import org.beatonma.commons.repo.repository.SocialRepository
import org.beatonma.commons.repo.repository.UserRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Singleton
    @Provides
    fun providesMemberRepository(
        remoteSource: CommonsApi,
        memberDao: MemberDao,
    ): MemberRepository = MemberRepositoryImpl(remoteSource, memberDao)

    @Singleton
    @Provides
    fun providesConstituencyRepository(
        remoteSource: CommonsApi,
        constituencyDao: ConstituencyDao,
        memberDao: MemberDao,
    ): ConstituencyRepository = ConstituencyRepository(remoteSource, constituencyDao, memberDao)

    @Singleton
    @Provides
    fun providesDivisionRepository(
        remoteSource: CommonsApi,
        divisionDao: DivisionDao,
        memberDao: MemberDao,
    ): DivisionRepository = DivisionRepository(remoteSource, divisionDao, memberDao)

    @Singleton
    @Provides
    fun providesBillRepository(
        commonsApi: CommonsApi,
        billDao: BillDao,
        memberDao: MemberDao,
    ): BillRepository = BillRepository(commonsApi, billDao, memberDao)

    @Singleton
    @Provides
    fun providesUserRepository(
        remoteSource: CommonsApi,
        userDao: UserDao,
    ): UserRepository = UserRepository(remoteSource, userDao)

    @Singleton
    @Provides
    fun providesSocialRepository(
        remoteSource: CommonsApi,
    ): SocialRepository = SocialRepository(remoteSource)

    @Singleton
    @Provides
    fun providesSearchRepository(
        remoteSource: CommonsApi,
    ): SearchRepository = SearchRepository(remoteSource)
}
