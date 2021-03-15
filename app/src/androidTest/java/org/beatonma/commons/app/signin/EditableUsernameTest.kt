package org.beatonma.commons.app.signin

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.filters.MediumTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.beatonma.commons.sampledata.SampleUserToken
import org.beatonma.commons.testcompose.test.ComposeTest
import org.junit.Test

@MediumTest
class EditableUsernameTest: ComposeTest() {
    private val readOnlyUsernameTag = "readonly_username"
    private val makeEditableTag = "action_make_editable"
    private val editableFieldTag = "validated_text"
    private val testRenameSuccessfulTag = "rename_successful"
    private val actionRequestRenameTag = "action_request_rename"
    private val actionCancelRenameTag = "action_cancel_rename"
    private val feedbackTextTag = "feedback_text"

    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher = TestCoroutineDispatcher()

    override fun withContent(content: @Composable () -> Unit) =
        composeTestRule.apply { setContent(content) }

    @Test
    fun readOnlyLayout_isCorrect() {
        withContent {
            TestLayout()
        }

        perform {
            onNodeWithTag(EditableState.ReadOnly)
                .assertIsDisplayed()

            onNodeWithTag(readOnlyUsernameTag)
                .assertIsDisplayed()

            onNodeWithTag(makeEditableTag)
                .assertIsDisplayed()

            onNodeWithTag(EditableState.Editable)
                .assertDoesNotExist()

            onNodeWithTag(EditableState.AwaitingResult)
                .assertDoesNotExist()

            onNodeWithText(SampleUserToken.username)
                .assertIsDisplayed()
        }
    }

    @Test
    fun editableLayout_isCorrect() {
        withContent {
            TestLayout(remember { mutableStateOf(EditableState.Editable) })
        }

        perform {
            onNodeWithTag(EditableState.ReadOnly)
                .assertDoesNotExist()

            onNodeWithTag(EditableState.Editable)
                .assertIsDisplayed()

            onNodeWithTag(EditableState.AwaitingResult)
                .assertDoesNotExist()

            onNodeWithText(SampleUserToken.username)
                .assertIsDisplayed()
                .assertIsFocused()
        }
    }



    @Test
    fun editableLayout_isEditable() {
        withContent {
            TestLayout(remember { mutableStateOf(EditableState.Editable) })
        }

        perform {
            onNodeWithTag(editableFieldTag).run {
                performTextInput("_3")
                assertTextEquals("PREVIEW_3")
            }
        }
    }

    @Test
    fun awaitingResultLayout_isCorrect() {
        withContent {
            TestLayout(
                remember { mutableStateOf(EditableState.AwaitingResult) }
            )
        }

        perform {
            onNodeWithTag(EditableState.ReadOnly)
                .assertDoesNotExist()

            onNodeWithTag(EditableState.Editable)
                .assertDoesNotExist()

            onNodeWithTag(EditableState.AwaitingResult)
                .assertIsDisplayed()
        }
    }

    @Test
    fun inReadOnly_clickEditUsername_shouldMakeUsernameEditable() {
        withContent {
            TestLayout()
        }

        perform {
            onNodeWithTag(EditableState.Editable)
                .assertDoesNotExist()

            onNodeWithTag(makeEditableTag)
                .performClick()

            onNodeWithTag(EditableState.Editable)
                .assertIsDisplayed()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun clickRequestRename_shouldTrigger__UserAccountActions_renameAccount() {
        withContent {
            TestLayout(
                remember { mutableStateOf(EditableState.Editable) }
            )
        }

        perform {
            dispatcher.runBlockingTest {
                onNodeWithTag(testRenameSuccessfulTag)
                    .assertTextEquals("false")

                onNodeWithTag(actionRequestRenameTag)
                    .performClick()

                onNodeWithTag(testRenameSuccessfulTag)
                    .assertTextEquals("true")

                onNodeWithTag(readOnlyUsernameTag)
                    .assertIsDisplayed()
            }
        }
    }

    @Test
    fun clickCancelRename_shouldMakeUsernameReadOnly() {
        withContent {
            TestLayout(
                remember { mutableStateOf(EditableState.Editable) }
            )
        }

        perform {
            onNodeWithTag(actionCancelRenameTag)
                .performClick()

            onNodeWithTag(readOnlyUsernameTag)
                .assertIsDisplayed()
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun validationMessageText_shouldBeUpdated_whenRenameRequested() {
        withContent {
            TestLayout(
                remember { mutableStateOf(EditableState.Editable) },
                renameResult = RenameResult.SERVER_DENIED
            )
        }

        perform {
            dispatcher.runBlockingTest {
                onNodeWithTag(actionRequestRenameTag)
                    .performClick()

                onNodeWithTag(feedbackTextTag)
                    .assertTextEquals("deniedByServer")
            }
        }
    }

    @Composable
    internal fun TestLayout(
        state: MutableState<EditableState> = remember { mutableStateOf(EditableState.ReadOnly) },
        renameResult: RenameResult = RenameResult.ACCEPTED,
        validationMessages: ValidationMessages = remember {
            ValidationMessages(
                tooLong = "tooLong",
                tooShort = "tooShort",
                formatError = "formatError",
                ok = "ok",
                error = "error",
                deniedByServer = "deniedByServer",
            )
        }
    ) {
        var renameSuccessful by remember { mutableStateOf(false) }
        val userAccountActions = remember {
            UserAccountActions(
                renameAccount = { _, _ ->
                    if (renameResult == RenameResult.ACCEPTED) renameSuccessful = true
                    renameResult
                }
            )
        }

        Column {
            EditableUsername(
                userToken = SampleUserToken,
                state = state,
                validationMessages = validationMessages,
                userAccountActions = userAccountActions
            )

            Text("$renameSuccessful", Modifier.testTag(testRenameSuccessfulTag))
        }
    }
}
