package org.beatonma.commons.kotlin.extensions

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.core.net.toUri
import androidx.navigation.Navigation

fun View.navigateTo(@IdRes navigationAction: Int, args: Bundle? = null) {
    Navigation.findNavController(this)
        .navigate(
            navigationAction,
            args
        )
}

fun View.navigateTo(url: String) {
    Navigation.findNavController(this)
        .navigate(url.toUri())
}

fun View.navigateTo(url: Uri) {
    Navigation.findNavController(this)
        .navigate(url)
}
