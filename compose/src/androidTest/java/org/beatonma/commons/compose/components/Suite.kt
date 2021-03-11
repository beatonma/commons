package org.beatonma.commons.compose.components

import org.beatonma.commons.compose.components.collapsibleheader.CollapsibleHeaderLayoutTest
import org.beatonma.commons.compose.components.stickyrow.StickyHeaderRowTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    CollapsedColumnTest::class,
    CollapsibleChipTest::class,
    CollapsibleHeaderLayoutTest::class,
    DoubleConfirmationButtonTest::class,
    FeedbackMessageLayoutTest::class,
    LinkedTextTest::class,
    SearchFieldTest::class,
    StickyHeaderRowTest::class,
    ValidatedTextFieldTest::class,
)
class ComponentTestSuite
