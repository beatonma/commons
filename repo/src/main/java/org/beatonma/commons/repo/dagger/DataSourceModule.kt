package org.beatonma.commons.repo.dagger

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import org.beatonma.commons.repo.remotesource.CommonsRemoteSource
import org.beatonma.commons.repo.remotesource.UkParliamentRemoteSource
import org.beatonma.commons.repo.remotesource.api.CommonsApi
import org.beatonma.commons.repo.remotesource.api.UkParliamentApi
import org.beatonma.commons.snommoc.CommonsService
import org.beatonma.commons.ukparliament.api.UkParliamentService
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class DataSourceModule {

    @Singleton
    @Provides
    fun providesCommonsRemoteSource(service: CommonsService): CommonsApi =
        CommonsRemoteSource(service)

    @Singleton
    @Provides
    fun providesUkParliamentRemoteSource(service: UkParliamentService): UkParliamentApi =
        UkParliamentRemoteSource(service)
}
