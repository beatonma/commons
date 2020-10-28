package org.beatonma.commons.app.social.compose

import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawOpacity
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import org.beatonma.commons.app.ui.compose.components.Avatar
import org.beatonma.commons.app.ui.compose.components.DEV_AVATAR
import org.beatonma.commons.compose.components.WithDesignGridlines
import org.beatonma.commons.snommoc.models.social.SocialComment
import org.beatonma.commons.snommoc.models.social.SocialContent
import org.beatonma.commons.snommoc.models.social.SocialVoteType
import org.beatonma.commons.snommoc.models.social.SocialVotes
import org.beatonma.commons.theme.compose.color.MaterialRed400
import org.beatonma.commons.theme.compose.theme.CommonsTheme
import java.time.LocalDateTime

@Composable
@Preview
private fun SocialScaffold2Preview() {
    val socialContent by remember { mutableStateOf(sampleSocialContent) }

    PreviewContext(gridlines = false) {
        Providers(SocialAmbient provides socialContent) {
            Column {
                Text("Above", Modifier.padding(8.dp))

                Box {
                    SocialScaffoldColumn(
                        Modifier.fillMaxWidth(),
                        contentBefore = { modifier ->
                            Column(modifier.background(MaterialRed400)) {
                                for (i in 0..10) {
                                    Text("Before social content")
                                }
                            }
                        },
                        contentAfter = { modifier ->
                            Avatar(DEV_AVATAR,
                                modifier.drawOpacity(0.8F)
                                    .fillMaxWidth()
                                    .aspectRatio(1F)
                            )
                        }
                    )
                }
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
