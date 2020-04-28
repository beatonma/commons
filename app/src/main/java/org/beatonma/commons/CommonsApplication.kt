package org.beatonma.commons

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import org.beatonma.commons.app.SystemTheme
import org.beatonma.commons.app.dagger.AppInjector
import org.beatonma.commons.data.core.repository.CommonsRepository
import org.beatonma.lib.util.kotlin.extensions.getPrefs
import javax.inject.Inject


class CommonsApplication : Application(), HasAndroidInjector {
    @Inject lateinit var androidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var repository: CommonsRepository

    override fun onCreate() {
        super.onCreate()

        AppInjector.init(this)

        initTheme()
    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

    private fun initTheme() {
        val forceNight = getPrefs(SystemTheme.THEME_PREFS)
            .getBoolean(SystemTheme.UI_FORCE_DARK_THEME, false)

        val mode = if (forceNight) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        val current = AppCompatDelegate.getDefaultNightMode()

        if (mode != current) {
            AppCompatDelegate.setDefaultNightMode(mode)
        }
    }
}

val Activity.commonsApp: CommonsApplication
    get() = application as CommonsApplication

val Context.commonsApp: CommonsApplication?
    get() = applicationContext as? CommonsApplication

val Fragment.commonsApp: CommonsApplication?
    get() = context?.commonsApp
