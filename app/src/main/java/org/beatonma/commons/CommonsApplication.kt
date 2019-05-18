package org.beatonma.commons

import android.app.Activity
import android.app.Application
import org.beatonma.commons.app.dagger.AppModule
import org.beatonma.commons.app.dagger.DaggerAppComponent
import org.beatonma.commons.data.CommonsDatabase
import javax.inject.Inject

class CommonsApplication : Application() {
    @Inject
    lateinit var db: CommonsDatabase

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
            .inject(this)
    }
}

val Activity.commonsApp
    get() = application as CommonsApplication
