package org.beatonma.commons.app

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.core.content.edit
import dagger.android.support.DaggerAppCompatActivity
import org.beatonma.lib.util.kotlin.extensions.getPrefs

private const val THEME_PREFS = "theme"
private const val UI_FORCE_DARK_THEME = "force_dark_theme"

abstract class DayNightActivity: DaggerAppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val forceNight = getPrefs(THEME_PREFS).getBoolean(UI_FORCE_DARK_THEME, false)

        AppCompatDelegate.setDefaultNightMode(if (forceNight) MODE_NIGHT_YES else MODE_NIGHT_FOLLOW_SYSTEM)
        delegate.applyDayNight()
    }

    fun forceNight(forceNight: Boolean) {
        getPrefs(THEME_PREFS).edit { putBoolean(UI_FORCE_DARK_THEME, forceNight) }
        recreate()
    }

    fun toggleForceNight() {
        forceNight(!getSharedPreferences(THEME_PREFS, Context.MODE_PRIVATE)
            .getBoolean(UI_FORCE_DARK_THEME, true))
    }
}
