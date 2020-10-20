package org.beatonma.commons.app.social.compose

import androidx.compose.animation.asDisposableClock
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Box
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.drawOpacity
import androidx.compose.ui.platform.AnimationClockAmbient
import androidx.compose.ui.unit.*
import androidx.ui.tooling.preview.Preview
import org.beatonma.commons.compose.components.Avatar
import org.beatonma.commons.compose.components.DEV_AVATAR
import org.beatonma.commons.compose.components.WithDesignGridlines
import org.beatonma.commons.snommoc.models.social.SocialComment
import org.beatonma.commons.snommoc.models.social.SocialContent
import org.beatonma.commons.snommoc.models.social.SocialVoteType
import org.beatonma.commons.snommoc.models.social.SocialVotes
import org.beatonma.commons.theme.compose.color.MaterialRed400
import org.beatonma.commons.theme.compose.theme.CommonsTheme
import java.time.LocalDateTime
import kotlin.math.roundToInt

@Composable
@Preview
private fun SocialScaffold2Preview() {
    val socialContent by remember { mutableStateOf(sampleSocialContent) }
    val clock = AnimationClockAmbient.current.asDisposableClock()

    PreviewContext(gridlines = false) {
        Providers(SocialAmbient provides socialContent) {
            Column() {
                Text("Above", Modifier.padding(8.dp))

                Box {
                    SocialScaffoldColumn(
                        Modifier.fillMaxWidth(),
                        clock = clock,
                        contentBefore = { modifier ->
                            Column(modifier.background(MaterialRed400)) {
//                                Avatar(DEV_AVATAR,
//                                    Modifier.drawOpacity(0.8F)
//                                        .fillMaxWidth()
//                                        .aspectRatio(1F)
//                                )
//                                        .colorize("BEFORE"))

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
//                                    .colorize("AFTER"))
//                        Column(Modifier.colorize()) {
//                            for (i in 0..10) {
//                                Text("After social content")
//                            }
//                        }
                        }
                    )
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


fun Modifier.wrapContentHeight(progress: Float, align: Alignment = Alignment.Center): Modifier {
    return this.then(WrapContentModifier(progress, align)  { size, layoutDirection ->
        align.align(size, layoutDirection)
    })
}


private data class WrapContentModifier(
    private val progress: Float,
    private val alignment: Any,
    private val alignmentCallback: (IntSize, LayoutDirection) -> IntOffset
) : LayoutModifier {
    override fun MeasureScope.measure(
        measurable: Measurable,
        constraints: Constraints
    ): MeasureScope.MeasureResult {
        val wrappedConstraints = Constraints(
            minWidth = constraints.minWidth,
            minHeight = constraints.minHeight,
            maxWidth = constraints.maxWidth,
            maxHeight = constraints.maxHeight
        )
        val placeable = measurable.measure(wrappedConstraints)
        val wrapperWidth = placeable.width.coerceIn(constraints.minWidth, constraints.maxWidth)

        val wrapperHeight = placeable.height.coerceIn(
            constraints.minHeight,
            constraints.minHeight + (progress * (constraints.maxHeight - constraints.minHeight)).roundToInt()
        )

        return layout(
            wrapperWidth,
            wrapperHeight
        ) {
            val position = alignmentCallback(
                IntSize(wrapperWidth - placeable.width, wrapperHeight - placeable.height),
                layoutDirection
            )
            placeable.place(position)
        }
    }
}
