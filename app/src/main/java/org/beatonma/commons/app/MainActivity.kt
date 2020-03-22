package org.beatonma.commons.app

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import dagger.android.AndroidInjection
import dagger.android.DispatchingAndroidInjector
import org.beatonma.commons.R
import javax.inject.Inject

class MainActivity: DayNightActivity() {
    @Inject
    lateinit var androidInjector: DispatchingAndroidInjector<Any>

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_navhost_main)

        navController = findNavController(R.id.nav_host_fragment)
    }
}
