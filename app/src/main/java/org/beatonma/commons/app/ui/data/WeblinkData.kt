package org.beatonma.commons.app.ui.data

import android.net.Uri
import com.google.android.material.chip.Chip
import org.beatonma.commons.R
import org.beatonma.commons.kotlin.extensions.drawableCompat
import org.beatonma.commons.kotlin.extensions.openUrl

data class WeblinkData(
    val url: String
) {
    val displayText: String
    private val icon: Int

    fun bindTo(chip: Chip) {
        chip.apply {
            text = displayText
            chipIcon = drawableCompat(icon)
            setOnClickListener { chip.context.openUrl(url) }
        }
    }

    init {
        val uri = Uri.parse(url)
        when (uri.host) {
            "www.twitter.com", "twitter.com" -> {
                displayText = getTwitterHandle(uri)
                icon = R.drawable.ic_twitter
            }

            "www.facebook.com", "facebook.com" -> {
                displayText = getFacebookHandle(uri)
                icon = R.drawable.ic_facebook
            }

            else -> {
                displayText = getGenericDisplayUrl(uri)
                icon = getIcon(uri)
            }
        }
    }

    private fun getTwitterHandle(uri: Uri): String {
        return "@${uri.path ?: uri}".replace("/", "")
    }

    private fun getFacebookHandle(uri: Uri): String {
        return "@${uri.path ?: uri}".replace("/", "")
    }

    private fun getGenericDisplayUrl(uri: Uri): String {
        val display = when (uri.scheme) {
            "http", "https" -> {
                uri.toString()
                    .replace(uri.scheme ?: "", "")
                    .replace("://", "")
            }
            else -> {
                uri.toString()
            }
        }
        return display
            .removePrefix("www.")
            .removeSuffix("/")
    }

    private fun getIcon(uri: Uri): Int = when(uri.scheme) {
        "https" -> R.drawable.ic_https
        "http" -> R.drawable.ic_http
        else -> 0
    }
}
