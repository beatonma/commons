package org.beatonma.commons.compose.components

import org.beatonma.commons.compose.components.collapsibleheader.CollapsibleHeaderLayoutTest
import org.beatonma.commons.compose.components.stickyrow.StickyHeaderRowTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    CollapsibleHeaderLayoutTest::class,
    StickyHeaderRowTest::class,
    CollapsibleChipTest::class,
    DoubleConfirmationButtonTest::class,
)
class ComponentTestSuite
