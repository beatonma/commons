package org.beatonma.commons.app.social.compose

import androidx.compose.animation.core.FloatPropKey
import androidx.compose.animation.core.transitionDefinition
import androidx.compose.animation.core.tween
import androidx.compose.animation.transition
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import org.beatonma.commons.app.social.State
import org.beatonma.commons.compose.components.WithDesignGridlines
import org.beatonma.commons.compose.layout.linkTo
import org.beatonma.commons.compose.util.update
import org.beatonma.commons.snommoc.models.social.SocialComment
import org.beatonma.commons.snommoc.models.social.SocialContent
import org.beatonma.commons.snommoc.models.social.SocialVoteType
import org.beatonma.commons.snommoc.models.social.SocialVotes
import org.beatonma.commons.theme.compose.color.MaterialAmber800
import org.beatonma.commons.theme.compose.color.MaterialGreen700
import org.beatonma.commons.theme.compose.color.MaterialLightBlue700
import org.beatonma.commons.theme.compose.theme.CommonsTheme
import java.time.LocalDateTime

@Composable
@Preview
private fun AnimatedSocialPreview() {
    val progressKey = remember { FloatPropKey() }
    val socialContent by remember { mutableStateOf(sampleSocialContent) }
    val state = remember { mutableStateOf(State.COLLAPSED) }

    PreviewContext {
        Providers(SocialAmbient provides socialContent) {
            val transitionDef = remember {
                transitionDefinition<State> {
                    state(State.COLLAPSED) {
                        this[progressKey] = 0F
                    }
                    state(State.EXPANDED) {
                        this[progressKey] = 1F
                    }
                    transition(
                        State.COLLAPSED to State.EXPANDED,
                        State.EXPANDED to State.COLLAPSED,
                    ) {
                        progressKey using tween(800)
                    }
                }
            }
            val transitionState = transition(definition = transitionDef, toState = state.value)

            Column {
                Surface(
                    Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                    color = MaterialAmber800
                ) {
                    Text("Above")
                }

                Surface(
                    Modifier
                        .fillMaxWidth()
                        .height(48.dp + (300.dp * transitionState[progressKey]))
                        .clip(RectangleShape)
                ) {
                    AdaptiveSocialContent(
                        onVoteUpClick = { println("voteUp") },
                        onVoteDownClick = { println("voteDown") },
                        onExpandedCommentIconClick = {
                            println("onCommentClick")
                            state.update(State.COLLAPSED)
                        },
                        onCommentClick = {},
                        state = state,
                    )
                }

                Surface(
                    Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                    color = MaterialGreen700
                ) {
                    Text("Below")
                }
            }
        }
    }
}

@Composable
@Preview
fun SocialScaffoldPreview() {
    val socialContent by remember { mutableStateOf(sampleSocialContent) }
    PreviewContext {
        Providers(SocialAmbient provides socialContent) {
            SocialScaffold(
                Modifier.fillMaxWidth(),
            ) { transitionState, socialContainer ->
                val (before, after) = createRefs()

                Surface(
                    Modifier
                        .constrainAs(before) {
                            linkTo(parent, bottom = socialContainer.top)
                        },
                    color = MaterialLightBlue700
                ) {
                    Text("Before social content")
                }

                Surface(
                    Modifier
                        .constrainAs(socialContainer) {
                            start.linkTo(parent.start)
                            top.linkTo(before.bottom)
                        }
                        .height(48.dp + (300.dp * transitionState[progressKey]))
                        .fillMaxWidth(),

                    color = MaterialGreen700
                ) {}

                Surface(
                    Modifier
                        .constrainAs(after) {
                            top.linkTo(socialContainer.bottom)
                        },
                    color = MaterialAmber800
                ) {
                    Text("After social content")
                }
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

@Composable
@Preview
private fun CombinedSocialPreview() {
    var socialContent by remember { mutableStateOf(sampleSocialContent) }
    PreviewContext(gridlines = false) {
        Providers(SocialAmbient provides socialContent) {
            Column {
                SocialContentView(
                    onVoteUpClick = {
                        socialContent = socialContent.copy(
                            userVote = if (socialContent.userVote == SocialVoteType.aye) null else SocialVoteType.aye
                        )
                    },
                    onVoteDownClick = {
                        socialContent = socialContent.copy(
                            userVote = if (socialContent.userVote == SocialVoteType.no) null else SocialVoteType.no
                        )
                    },
                    onExpandedCommentIconClick = {},
                    onCommentClick = {},
                )

                ExpandedSocial(
                    onVoteUpClick = {
                        socialContent = socialContent.copy(
                            userVote = if (socialContent.userVote == SocialVoteType.aye) null else SocialVoteType.aye
                        )
                    },
                    onVoteDownClick = {
                        socialContent = socialContent.copy(
                            userVote = if (socialContent.userVote == SocialVoteType.no) null else SocialVoteType.no
                        )
                    },
                    onCommentIconClick = {},
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
