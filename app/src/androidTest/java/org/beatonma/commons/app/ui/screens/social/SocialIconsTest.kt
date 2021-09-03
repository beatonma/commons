package org.beatonma.commons.app.ui.screens.social

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertContentDescriptionContains
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.test.filters.MediumTest
import org.beatonma.commons.app.TestTheme
import org.beatonma.commons.sampledata.SampleSocialContent
import org.beatonma.commons.snommoc.models.social.SocialContent
import org.beatonma.commons.test.extensions.assertions.shouldbe
import org.beatonma.commons.testcompose.test.ComposeTest
import org.beatonma.commons.theme.CommonsTheme
import org.junit.Test

@MediumTest
class SocialIconsTest : ComposeTest() {
    private val iconsTag = SocialTestTag.Icons

    @Test
    fun collapsedLayout_isCorrect() {
        withContent {
            TestLayout(
                state = rememberSocialUiState(SocialUiState.Collapsed)
            )
        }

        perform {
            onNodeWithTag(iconsTag)
                .assertExists()
                .assertIsDisplayed()
                .assertHasClickAction()
                .assertContentDescriptionContains("3", substring = true) // Comment count
                .assertContentDescriptionContains("4", substring = true) // Upvotes
                .assertContentDescriptionContains("7", substring = true) // Downvotes
        }
    }

    @Test
    fun expandedLayout_isCorrect() {
        withContent {
            TestLayout(
                state = rememberSocialUiState(SocialUiState.Expanded)
            )
        }

        perform {
            onNodeWithTag(iconsTag)
                .onChildren()
                .assertCountEquals(3)

            onNodeWithTag(iconsTag)
                .onChildAt(0)
                .assertIsDisplayed()
                .assertHasClickAction()
                .assertContentDescriptionContains("3", substring = true)

            onNodeWithTag(iconsTag)
                .onChildAt(1)
                .assertIsDisplayed()
                .assertHasClickAction()
                .assertContentDescriptionContains("4", substring = true)

            onNodeWithTag(iconsTag)
                .onChildAt(2)
                .assertIsDisplayed()
                .assertHasClickAction()
                .assertContentDescriptionContains("7", substring = true)
        }
    }

    @Test
    fun onClickUpvote_isCorrect() {
        val socialContent = mutableStateOf(SampleSocialContent)

        withContent {
            TestLayout(
                state = rememberSocialUiState(SocialUiState.Expanded),
                socialContent = socialContent,
            )
        }

        perform {
            onNodeWithTag(iconsTag)
                .onChildAt(1)
                .performClick()

            socialContent.value.ayeVotes shouldbe 5
        }
    }

    @Test
    fun onClickDownvote_isCorrect() {
        val socialContent = mutableStateOf(SampleSocialContent)

        withContent {
            TestLayout(
                state = rememberSocialUiState(SocialUiState.Expanded),
                socialContent = socialContent,
            )
        }

        perform {
            onNodeWithTag(iconsTag)
                .onChildAt(2)
                .performClick()

            socialContent.value.noVotes shouldbe 8
        }
    }

    @Test
    fun onClick_whenCollapsed_shouldExpand() {
        val state = mutableStateOf(SocialUiState.Collapsed)
        withContent {
            TestLayout(
                state = state,
            )
        }

        perform {
            onNodeWithTag(iconsTag)
                .performClick()

            state.value shouldbe SocialUiState.Expanded
        }
    }

    @Composable
    fun TestLayout(
        state: MutableState<SocialUiState>,
        socialContent: MutableState<SocialContent> = remember { mutableStateOf(SampleSocialContent) },
    ) {
        val actions = SocialActions(
            onVoteUpClick = {
                val votes = socialContent.value.votes

                socialContent.value = socialContent.value.copy(
                    votes = votes.copy(
                        aye = votes.aye + 1
                    )
                )
            },
            onVoteDownClick = {
                val votes = socialContent.value.votes

                socialContent.value = socialContent.value.copy(
                    votes = votes.copy(
                        no = votes.no + 1
                    )
                )
            },
        )

        CommonsTheme {
            SocialIcons(
                socialContent.value,
                state,
                actions,
            )
        }
    }

    @Composable
    private fun SocialIcons(
        socialContent: SocialContent,
        state: MutableState<SocialUiState>,
        actions: SocialActions,
    ) {
        TestTheme {
            SocialIcons(
                socialContent,
                state.value,
                socialTheme(),
                actions,
                Modifier,
                collapseAction = { state.value = SocialUiState.Collapsed },
                expandAction = { state.value = SocialUiState.Expanded },
            )
        }
    }
}
