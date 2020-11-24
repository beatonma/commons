package org.beatonma.commons.app

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.navigation.BackPressConsumer
import org.beatonma.commons.commonsApp
import org.beatonma.commons.kotlin.extensions.PermissionResults
import org.beatonma.commons.theme.compose.theme.systemui.setDecorFitsSystemWindows

@AndroidEntryPoint
class MainComposeActivity : DayNightActivity() {
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        // TODO Handle search intent.
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIntent(intent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        val results = PermissionResults(permissions, grantResults)
        when (requestCode) {
            // TODO Location or whatever
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
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
