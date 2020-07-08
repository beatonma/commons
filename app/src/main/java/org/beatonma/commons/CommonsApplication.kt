package org.beatonma.commons

import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.hilt.work.HiltWorkerFactory
import androidx.lifecycle.AndroidViewModel
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.HiltAndroidApp
import org.beatonma.commons.app.SystemTheme
import org.beatonma.commons.data.cleanup.CommonsCleanupWorker
import org.beatonma.commons.kotlin.extensions.getPrefs
import javax.inject.Inject
import android.content.res.Configuration as ResConfiguration
import androidx.work.Configuration as WorkConfiguration

private const val TAG = "CommonsApp"

@HiltAndroidApp
class CommonsApplication : Application(), WorkConfiguration.Provider {

    @Inject lateinit var workerFactory: HiltWorkerFactory

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
        Log.d(TAG, "Scheduling database cleanup with WorkManager")

        WorkManager.getInstance(this)
            .enqueue(
                OneTimeWorkRequestBuilder<CommonsCleanupWorker>()
                    .setConstraints(
                        Constraints.Builder()
                            .setRequiresBatteryNotLow(true)
                            .build()
                    )
                    .build()
            )
    }

    override fun getWorkManagerConfiguration() =
        WorkConfiguration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
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
