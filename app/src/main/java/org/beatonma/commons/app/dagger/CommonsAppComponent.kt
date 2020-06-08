package org.beatonma.commons.app.dagger

import dagger.Component
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import org.beatonma.commons.CommonsApplication
import org.beatonma.commons.app.ExperimentalFrontPageFragment
import org.beatonma.commons.app.MainActivity
import org.beatonma.commons.app.bill.BillDetailFragment
import org.beatonma.commons.app.constituency.ConstituencyDetailFragment
import org.beatonma.commons.app.constituency.ConstituencyElectionResultsFragment
import org.beatonma.commons.app.division.DivisionDetailFragment
import org.beatonma.commons.app.memberprofile.MemberProfileFragment
import org.beatonma.commons.app.signin.SignInFragment
import org.beatonma.commons.app.social.CompactSocialFragment
import org.beatonma.commons.app.social.ExpandedSocialFragment
import org.beatonma.commons.data.core.dagger.CommonsDataModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        CommonsDataModule::class,
        UserModule::class,
        LocationModule::class,
        MainActivityModule::class,
        ViewModelModule::class
    ]
)
interface CommonsAppComponent {
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
    abstract fun providesMemberDetailFragment(): MemberProfileFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun providesBillDetailFragment(): BillDetailFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun providesDivisionDetailFragment(): DivisionDetailFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun providesConstituencyDetailFragment(): ConstituencyDetailFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun providesConstituencyElectionResultsFragment(): ConstituencyElectionResultsFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun providesSignInFragment(): SignInFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun providesCompactSocialFragment(): CompactSocialFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun providesExpandedSocialFragment(): ExpandedSocialFragment
}
