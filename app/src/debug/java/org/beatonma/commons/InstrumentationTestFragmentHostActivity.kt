package org.beatonma.commons

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import org.beatonma.commons.app.ui.navigation.BackPressConsumer

private const val FRAGMENT_TAG = "testfragment"

@AndroidEntryPoint
class InstrumentationTestFragmentHostActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_activity_fragmenthost)
    }

    fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment, FRAGMENT_TAG)
        }.commit()
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentByTag(FRAGMENT_TAG)
        if (fragment is BackPressConsumer) {
            if (fragment.onBackPressed()) {
                // Back press consumed by fragment
                return
            }
        }
        super.onBackPressed()
    }
}
