package org.beatonma.commons.app.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import org.beatonma.commons.compose.TestTag
import org.beatonma.commons.compose.components.text.Caption
import org.beatonma.commons.theme.formatting.formatted
import java.time.LocalDate
import java.time.LocalDateTime


@Composable
fun Date(
    date: LocalDate,
    modifier: Modifier = Modifier,
) {
    Caption(date.formatted(), modifier.testTag(TestTag.Date))
}


@Composable
fun DateTime(
    datetime: LocalDateTime,
    modifier: Modifier = Modifier,
) {
    Caption(datetime.formatted(), modifier.testTag(TestTag.DateTime))
}
