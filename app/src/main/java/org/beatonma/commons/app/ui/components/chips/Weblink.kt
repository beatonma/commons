package org.beatonma.commons.app.ui.components.chips

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.beatonma.commons.R
import org.beatonma.commons.app.ui.components.image.AppIcon
import org.beatonma.commons.app.util.openUrl
import org.beatonma.commons.compose.components.CollapsibleChip
import org.beatonma.commons.data.core.room.entities.member.WebAddress

private const val WIKIPEDIA_PATH_DESCRIPTION = "wikipedia_path"

@Composable
fun Weblink(
    weblink: WebAddress,
    modifier: Modifier = Modifier,
) {
    val uri = Uri.parse(weblink.url)
    val host = uri.host

    when {
        weblink.description == WIKIPEDIA_PATH_DESCRIPTION -> {
            WikipediaWeblink(weblink.url, modifier)
        }

        host == "www.twitter.com" || host == "twitter.com" -> {
            TwitterWeblink(getTwitterUsername(uri), modifier)
        }

        host == "www.facebook.com" || host == "facebook.com" -> {
            FacebookWeblink(getFacebookUsername(uri), modifier)
        }

        else -> {
            Weblink(
                displayText = AnnotatedString(getGenericDisplayUrl(uri)),
                url = weblink.url,
                contentDescription = stringResource(R.string.action_open_url, weblink.url),
                icon = AppIcon.Link,
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
        displayText = AnnotatedString(username),
        url = "https://facebook.com/$username/",
        contentDescription = stringResource(R.string.action_open_facebook_user, username),
        icon = AppIcon.Facebook,
        modifier = modifier
    )
}

@Composable
private fun TwitterWeblink(
    username: String,
    modifier: Modifier,
) {
    Weblink(
        displayText = AnnotatedString("@$username"),
        url = "https://twitter.com/$username/",
        contentDescription = stringResource(R.string.action_open_twitter_user, username),
        drawableId = R.drawable.ic_twitter,
        modifier = modifier
    )
}

@Composable
private fun WikipediaWeblink(
    path: String,
    modifier: Modifier,
) {
    Weblink(
        displayText = AnnotatedString(path),
        url = "https://${stringResource(R.string.url_wikipedia_language_code)}.wikipedia.org/wiki/$path/",
        contentDescription = stringResource(R.string.action_open_wikipedia_page, path),
        drawableId = R.drawable.ic_wikipedia,
        modifier = modifier,
    )
}

@Composable
private fun Weblink(
    displayText: AnnotatedString,
    url: String,
    contentDescription: String,
    drawableId: Int,
    modifier: Modifier = Modifier,
    autoCollapse: Long = 2500,
) {
    CollapsibleChip(
        text = displayText,
        contentDescription = contentDescription,
        drawableId = drawableId,
        modifier = modifier,
        autoCollapse = autoCollapse,
        confirmAction = openUrl(url),
    )
}

@Composable
private fun Weblink(
    displayText: AnnotatedString,
    url: String,
    contentDescription: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    autoCollapse: Long = 2500,
) {
    CollapsibleChip(
        text = displayText,
        contentDescription = contentDescription,
        icon = icon,
        modifier = modifier,
        autoCollapse = autoCollapse,
        confirmAction = openUrl(url)
    )
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

                LazyRow {
                    items(links) {
                        Weblink(it, Modifier.padding(16.dp))
                    }
                }
            }
        }
    }
}
