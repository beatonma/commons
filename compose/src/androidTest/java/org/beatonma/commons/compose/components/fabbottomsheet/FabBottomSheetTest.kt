package org.beatonma.commons.compose.components.fabbottomsheet

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import androidx.test.filters.MediumTest
import com.google.accompanist.insets.ProvideWindowInsets
import org.beatonma.commons.compose.TestTag
import org.beatonma.commons.test.extensions.assertions.shouldbe
import org.beatonma.commons.testcompose.test.ComposeTest
import org.junit.Test

@MediumTest
class FabBottomSheetTest: ComposeTest() {
    private val contentTag = "content"
    private val fabText = "Click me"
    private val fabClickLabel = "Open content"


    @Test
    fun fabLayout_isCorrect() {
        withContent {
            TestLayout()
        }

        perform {
            onNodeWithTag(TestTag.Fab)
                .assertExists()
                .assertIsDisplayed()
                .assertHasClickAction()

            onNodeWithText(fabText)
                .assertExists()
                .assertIsDisplayed()

            onNodeWithTag(contentTag)
                .assertDoesNotExist()

            onNodeWithTag(TestTag.ModalScrim)
                .assertDoesNotExist()

            onNodeWithTag(TestTag.FabBottomSheet)
                .assertDoesNotExist()
        }
    }

    @Test
    fun bottomSheetLayout_isCorrect() {
        val state = mutableStateOf(FabBottomSheetState.BottomSheet)

        withContent {
            TestLayout(state)
        }

        perform {
            onNodeWithTag(TestTag.FabBottomSheet)
                .assertIsDisplayed()

            onNodeWithTag(contentTag)
                .assertExists()
                .assertIsDisplayed()
                .assertHeightIsEqualTo(300.dp)

            onNodeWithTag(TestTag.Fab)
                .assertDoesNotExist()
        }
    }

    /**
     * Whatever is behind the bottomsheet should not be clickable.
     */
    @Test
    fun bottomSheet_shouldBeOpaqueToTouchEvents() {
        val state = mutableStateOf(FabBottomSheetState.BottomSheet)
        val dismissed = mutableStateOf(false)

        withContent {
            TestLayout(state, dismissed = dismissed)
        }

        perform {
            onNodeWithTag(TestTag.FabBottomSheet)
                .performClick()

            dismissed.value shouldbe false
        }
    }

    @Test
    fun clickOnModalScrim_shouldCallOnDismiss() {
        val state = mutableStateOf(FabBottomSheetState.BottomSheet)
        val dismissed = mutableStateOf(false)

        withContent {
            TestLayout(state, dismissed = dismissed)
        }

        perform {
            onNodeWithTag(TestTag.ModalScrim)
                .performClick()

            dismissed.value shouldbe true
        }
    }

    @Composable
    fun TestLayout(
        state: MutableState<FabBottomSheetState> = rememberFabBottomSheetState(),
        dismissed: MutableState<Boolean> = remember { mutableStateOf(false) },
    ) {
        ProvideWindowInsets {
            Box {
                FabBottomSheet(
                    state = state,
                    fabClickLabel = fabClickLabel,
                    onDismiss = { dismissed.value = true },
                    fabContent = {
                        FabText(fabText, it)
                    },
                    bottomSheetContent = {
                        BottomSheetText(progress = it) {
                            Spacer(
                                Modifier
                                    .fillMaxWidth()
                                    .height(300.dp)
                                    .testTag(contentTag)
                            )
                        }
                    },
                )
            }
        }
    }
}
