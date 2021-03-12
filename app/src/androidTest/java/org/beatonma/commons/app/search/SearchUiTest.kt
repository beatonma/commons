package org.beatonma.commons.app.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onLast
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.filters.MediumTest
import dev.chrisbanes.accompanist.insets.ProvideWindowInsets
import org.beatonma.commons.compose.animation.ExpandCollapseState
import org.beatonma.commons.compose.animation.rememberExpandCollapseState
import org.beatonma.commons.compose.util.rememberListOf
import org.beatonma.commons.sampledata.SampleSearchResults
import org.beatonma.commons.snommoc.models.search.MemberSearchResult
import org.beatonma.commons.snommoc.models.search.SearchResult
import org.beatonma.commons.test.extensions.assertions.shouldbe
import org.beatonma.commons.testcompose.test.ComposeTest
import org.junit.Test

@MediumTest
class SearchUiTest: ComposeTest() {
    private val iconTag = "search_icon"
    private val resultTag = "search_result"
    private val fieldTag = "search_field"
    private val modalScrimTag = "modal_scrim"

    private val results = SampleSearchResults

    override fun withContent(content: @Composable () -> Unit) =
        composeTestRule.apply { setContent(content) }

    private fun ComposeTestRule.performClickOnIcon() {
        onNodeWithTag(iconTag)
            .performClick()
    }

    @Test
    fun collapsedLayout_isCorrect() {
        withContent {
            TestLayout()
        }

        perform {
            onNodeWithTag(iconTag)
                .assertIsDisplayed()
                .assertHasClickAction()

            onNodeWithTag(fieldTag)
                .assertDoesNotExist()

            onNodeWithTag(resultTag)
                .assertDoesNotExist()
        }
    }

    @Test
    fun expandedLayout_isCorrect() {
        withContent {
            TestLayout(state = rememberExpandCollapseState(ExpandCollapseState.Expanded))
        }

        perform {
            onNodeWithTag(fieldTag)
                .assertIsDisplayed()
                .assertTextEquals("")
                .assertIsFocused()
        }
    }

    @Test
    fun clickOnIcon_togglesState() {
        withContent {
            TestLayout()
        }

        perform {
            performClickOnIcon()

            onNodeWithTag(fieldTag)
                .assertIsDisplayed()

            performClickOnIcon()

            onNodeWithTag(fieldTag)
                .assertDoesNotExist()
        }
    }

    @Test
    fun layout_withSearchResults_isCorrect() {
        check(results.size == 2)

        withContent {
            TestLayout(
                state = rememberExpandCollapseState(ExpandCollapseState.Expanded),
                results = remember { mutableStateOf(results) },
            )
        }

        perform {
            onNodeWithText("Michael", substring = true)
                .assertExists()
                .assertIsDisplayed()

            onAllNodesWithTag("search_result")
                .assertCountEquals(2)
        }
    }

    @Test
    fun click_onModalScrim_shouldCollapseState() {
        withContent {
            TestLayout(
                state = rememberExpandCollapseState(ExpandCollapseState.Expanded)
            )
        }

        perform {
            onNodeWithTag(modalScrimTag)
                .performClick()

            onNodeWithTag(fieldTag)
                .assertDoesNotExist()
        }
    }

    @Test
    fun onSubmit_isTriggeredOnTextEntry() {
        var counter by mutableStateOf(0)

        withContent {
            TestLayout(
                state = rememberExpandCollapseState(ExpandCollapseState.Expanded),
                onSubmit = { counter += 1 },
            )
        }

        perform {
            onNodeWithTag(fieldTag)
                .performTextInput("a")
            onNodeWithTag(fieldTag)
                .performTextInput("b")
            onNodeWithTag(fieldTag)
                .performTextInput("c")
            onNodeWithTag(fieldTag)
                .performTextInput("d")
            onNodeWithTag(fieldTag)
                .performTextInput("e")
            onNodeWithTag(fieldTag)
                .performTextInput("f")

            counter shouldbe 6
        }
    }

    @Test
    fun onClickMember_isTriggered_whenMemberClicked() {
        var clickedName by mutableStateOf("")
        withContent {
            TestLayout(
                state = rememberExpandCollapseState(ExpandCollapseState.Expanded),
                results = rememberListOf(results),
                onClickMember = { clickedName = it.name }
            )
        }

        perform {
            onAllNodesWithTag(resultTag)
                .onFirst()
                .performClick()

            clickedName shouldbe "Michael Beaton"

            onAllNodesWithTag(resultTag)
                .onLast()
                .performClick()

            clickedName shouldbe "MemberName"
        }
    }

    @Composable
    fun TestLayout(
        state: MutableState<SearchUiState> = rememberExpandCollapseState(),
        results: MutableState<List<SearchResult>> = rememberListOf(),
        onSubmit: (String) -> Unit = {},
        onClickMember: (MemberSearchResult) -> Unit = {},
    ) {
        val searchActions = remember {
            SearchActions(
                onSubmit = onSubmit,
                onClickMember = onClickMember,
            )
        }

        CompositionLocalProvider(
            LocalSearchActions provides searchActions,
        ) {
            ProvideWindowInsets {
                SearchUi(
                    results = results.value,
                    state = state,
                )
            }
        }
    }
}
