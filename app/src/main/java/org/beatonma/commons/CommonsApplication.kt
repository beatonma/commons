package org.beatonma.commons

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.HiltAndroidApp
import org.beatonma.commons.app.SystemTheme
import org.beatonma.commons.kotlin.extensions.getPrefs
import android.content.res.Configuration as ResConfiguration

private const val TAG = "CommonsApp"

@HiltAndroidApp
class CommonsApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initTheme()
    }

    private fun initTheme() {
        val forceNight = getPrefs(SystemTheme.THEME_PREFS)
            .getBoolean(SystemTheme.UI_FORCE_DARK_THEME, false)

        val mode = if (forceNight) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        val current = AppCompatDelegate.getDefaultNightMode()

        if (mode != current) {
            AppCompatDelegate.setDefaultNightMode(mode)
        }
    }

    fun isNightMode(): Boolean {
        val currentNightMode = resources.configuration.uiMode and ResConfiguration.UI_MODE_NIGHT_MASK
        return when (currentNightMode) {
            ResConfiguration.UI_MODE_NIGHT_YES -> true
            ResConfiguration.UI_MODE_NIGHT_NO -> false
            else -> false
        }
    }

    fun scheduleDatabaseCleanup() {

    }
}

val Activity.commonsApp: CommonsApplication
    get() = application as CommonsApplication

val Context.commonsApp: CommonsApplication
    get() = applicationContext as CommonsApplication

val Fragment.commonsApp: CommonsApplication?
    get() = context?.commonsApp

fun Context.isNightMode() = commonsApp.isNightMode()

val AndroidViewModel.context: Context
    get() = getApplication()

val AndroidViewModel.commonsApp: CommonsApplication
    get() = getApplication() as CommonsApplication
