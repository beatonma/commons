package org.beatonma.commons.app.social

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onChildAt
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.filters.MediumTest
import org.beatonma.commons.compose.TestTag
import org.beatonma.commons.sampledata.SampleSocialContent
import org.beatonma.commons.snommoc.models.social.SocialComment
import org.beatonma.commons.test.extensions.assertions.shouldbe
import org.beatonma.commons.testcompose.test.ComposeTest
import org.junit.Test

@MediumTest
class CommentListTest : ComposeTest() {
    private val listTag = SocialTestTag.CommentsList
    private val emptyTag = SocialTestTag.CommentsEmpty


    @Test
    fun emptyLayout_isCorrect() {
        withContent {
            TestLayout(comments = listOf())
        }

        perform {
            onNodeWithTag(emptyTag)
                .assertExists()
                .assertIsDisplayed()
        }
    }

    @Test
    fun withCommentsLayout_isCorrect() {
        withContent {
            TestLayout()
        }

        perform {
            onNodeWithTag(listTag)
                .assertExists()
                .assertIsDisplayed()
                .onChildren()
                .assertCountEquals(3)
        }
    }

    @Test
    fun clickOnComment_shouldTriggerOnClickAction() {
        val clickedComment: MutableState<SocialComment?> = mutableStateOf(null)

        withContent {
            TestLayout { comment ->
                clickedComment.value = comment
            }
        }

        perform {
            onNodeWithTag(listTag)
                .onChildAt(1)
                .performClick()

            with(clickedComment.value!!) {
                username shouldbe "username2"
                text shouldbe "such insight"
            }
        }
    }

    @Test
    fun comment_hasRequiredData() {
        withContent {
            TestLayout(SampleSocialContent.comments.take(1))
        }

        perform {
            onNodeWithText("username2", useUnmergedTree = true)
                .assertExists()
                .assertIsDisplayed()

            onNodeWithText("and furthermore", useUnmergedTree = true)
                .assertExists()
                .assertIsDisplayed()

            onNodeWithTag(TestTag.DateTime, useUnmergedTree = true)
                .assertExists()
                .assertIsDisplayed()
        }
    }

    @Composable
    private fun TestLayout(
        comments: List<SocialComment> = SampleSocialContent.comments,
        onClick: (SocialComment) -> Unit = {},
    ) {
        CommentList(
            comments = comments,
            modifier = Modifier,
            expandProgress = 1F,
            onClick = onClick,
        )
    }
}
