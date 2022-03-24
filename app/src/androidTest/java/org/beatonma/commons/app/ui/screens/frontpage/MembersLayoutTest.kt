package org.beatonma.commons.app.ui.screens.frontpage

import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.assertWidthIsEqualTo
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import androidx.test.filters.MediumTest
import org.beatonma.commons.app.TestTheme
import org.beatonma.commons.app.ui.components.members.MemberLayoutCardWidth
import org.beatonma.commons.app.ui.components.members.MembersLayout
import org.beatonma.commons.compose.TestTag
import org.beatonma.commons.sampledata.SampleMember
import org.beatonma.commons.testcompose.test.ComposeTest
import org.beatonma.commons.testcompose.withUnclippedBoundsInRoot
import org.junit.Test


@MediumTest
class MembersLayoutTest : ComposeTest() {
    private fun memberWithPortrait(name: String? = null) =
        SampleMember.copy(name = name ?: SampleMember.name)

    private fun memberNoPortrait(name: String? = null) =
        SampleMember.copy(
            name = name ?: SampleMember.name,
            portraitUrl = null,
        )

    @Test
    fun MembersLayout_withSmallAndLargeItems_alignsCorrectly() {
        var smallTotalHeight = 0.dp
        withContent {
            TestTheme {
                MembersLayout(
                    listOf(
                        memberWithPortrait("large"),
                        memberNoPortrait("small1"),
                        memberNoPortrait("small2"),
                        memberNoPortrait("small3"),
                    ),
                    onClick = {},
                )
            }
        }

        perform {
            listOf("small1", "small2", "small3").forEach { name ->
                onNodeWithTag(TestTag.member(name))
                    .withUnclippedBoundsInRoot { smallTotalHeight += it.height.toDp() }
            }

            onNodeWithTag(TestTag.member("large"))
                .assertHeightIsEqualTo(smallTotalHeight)
                .assertWidthIsEqualTo(MemberLayoutCardWidth)
        }
    }
}
