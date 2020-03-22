package org.beatonma.commons

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.fragment.app.Fragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import org.beatonma.commons.app.dagger.AppInjector
import org.beatonma.commons.data.core.CommonsRepository
import javax.inject.Inject

class CommonsApplication : Application(), HasAndroidInjector {
    @Inject lateinit var androidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var repository: CommonsRepository

    override fun onCreate() {
        super.onCreate()
        AppInjector.init(this)
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector
}

val Activity.commonsApp: CommonsApplication
    get() = application as CommonsApplication

val Context.commonsApp: CommonsApplication?
    get() = applicationContext as? CommonsApplication

val Fragment.commonsApp: CommonsApplication?
    get() = context?.commonsApp
