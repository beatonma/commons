package org.beatonma.commons.theme.compose.padding

import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import org.beatonma.commons.test.extensions.assertions.shouldbe
import org.junit.Test

class PaddingTest {
    @Test
    fun paddingValues_isCorrect() {
        paddingValues() shouldbe Padding(0.dp, 0.dp, 0.dp, 0.dp)
        paddingValues(12.dp) shouldbe Padding(12.dp, 12.dp, 12.dp, 12.dp)
        paddingValues(all = 12.dp) shouldbe Padding(12.dp, 12.dp, 12.dp, 12.dp)

        paddingValues(horizontal = 12.dp) shouldbe Padding(12.dp, 0.dp, 12.dp, 0.dp)
        paddingValues(vertical = 12.dp) shouldbe Padding(0.dp, 12.dp, 0.dp, 12.dp)

        paddingValues(start = 12.dp) shouldbe Padding(12.dp, 0.dp, 0.dp, 0.dp)
        paddingValues(top = 12.dp) shouldbe Padding(0.dp, 12.dp, 0.dp, 0.dp)
        paddingValues(end = 12.dp) shouldbe Padding(0.dp, 0.dp, 12.dp, 0.dp)
        paddingValues(bottom = 12.dp) shouldbe Padding(0.dp, 0.dp, 0.dp, 12.dp)

        paddingValues(horizontal = 12.dp, vertical = 6.dp) shouldbe Padding(12.dp, 6.dp, 12.dp, 6.dp)
        paddingValues(4.dp, vertical = 6.dp) shouldbe Padding(4.dp, 6.dp, 4.dp, 6.dp)
        paddingValues(horizontal = 12.dp, top = 4.dp) shouldbe Padding(12.dp, 4.dp, 12.dp, 0.dp)
    }

    @Test
    fun padding_toPaddingValues_isCorrect() {
        val ltr = LayoutDirection.Ltr

        paddingValues().asPaddingValues().calculateBottomPadding() shouldbe 0.dp

        with(paddingValues(horizontal = 12.dp, top = 4.dp, bottom = 6.dp).asPaddingValues()) {
            calculateStartPadding(ltr) shouldbe 12.dp
            calculateTopPadding() shouldbe 4.dp
            calculateEndPadding(ltr) shouldbe 12.dp
            calculateBottomPadding() shouldbe 6.dp
        }
    }
}
