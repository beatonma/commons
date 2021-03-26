package org.beatonma.commons.app

import org.beatonma.commons.app.bill.BillDetailViewModelTest
import org.beatonma.commons.app.memberprofile.ProfileHistoryCompressionTestSuite
import org.beatonma.commons.app.timeline.TimelineTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    BillDetailViewModelTest::class,
    ProfileHistoryCompressionTestSuite::class,
    TimelineTest::class,
)
class AppTestSuite
