package org.beatonma.commons.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.base.DayNightActivity
import org.beatonma.commons.app.ui.navigation.BackPressConsumer
import org.beatonma.commons.app.ui.screens.signin.LocalPlatformUserAccountActions
import org.beatonma.commons.app.ui.screens.signin.PlatformUserAccountActions
import org.beatonma.commons.app.ui.screens.signin.UserAccountViewModel
import org.beatonma.commons.app.ui.screens.signin.userProfileActions
import org.beatonma.commons.theme.compose.theme.systemui.setDecorFitsSystemWindows

@AndroidEntryPoint
class MainActivity : DayNightActivity() {
    lateinit var navController: NavController
    private val accountViewModel: UserAccountViewModel by viewModels()
    private lateinit var platformUserAccountActions: PlatformUserAccountActions

    init {
        LocalPlatformUserAccountActions = staticCompositionLocalOf { platformUserAccountActions }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        platformUserAccountActions = userProfileActions(this, accountViewModel)

        setContentView(R.layout.activity_main)
        setDecorFitsSystemWindows(false)

        navController = findNavController(R.id.nav_host_fragment)

        handleIntent(intent)
    }

    override fun onDestroy() {
        commonsApp.scheduleDatabaseCleanup()
        super.onDestroy()
    }

    private fun handleIntent(intent: Intent) {
        println("TODO Handle search intent: handleIntent $intent")
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    override fun onBackPressed() {
        val fragment = getContentFragment()
        val consumedByContent = fragment is BackPressConsumer && fragment.onBackPressed()
        if (!consumedByContent) {
            super.onBackPressed()
        }
    }

    /**
     * FragmentManager instance that is responsible for navController content
     */
    private fun getNavigationFragmentManager() =
        supportFragmentManager.primaryNavigationFragment?.childFragmentManager

    /**
     * Return the fragment that is currently the navController destination.
     */
    private fun getContentFragment(): Fragment? =
        getNavigationFragmentManager()?.primaryNavigationFragment  // Foreground fragment
}
