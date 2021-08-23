package org.beatonma.commons.app

import org.beatonma.commons.app.timeline.TimelineTest
import org.beatonma.commons.app.ui.screens.bill.BillDetailViewModelTest
import org.beatonma.commons.app.ui.screens.memberprofile.ProfileHistoryCompressionTestSuite
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    BillDetailViewModelTest::class,
    ProfileHistoryCompressionTestSuite::class,
    TimelineTest::class,
)
class AppTestSuite
