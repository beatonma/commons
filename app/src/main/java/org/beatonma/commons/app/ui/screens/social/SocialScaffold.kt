package org.beatonma.commons.app.ui.screens.social

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.beatonma.commons.compose.components.text.ScreenTitle
import org.beatonma.commons.compose.layout.stickyHeaderWithInsets
import org.beatonma.commons.compose.modifiers.wrapContentHeight
import org.beatonma.commons.compose.modifiers.wrapContentOrFillWidth
import org.beatonma.commons.compose.padding.endOfContent
import org.beatonma.commons.compose.padding.padding
import org.beatonma.commons.compose.util.positiveDp
import org.beatonma.commons.core.extensions.lerpBetween
import org.beatonma.commons.core.extensions.progressIn
import org.beatonma.commons.core.extensions.reversed
import org.beatonma.commons.preview.PreviewProviders
import org.beatonma.commons.snommoc.models.social.SocialContent
import org.beatonma.commons.themed.animation
import kotlin.math.max

private const val DefaultParallax: Float = .33f

@Composable
fun SocialScaffold(
    title: String,
    aboveSocial: (@Composable (Int) -> Unit)?,
    modifier: Modifier = Modifier,
    belowSocial: (@Composable () -> Unit)? = null,
    scrollState: LazyListState = rememberLazyListState(),
    socialContent: SocialContent = LocalSocialContent.current,
    actions: SocialActions = LocalSocialActions.current,
    theme: SocialTheme = LocalSocialTheme.current,
    parallax: Float = DefaultParallax,
    content: LazyListScope.(visibilityModifier: Modifier) -> Unit,
) {
    var state by rememberSocialUiState()

    SocialScaffold(
        {
            ScreenTitle(
                title,
                textAlign = if (state.isCollapsed) TextAlign.Start else TextAlign.Center
            )
        },
        state,
        { state = it },
        aboveSocial,
        belowSocial,
        modifier,
        scrollState,
        socialContent,
        actions,
        theme,
        parallax,
        content
    )
}

