package org.beatonma.commons.app.ui.base

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import org.beatonma.commons.kotlin.extensions.getPrefs

object SystemTheme {
    const val THEME_PREFS = "theme"
    const val UI_FORCE_DARK_THEME = "force_dark_theme"
}

abstract class DayNightActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        delegate.applyDayNight()
    }

    fun forceNight(forceNight: Boolean) {
        getPrefs(SystemTheme.THEME_PREFS).edit { putBoolean(SystemTheme.UI_FORCE_DARK_THEME, forceNight) }
        recreate()
    }

    fun toggleForceNight() {
        forceNight(!getSharedPreferences(SystemTheme.THEME_PREFS, Context.MODE_PRIVATE)
            .getBoolean(SystemTheme.UI_FORCE_DARK_THEME, true))
    }
}
