package org.beatonma.commons.app.ui.compose.components.chips

import android.net.Uri
import androidx.compose.foundation.ScrollableRow
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import androidx.ui.tooling.preview.Preview
import org.beatonma.commons.R
import org.beatonma.commons.compose.components.CollapsibleChip
import org.beatonma.commons.compose.util.withAnnotatedStyle
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
    val handle = AnnotatedString.Builder().apply {
        withStyle(SpanStyle(letterSpacing = 1.05.sp)) {
            append('@')
            append(username)
        }
    }.toAnnotatedString()

    Weblink(
        handle,
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
    autoCollapse: Long = 2500,
    modifier: Modifier = Modifier,
) {
    val context = ContextAmbient.current
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
        Surface {
            Column {
                Weblink(weblink)

                ScrollableRow {
                    links.forEach { Weblink(it) }
                }

                Text("**Hello** *fallofmath*".withAnnotatedStyle())
                Text("*Hello* **fallofmath**".withAnnotatedStyle())
                Text("**Hello** **fallofmath**".withAnnotatedStyle())
            }
        }
    }
}