@Composable
fun SocialScaffold(
    title: (@Composable () -> Unit)?,
    aboveSocial: (@Composable (Int) -> Unit)?,
    modifier: Modifier = Modifier,
    belowSocial: (@Composable () -> Unit)? = null,
    scrollState: LazyListState = rememberLazyListState(),
    socialContent: SocialContent = LocalSocialContent.current,
    actions: SocialActions = LocalSocialActions.current,
    theme: SocialTheme = LocalSocialTheme.current,
    parallax: Float = DefaultParallax,
    content: LazyListScope.(visibilityModifier: Modifier) -> Unit,
) {
    var state by rememberSocialUiState()

    SocialScaffold(
        title,
        state,
        { state = it },
        aboveSocial,
        belowSocial,
        modifier,
        scrollState,
        socialContent,
        actions,
        theme,
        parallax,
        content
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SocialScaffold(
    title: (@Composable () -> Unit)?,
    socialUiState: SocialUiState,
    onStateChange: (SocialUiState) -> Unit,
    aboveSocial: (@Composable (Int) -> Unit)?,
    belowSocial: (@Composable () -> Unit)?,
    modifier: Modifier = Modifier,
    scrollState: LazyListState = rememberLazyListState(),
    socialContent: SocialContent = LocalSocialContent.current,
    actions: SocialActions = LocalSocialActions.current,
    theme: SocialTheme = LocalSocialTheme.current,
    parallax: Float = DefaultParallax,
    content: LazyListScope.(visibilityModifier: Modifier) -> Unit,
) {
    val metrics = rememberHeaderMetrics()

    val socialExpansion by socialUiState.animateExpansionAsState()
    val primaryContentVisibility = socialExpansion.reversed()

    val isSocialContentVisible = socialExpansion > 0f
    val isPrimaryContentVisible = primaryContentVisibility > 0f

    BackHandler(enabled = socialUiState.isExpanded) {
        onStateChange(socialUiState.previousState)
    }

    CompositionLocalProvider(
        LocalContentColor provides theme.contentColor(socialExpansion)
    ) {
        LazyColumn(
            modifier.fillMaxSize(),
            state = scrollState,
        ) {

            val primaryContentModifier = Modifier
                .clipToBounds()
                .alpha(primaryContentVisibility)

            if (aboveSocial != null && isPrimaryContentVisible) {
                item {
                    Box(
                        primaryContentModifier
                            .wrapContentHeight(primaryContentVisibility)
                            .graphicsLayer(translationY = scrollState.firstVisibleItemScrollOffset * parallax)
                    ) {
                        aboveSocial(scrollState.firstVisibleItemScrollOffset)
                    }
                }
            }

            stickyHeaderWithInsets(
                scrollState,
                "social_header",
            ) { _, headerModifier ->
                val headerHeight by animation.animateIntAsState(
                    if (socialUiState.isCollapsed) metrics.primaryHeaderHeight else metrics.socialHeaderHeight
                )

                HeaderLayout(
                    headerHeight,
                    onHeaderHeightChange = { height ->
                        when (socialExpansion) {
                            0f -> metrics.primaryHeaderHeight = height
                            1f -> metrics.socialHeaderHeight = height
                        }
                    },
                    modifier = Modifier
                        .background(theme.backgroundColor(socialExpansion))
                        .fillMaxWidth()
                        .then(headerModifier)
                ) {
                    HeaderContent(
                        socialUiState,
                        onStateChange,
                        socialContent,
                        theme,
                        actions,
                        socialExpansion,
                        title,
                        belowSocial,
                        isPrimaryContentVisible,
                        primaryContentModifier,
                    )
                }
            }

            if (isPrimaryContentVisible) {
                content(primaryContentModifier)
            }

            if (isSocialContentVisible) {
                val visibility = socialExpansion.progressIn(.7f, 1f)
                if (visibility >= .7f) {
                    CommentList(
                        comments = socialContent.comments,
                        modifier = Modifier
                            .wrapContentHeight(visibility)
                            .alpha(visibility),
                        onClick = actions.onCommentClick,
                    )
                }
            }

            endOfContent()
        }
    }

    if (isSocialContentVisible) {
        CreateCommentUi(Modifier.alpha(socialExpansion.progressIn(.7f, 1f)))
    }
}

@Composable
private fun HeaderContent(
    socialUiState: SocialUiState,
    onStateChange: (SocialUiState) -> Unit,
    socialContent: SocialContent,
    theme: SocialTheme,
    actions: SocialActions,
    socialExpansion: Float,
    title: @Composable (() -> Unit)?,
    belowSocial: @Composable (() -> Unit)?,
    isPrimaryContentVisible: Boolean,
    @SuppressLint("ModifierParameter") primaryContentModifier: Modifier,
) {
    Column {
        Box(
            Modifier
                .padding(
                    horizontal = socialExpansion.lerpBetween(0F, 32F).positiveDp,
                    top = socialExpansion.lerpBetween(8F, 16F).positiveDp,
                    bottom = socialExpansion.lerpBetween(0f, 16f).positiveDp,
                )
                .wrapContentOrFillWidth(socialExpansion),
            contentAlignment = Alignment.Center
        ) {
            title?.invoke()
        }

        SocialIcons(
            socialContent = socialContent,
            state = socialUiState,
            theme = theme,
            actions = actions,
            modifier = Modifier
                .padding(
                    horizontal = socialExpansion.lerpBetween(8F, 32F).positiveDp,
                    vertical = socialExpansion.lerpBetween(8F, 16F).positiveDp
                )
                .wrapContentOrFillWidth(socialExpansion),
            expandAction = { onStateChange(SocialUiState.Expanded) },
            collapseAction = { onStateChange(SocialUiState.Collapsed) },
        )

        if (belowSocial != null && isPrimaryContentVisible) {
            Box(primaryContentModifier) {
                belowSocial()
            }
        }
    }
}

@Composable
private fun HeaderLayout(
    headerHeight: Int,
    onHeaderHeightChange: (Int) -> Unit,
    modifier: Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        content,
        modifier,
    ) { measurables, constraints ->
        val placeables = measurables.map { it.measure(constraints) }
        val height = placeables.sumOf(Placeable::height)

        if (height > headerHeight) {
            onHeaderHeightChange(height)
        }

        layout(
            width = placeables.maxOf(Placeable::width),
            height = max(height, headerHeight)
        ) {
            var y = 0
            placeables.forEach {
                it.placeRelative(0, y)
                y += it.height
            }
        }
    }
}

private class HeaderMetrics {
    private val _primaryHeaderHeight: MutableState<Int> = mutableStateOf(0)
    private val _socialHeaderHeight: MutableState<Int> = mutableStateOf(0)

    /**
     * Header height when state is [SocialUiState.isCollapsed]
     */
    var primaryHeaderHeight by _primaryHeaderHeight

    /**
     * Header height when state is [SocialUiState.isExpanded]
     */
    var socialHeaderHeight by _socialHeaderHeight
}

@Composable
private fun rememberHeaderMetrics() =
    remember { HeaderMetrics() }


@Preview
@Composable
private fun NewSocialScaffoldPreview() {
    PreviewProviders {
        SocialScaffold(
            title = { Text("Title") },
            aboveSocial = {
                Text("Above social")//, Modifier.alpha(scroll))
            },
            belowSocial = {
                Text("Below social")//, Modifier.wrapContentHeight(scroll))
            },

            ) { visibilityModifier ->
            items((0..10).toList()) { index ->
                Text("Example item #$index", visibilityModifier.padding(32.dp))
            }
        }
    }
}
