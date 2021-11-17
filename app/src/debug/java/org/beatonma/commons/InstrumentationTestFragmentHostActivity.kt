package org.beatonma.commons

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint

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
}
