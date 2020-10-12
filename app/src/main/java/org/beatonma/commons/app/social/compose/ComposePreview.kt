package org.beatonma.commons.app.social.compose

import androidx.compose.animation.asDisposableClock
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.AnimationClockAmbient
import androidx.ui.tooling.preview.Preview
import org.beatonma.commons.compose.components.WithDesignGridlines
import org.beatonma.commons.compose.modifiers.colorize
import org.beatonma.commons.snommoc.models.social.SocialComment
import org.beatonma.commons.snommoc.models.social.SocialContent
import org.beatonma.commons.snommoc.models.social.SocialVoteType
import org.beatonma.commons.snommoc.models.social.SocialVotes
import org.beatonma.commons.theme.compose.theme.CommonsTheme
import java.time.LocalDateTime

@Composable
@Preview
private fun SocialScaffold2Preview() {
    val socialContent by remember { mutableStateOf(sampleSocialContent) }
    val clock = AnimationClockAmbient.current.asDisposableClock()

    PreviewContext(gridlines = false) {
        Providers(SocialAmbient provides socialContent) {
            Box(Modifier.fillMaxSize()) {
                SocialScaffoldColumn(
                    clock = clock,
                    contentAbove = {
                        Column(Modifier.colorize()) {
                            for (i in 0..10) {
                                Text("Before social content")
                            }
                        }
                    },
                    contentBelow = {
                        Column(Modifier.colorize()) {
                            for (i in 0..10) {
                                Text("After social content")
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
@Preview
private fun CompactSocialPreview() {
    CommonsTheme {
        Surface {
            Column(Modifier.fillMaxWidth()) {
                CompactSocial(onClick = {})
            }
        }
    }
}

@Composable
@Preview
private fun ExpandedSocialPreview() {
    val socialContent by remember { mutableStateOf(sampleSocialContent) }
    PreviewContext(gridlines = false) {
        Providers(SocialAmbient provides socialContent) {
            ExpandedSocial(
                onVoteUpClick = {},
                onVoteDownClick = {},
                onCommentIconClick = {},
                onCommentClick = {},
            )
        }
    }
}

/**
 * Show the given content within a typical app context.
 */
@Composable
fun PreviewContext(gridlines: Boolean = true, content: @Composable () -> Unit) {
    CommonsTheme {
        Surface {
            WithDesignGridlines(enabled = gridlines, content = content)
        }
    }
}

private val sampleSocialContent = SocialContent(
    title = "House of Commons",
    comments = listOf(
        SocialComment(text = "This is a comment",
            username = "fallofmath",
            created = LocalDateTime.now(),
            modified = LocalDateTime.now()),
        SocialComment(text = "This is another comment",
            username = "fallofmath",
            created = LocalDateTime.now(),
            modified = LocalDateTime.now()),
    ),
    votes = SocialVotes(
        aye = 137,
        no = 71,
    ),
    userVote = SocialVoteType.aye
)
