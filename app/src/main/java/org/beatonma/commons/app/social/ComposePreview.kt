package org.beatonma.commons.app.social

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.ui.tooling.preview.Preview
import org.beatonma.commons.compose.components.WithDesignGridlines
import org.beatonma.commons.snommoc.models.social.SocialComment
import org.beatonma.commons.snommoc.models.social.SocialContent
import org.beatonma.commons.snommoc.models.social.SocialVoteType
import org.beatonma.commons.snommoc.models.social.SocialVotes
import org.beatonma.commons.theme.compose.color.MaterialAmber800
import org.beatonma.commons.theme.compose.theme.CommonsTheme
import java.time.LocalDateTime

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
    PreviewContext {
        provideSocialContent(sampleSocialContent) {
            ExpandedSocial(
                onVoteUpClick = {},
                onVoteDownClick = {},
                onCommentClick = {},
            )
        }
    }
}

@Composable
@Preview
private fun CombinedSocialPreview() {
    PreviewContext(gridlines = false) {
        provideSocialContent(sampleSocialContent) {
            Column {
                CompactSocial(
                    modifier = Modifier.fillMaxWidth().background(MaterialAmber800),
                    onClick = {},
                )

                ExpandedSocial(
                    onVoteUpClick = {},
                    onVoteDownClick = {},
                    onCommentClick = {},
                )
            }
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
