package org.beatonma.commons.compose.components

import org.beatonma.commons.compose.components.collapsibleheader.CollapsibleHeaderLayoutTest
import org.beatonma.commons.compose.components.stickyrow.StickyHeaderRowTest
import org.beatonma.commons.compose.components.text.TextTestSuite
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    CollapsedColumnTest::class,
    CollapsibleChipTest::class,
    CollapsibleHeaderLayoutTest::class,
    DoubleConfirmationButtonTest::class,
    FeedbackMessageLayoutTest::class,
    ModalTest::class,
    StickyHeaderRowTest::class,
    TextTestSuite::class,
)
class ComponentTestSuite
