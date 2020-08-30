package org.beatonma.commons.app.signin

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import org.beatonma.commons.R
import org.beatonma.commons.kotlin.extensions.stringCompat

interface SignInHost {
    fun showSignInDialog(activity: AppCompatActivity) {
        val manager = activity.supportFragmentManager
        val tag = activity.stringCompat(R.string.fragment_signin)

        val fragment = (
                activity.supportFragmentManager.findFragmentByTag(tag)
                    ?: SignInFragment()
                ) as? DialogFragment
        fragment?.show(manager, tag)
    }

    fun enableSignIn()
    fun disableSignIn()
}
