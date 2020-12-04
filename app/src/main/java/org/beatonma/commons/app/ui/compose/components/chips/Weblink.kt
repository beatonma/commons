package org.beatonma.commons.app.ui.compose.components.chips

import android.net.Uri
import androidx.compose.foundation.ScrollableRow
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.beatonma.commons.R
import org.beatonma.commons.compose.components.CollapsibleChip
import org.beatonma.commons.data.core.room.entities.member.WebAddress
import org.beatonma.commons.kotlin.extensions.openUrl

@Composable
fun Weblink(
    weblink: WebAddress,
    modifier: Modifier = Modifier,
) {
    val uri = Uri.parse(weblink.url)

    when (uri.host) {
        "www.twitter.com", "twitter.com" -> {
            TwitterWeblink(getTwitterUsername(uri), modifier)
        }

        "www.facebook.com", "facebook.com" -> {
            FacebookWeblink(getFacebookUsername(uri), modifier)
        }

        else -> {
            Weblink(
                AnnotatedString(getGenericDisplayUrl(uri)),
                weblink.url,
                R.drawable.ic_link,
                modifier = modifier,
            )
        }
    }
}

@Composable
private fun FacebookWeblink(
    username: String,
    modifier: Modifier,
) {
    Weblink(
        AnnotatedString(username),
        "https://facebook.com/$username/",
        R.drawable.ic_facebook,
        modifier = modifier
    )
}

@Composable
private fun TwitterWeblink(
    username: String,
    modifier: Modifier,
) {
    Weblink(
        AnnotatedString("@$username"),
        "https://twitter.com/$username/",
        R.drawable.ic_twitter,
        modifier = modifier
    )
}

@Composable
private fun Weblink(
    displayText: AnnotatedString,
    url: String,
    drawableId: Int,
    modifier: Modifier = Modifier,
    autoCollapse: Long = 2500,
) {
    val context = AmbientContext.current
    CollapsibleChip(displayText, drawableId, R.drawable.ic_close, modifier, autoCollapse) {
        context.openUrl(url)
    }
}

private fun getTwitterUsername(uri: Uri): String = "${uri.path ?: uri}".replace("/", "")
private fun getFacebookUsername(uri: Uri): String = "${uri.path ?: uri}".replace("/", "")

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

@Composable
@Preview
fun WeblinkPreview() {
    val weblink = WebAddress("https://twitter.com/_beatonma", "twitter", 0)
    val links = listOf(weblink, weblink, weblink, weblink)

    MaterialTheme {
        Surface(Modifier.fillMaxSize(), color = Color(1F, .3F, .3F)) {
            Column {
                Weblink(weblink, Modifier.padding(16.dp))

                ScrollableRow {
                    links.forEach { Weblink(it, Modifier.padding(16.dp)) }
                }
            }
        }
    }
}
