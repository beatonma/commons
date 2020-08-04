package org.beatonma.commons.app.signin

import androidx.appcompat.app.AppCompatActivity
import org.beatonma.commons.R
import org.beatonma.commons.kotlin.extensions.stringCompat

interface SignInHost {
    fun showSignIn(activity: AppCompatActivity) {
        val transaction = activity.supportFragmentManager.beginTransaction()
        SignInFragment().show(transaction, activity.stringCompat(R.string.fragment_signin))
    }
}
