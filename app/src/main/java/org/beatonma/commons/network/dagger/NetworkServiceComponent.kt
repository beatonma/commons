package org.beatonma.commons.network.dagger

import dagger.Component
import org.beatonma.commons.app.MpActivity
import org.beatonma.commons.network.HttpClientModule
import org.beatonma.commons.network.retrofit.CommonsService
import org.beatonma.commons.network.retrofit.TwfyService
import org.beatonma.commons.network.retrofit.WikiService
import javax.inject.Singleton

@Singleton
@Component(modules = [HttpClientModule::class, NetworkServiceModule::class, RetrofitModule::class])
interface NetworkServiceComponent {
    fun wikiService(): WikiService
    fun commonsNetworkService(): CommonsService
    fun twfyNetworkService(): TwfyService
    fun inject(activity: MpActivity)
}
