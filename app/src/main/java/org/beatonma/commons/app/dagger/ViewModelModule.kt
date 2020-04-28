package org.beatonma.commons.app.dagger

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import org.beatonma.commons.app.bill.BillProfileViewModel
import org.beatonma.commons.app.constituency.ConstituencyDetailViewModel
import org.beatonma.commons.app.division.DivisionDetailViewModel
import org.beatonma.commons.app.featured.FeaturedContentViewModel
import org.beatonma.commons.app.memberprofile.MemberProfileViewModel
import org.beatonma.commons.app.search.SearchViewModel
import org.beatonma.commons.app.signin.SignInViewModel
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton
import kotlin.reflect.KClass

@Singleton
class ViewModelFactory @Inject constructor(
    private val viewModels: MutableMap<Class<out ViewModel>, Provider<ViewModel>>
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        viewModels[modelClass]?.get() as T
}

@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(AnnotationRetention.RUNTIME)
@MapKey
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)

@Module
abstract class ViewModelModule {
    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(FeaturedContentViewModel::class)
    internal abstract fun bindFeaturedContentViewModel(viewModel: FeaturedContentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MemberProfileViewModel::class)
    internal abstract fun bindMemberProfileViewModel(viewModel: MemberProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BillProfileViewModel::class)
    internal abstract fun bindBillProfileViewModel(viewModel: BillProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DivisionDetailViewModel::class)
    internal abstract fun bindDivisionProfileViewModel(viewModel: DivisionDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ConstituencyDetailViewModel::class)
    internal abstract fun bindConstituencyProfileViewModel(viewModel: ConstituencyDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SignInViewModel::class)
    internal abstract fun bindSignInViewModel(viewModel: SignInViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    internal abstract fun bindSearchViewModel(viewModel: SearchViewModel): ViewModel
}
