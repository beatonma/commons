package org.beatonma.commons.repo.dagger

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import org.beatonma.commons.repo.CommonsApi
import org.beatonma.commons.repo.CommonsRemoteDataSource
import org.beatonma.commons.snommoc.CommonsService
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class DataSourceModule {

    @Singleton
    @Provides
    fun providesCommonsRemoteDataSource(commonsService: CommonsService): CommonsApi =
        CommonsRemoteDataSource(commonsService)
}
