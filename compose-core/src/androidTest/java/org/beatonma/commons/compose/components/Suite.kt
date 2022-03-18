package org.beatonma.commons.compose.components

import org.beatonma.commons.compose.components.fabbottomsheet.FabBottomSheetTest
import org.beatonma.commons.compose.components.text.TextTestSuite
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    CollapsedColumnTest::class,
    CollapsibleChipTest::class,
    DoubleConfirmationButtonTest::class,
    FabBottomSheetTest::class,
    FeedbackMessageLayoutTest::class,
    ModalTest::class,
    StickyHeaderRowTest::class,
    TextTestSuite::class,
)
class ComponentTestSuite
