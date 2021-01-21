package org.beatonma.commons.kotlin.extensions

import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.net.toUri
import org.beatonma.commons.R

@Deprecated("Glide has been removed in favour of Coil. Also we are moving towards Compose so ImageView shouldn't be used either.")
fun ImageView.load(url: Uri?, @DrawableRes fallback: Int = R.mipmap.ic_launcher) {
//    Glide.with(this)
//        .load(if (BuildConfig.DEBUG) R.mipmap.politician_generic else url)
//        .fallback(fallback)
//        .into(this)
}

fun ImageView.load(url: String?) = load(url?.toUri())
