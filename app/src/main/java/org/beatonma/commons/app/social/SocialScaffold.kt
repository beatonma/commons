package org.beatonma.commons.app.social

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import org.beatonma.commons.app.preview.ProvideLocalForPreview
import org.beatonma.commons.compose.animation.withEasing
import org.beatonma.commons.compose.components.collapsibleheader.CollapsibleHeaderLayout
import org.beatonma.commons.compose.modifiers.design.colorize
import org.beatonma.commons.compose.modifiers.wrapContentHeight
import org.beatonma.commons.compose.modifiers.wrapContentOrFillHeight
import org.beatonma.commons.compose.util.positiveDp
import org.beatonma.commons.core.extensions.lerpBetween
import org.beatonma.commons.core.extensions.progressIn
import org.beatonma.commons.core.extensions.reversed
import org.beatonma.commons.snommoc.models.social.SocialContent
import org.beatonma.commons.theme.compose.Layer

private typealias HeaderBlock = @Composable (headerExpansion: HeaderExpansion, Modifier) -> Unit
private typealias SocialBlock = @Composable (socialProgress: SocialExpansion, headerExpansion: HeaderExpansion, social: @Composable () -> Unit) -> Unit

/**
 * Inline class for type safety to reduce confusion when coordinating animations
 * with different sources.
 */
inline class HeaderExpansion(val value: Float)

/**
 * Inline class for type safety to reduce confusion when coordinating animations
 * with different sources.
 */
inline class SocialExpansion(val value: Float)

/**
 * A scaffold for using the social UI in a [CollapsibleHeaderLayout].
 */
@Composable
fun StickySocialScaffold(
    modifier: Modifier = Modifier,
    socialContent: SocialContent = LocalSocialContent.current,
    state: MutableState<SocialUiState> = LocalSocialUiState.current,
    actions: SocialActions = LocalSocialActions.current,
    scrollEnabled: Boolean = true,
    onScrollStarted: () -> Unit = {},
    onScrollStopped: () -> Unit = {},
    theme: SocialTheme = LocalSocialTheme.current,
    aboveSocial: HeaderBlock,
    social: SocialBlock? = null,
    belowSocial: HeaderBlock? = null,
    snapToStateAt: Float = 0.3F,
    lazyListContent: LazyListScope.() -> Unit,
) {
    CompositionLocalProvider(
        LocalSocialTheme provides theme,
    ) {
        CollapsibleHeaderLayout(
            collapsingHeader = { headerExpansion: Float ->
                CollapsibleSocialHeader(
                    socialContent = socialContent,
                    headerExpansion = HeaderExpansion(headerExpansion),
                    modifier = modifier,
                    state = state,
                    actions = actions,
                    theme = theme,
                    aboveSocial = aboveSocial,
                    socialBlock = social,
                    belowSocial = belowSocial,
                )
            },
            lazyListContent = lazyListContent,
            scrollEnabled = scrollEnabled,
            onScrollStarted = onScrollStarted,
            onScrollFinished = onScrollStopped,
            snapToStateAt = snapToStateAt,
        )
    }
}

@Composable
private fun CollapsibleSocialHeader(
    socialContent: SocialContent,
    headerExpansion: HeaderExpansion,
    modifier: Modifier,
    state: MutableState<SocialUiState>,
    actions: SocialActions,
    theme: SocialTheme,
    aboveSocial: HeaderBlock,
    socialBlock: SocialBlock?,
    belowSocial: HeaderBlock?,
) {
    val progress by animateFloatAsState(if (state.value == SocialUiState.Collapsed) 0F else 1F)
    val reverseProgress = progress.reversed()

    Column(
        modifier.fillMaxWidth()
    ) {
        if (progress != 1F) {
            aboveSocial(
                headerExpansion,
                Modifier
                    .wrapContentHeight(reverseProgress)
                    .alpha(reverseProgress.progressIn(0F, 0.6F))
                    .zIndex(Layer.Bottom)
            )
        }

        val social = @Composable {
            SocialContentUi(
                modifier = Modifier
                    .zIndex(Layer.High)
                    .wrapContentOrFillHeight(progress.withEasing(FastOutSlowInEasing))
                    .padding(
                        start = progress.lerpBetween(12F, 0F).positiveDp,
                        bottom = progress.lerpBetween(8F, 0F).positiveDp
                    )
                    .testTag("social_content"),
                socialContent = socialContent,
                state = state.value,
                onStateChange = { state.value = it },
                actions = actions,
                theme = theme,
            )
        }

        if (socialBlock == null) {
            social()
        }
        else {
            socialBlock(SocialExpansion(reverseProgress), headerExpansion, social)
        }

        if (belowSocial != null && progress != 1F) {
            belowSocial(
                headerExpansion,
                Modifier
                    .wrapContentHeight(reverseProgress)
                    .alpha(reverseProgress.progressIn(0F, 0.6F))
                    .zIndex(Layer.Bottom)
            )
        }
    }
}

@Preview
@Composable
private fun CollapsibleSocialPreview() {
    val listItems = (1..100).toList()
    ProvideLocalForPreview {
        StickySocialScaffold(
            aboveSocial = { headerExpansion, modifier ->
                Text(
                    "Above social content",
                    modifier
                        .height(28.dp + (60.dp * headerExpansion.value))
                )
            },
            belowSocial = { headerExpansion, modifier ->
                Text(
                    "Below social content",
                    modifier
                        .height(60.dp * headerExpansion.value)
                )
            },
            lazyListContent = {
                items(listItems) { item ->
                    Text(
                        "$item",
                        Modifier
                            .padding(4.dp)
                            .colorize()
                    )
                }
            }
        )
    }
}
