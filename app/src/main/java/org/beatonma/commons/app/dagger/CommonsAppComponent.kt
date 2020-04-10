package org.beatonma.commons.app.dagger

import android.content.Context
import com.squareup.moshi.Moshi
import dagger.Component
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import org.beatonma.commons.CommonsApplication
import org.beatonma.commons.app.ExperimentalFrontPageFragment
import org.beatonma.commons.app.MainActivity
import org.beatonma.commons.app.bill.BillProfileFragment
import org.beatonma.commons.app.division.DivisionProfileFragment
import org.beatonma.commons.app.memberprofile.MemberProfileFragment
import org.beatonma.commons.data.CommonsRemoteDataSource
import org.beatonma.commons.data.core.CommonsRepository
import org.beatonma.commons.data.core.dagger.CommonsDataModule
import org.beatonma.commons.data.core.room.CommonsDatabase
import org.beatonma.commons.network.retrofit.CommonsService
import org.beatonma.commons.network.retrofit.GenericUrlService
import org.beatonma.commons.network.retrofit.TwfyService
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        CommonsDataModule::class,
        MainActivityModule::class,
        ViewModelModule::class
    ]
)
interface CommonsAppComponent {
    fun context(): Context
    fun commonsApplication(): CommonsApplication
    fun commonsDatabase(): CommonsDatabase
    fun commonsRepository(): CommonsRepository
    fun commonsRemoteDataSource(): CommonsRemoteDataSource

    fun moshi(): Moshi
    fun commonsNetworkService(): CommonsService
    fun twfyNetworkService(): TwfyService
    fun genericUrlService(): GenericUrlService

    fun inject(application: CommonsApplication)
}


@Module(includes = [
    FragmentsModule::class
])
abstract class MainActivityModule {
    @ActivityScope
    @ContributesAndroidInjector
    abstract fun providesActivity(): MainActivity
}


@Module
abstract class FragmentsModule {
    @FragmentScope
    @ContributesAndroidInjector
    abstract fun providesExperimentalFrontPageFragment(): ExperimentalFrontPageFragment

    @ExperimentalStdlibApi
    @FragmentScope
    @ContributesAndroidInjector
    abstract fun providesMemberProfileFragment(): MemberProfileFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun providesBillProfileFragment(): BillProfileFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun providesDivisionProfileFragment(): DivisionProfileFragment
}
